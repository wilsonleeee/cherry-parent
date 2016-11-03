/*
R * @(#)BINBEIFCOU01_BL.java     1.0 2010/11/12
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */

package com.cherry.ia.cou.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.CounterConstants;
import com.cherry.ia.cou.service.BINBEIFCOU01_Service;
import com.cherry.ia.dep.service.BINBEIFDEP01_Service;

/**
 * 
 * 柜台列表导入BL
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.11.12
 */
public class BINBEIFCOU01_BL {

	/** 柜台列表导入Service */
	@Resource
	private BINBEIFCOU01_Service binbeifcou01Service;
	
	/** 部门列表导入Service */
	@Resource
	private BINBEIFDEP01_Service binBEIFDEP01_Service;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	/** 需要删除的总条数 */
	private int delTotalCount = 0;
	/** 实际删除条数 */
	private int delCount = 0;
	
	/** 每批次(页)处理数量 1000 */
	private final int BTACH_SIZE = 1000;
	
	/** 记录导入失败的柜台 */
	private List<String> faildCntList = new ArrayList<String>();
	
	/** 共通map */
	private Map<String, Object> comMap;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	/**
	 * 
	 * 从接口数据库把柜台信息导入到Cherry数据库batch主处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_batchCounters(Map<String, Object> map) throws CherryBatchException,Exception {
		
		// 初始化
		init(map);
		
		// 世代管理和备份Cherry数据库中的柜台信息
		bakCnt(map);
		
		// 上一批次(页)最后一条CounterCode
		String bathLastCntCode = "";
		
		while (true) {

			// 查询接口柜台列表
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.putAll(comMap);
			paraMap.put("batchSize", BTACH_SIZE);
			paraMap.put("bathLastCntCode", bathLastCntCode);
			List<Map<String, Object>> cntList = binbeifcou01Service.getCountersList(paraMap);
			if (!CherryBatchUtil.isBlankList(cntList)) {
				
				 // 当前批次最后一个柜台的Countercode赋给bathLastCntCode，用于当前任务下一批次(页)柜台数据的筛选条件
				 bathLastCntCode = CherryBatchUtil.getString(cntList.get(cntList.size()- 1).get("counterCode"));
				 // 统计总条数
				 totalCount += cntList.size();
				 
				 // 把接口数据库中取得的柜台信息导入到Cherry数据库中
				 updateWitpos(cntList,map);
				 
				 // 接口柜台列表为空或柜台数据少于一批次(页)处理数量，跳出循环
				 if (cntList.size() < BTACH_SIZE) {
					 break;
				 }
				 
			} else {
				break;
			}
		}
		
		
		// 找出没有更新的数据，并伦理删除
		delOldCnt(map);
		
		// 未知节点下不存在子节点的场合，删除未知节点
		delUnkonw(map);
		
		// 输出处理结果信息
		outMessage();
		
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(map);

		return flag;
	}
	
	/**
	 * 世代管理和备份Cherry数据库中的柜台信息
	 * @param map
	 */
	private void bakCnt(Map<String, Object> map) throws CherryBatchException{
		
		try {
			// 世代管理上限
			map.put("count", CherryBatchConstants.COUNT);
			
			// 更新世代番号
			binbeifcou01Service.updateBackupCount(map);
			
			// 删除世代番号超过上限的数据
			binbeifcou01Service.clearBackupData(map);
			
			// 备份柜台信息表
			binbeifcou01Service.backupCounters(map);
			
			// Cherry数据库事务提交
			binbeifcou01Service.manualCommit();
			
		} catch (Exception e) {
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF02001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw new CherryBatchException(batchExceptionDTO);
		}
	}

