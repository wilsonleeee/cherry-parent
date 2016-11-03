/*
 * @(#)BINBEPLDPL02_BL.java     1.0 2010/11/04
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

package com.cherry.pl.dpl.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.pl.dpl.service.BINBEPLDPL01_Service;
import com.cherry.pl.dpl.service.BINBEPLDPL02_Service;

/**
 * 岗位数据过滤权限共通BL
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINBEPLDPL02_BL {
	
	/** 岗位数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL02_Service binBEPLDPL02_Service;
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINBEPLDPL01_Service binBEPLDPL01_Service;
	
	/** 部门数据过滤权限共通BL */
	@Resource
	private BINBEPLDPL01_BL binBEPLDPL01_BL;
	
	/**
	 * 创建岗位数据过滤权限
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_createDataPrivilege(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		
		// 权限配置信息对象
		Map<String, Object> privilegeConfInfo = new HashMap<String, Object>();
		try {
			// 取得权限配置信息
			privilegeConfInfo = binBEPLDPL01_BL.getPrivilegeConfInfo();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00007");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		// 权限配置信息不存在
		if(privilegeConfInfo == null || privilegeConfInfo.isEmpty()) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00008");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		try {
//			// 人员权限从真实表复制到临时表
//			binBEPLDPL02_Service.copyEmployeePrivilegeToTemp(map);
			// 清空人员权限临时表数据
			binBEPLDPL02_Service.truncateEmployeePrivilegeTemp(map);
			binBEPLDPL02_Service.manualCommit();
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00026");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			flag = CherryBatchConstants.BATCH_WARNING;
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "employeeId");
		while (true) {
			// 查询开始位置
			int startNum = dataSize * currentNum + 1;
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, startNum + dataSize - 1);
			// 数据抽出次数累加
			currentNum++;
			// 查询所有的员工信息
			List<Map<String, Object>> employeeList = binBEPLDPL01_Service.getEmployeeList(map);
			if(employeeList != null && employeeList.size() > 0) {
				for(int i = 0; i < employeeList.size(); i++) {
					Map<String, Object> employeeMap = employeeList.get(i);
					// 用户ID
					Object userId = employeeMap.get("userId");
					// 雇员ID
					Object employeeId = employeeMap.get("employeeId");
					// 所属部门
					Object type = employeeMap.get("type");
					// 所属岗位
					Object positionCategoryId = employeeMap.get("positionCategoryId");
					// 临时保存权限对象
					Map<Integer, Object> privilegeMap = new HashMap<Integer, Object>();
					try {
						// 取得员工的权限配置信息
						List<Map<String, Object>> empPlConfInfoList = binBEPLDPL01_BL.getEmpPlConfInfo(privilegeConfInfo, employeeMap);
						if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
							
//							// 删除岗位数据过滤权限
//							binBEPLDPL02_Service.deletePositionPrivilege(employeeMap);
							for(int j = 0; j < empPlConfInfoList.size(); j++) {
								Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
								// 业务类型
								String businessType = (String)empPlConfInfoMap.get("businessType");
								// 操作类型
								String operationType = (String)empPlConfInfoMap.get("operationType");
								// 权限类型
								int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");
								// 用户拥有的所有权限
								List<Map<String, Object>> privilegeList = null;
								// 权限已经存在临时对象中
								if(privilegeMap.containsKey(privilegeType)) {
									privilegeList = (List)privilegeMap.get(privilegeType);
								} else {
									Map<String, Object> paramMap = new HashMap<String, Object>();
									// 雇员ID
									paramMap.put("employeeId", employeeId);
									// 权限类型
									paramMap.put("privilegeType", privilegeType);
									// 查询该用户的所有岗位权限
									privilegeList = getPositionIDList(paramMap);
									// 把岗位权限储存在临时对象中
									privilegeMap.put(privilegeType, privilegeList);
								}
								if(privilegeList != null && !privilegeList.isEmpty()) {
									for(int n = 0; n < privilegeList.size(); n++) {
										Map<String, Object> dataPrivilegeMap = privilegeList.get(n);
										// 雇员ID
										dataPrivilegeMap.put("employeeId", employeeId);
										if(userId != null) {
											// 用户ID
											dataPrivilegeMap.put("userId", userId);
										}
										// 业务类型
										dataPrivilegeMap.put("businessType", businessType);
										// 操作类型
										dataPrivilegeMap.put("operationType", operationType);
										// 作成者
										dataPrivilegeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
										// 作成程序名
										dataPrivilegeMap.put(CherryBatchConstants.CREATEPGM, "BINBEPLDPL01");
										// 更新者
										dataPrivilegeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
										// 更新程序名
										dataPrivilegeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEPLDPL01");
									}
									try {
										// 添加岗位数据过滤权限
										binBEPLDPL02_Service.addPositionPrivilege(privilegeList);
										privilegeList = null;
									} catch (Exception e) {
										BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
										batchExceptionDTO.setBatchName(this.getClass());
										batchExceptionDTO.setErrorCode("EPL00003");
										batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
										// 雇员ID
										batchExceptionDTO.addErrorParam(employeeId.toString());
										// 业务类型
										batchExceptionDTO.addErrorParam(businessType);
										// 操作类型
										batchExceptionDTO.addErrorParam(operationType);
										batchExceptionDTO.setException(e);
										throw new CherryBatchException(batchExceptionDTO);
									}
								}
							}
						} else {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EPL00009");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 雇员ID
							batchExceptionDTO.addErrorParam(employeeId.toString());
							// 岗位类别
							if(positionCategoryId != null) {
								batchExceptionDTO.addErrorParam(positionCategoryId.toString());
							} else {
								batchExceptionDTO.addErrorParam("");
							}
							// 部门类型
							if(type != null) {
								batchExceptionDTO.addErrorParam(type.toString());
							} else {
								batchExceptionDTO.addErrorParam("");
							}
							throw new CherryBatchException(batchExceptionDTO);
						}
						successCount++;
					} catch (CherryBatchException cherryBatchException) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
					} catch (Exception e) {
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EPL00004");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(employeeId.toString());
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
					privilegeMap = null;
				}
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(employeeList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		try {
			// 人员权限从临时表复制到真实表
			binBEPLDPL02_Service.copyEmployeePrivilege(map);
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEPLDPL02_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EPL00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode("IPL00003");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(successCount));
		batchLoggerDTO.addParam(String.valueOf(failCount));
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		
		return flag;
	}
	
	/**
	 * 
	 * 查询某一用户管辖的所有岗位ID
	 * 
	 * @param map 查询条件
	 * 
	 */
	public List<Map<String, Object>> getPositionIDList(Map<String, Object> map) {
		
		if(map.get("privilegeType") == null) 
			return null;
		// 权限类型
		int privilegeType = (Integer)map.get("privilegeType");
		if(privilegeType == 0) {
			// 查询某一用户管辖的所有岗位ID(权限类型为0时)
			return binBEPLDPL02_Service.getPositionID0List(map);
		} else if(privilegeType == 1) {
			// 查询某一用户管辖的所有岗位ID(权限类型为1时)
			return binBEPLDPL02_Service.getPositionID1List(map);
		} else if(privilegeType == 2) {
			// 查询某一用户管辖的所有岗位ID(权限类型为2时)
			return binBEPLDPL02_Service.getPositionID2List(map);
		} else if(privilegeType == 3) {
			// 查询某一用户管辖的所有岗位ID(权限类型为3时)
			return binBEPLDPL02_Service.getPositionID1List(map);
		} else {
			return null;
		}
	}

}
