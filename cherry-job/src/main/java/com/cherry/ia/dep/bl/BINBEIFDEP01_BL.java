/*
 * @(#)BINBEIFDEP01_BL.java     1.0 2010/11/12
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

package com.cherry.ia.dep.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ia.dep.service.BINBEIFDEP01_Service;

/**
 * 
 * 部门列表导入BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFDEP01_BL {
	
	/** 部门列表导入Service */
	@Resource
	private BINBEIFDEP01_Service binBEIFDEP01_Service;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;

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
	/** 删除条数 */
	private int delCount = 0;
	
	/**
	 * 
	 * 部门列表导入batch主处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_batchDepart(Map<String, Object> map) throws CherryBatchException {
		
		// 世代管理和备份Cherry数据库中的部门信息
		try {
			// 世代管理上限
			map.put("count", CherryBatchConstants.COUNT);
			// 更新世代番号
			binBEIFDEP01_Service.updateBackupCount(map);
			// 删除世代番号超过上限的数据
			binBEIFDEP01_Service.clearBackupData(map);
			// 备份部门信息表
			binBEIFDEP01_Service.backupDepart(map);
			// Cherry数据库事务提交
			binBEIFDEP01_Service.manualCommit();
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF01000");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw new CherryBatchException(batchExceptionDTO);
		}

		// 从接口数据库中分批取得部门信息，并处理
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "level");
		while (true) {
			// 查询开始位置
			startNum = dataSize * currentNum + 1;
			// 查询结束位置
			endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, endNum);
			// 取得部门信息List
			List<Map<String, Object>> departList = binBEIFDEP01_Service.getDepartList(map);
			// 部门数据不为空
			if (!CherryBatchUtil.isBlankList(departList)) {
				// 统计总条数
				totalCount += departList.size();
				// 把接口数据库中取得的部门信息导入到Cherry数据库中
				updateDepart(departList, map);
				// 部门数据少于一次抽取的数量，即为最后一页，跳出循环
				if(departList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 找出没有更新的数据，并伦理删除
		// 查询需要伦理删除的部门数据
		List<Map<String, Object>> delList = binBEIFDEP01_Service.getDelDepartList(map);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			// 统计需要删除的总件数
			delTotalCount += delList.size();
			// 取得系统时间
			String sysDate = binBEIFDEP01_Service.getSYSDate();
			for (Map<String, Object> delMap : delList) {
				// 部门代号
				String departCode = CherryBatchUtil.getString(delMap.get("departCode"));
				// 部门姓名
				String departName = CherryBatchUtil.getString(delMap.get("departName"));
				try {
					// 设置更新时间
					delMap.put(CherryBatchConstants.UPDATE_TIME, sysDate);
					// 更新者
					delMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					delMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFDEP01");
					// 伦理删除无效的部门数据
					binBEIFDEP01_Service.delInvalidDepart(delMap);
					// 统计删除条数
					delCount++;
					// Cherry数据库事务提交
					binBEIFDEP01_Service.manualCommit();
				} catch (Exception e) {
					try {
						// Cherry数据库事务回滚
						binBEIFDEP01_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF01004");
					// 部门代号
					batchLoggerDTO1.addParam(departCode);
					// 部门姓名
					batchLoggerDTO1.addParam(departName);
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				}
			}
		}
		
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
					binBEIFDEP01_Service.manualCommit();
				}
			}
		} catch (Exception e) {
			try {
				// Cherry数据库事务回滚
				binBEIFDEP01_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF01006");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
		
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
		loggerDelDTO1.setCode("IIF01001");
		loggerDelDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO1.addParam(String.valueOf(delTotalCount));
		// 删除件数
		BatchLoggerDTO loggerDelDTO2 = new BatchLoggerDTO();
		loggerDelDTO2.setCode("IIF01002");
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
		return flag;
	}
	
	/**
	 * 
	 * 把接口数据库中取得的部门信息导入到Cherry数据库中
	 * 
	 * @param departList 接口数据库中的部门信息List
	 * @param map 组织ID、品牌ID等其他参数
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void updateDepart(List<Map<String, Object>> departList, Map<String, Object> map)
			throws CherryBatchException {
		
		// 取得品牌ID
		Object brandId = map.get(CherryBatchConstants.BRANDINFOID);
		// 取得组织ID
		Object orgInfoId = map.get(CherryBatchConstants.ORGANIZATIONINFOID);

		// 循环导入部门数据到Cherry数据库中
		for (Map<String, Object> departMap : departList) {
			// 部门代号
			String departCode = CherryBatchUtil.getString(departMap.get("departCode"));
			// 部门姓名
			String departName = CherryBatchUtil.getString(departMap.get("departName"));
			// 部门类型
			String departType = (String)departMap.get("type");
			try {
				// 作成者
				departMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 作成程序名
				departMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFDEP01");
				// 更新者
				departMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				departMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFDEP01");
				// 把品牌Id设入条件map
				departMap.put(CherryBatchConstants.BRANDINFOID, brandId);
				// 把所属组织Id设入条件map
				departMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
				// 到期日expiringDate
				departMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
				// 取得上级部门代码
				String seniorDepartCode = (String)departMap.get("seniorDepartCode");
				// 上级部门节点
				Object seniorDepPath = null;
				if(seniorDepartCode != null && !"".equals(seniorDepartCode)) {
					// 查询部门上级节点
					seniorDepPath = binBEIFDEP01_Service.getSeniorDepPath(departMap);
				}
				// 上级部门节点不为空的场合
				if(seniorDepPath != null) {
					departMap.put("path", seniorDepPath);
				} else {
					// 取得未知节点来作为部门的上级节点
					Object unknownPath = map.get("unknownPath");
					// 未知节点为空的场合
					if(unknownPath == null) {
						// 取得品牌下的未知节点
						unknownPath = binBEIFDEP01_Service.getUnknownPath(departMap);
						if(unknownPath == null) {
							// 在品牌下添加一个未知节点来作为没有上级部门的部门的上级节点
							Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
							// 作成者
							unknownOrgMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 作成程序名
							unknownOrgMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFDEP01");
							// 更新者
							unknownOrgMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 更新程序名
							unknownOrgMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFDEP01");
							// 未知节点的品牌ID
							unknownOrgMap.put(CherryBatchConstants.BRANDINFOID, brandId);
							// 未知节点的组织ID
							unknownOrgMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
							// 未知节点添加在品牌节点下
							unknownOrgMap.put("path", binBEIFDEP01_Service.getFirstPath(departMap));
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
						map.put("unknownPath", unknownPath);
					}
					departMap.put("path", unknownPath);
				}
				// 设置新节点
				departMap.put("nodeId", binBEIFDEP01_Service.getNewDepNodeId(departMap));
				// 更新件数
				int result = 0;
				try {
					// 更新部门信息
					result = binBEIFDEP01_Service.updateDepart(departMap);
				} catch (Exception e) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					batchExceptionDTO.setErrorCode("EIF01001");
					// 部门代号
					batchExceptionDTO.addErrorParam(departCode);
					// 部门姓名
					batchExceptionDTO.addErrorParam(departName);
					batchExceptionDTO.setException(e);
					throw new CherryBatchException(batchExceptionDTO);
				}
				// 更新0件的场合，添加新部门
				if(result == 0) {
					try {
						// 插入部门信息
						Object departId = binBEIFDEP01_Service.insertDepart(departMap);
						// 所属部门
						departMap.put("organizationId", departId);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						batchExceptionDTO.setErrorCode("EIF01002");
						// 部门代号
						batchExceptionDTO.addErrorParam(departCode);
						// 部门姓名
						batchExceptionDTO.addErrorParam(departName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					try {
						// 缺省仓库区分
						departMap.put("defaultFlag", CherryBatchConstants.IVT_DEFAULTFLAG);
						// 仓库名称
						departMap.put("inventoryNameCN", departName+CherryBatchConstants.IVT_NAME_CN_DEFAULT);
						// 部门类型为柜台的场合
						if(departType != null && "4".equals(departType)) {
							// 设定仓库类型为柜台仓库
							departMap.put("depotType", "02");
						} else {
							// 设定仓库类型为非柜台仓库
							departMap.put("depotType", "01");
							departMap.put("validFlagVal", "0");
						}
						// 仓库编码类型（仓库为3）
						departMap.put("type", CherryBatchConstants.IVT_CODE_TYPE);
						// 仓库编码最小长度
						departMap.put("length", CherryBatchConstants.IVT_CODE_LEN);
						// 自动生成仓库编码
						departMap.put("inventoryCode", CherryBatchConstants.IVT_CODE_PREFIX+binOLCM15_BL.getSequenceId(departMap));
						// 判断仓库编码是否已经存在
						int depotCount = binBEIFDEP01_Service.getDepotCountByCode(departMap);
						while(depotCount > 0) {
							// 自动生成仓库编码
							departMap.put("inventoryCode", CherryBatchConstants.IVT_CODE_PREFIX+binOLCM15_BL.getSequenceId(departMap));
							// 判断仓库编码是否已经存在
							depotCount = binBEIFDEP01_Service.getDepotCountByCode(departMap);
						}
						// 添加仓库
						int depotInfoId = binBEIFDEP01_Service.addDepotInfo(departMap);
						departMap.put("depotInfoId", depotInfoId);
						departMap.put("validFlagVal", "1");
						// 添加部门仓库关系
						binBEIFDEP01_Service.addInventoryInfo(departMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						batchExceptionDTO.setErrorCode("EIF01005");
						// 部门代号
						batchExceptionDTO.addErrorParam(departCode);
						// 部门姓名
						batchExceptionDTO.addErrorParam(departName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					// 插入件数加一
					insertCount++;
				} else {
					// 更新件数加一
					updateCount++;
				}
				// Cherry数据库事务提交
				binBEIFDEP01_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					// Cherry数据库事务回滚
					binBEIFDEP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库事务回滚
					binBEIFDEP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EIF01003");
				// 部门代号
				batchLoggerDTO.addParam(departCode);
				// 部门姓名
				batchLoggerDTO.addParam(departName);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			} catch (Throwable t) {
				try {
					// Cherry数据库事务回滚
					binBEIFDEP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

}