	/**
	 * 
	 * 把接口数据库中取得的柜台信息导入到Cherry数据库中
	 * 
	 * @param countersList 接口数据库中的柜台信息List
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void updateWitpos(List<Map<String, Object>> countersList, Map<String, Object> paramMap)
			throws CherryBatchException,Exception {

		// 取得品牌ID
		Object brandId = paramMap.get(CherryBatchConstants.BRANDINFOID);
		// 取得组织ID
		Object orgInfoId = paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID);

		// 循环导入柜台数据到Cherry数据库中
		for (Map<String, Object> counterMap : countersList) {
			// 柜台号
			String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
			// 柜台名
			String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
			
			//处理柜台名称（必填）
			counterName = CherryBatchUtil.convertSpecStr(counterName);
			counterMap.put("counterName", counterName);
			try {
				
				counterMap.putAll(comMap);
				
				// 把品牌Id设入条件map
				counterMap.put(CherryBatchConstants.BRANDINFOID, brandId);
				// 把所属组织Id设入条件map
				counterMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
				// 设置渠道ID
				setChannelId(counterMap);
				// 设置区域ID
				String cntImpAddRegion_Flag = PropertiesUtil.pps.getProperty("CntImpAddRegion_Flag","false"); // 柜台是否支持新增区域
				
				if("true".equals(cntImpAddRegion_Flag)){
					setRegionIdOld(counterMap, paramMap);
				} else{
					setRegionId(counterMap, paramMap);
				}
				
				// 设置经销商ID
				setResellerInfoId(counterMap);
				// 导入柜台相关表
				updateCounterRef(counterMap, paramMap);
				// Cherry数据库事务提交
				binbeifcou01Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					fReason = "导入柜台信息发生异常，详细异常见日志文件。";
					// 记录导入失败的柜台
					faildCntList.add(counterCode);
					
					// Cherry数据库回滚事务
					binbeifcou01Service.manualRollback();
				} catch (Exception ex) {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EIF02026");
					// 柜台号
					batchLoggerDTO.addParam(counterCode);
					// 柜台名
					batchLoggerDTO.addParam(counterName);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, ex);
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					fReason = "导入柜台信息发生异常，详细异常见日志文件。";
					// 记录导入失败的柜台
					faildCntList.add(counterCode);
					
					// Cherry数据库回滚事务
					binbeifcou01Service.manualRollback();
				} catch (Exception ex) {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EIF02026");
					// 柜台号
					batchLoggerDTO.addParam(counterCode);
					// 柜台名
					batchLoggerDTO.addParam(counterName);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EIF02011");
				// 柜台号
				batchLoggerDTO.addParam(counterCode);
				// 柜台名
				batchLoggerDTO.addParam(counterName);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			} catch (Throwable t) {
				try {
					// Cherry数据库回滚事务
					binbeifcou01Service.manualRollback();
				} catch (Exception ex) {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EIF02026");
					// 柜台号
					batchLoggerDTO.addParam(counterCode);
					// 柜台名
					batchLoggerDTO.addParam(counterName);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, ex);
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	/**
	 * 
	 * 导入柜台相关表
	 * 
	 * @param counterMap 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void updateCounterRef(Map<String, Object> counterMap, Map<String, Object> paramMap)
			throws CherryBatchException ,Exception{
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		
		try{
			// 柜台类型
			Object counterKind = counterMap.get("counterKind");
			if(counterKind == null || "".equals(counterKind.toString())) {
				if(counterName.contains(CounterConstants.COUNTERNAME_TEST)) {
					counterMap.put("counterKind", 1);
					counterMap.put("testType", 1);
				} else {
					counterMap.put("counterKind", 0);
					counterMap.put("testType", 0);
				};
			} else {
				if("1".equals(counterKind.toString())) {
					counterMap.put("testType", 1);
				} else {
					counterMap.put("testType", 0);
				}
			}
			String endDate = (String)counterMap.get("endDate");
			String sysDate = (String)paramMap.get("sysDate");
			// 柜台是否有效，true表示有效，false表示无效
			boolean validFlag = true;
			// 关店时间小于系统时间表示柜台已无效
			if(endDate != null && endDate.compareTo(sysDate) <= 0) {
				validFlag = false;
				counterMap.put("validFlag", "0");
			} else {
				counterMap.remove("endDate");
				counterMap.put("validFlag", "1");
			}
			
			// 查询组织结构中的柜台信息
			Map<String, Object> organizationInfo = binbeifcou01Service.getOrganizationId(counterMap);
			Object organizationId = null;
			// 组织结构中的柜台信息不存在时，在组织结构表中插入柜台信息
			if(organizationInfo == null) {
				try {
					// 取得未知节点来作为部门的上级节点
					Object unknownPath = paramMap.get("unknownPath");
					// 未知节点不存在的场合
					if(unknownPath == null) {
						// 取得品牌下的未知节点
						unknownPath = binBEIFDEP01_Service.getUnknownPath(counterMap);
						if(unknownPath == null) {
							// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
							Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
							// 作成者
							unknownOrgMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 作成程序名
							unknownOrgMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFCOU01");
							// 更新者
							unknownOrgMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 更新程序名
							unknownOrgMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFCOU01");
							// 未知节点的品牌ID
							unknownOrgMap.put(CherryBatchConstants.BRANDINFOID, counterMap.get(CherryBatchConstants.BRANDINFOID));
							// 未知节点的组织ID
							unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID, counterMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
							// 未知节点添加在品牌节点下
							unknownOrgMap.put("path", binBEIFDEP01_Service.getFirstPath(counterMap));
							// 取得未知节点path
							unknownPath = binBEIFDEP01_Service.getNewDepNodeId(unknownOrgMap);
							unknownOrgMap.put("nodeId", unknownPath);
							// 未知节点的部门代码
							unknownOrgMap.put("departCode", CherryBatchConstants.UNKNOWN_DEPARTCODE);
							// 未知节点的部门名称
							unknownOrgMap.put("departName", CherryBatchConstants.UNKNOWN_DEPARTNAME);
							// 未知节点的部门类型
							unknownOrgMap.put("type", CherryBatchConstants.UNKNOWN_DEPARTTYPE);
							// 未知节点的到期日expiringDate
							unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
							// 添加未知节点
							binBEIFDEP01_Service.insertDepart(unknownOrgMap);
						}
						paramMap.put("unknownPath", unknownPath);
					}
					counterMap.put("agencyNodeId", unknownPath);
					
					// 查询柜台在组织结构表中的插入位置
					counterMap.put("counterNodeId", binbeifcou01Service.getCounterNodeId(counterMap));
					// 柜台无效的场合
					if(!validFlag) {
						counterMap.put("validFlagVal", "0");
					}
					// 在组织结构表中插入柜台节点
					counterMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
					organizationId = binbeifcou01Service.insertCouOrg(counterMap);
					counterMap.remove("validFlagVal");
					// 所属部门
					counterMap.put("organizationId", organizationId);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02007");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
				try {
					// 缺省仓库区分
					counterMap.put("defaultFlag", CherryBatchConstants.IVT_DEFAULTFLAG);
					// 仓库名称
					counterMap.put("inventoryNameCN", counterName+CherryBatchConstants.IVT_NAME_CN_DEFAULT);
					// 设定仓库类型为柜台仓库
					counterMap.put("depotType", "02");
					// 仓库编码类型（仓库为3）
					counterMap.put("type", CherryBatchConstants.IVT_CODE_TYPE);
					// 仓库编码最小长度
					counterMap.put("length", CherryBatchConstants.IVT_CODE_LEN);
					// 自动生成仓库编码
					counterMap.put("inventoryCode", CherryBatchConstants.IVT_CODE_PREFIX+binOLCM15_BL.getSequenceId(counterMap));
					// 判断仓库编码是否已经存在
					int depotCount = binBEIFDEP01_Service.getDepotCountByCode(counterMap);
					while(depotCount > 0) {
						// 自动生成仓库编码
						counterMap.put("inventoryCode", CherryBatchConstants.IVT_CODE_PREFIX+binOLCM15_BL.getSequenceId(counterMap));
						// 判断仓库编码是否已经存在
						depotCount = binBEIFDEP01_Service.getDepotCountByCode(counterMap);
					}
					// 添加仓库
					int depotInfoId = binBEIFDEP01_Service.addDepotInfo(counterMap);
					counterMap.put("depotInfoId", depotInfoId);
					// 添加部门仓库关系
					binBEIFDEP01_Service.addInventoryInfo(counterMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF01005");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			} else {
				try {
					organizationId = organizationInfo.get("organizationId");
					counterMap.put("organizationId", organizationId);
					counterMap.put("inventoryNameCN", counterName+CherryBatchConstants.IVT_NAME_CN_DEFAULT);
					
					// 更新柜台仓库名称
					binBEIFDEP01_Service.updateDepotInfo(counterMap);
					
					// 取得柜台上级节点
					String couHigherPath = (String)organizationInfo.get("couHigherPath");
					// 取得柜台主管所在部门节点
					String couHeadDepPath = binbeifcou01Service.getCouHeadDepPath(organizationInfo);
					if(couHeadDepPath != null) {
						if(couHigherPath == null || !couHeadDepPath.equals(couHigherPath)) {
							counterMap.put("agencyNodeId", couHeadDepPath);
							// 查询柜台在组织结构表中的插入位置
							counterMap.put("couNodeId", binbeifcou01Service.getCounterNodeId(counterMap));
						}
					}
					// 更新在组织结构中的柜台
					binbeifcou01Service.updateCouOrg(counterMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02015");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			}
			// 把部门ID设置到柜台信息中
			counterMap.put("orgId", organizationId);	
			
			// 查询柜台信息
			Map<String, Object> counterInfo = binbeifcou01Service.getCounterId(counterMap);
			// 柜台数据不存在时，插入柜台信息
			if (null == counterInfo) {
				try {
					// 柜台无效的场合
					if(!validFlag) {
						counterMap.put("validFlagVal", "0");
						counterMap.put("status", "4");
					} else {
						counterMap.put("status", "0");
					}
					// 插入柜台信息
					int counterId = binbeifcou01Service.insertCounterInfo(counterMap);
					counterMap.remove("validFlagVal");
					// 设置柜台信息ID
					counterMap.put(CounterConstants.COUNTER_ID, counterId);
					// 柜台有效的场合
					if(validFlag) {
						// 设置事件名称ID（营业）
						counterMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_0);
					} else {
						// 设置事件名称ID（关店）
						counterMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_4);
					}
					try {
						// 插入柜台开始事件信息
						binbeifcou01Service.insertCounterEvent(counterMap);
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EIF02010");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchLoggerDTO1.addParam(counterCode);
						// 柜台名
						batchLoggerDTO1.addParam(counterName);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
					// 插入件数加一
					insertCount++;
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02008");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			} else {
				try {
					// 添加status
					if(!validFlag) {
						counterMap.put("status", "4");
					} else {
						counterMap.put("status", "0");
					}
					// 更新柜台信息表
					binbeifcou01Service.updateCounterInfo(counterMap);
					// 更新件数加一
					updateCount++;
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02009");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
				// 柜台无效的场合
				if(!validFlag) {
					try {
						// 设置柜台信息ID
						counterMap.put(CounterConstants.COUNTER_ID, counterInfo.get("counterInfoId"));
						// 设置事件名称ID（关店）
						counterMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_4);
						List<String> counterEventIdList = binbeifcou01Service.getCounterEventId(counterMap);
						if(counterEventIdList == null || counterEventIdList.isEmpty()) {
							// 插入柜台开始事件信息
							binbeifcou01Service.insertCounterEvent(counterMap);
						}
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EIF02010");
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchLoggerDTO1.addParam(counterCode);
						// 柜台名
						batchLoggerDTO1.addParam(counterName);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
				} else {
					Object couValidFlag = counterInfo.get("validFlag");
					if(couValidFlag != null && "0".equals(couValidFlag.toString())) {
						try {
							// 设置柜台信息ID
							counterMap.put(CounterConstants.COUNTER_ID, counterInfo.get("counterInfoId"));
							// 设置事件名称ID（营业）
							counterMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_0);
							// 插入柜台开始事件信息
							binbeifcou01Service.insertCounterEvent(counterMap);
						} catch (Exception e) {
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EIF02010");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							// 柜台号
							batchLoggerDTO1.addParam(counterCode);
							// 柜台名
							batchLoggerDTO1.addParam(counterName);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						}
					}
				}
			}
		}catch(Exception e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF02025");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台号
			batchExceptionDTO.addErrorParam(counterCode);
			// 柜台名
			batchExceptionDTO.addErrorParam(counterName);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		
	}

	/**
	 * 
	 * 设置渠道ID
	 * 
	 * @param counterMap 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setChannelId(Map<String, Object> counterMap)
			throws CherryBatchException {
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		// 取得渠道名称
		String channelName = CherryBatchUtil.getString(counterMap.get(CounterConstants.CHANNEL_NAME));
		if (!CherryBatchConstants.BLANK.equals(channelName)) {
			// 查询渠道ID
			Object channelId = binbeifcou01Service.getChannelId(counterMap);
			if (null == channelId) {
				try {
					// 插入渠道信息
					channelId = binbeifcou01Service.insertChannelId(counterMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02006");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					// 渠道名称
					batchExceptionDTO.addErrorParam(channelName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			}
			// 设置渠道ID
			counterMap.put(CounterConstants.CHANNEL_ID, channelId);
		} else {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF02005");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_WARNING);
			// 柜台号
			batchLoggerDTO1.addParam(counterCode);
			// 柜台名
			batchLoggerDTO1.addParam(counterName);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		}
	}

	/**
	 * 
	 * 设置区域ID
	 * 
	 * @param counterMap 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setRegionId(Map<String, Object> counterMap, Map<String, Object> paramMap)
			throws CherryBatchException,Exception {
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		try{
			
			// 区域名称
//		String regionName = CherryBatchUtil.getString(counterMap.get(CounterConstants.REGION_NAME));
			
			// 区域CODE(regionCode)
			String regionCode = CherryBatchUtil.getString(counterMap.get("regionCode"));
			
			// 城市CODE(cityCode)
			String cityCode = CherryBatchUtil.getString(counterMap.get("cityCode"));
			
			if (CherryBatchConstants.BLANK.equals(cityCode)) {
				return;
			} else{
				// 取得cityCode的上一级/上二级区域信息
				Map<String, Object> parentRegionMap = binbeifcou01Service.getParentRegionByCity(counterMap);
				if ( null != parentRegionMap && !parentRegionMap.isEmpty()) {
					
					// 当前CityCode
					String curId = CherryBatchUtil.getString(parentRegionMap.get("curId"));
					// 上级ID
					String pId = CherryBatchUtil.getString(parentRegionMap.get("pId"));
					// 上二级ID
					String ppId = CherryBatchUtil.getString(parentRegionMap.get("ppId"));
					
					if (!CherryBatchConstants.BLANK.equals(curId)) {
						counterMap.put(CounterConstants.REGION_ID, curId);
						counterMap.put("departCityId", curId);
					}
					if (!CherryBatchConstants.BLANK.equals(pId)) {
						counterMap.put("departProvinceId", pId);
					}
					if (!CherryBatchConstants.BLANK.equals(ppId)) {
						counterMap.put("departRegionId", ppId);
					}
				}else{
					// 找不到对应的区域信息
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02018");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台CODE
					batchExceptionDTO.addErrorParam(counterCode);
					//区域CODE
					batchExceptionDTO.addErrorParam(regionCode);
					// 城市CODE
					batchExceptionDTO.addErrorParam(cityCode);
					throw new CherryBatchException(batchExceptionDTO);
				}
			}
		}catch(Exception e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF02024");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台号
			batchExceptionDTO.addErrorParam(counterCode);
			// 柜台名
			batchExceptionDTO.addErrorParam(counterName);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
	}
	
	/**
	 * 
	 * 设置区域ID
	 * 
	 * @param counterMap 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setRegionIdOld(Map<String, Object> counterMap, Map<String, Object> paramMap)
			throws CherryBatchException,Exception {
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		// 区域名称
		String regionName = CherryBatchUtil.getString(counterMap.get(CounterConstants.REGION_NAME));
		try{
			
			if (CherryBatchConstants.BLANK.equals(regionName)) {
				return;
			}
			// 查询区域信息
			Map<String, Object> regionInfo = binbeifcou01Service.getRegionInfo(counterMap);
			// 区域不存在的场合，插入区域信息
			if (regionInfo == null || regionInfo.isEmpty()) {
				// 取得区域表中的品牌节点
				Object brandPath = paramMap.get("brandPath");
				// 品牌节点为空的场合
				if(brandPath == null) {
					// 查询区域表中的品牌节点
					brandPath = binbeifcou01Service.getBrandRegionPath(counterMap);
					if(brandPath == null) {
						// 区域节点的父节点为根节点"/"
						brandPath = "/";
					}
					paramMap.put("brandPath", brandPath);
				}
				counterMap.put(CounterConstants.NODE_ID, brandPath);
				
				// 查询区域节点插入位置
				String regNode = CherryBatchUtil.getString(binbeifcou01Service.getNewRegNode(counterMap));
				// 区域NodeId
				counterMap.put(CounterConstants.NODE_ID, regNode);
				try {
					// 插入区域节点
					int regionId = binbeifcou01Service.insertRegNode(counterMap);
					counterMap.put("departRegionId", regionId);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02003");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			} else {
				// 区域NodeId
				counterMap.put(CounterConstants.NODE_ID, regionInfo.get("path"));
				counterMap.put("departRegionId", regionInfo.get("regionId"));
			}
			
			// 省中文名字
			String prvnNameCh = CherryBatchUtil.getString(counterMap.get(CounterConstants.PRVNNAME_CH));
			// 省中文名字不为空的场合
			if (!CherryBatchConstants.BLANK.equals(prvnNameCh)) {
				// 查询省区域信息
				Map<String, Object> provinceInfo = binbeifcou01Service.getProvinceInfo(counterMap);
				// 省不存在的场合，插入省信息
				if (provinceInfo == null || provinceInfo.isEmpty()) {
					// 查询省区域节点插入位置
					String prvnNode = CherryBatchUtil.getString(binbeifcou01Service.getNewRegNode(counterMap));
					// 区域NodeId
					counterMap.put(CounterConstants.NODE_ID, prvnNode);
					try {
						// 插入省区域节点
						int regionId = binbeifcou01Service.insertPrvnRegNode(counterMap);
						counterMap.put("departProvinceId", regionId);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF02002");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchExceptionDTO.addErrorParam(counterCode);
						// 柜台名
						batchExceptionDTO.addErrorParam(counterName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
				} else {
					// 区域NodeId
					counterMap.put(CounterConstants.NODE_ID, provinceInfo.get("path"));
					counterMap.put("departProvinceId", provinceInfo.get("regionId"));
				}
			}
			
			// 城市名字
			String cityName = CherryBatchUtil.getString(counterMap.get(CounterConstants.CITYNAME_CH));
			if (CherryBatchConstants.BLANK.equals(cityName)) {
				return;
			}
			// 查询城市区域信息
			Map<String, Object> cityInfo = binbeifcou01Service.getCityInfo(counterMap);
			Object regionId = null;
			// 城市不存在的场合，添加城市
			if (null == cityInfo) {
				// 查询城市NODE插入位置
				String cityLoac = CherryBatchUtil.getString(binbeifcou01Service.getNewRegNode(counterMap));
				// 城市NodeId
				counterMap.put(CounterConstants.NODE_ID, cityLoac);
				try {
					// 插入城市
					regionId = binbeifcou01Service.insertCityNode(counterMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EIF02004");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					// 柜台号
					batchExceptionDTO.addErrorParam(counterCode);
					// 柜台名
					batchExceptionDTO.addErrorParam(counterName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
			} else {
				regionId = cityInfo.get("regionId");
				String higherNodeId = (String)cityInfo.get("higherNodeId");
				String nodeId = (String)counterMap.get(CounterConstants.NODE_ID);
				// 城市上级节点变更的场合
				if(!nodeId.equals(higherNodeId)) {
					counterMap.put("regionId", regionId);
					// 城市NodeId
					counterMap.put(CounterConstants.NODE_ID, binbeifcou01Service.getNewRegNode(counterMap));
					try {
						// 更新城市
						binbeifcou01Service.updateCityNode(counterMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF02014");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 柜台号
						batchExceptionDTO.addErrorParam(counterCode);
						// 柜台名
						batchExceptionDTO.addErrorParam(counterName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
				}
			}
			counterMap.put(CounterConstants.REGION_ID, regionId);
			counterMap.put("departCityId", regionId);
			
		}catch(Exception e){
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF02023");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台号
			batchExceptionDTO.addErrorParam(counterCode);
			// 柜台名
			batchExceptionDTO.addErrorParam(counterName);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 
	 * 设置经销商ID
	 * 
	 * @param counterMap 柜台信息
	 * 
	 * @return null
	 * @throws CherryBatchException
	 * 
	 */
	private void setResellerInfoId(Map<String, Object> counterMap)
			throws CherryBatchException,Exception {
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		// 取得经销商代码
		String agentCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.AGENTCODE));
		if (!CherryBatchConstants.BLANK.equals(agentCode)) {
			try {
				// 查询经销商ID
				Object resellerInfoId = binbeifcou01Service.getResellerInfoId(counterMap);
				if (null == resellerInfoId) {
						// 插入经销商信息
						resellerInfoId = binbeifcou01Service.insertResellerInfo(counterMap);
				}
				// 设置经销商ID
				counterMap.put(CounterConstants.RESELLERINFOID, resellerInfoId);
			} catch (Exception e) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EIF02016");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 柜台号
				batchExceptionDTO.addErrorParam(counterCode);
				// 柜台名
				batchExceptionDTO.addErrorParam(counterName);
				// 经销商代码
				batchExceptionDTO.addErrorParam(agentCode);
				batchExceptionDTO.setException(e);
				throw new CherryBatchException(batchExceptionDTO);
			}
		}
	}
	
	/**
	 * 程序初始化参数
	 * @param map
	 */
	private void init(Map<String, Object> map) {
		
		comMap = getComMap(map);
		
		// 系统时间
		String sysDateTime =  ConvertUtil.getString(comMap.get("sysDateTime"));
		String sysYMD = DateUtil.getSpecificDate(sysDateTime,DateUtil.DATE_PATTERN );
		map.put("sysDate", sysYMD);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT002");
		
		// 程序【开始运行时间】
		map.put("RunStartTime", sysDateTime);
	}
	
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		baseMap.put("brandCode", map.get("brandCode"));
		
		// 系统时间
		String sysdateTime = binbeifcou01Service.getSYSDateTime();
		baseMap.put("sysDateTime", sysdateTime);

		// 作成时间
		baseMap.put(CherryBatchConstants.CREATE_TIME, sysdateTime);
		// 更新时间
		baseMap.put(CherryBatchConstants.UPDATE_TIME, sysdateTime);
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFCOU01");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFCOU01");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		return baseMap;
	}
	
	/**
	 * 找出没有更新的数据，并伦理删除
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void delOldCnt(Map<String, Object> map) throws CherryBatchException,Exception{
		// 找出没有更新的数据，并伦理删除
		// 查询要伦理删除的柜台数据
		// 导入失败的柜台在新后台保持原样()
		map.put("faildCntList", faildCntList);
		List<Map<String, Object>> delList = binbeifcou01Service.getDelList(map);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			// 统计需要删除的总条数
			delTotalCount += delList.size();
			for (Map<String, Object> delMap : delList) {
				// 柜台号
				String counterCode = CherryBatchUtil.getString(delMap.get(CounterConstants.COUNTER_CODE));
				// 柜台名
				String counterName = CherryBatchUtil.getString(delMap.get(CounterConstants.COUNTER_NAME));
				try {
					/*
					// 作成者
					delMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					delMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFCOU01");
					// 更新者
					delMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					delMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFCOU01");
					*/
					delMap.putAll(comMap);
					// 伦理删除无效的柜台数据
					binbeifcou01Service.delInvalidCounters(delMap);
					// 伦理删除无效的部门数据
					binbeifcou01Service.delInvalidDepart(delMap);
					// 设置事件名称ID（关店）
					delMap.put(CounterConstants.EVENTNAME_ID, CounterConstants.EVENTNAME_ID_VALUE_4);
					try {
						// 插入柜台裁撤事件信息
						binbeifcou01Service.insertCounterEvent(delMap);
					} catch (Exception e) {
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EIF02013");
						// 柜台号
						batchLoggerDTO1.addParam(counterCode);
						// 柜台名
						batchLoggerDTO1.addParam(counterName);
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
					// Cherry数据库事务提交
					binbeifcou01Service.manualCommit();
					// 统计删除条数
					delCount++;
				} catch (Exception e) {
					try {
						// Cherry数据库回滚事务
						binbeifcou01Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF02012");
					// 柜台号
					batchLoggerDTO1.addParam(counterCode);
					// 柜台名
					batchLoggerDTO1.addParam(counterName);
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				}
			}
		}
	}
	
	/**
	 *  未知节点下不存在子节点的场合，删除未知节点
	 * @param map
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void delUnkonw(Map<String, Object> map) throws CherryBatchException,Exception{
		try {
			// 取得品牌下的未知节点信息
			Map<String, Object> subNodeCount = binBEIFDEP01_Service.getSubNodeCount(map);
			// 未知节点信息存在的场合
			if(subNodeCount != null && !subNodeCount.isEmpty()) {
				// 未知节点下不存在子节点的场合，删除未知节点
				if(subNodeCount.get("child") != null && !"".equals(subNodeCount.get("child")) 
						&& Integer.parseInt(subNodeCount.get("child").toString()) == 0) {
					// 删除未知节点
					binBEIFDEP01_Service.delUnknownNode(subNodeCount);
					// Cherry数据库事务提交
					binbeifcou01Service.manualCommit();
				}
			}
		} catch (Exception e) {
			try {
				// Cherry数据库回滚事务
				binbeifcou01Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF01006");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
	}
	
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException,Exception {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		// 需要删除的总件数
		BatchLoggerDTO loggerDelDTO1 = new BatchLoggerDTO();
		loggerDelDTO1.setCode("IIF02001");
		loggerDelDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO1.addParam(String.valueOf(delTotalCount));
		// 删除件数
		BatchLoggerDTO loggerDelDTO2 = new BatchLoggerDTO();
		loggerDelDTO2.setCode("IIF02002");
		loggerDelDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO2.addParam(String.valueOf(delCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO4);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		// 需要删除的总件数
		cherryBatchLogger.BatchLogger(loggerDelDTO1);
		// 删除件数
		cherryBatchLogger.BatchLogger(loggerDelDTO2);
	}
	
	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		 
		// 程序结束时，插入Job运行履历表
		paraMap.putAll(comMap);
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReason);
		paraMap.remove("validFlagVal");
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

}
