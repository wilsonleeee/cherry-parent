/*
 * @(#)BINBEIFEMP01_BL.java     1.0 2010/11/12
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
import com.cherry.ia.emp.service.BINBEIFEMP01_Service;
import com.cherry.ia.emp.service.BINBEIFEMP02_Service;

/**
 * 
 * 营业员列表导入BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP01_BL {
	
	/** 营业员列表导入Service */
	@Resource
	private BINBEIFEMP01_Service binBEIFEMP01_Service;
	
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
	 * 营业员列表导入batch主处理
	 * 
	 * @param map 组织ID、品牌ID等参数
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public int tran_batchBaInfo(Map<String, Object> map) throws CherryBatchException {
		
		// 世代管理和备份Cherry数据库中的营业员信息
		try {
			// 世代管理上限
			map.put("count", CherryBatchConstants.COUNT);
			// 更新世代番号
			binBEIFEMP01_Service.updateBackupCount(map);
			// 删除世代番号超过上限的数据
			binBEIFEMP01_Service.clearBackupData(map);
			// 备份营业员信息表
			binBEIFEMP01_Service.backupBaInfo(map);
			// Cherry数据库事务提交
			binBEIFEMP01_Service.manualCommit();
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EIF04001");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_WARNING;
			throw new CherryBatchException(batchExceptionDTO);
		}

		// 从接口数据库中分批取得营业员列表，并处理
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "flag");
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
			// 从接口数据库中取得营业员信息List
			List<Map<String, Object>> baInfoList = binBEIFEMP01_Service.getBaInfoList(map);
			// 营业员数据不为空
			if (!CherryBatchUtil.isBlankList(baInfoList)) {
				// 统计总条数
				totalCount += baInfoList.size();
				// 把接口数据库中取得的营业员信息导入到Cherry数据库中
				updateBaInfo(baInfoList, map);
				// 营业员数据少于一次抽取的数量，即为最后一页，跳出循环
				if(baInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 找出没有更新的数据，并伦理删除
		// 查询需要伦理删除的营业员数据
		List<Map<String, Object>> delList = binBEIFEMP01_Service.getDelList(map);
		// 执行删除操作
		if (!CherryBatchUtil.isBlankList(delList)) {
			// 统计需要删除的总件数
			delTotalCount += delList.size();
			// 取得系统时间
			String sysDate = binBEIFEMP01_Service.getSYSDate();
			for (Map<String, Object> delMap : delList) {
				// 取得营业员代码
				String baCode = (String)delMap.get("baCode");
				// 取得营业员姓名
				String baName = (String)delMap.get("baName");
				try {
					// 设置更新时间
					delMap.put(CherryBatchConstants.UPDATE_TIME, sysDate);
					// 更新者
					delMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					delMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFEMP01");
					// 删除无效的营业员数据
					binBEIFEMP01_Service.delInvalidBaInfo(delMap);
					// 删除无效的员工信息
					binBEIFEMP02_Service.delInvalidEmployees(delMap);
					// 更新员工入退职信息
					binBEIFEMP02_Service.delInvalidEmployeeQuit(delMap);
					// 统计删除条数
					delCount++;
					// Cherry数据库事务提交
					binBEIFEMP01_Service.manualCommit();
				} catch (Exception e) {
					try {
						// Cherry数据库事务回滚
						binBEIFEMP01_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EIF04005");
					// 营业员代码
					batchLoggerDTO1.addParam(baCode);
					// 营业员姓名
					batchLoggerDTO1.addParam(baName);
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
		loggerDelDTO1.setCode("IIF04001");
		loggerDelDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		loggerDelDTO1.addParam(String.valueOf(delCount));
		// 删除件数
		BatchLoggerDTO loggerDelDTO2 = new BatchLoggerDTO();
		loggerDelDTO2.setCode("IIF04002");
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
		cherryBatchLogger.BatchLogger(loggerDelDTO1);
		return flag;
	}
	
	/**
	 * 
	 * 把接口数据库中取得的营业员信息导入到Cherry数据库中
	 * 
	 * @param employeeList 接口数据库中的营业员信息List
	 * @param map 组织ID、品牌ID等参数
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void updateBaInfo(List<Map<String, Object>> baInfoList, Map<String, Object> map)
			throws CherryBatchException {
		
		// 取得品牌ID
		Object brandId = map.get(CherryBatchConstants.BRANDINFOID);
		// 取得组织ID
		Object orgInfoId = map.get(CherryBatchConstants.ORGANIZATIONINFOID);

		// 循环导入营业员数据到Cherry数据库中
		for (Map<String, Object> baInfoMap : baInfoList) {
			// 取得营业员code
			String bacode = (String)baInfoMap.get("bacode");
			// 取得营业员姓名
			String baname = (String)baInfoMap.get("baname");
			try {
				// 作成者
				baInfoMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 作成程序名
				baInfoMap.put(CherryBatchConstants.CREATEPGM, "BINBEIFEMP01");
				// 更新者
				baInfoMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				baInfoMap.put(CherryBatchConstants.UPDATEPGM, "BINBEIFEMP01");
				// 把品牌Id设入条件map
				baInfoMap.put(CherryBatchConstants.BRANDINFOID, brandId);
				// 把所属组织Id设入条件map
				baInfoMap.put(CherryBatchConstants.ORGANIZATIONINFOID, orgInfoId);
				// 取得柜台号
				String counterCode = (String)baInfoMap.get("counterCode");
				// 营业员上级
				Object higherPath = null;
				// 柜台号存在的场合，取得部门ID
				if(counterCode != null && !"".equals(counterCode)) {
					// 查询营业员所在部门ID
					Object organizationId = binBEIFEMP01_Service.getOrganizationId(baInfoMap);
					if(organizationId != null) {
						// 把部门ID设置到条件map中
						baInfoMap.put("organizationId", organizationId);
						// 查询营业员所在柜台的柜台主管
						higherPath = binBEIFEMP01_Service.getHigherPath(baInfoMap);
					}
				}
//				if(baInfoMap.get("organizationId") == null) {
//					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
//					batchExceptionDTO.setBatchName(this.getClass());
//					batchExceptionDTO.setErrorCode("EIF04006");
//					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
//					// 营业员代号
//					batchExceptionDTO.addErrorParam(bacode);
//					// 营业员姓名
//					batchExceptionDTO.addErrorParam(baname);
//					throw new CherryBatchException(batchExceptionDTO);
//				}
				if(higherPath != null) {
					baInfoMap.put("path", higherPath);
				} else {
					baInfoMap.put("path", "/");
				}
				baInfoMap.put("nodeId", binBEIFEMP02_Service.getNewEmpNodeId(baInfoMap));
				// 类别代码
				baInfoMap.put("positionCode", "01");
				// 查询员工岗位类别ID
				Object posId = binBEIFEMP01_Service.getPositionCategoryId(baInfoMap);
				if(posId != null) {
					// 把岗位类别ID设置到条件map中
					baInfoMap.put("positionCategoryId", posId);
				}
				
				// 取得营业员信息ID
				Map<String, Object> baInfo = binBEIFEMP01_Service.getBaInfo(baInfoMap);
				// 营业员信息存在的场合，更新营业员信息，否则添加营业员信息
				if(baInfo != null) {
					try {
						baInfoMap.put("baInfoId", baInfo.get("baInfoId"));
						baInfoMap.put("employeeId", baInfo.get("employeeId"));
						// 更新营业员信息
						binBEIFEMP01_Service.updateBaInfo(baInfoMap);
						// 更新员工信息表
						binBEIFEMP01_Service.updateEmployee(baInfoMap);
						// 统计更新件数
						updateCount++;
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF04003");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 营业员代号
						batchExceptionDTO.addErrorParam(bacode);
						// 营业员姓名
						batchExceptionDTO.addErrorParam(baname);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
				} else {
					try {
						// 插入员工信息
						int employeeId = binBEIFEMP01_Service.insertEmployee(baInfoMap);
						baInfoMap.put("employeeId", employeeId);
						// 插入营业员信息
						binBEIFEMP01_Service.insertBaInfo(baInfoMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF04002");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 营业员代号
						batchExceptionDTO.addErrorParam(bacode);
						// 营业员姓名
						batchExceptionDTO.addErrorParam(baname);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					try {
						// 插入信息到员工入退职信息表
						binBEIFEMP02_Service.insertEmployeeQuit(baInfoMap);
					} catch (Exception e) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EIF03004");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 营业员代号
						batchExceptionDTO.addErrorParam(bacode);
						// 营业员姓名
						batchExceptionDTO.addErrorParam(baname);
						batchExceptionDTO.setException(e);
						throw new CherryBatchException(batchExceptionDTO);
					}
					// 统计插入件数
					insertCount++;
				}
				// Cherry数据库事务提交
				binBEIFEMP01_Service.manualCommit();
			} catch (CherryBatchException cherryBatchException) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EIF04004");
				// 营业员代号
				batchLoggerDTO.addParam(bacode);
				// 营业员姓名
				batchLoggerDTO.addParam(baname);
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			} catch (Throwable t) {
				try {
					// Cherry数据库事务回滚
					binBEIFEMP01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数加一
				failCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

}
