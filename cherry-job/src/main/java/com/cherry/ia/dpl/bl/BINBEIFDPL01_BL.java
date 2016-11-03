/*
 * @(#)BINBEIFDPL01_BL.java     1.0 2010/07/04
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

package com.cherry.ia.dpl.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ia.dpl.service.BINBEIFDPL01_Service;

/**
 * 
 * 员工管辖部门数据导入BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.07.04
 */
public class BINBEIFDPL01_BL {
	
	/** 员工管辖部门数据导入Service */
	@Resource
	private BINBEIFDPL01_Service binBEIFDPL01_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	/** 成功条数 */
	private int successCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 
	 * 员工管辖部门数据导入batch主处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchEmpDepart(Map<String, Object> map) throws CherryBatchException {
		
		// 从接口数据库中查询员工管辖部门对应关系
		List<Map<String, Object>> empDepartList = binBEIFDPL01_Service.getEmpDepartList(map);
		// 员工管辖部门对应关系存在的场合
		if(empDepartList != null && !empDepartList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"employeeCode"};
			keyList.add(key1);
			CherryBatchUtil.convertList2DeepList(empDepartList,list,keyList,0);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> empMap = list.get(i);
				String employeeCode = (String)empMap.get("employeeCode");
				try {
					map.put("employeeCode", employeeCode);
					// 查询员工信息
					Map<String, Object> empInfo = binBEIFDPL01_Service.getEmpInfo(map);
					if(empInfo != null && !empInfo.isEmpty()) {
						Object employeeId = empInfo.get("employeeId");
						empInfo.put("manageType", "1");
						// 删除员工管辖部门对应关系
						binBEIFDPL01_Service.delEmployeeDepart(empInfo);
						List<Map<String, Object>> departList = (List)empMap.get("list");
						if(departList != null && !departList.isEmpty()) {
							List<Map<String, Object>> insertEmpDepartList = new ArrayList<Map<String,Object>>();
							for(int j = 0; j < departList.size(); j++) {
								String departCode = (String)departList.get(j).get("departCode");
								map.put("departCode", departCode);
								// 查询部门信息
								Map<String, Object> departInfo = binBEIFDPL01_Service.getDepartInfo(map);
								if(departInfo != null && !departInfo.isEmpty()) {
									// 作成者
									departInfo.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
									// 作成程序名
									departInfo.put(CherryBatchConstants.CREATEPGM, "BINBEIFDPL01");
									// 更新者
									departInfo.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
									// 更新程序名
									departInfo.put(CherryBatchConstants.UPDATEPGM, "BINBEIFDPL01");
									departInfo.put("employeeId", employeeId);
									departInfo.put("manageType", "1");
									insertEmpDepartList.add(departInfo);
								} else {
									BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
									batchExceptionDTO.setBatchName(this.getClass());
									batchExceptionDTO.setErrorCode("EIF05002");
									batchExceptionDTO
											.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
									// 员工代号
									batchExceptionDTO.addErrorParam(employeeCode);
									// 部门代号
									batchExceptionDTO.addErrorParam(departCode);
									throw new CherryBatchException(batchExceptionDTO);
								}
							}
							empMap.put("organizationIdList", insertEmpDepartList);
							// 删除指定部门的员工管辖部门对应关系
							binBEIFDPL01_Service.delEmployeeDepartByOrg(empMap);
							try {
								// 插入员工管辖部门对应表
								binBEIFDPL01_Service.insertEmployeeDepart(insertEmpDepartList);
							} catch (Exception e) {
								BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
								batchExceptionDTO.setBatchName(this.getClass());
								batchExceptionDTO.setErrorCode("EIF05003");
								batchExceptionDTO
										.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
								// 员工代号
								batchExceptionDTO.addErrorParam(employeeCode);
								batchExceptionDTO.setException(e);
								throw new CherryBatchException(batchExceptionDTO);
							}
						}
					} else {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF05001");
						batchExceptionDTO
								.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 员工代号
						batchExceptionDTO.addErrorParam(employeeCode);
						throw new CherryBatchException(batchExceptionDTO);
					}
					successCount++;
					// 事务提交
					binBEIFDPL01_Service.manualCommit();
				} catch (CherryBatchException cherryBatchException) {
					try {
						// 事务回滚
						binBEIFDPL01_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					// 失败件数加一
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
				} 
				catch (Exception e) {
					try {
						// 事务回滚
						binBEIFDPL01_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					// 失败件数加一
					failCount++;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EIF05004");
					// 员工代号
					batchLoggerDTO.addParam(employeeCode);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
							this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					flag = CherryBatchConstants.BATCH_WARNING;
				} catch (Throwable t) {
					try {
						// 事务回滚
						binBEIFDPL01_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					// 失败件数加一
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount+failCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
				.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		return flag;
	}

}
