/*
 * @(#)BINBEIFEMP02_BL.java     1.0 2010/11/12
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

package com.cherry.ia.emp.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ia.emp.service.BINBEIFEMP02_Service;

/**
 * 
 * 员工列表导入BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP02_BL {
	
	/** 员工列表导入Service */
	@Resource
	private BINBEIFEMP02_Service binBEIFEMP02_Service;

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
	 * 员工列表导入batch主处理
	 * 
	 * @param map 组织ID、品牌ID等参数
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_batchEmployee(Map<String, Object> map) throws CherryBatchException {
		
		// 世代管理和备份Cherry数据库中的员工信息
		try {
			// 世代管理上限
			map.put("count", CherryBatchConstants.COUNT);
			// 更新世代番号
			binBEIFEMP02_Service.updateBackupCount(map);
			// 删除世代番号超过上限的数据
			binBEIFEMP02_Service.clearBackupData(map);
			// 备份员工信息表
			binBEIFEMP02_Service.backupEmployee(map);
			// Cherry数据库事务提交
			binBEIFEMP02_Service.manualCommit();
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF03001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw new CherryBatchException(batchExceptionDTO);
		}

		// 从品牌数据库中分批取得员工列表，并处理
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
			// 从接口数据库中查询员工信息List
			List<Map<String, Object>> employeeList = binBEIFEMP02_Service.getEmployeesList(map);
			// 员工数据不为空
			if (!CherryBatchUtil.isBlankList(employeeList)) {
				// 统计总条数
				totalCount += employeeList.size();
				// 把接口数据库中取得的员工信息导入到Cherry数据库中
				updateEmployee(employeeList, map);
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(employeeList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 找出没有更新的数据，并伦理删除
		// 查询需要伦理删除的员工数据
		List<Map<String, Object>> delList = binBEIFEMP02_Service.getDelList(map);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			// 统计需要删除的总件数
			delTotalCount += delList.size();
			// 取得系统时间
			String sysDate = binBEIFEMP02_Service.getSYSDate();
			for (Map<String, Object> delMap : delList) {
				// 取得员工代码
				String employeeCode = (String)delMap.get("employeeCode");
				// 取得员工姓名
				String employeeName = (String)delMap.get("employeeName");
				try {
					// 设置更新时间
					delMap.put(CherryBatchConstants.UPDATE_TIME, sysDate);
					// 更新者
					delMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					delMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFEMP02");
					// 删除无效的员工数据
					binBEIFEMP02_Service.delInvalidEmployees(delMap);
					// 更新员工入退职信息
					binBEIFEMP02_Service.delInvalidEmployeeQuit(delMap);
					// 取得用户ID
					Object userId = delMap.get("userId");
					// 用户存在的场合，删除用户信息
					if(userId != null) {
						// 删除无效的用户数据
						binBEIFEMP02_Service.delInvalidUser(delMap);
						// 删除无效的用户数据(配置数据库)
						binBEIFEMP02_Service.delInvalidUserConf(delMap);
					}
					// 统计删除条数
					delCount++;
					// Cherry数据库事务提交
					binBEIFEMP02_Service.manualCommit();
				} catch (Exception e) {
					try {
						// Cherry数据库事务回滚
						binBEIFEMP02_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF03006");
					// 员工代码
					batchLoggerDTO1.addParam(employeeCode);
					// 员工姓名
					batchLoggerDTO1.addParam(employeeName);
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				}
			}
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
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 需要删除的总件数
		BatchLoggerDTO loggerDelDTO1 = new BatchLoggerDTO();
		loggerDelDTO1.setCode("IIF03001");
		loggerDelDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO1.addParam(String.valueOf(delTotalCount));
		// 删除件数
		BatchLoggerDTO loggerDelDTO2 = new BatchLoggerDTO();
		loggerDelDTO2.setCode("IIF03002");
		loggerDelDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO2.addParam(String.valueOf(delCount));

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
	 * 把接口数据库中取得的员工信息导入到Cherry数据库中
	 * 
	 * @param employeeList 接口数据库中的员工信息List
	 * @param map 组织ID、品牌ID等参数
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void updateEmployee(List<Map<String, Object>> employeeList, Map<String, Object> map)
			throws CherryBatchException {
		
		// 取得品牌ID
		Object brandId = map.get(CherryBatchConstants.BRANDINFOID);
		// 取得组织ID
		Object orgInfoId = map.get(CherryBatchConstants.ORGANIZATIONINFOID);

		// 循环导入员工数据到Cherry数据库中
		for (Map<String, Object> employeeMap : employeeList) {
			// 取得员工代码
			String employeeCode = (String)employeeMap.get("employeeCode");
			// 取得员工姓名
			String employeeName = (String)employeeMap.get("employeeName");
			try {
				// 作成者
				employeeMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 作成程序名
				employeeMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFEMP02");
				// 更新者
				employeeMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				employeeMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFEMP02");
				// 把品牌Id设入条件map
				employeeMap.put(CherryBatchConstants.BRANDINFOID, brandId);
				// 把所属组织Id设入条件map
				employeeMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
				// 取得部门代码
				String departCode = (String)employeeMap.get("departCode");
				// 部门代码存在的场合，取得部门ID
				if(departCode != null && !"".equals(departCode)) {
					// 查询员工部门ID
					Map<String, Object> orgInfo = binBEIFEMP02_Service.getOrganization(employeeMap);
					if(orgInfo != null) {
						// 把部门ID设置到条件map中
						employeeMap.put("organizationId", CherryBatchUtil.getString(orgInfo.get("organizationId")));
						// 把部门类型设置到条件map中
						employeeMap.put("departType", CherryBatchUtil.getString(orgInfo.get("type")));
					}
				}
				// 取得岗位代号
				String positionCode = (String)employeeMap.get("positionCode");
				// 岗位代号存在的场合，取得岗位类别ID
				if(positionCode != null && !"".equals(positionCode)) {
					// 查询员工岗位类别ID
					Object posId = binBEIFEMP02_Service.getPositionCategoryId(employeeMap);
					if(posId != null) {
						// 把岗位类别ID设置到条件map中
						employeeMap.put("positionCategoryId", posId);
					}
				}
				// 取得部门ID
				String organizationId = (String)employeeMap.get("organizationId");
//				// 员工的所属部门不存在的场合
//				if(organizationId == null) {
//					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
//					batchExceptionDTO.setBatchName(this.getClass());
//					batchExceptionDTO.setErrorCode("EIF03007");
//					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
//					// 员工代号
//					batchExceptionDTO.addErrorParam(employeeCode);
//					// 员工姓名
//					batchExceptionDTO.addErrorParam(employeeName);
//					throw new CherryBatchException(batchExceptionDTO);
//				}
				
				// 取得上级员工代号
				String seniorEmployeeCode = (String)employeeMap.get("seniorEmployeeCode");
				if(seniorEmployeeCode != null) {
					// 查询员工上级节点
					Object seniorEmpPath = binBEIFEMP02_Service.getSeniorEmpPath(employeeMap);
					if(seniorEmpPath != null) {
						employeeMap.put("path", seniorEmpPath);
					} else {
						employeeMap.put("path", "/");
					}
				} else {
					employeeMap.put("path", "/");
				}
				employeeMap.put("nodeId", binBEIFEMP02_Service.getNewEmpNodeId(employeeMap));
				
				// 取得员工信息
				Map<String, Object> employeeInfo = binBEIFEMP02_Service.getEmployeeInfo(employeeMap);
				// 员工信息存在的场合，更新员工信息，否则添加员工信息
				if(employeeInfo != null) {
					try {
						employeeMap.put("employeeId", employeeInfo.get("employeeId"));
						// 更新员工信息
						binBEIFEMP02_Service.updateEmployee(employeeMap);
						if(organizationId != null && !"".equals(organizationId)) {
							// 更新员工管辖部门对应表
							binBEIFEMP02_Service.updateEmployeeDepart(employeeMap);
						}
						// 取得用户ID
						Object userId = employeeInfo.get("userId");
						// 用户存在的场合，更新用户信息
						if(userId != null) {
							employeeMap.put("userId", userId);
							employeeMap.put("longinName", employeeInfo.get("longinName"));
							// 更新用户信息 
							binBEIFEMP02_Service.updateUser(employeeMap);
							// 更新用户信息(配置数据库)
							binBEIFEMP02_Service.updateUserConf(employeeMap);
						}
						// 统计更新件数
						updateCount++;
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF03003");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 员工代号
						batchExceptionDTO.addErrorParam(employeeCode);
						// 员工姓名
						batchExceptionDTO.addErrorParam(employeeName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
				} else {
					try {
						// 插入员工信息
						int employeeId = binBEIFEMP02_Service.insertEmployee(employeeMap);
						employeeMap.put("employeeId", employeeId);
						if(organizationId != null && !"".equals(organizationId)) {
							// 插入员工管辖部门对应表
							binBEIFEMP02_Service.insertEmployeeDepart(employeeMap);
						}
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF03002");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 员工代号
						batchExceptionDTO.addErrorParam(employeeCode);
						// 员工姓名
						batchExceptionDTO.addErrorParam(employeeName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					try {
						// 插入信息到员工入退职信息表
						binBEIFEMP02_Service.insertEmployeeQuit(employeeMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF03004");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 员工代号
						batchExceptionDTO.addErrorParam(employeeCode);
						// 员工姓名
						batchExceptionDTO.addErrorParam(employeeName);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					// 统计插入件数
					insertCount++;
				}
				// Cherry数据库事务提交
				binBEIFEMP02_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP02_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP02_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EIF03005");
				// 员工代号
				batchLoggerDTO.addParam(employeeCode);
				// 员工姓名
				batchLoggerDTO.addParam(employeeName);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			} catch (Throwable t) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP02_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

}
