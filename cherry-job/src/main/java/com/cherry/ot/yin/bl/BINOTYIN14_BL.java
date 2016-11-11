/*	
 * @(#)BINOTYIN14_BL.java     1.0 @2015-5-26
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
package com.cherry.ot.yin.bl;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.common.CounterConstants;
import com.cherry.ia.dep.service.BINBEIFDEP01_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ot.yin.service.BINOTYIN14_Service;
import com.cherry.synchro.bs.interfaces.CounterSynchro_IF;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 颖通接口：颖通大陆柜台导入BL
 *
 * @author jijw
 *
 * @version  2015-5-26
 */
public class BINOTYIN14_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTYIN14_BL.class);	
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** 颖通大陆柜台导入Service */
	@Resource
	private BINOTYIN14_Service binOTYIN14_Service;
	
	/** 部门列表导入Service */
	@Resource
	private BINBEIFDEP01_Service binBEIFDEP01_Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM27_BL")
	private BINOLCM27_BL binOLCM27_BL;
	
	@Resource(name="counterSynchro")
	private CounterSynchro_IF counterSynchro;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 5000 */
	private final int BTACH_SIZE = 1000;
	
	private Map<String, Object> comMap;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 导入失败的itemCode */
	private List<String> faildCounterList = new ArrayList<String>();
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	/**
	 * batch处理
	 * 
	 * @param map
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchOTYIN14(Map<String, Object> map)
			throws CherryBatchException,Exception {

		// 初始化
		init(map);
		
		// 上一批次(页)最后一条SAPCode
		String bathLastSAPCode = "";
		
		while (true) {

			// 查询接口柜台列表
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.putAll(comMap);
			paraMap.put("batchSize", BTACH_SIZE);
			paraMap.put("bathLastSAPCode", bathLastSAPCode);
			List<Map<String, Object>> counterList = binOTYIN14_Service.getCounterListForOT(paraMap);
			if (CherryBatchUtil.isBlankList(counterList)) {
				break;
			} 
			 // 当前批次最后一个柜台的U_SAPCode赋给bathLastSAPCode，用于当前任务下一批次(页)柜台数据的筛选条件
			bathLastSAPCode = CherryBatchUtil.getString(counterList.get(counterList.size()- 1).get(CounterConstants.COUNTER_CODE));
			 // 统计总条数
			 totalCount += counterList.size();
			 // 更新新后台数据库柜台相关表
			 updateBackEnd(counterList,map);
			 // 接口柜台列表为空或产品数据少于一批次(页)处理数量，跳出循环
			 if (counterList.size() < BTACH_SIZE) {
				 break;
			 }
		}
		
		// log日志
		outMessage();
		// 程序结束时，处理Job共通(插入Job运行履历表)
		programEnd(map);
		return flag;
	}
	
	/**
	 * 取得香港的城市、省份、大区id
	 * @param
	 */
	private Map<String, Object> getCounterRegionId() {
		Map<String, Object> counterIdMap = new HashMap<String, Object>();
		//颖通导过来的数据都属于香港，则大区、省份或者直辖市id直接从数据库中取得
		counterIdMap.put("cityName", "香港");
		List<Map<String, Object>> HKCounterIDList = binOTYIN14_Service
				.getCounterRegionID(counterIdMap);
		
		for (Map<String, Object> HKCounterID : HKCounterIDList) {
			String regionType = HKCounterID.get("regionType").toString();
			if("0".equals(regionType)){
				//大区
				counterIdMap.put("departRegionId", HKCounterID.get("regionId"));
			}else if("1".equals(regionType)){
				//省份
				counterIdMap.put("departProvinceId", HKCounterID.get("regionId"));
			}else if("3".equals(regionType)){
				//城市
				counterIdMap.put("departCityId", HKCounterID.get("regionId"));
			}
		}
		return counterIdMap;
	}
	
	/**
	 * 更新新后台柜台相关表
	 * @param counterList
	 * @param map
	 */
	private void updateBackEnd(List<Map<String, Object>> counterList,Map<String, Object> map) {
		
		for (Map<String, Object> counterMap : counterList) {
			counterMap = CherryBatchUtil.removeEmptyVal(counterMap);
			screenMap(counterMap);
			counterMap.putAll(comMap);
//			counterMap.putAll(getCounterRegionId());
			try {
				// 保存或更新柜台信息
				updateCounterRef(counterMap,map);
				// webservice下发
				issued_BL(counterMap);
				binOTYIN14_Service.manualCommit();
			} catch (CherryBatchException e) {
				binOTYIN14_Service.manualRollback();
				// 记载导入新后台失败的U_SAPCode
				faildCounterList.add(CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE)));
				// 失败件数加一
				failCount += 1;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
		
	}
	
	/**
	 * 导入柜台相关表
	 * @param counterMap 柜台信息
	 * @param paramMap
	 * @throws CherryBatchException
	 */
	private void updateCounterRef(Map<String, Object> counterMap,Map<String, Object> paramMap)
			throws CherryBatchException {
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		
		// 柜台类型
		if(counterName.contains(CounterConstants.COUNTERNAME_TEST) || counterName.contains(CounterConstants.COUNTERNAME_TEST_TW)) {
			counterMap.put("counterKind", 1);
			counterMap.put("testType", 1);
		} else {
			counterMap.put("counterKind", 0);
			counterMap.put("testType", 0);
		};
		
		// 查询组织结构中的柜台信息
		Map<String, Object> organizationInfo = binOTYIN14_Service.getOrganizationId(counterMap);
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
						unknownOrgMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN11");
						// 更新者
						unknownOrgMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
						// 更新程序名
						unknownOrgMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN11");
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
				counterMap.put("counterNodeId", binOTYIN14_Service.getCounterNodeId(counterMap));
				// 在组织结构表中插入柜台节点
				organizationId = binOTYIN14_Service.insertCouOrg(counterMap);
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
				String couHeadDepPath = binOTYIN14_Service.getCouHeadDepPath(organizationInfo);
				if(couHeadDepPath != null) {
					if(couHigherPath == null || !couHeadDepPath.equals(couHigherPath)) {
						counterMap.put("agencyNodeId", couHeadDepPath);
						// 查询柜台在组织结构表中的插入位置
						counterMap.put("couNodeId", binOTYIN14_Service.getCounterNodeId(counterMap));
					}
				}
				// 取得柜台信息(新老后台交互时使用)【增加了柜台地址与柜台电话】
				Map<String, Object> counterInfo = binOTYIN14_Service.getCounterInfo(counterMap);
				if(counterInfo != null && !counterInfo.isEmpty()){
					//柜台协同区分
					String counterSynergyFlag =ConvertUtil.getString(counterInfo.get("CounterSynergyFlag"));
					if(ConvertUtil.isBlank(counterSynergyFlag)) {
						counterSynergyFlag ="1";
					}
					counterMap.put("counterSynergyFlag", counterSynergyFlag);
				}
				// 更新在组织结构中的柜台
				binOTYIN14_Service.updateCouOrg(counterMap);
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
		
		// 查询柜台ID
		Object counterId = binOTYIN14_Service.getCounterId(counterMap);
		// 柜台数据不存在时，插入柜台信息
		if (null == counterId) {
			try {
				// 插入柜台信息
				counterId = binOTYIN14_Service.insertCounterInfo(counterMap);
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
				// 更新柜台信息表
				binOTYIN14_Service.updateCounterInfo(counterMap);
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
		}
		// 柜台信息表ID，用于webservice下发
		counterMap.put("counterInfoId", counterId);
	}
	
	/**
	 * 是否调用Webservice进行柜台数据同步，是则下发
	 * @param counterMap
	 * @throws Exception
	 */
	private void issued_BL(Map<String,Object> counterMap) throws CherryBatchException {
		// 组织ID
		String organizationInfoId = ConvertUtil.getString(comMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(comMap.get(CherryBatchConstants.BRANDINFOID));
		
		// 柜台号
		String counterCode = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_CODE));
		// 柜台名
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		String Data ="";
		try{
			//是否调用Webservice进行柜台数据同步
			if(binOLCM14_BL.isConfigOpen("1055", organizationInfoId, brandInfoId)) {
				/* 暂时注释掉，后面改为存储过程
				//通过MQ将柜台信息下发
				Map<String,Object> WSMap = getCounterWSMap(counterMap);
				if(WSMap.isEmpty()) return;
				//WebService返回值Map
				Map<String,Object> resultMap = binOLCM27_BL.accessWebService(WSMap);
				String State = ConvertUtil.getString(resultMap.get("State"));
				Data = ConvertUtil.getString(resultMap.get("Data"));
				
				if(State.equals("ERROR")){
					throw new CherryBatchException();
				}
				*/
				
				//柜台下发
				Map<String,Object> synchroInfo = counterSynchro.assemblingSynchroInfo(counterMap);
				if(null != synchroInfo){
					// 操作类型--新增更新
					synchroInfo.put("Operate", "IUE");
					counterSynchro.synchroCounter(synchroInfo);
				}
			}
			
		}catch(Exception e){
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00030");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 柜台号
			batchExceptionDTO.addErrorParam(counterCode);
			// 柜台名
			batchExceptionDTO.addErrorParam(counterName);
			// webservice错误原因
			batchExceptionDTO.addErrorParam(Data);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		
	}
	
	
	/**
	 * 组装柜台信息的WebService
	 * 
	 * 
	 * */
	public Map<String,Object> getCounterWSMap(Map<String,Object> map) throws Exception{
		Map<String,Object> counterWSMap = new HashMap<String,Object>();
		// 取得柜台信息(新老后台交互时使用)
		Map<String, Object> counterInfo = binOTYIN14_Service.getCounterInfo(map);
		if(counterInfo != null && !counterInfo.isEmpty()){
			//品牌编码
			String brandCode = ConvertUtil.getString(counterInfo.get("BrandCode"));
			if("".equals(brandCode)){
				//抛出自定义异常：组装消息体时失败，品牌代码为空！
				throw new CherryException("EOT00031");
			}
			//柜台代码
			String counterCode = ConvertUtil.getString(counterInfo.get("CounterCode"));
			if("".equals(counterCode)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台编码！
				throw new CherryException("EOT00025"); 
			}
			//柜台名称
			String counterName = ConvertUtil.getString(counterInfo.get("CounterName"));
			if("".equals(counterName)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台名称！
				throw new CherryException("EOT00026"); 
			}
			//柜台协同区分
			String counterSynergyFlag =ConvertUtil.getString(counterInfo.get("CounterSynergyFlag"));
			if("".equals(counterSynergyFlag)){
				counterSynergyFlag="0";
			}
			//柜台类型
			String counterKind = ConvertUtil.getString(counterInfo.get("counterKind"));
			if("".equals(counterKind)){
				//抛出自定义异常：组装MQ消息体时失败，没有查询出柜台类型！
				throw new CherryException("EOT00027"); 
			}
			//柜台有效性区分
			String validFlag = ConvertUtil.getString(counterInfo.get("ValidFlag"));
			if("".equals(validFlag)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台有效性区分！
				throw new CherryException("EOT00028"); 
			}
			// 到期日
			String expiringDate = ConvertUtil.getString(counterInfo.get("ExpiringDate"));
			if("".equals(expiringDate)){
				//抛出自定义异常：组装消息体时失败，没有查询出柜台的到期日！
				throw new CherryException("EOT00038"); 
			}
			//品牌代码
			counterWSMap.put("BrandCode", brandCode);
			//业务类型
			counterWSMap.put("BussinessType", "Counter");
			//消息体版本号
			counterWSMap.put(MessageConstants.MESSAGE_VERSION_TITLE, "1.0");
			//柜台代码
			counterWSMap.put("CounterCode", counterCode);
			//柜台名称
			counterWSMap.put("CounterName", counterName);
			//区域代码
			counterWSMap.put("RegionCode", ConvertUtil.getString(counterInfo.get("RegionCode")));
			//区域名称
			counterWSMap.put("RegionName", ConvertUtil.getString(counterInfo.get("RegionName")));
			//城市代码
			counterWSMap.put("CityCode", ConvertUtil.getString(counterInfo.get("Citycode")));
			//城市名称
			counterWSMap.put("CityName", ConvertUtil.getString(counterInfo.get("CityName")));
			//渠道名称
			counterWSMap.put("ChannelName", ConvertUtil.getString(counterInfo.get("Channel")));
			//经销商编码
			counterWSMap.put("AgentCode", ConvertUtil.getString(counterInfo.get("AgentCode")));
			//经销商名称
			counterWSMap.put("AgentName", ConvertUtil.getString(counterInfo.get("AgentName")));
			//柜台密码
			counterWSMap.put("PassWord", ConvertUtil.getString(counterInfo.get("PassWord")));
			//柜台协同区分
			counterWSMap.put("CounterSynergyFlag", counterSynergyFlag);
			//柜台类型
			counterWSMap.put("CounterKind", counterKind);
			//柜台有效性区分
			counterWSMap.put("ValidFlag", validFlag);
			//到期日
			counterWSMap.put("ExpiringDate", expiringDate);
		}else{
			//抛出自定义异常：组装MQ消息体是出错，没有查询出柜台信息！
			throw new CherryException("EOT00029");
		}
		
		return counterWSMap;
	}
	
	/**
	 * 特殊字符处理
	 * @param counterMap
	 */
	private void screenMap(Map<String, Object> counterMap){
		
		String counterName = CherryBatchUtil.getString(counterMap.get(CounterConstants.COUNTER_NAME));
		
		// 替换半角单引号为全角--防止SQL注入
		counterName = counterName.replaceAll("'", "’");
		// 替代连续双横杠为中文双横杠--防止SQL注入
		counterName = counterName.replaceAll("--", "——");
		
		counterMap.put("counterName", counterName);
		
	}
	
	/**
	 * init
	 * @param map
	 */
	private void init(Map<String, Object> map){
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT109");
		
		// 程序【开始运行时间】
		String runStartTime = binOTYIN14_Service.getSYSDateTime();
		map.put("RunStartTime", runStartTime);
		
		// 系统时间
		String sysDate = binOTYIN14_Service.getSYSDate();
		// 作成日时
		comMap.put(CherryConstants.CREATE_TIME, sysDate);
		
		// 品牌Code
		String branCode = binOTYIN14_Service.getBrandCode(map);
		// 品牌Code
		comMap.put(CherryConstants.BRAND_CODE, branCode);
		
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN14");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN14");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		return baseMap;
	}
	
	/**
	 * 程序结束时，处理Job共通(插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		paraMap.putAll(comMap);
		 
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
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
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		// 失败柜台编码SAPCode集合
		if(!CherryBatchUtil.isBlankList(faildCounterList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00022");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildCounterList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "柜台导入部分数据处理失败，具体见log日志";
		}
		
	}
	

}
