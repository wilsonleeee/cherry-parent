/*
 * @(#)BINBEDRHAN12_BL.java     1.0 2012/08/22
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
package com.cherry.dr.handler.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.mongo.domain.MemRuleExecRecord;
import com.cherry.cm.mongo.domain.QMemRuleExecRecord;
import com.cherry.cm.mongo.repositories.MemRuleExecRecordRepository;
import com.cherry.cm.util.Bean2Map;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.handler.service.BINBEDRHAN12_Service;
import com.mysema.query.BooleanBuilder;

/**
 * 会员规则履历处理BL
 * 
 * @author WangCT
 * @version 1.0 2012/08/22
 */
public class BINBEDRHAN12_BL {
	
	/** 会员规则履历处理Service **/
	@Resource
	private BINBEDRHAN12_Service binBEDRHAN12_Service;
	
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 操作MongoDB的会员规则履历表对象 **/
	@Resource
	private MemRuleExecRecordRepository memRuleExecRecordRepository;
	
	/**
	 * 把规则履历从数据库迁移到MongoDB
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等	
	 * @return BATCH处理标志
	 */
	public int tran_memRuleRecordHandle(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总条数
		int totalCount = 0;
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "memberInfoId, recordKbn, billId, tradeType");
		try {
			String organizationInfoID = map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString();
			String brandInfoID = map.get(CherryBatchConstants.BRANDINFOID).toString();
			String months = binOLCM14_BL.getConfigValue("1053", organizationInfoID, brandInfoID);
			if(months != null && !"".equals(months)) {
				// 迁移会员规则履历到MongoDB的时间点（月数）
				int monthCount = Integer.parseInt(months);
				// 取得业务日期
				String bussinessDate = binBEDRHAN12_Service.getBussinessDate(map);
				// 设置迁移会员规则履历到MongoDB的时间点
				map.put("ticketDate", DateUtil.addDateByMonth("yyyyMMdd", bussinessDate, -monthCount));
			} else {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 会员规则履历迁移时间不存在
				batchLoggerDTO1.setCode("EDR00040");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
				return flag;
			}
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 会员规则履历迁移时间不存在
			batchLoggerDTO1.setCode("EDR00040");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		try {
			// 把所有规则履历的数据状态更新成未处理
			binBEDRHAN12_Service.updAllDataStatus(map);
			// 把迁移时间点前的规则履历的数据状态更新成处理中
			binBEDRHAN12_Service.updDataStatus(map);
			// 把需要保留的规则履历的数据状态更新成未处理
			binBEDRHAN12_Service.updKeepRuleRecord(map);
			// 提交事务
			binBEDRHAN12_Service.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 规则履历更新成处理中状态失败
			batchLoggerDTO1.setCode("EDR00041");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		while (true) {
			try {
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
				// 查询需要迁移到MongoDB的会员规则履历List
				List<Map<String, Object>> memRuleRecordList = binBEDRHAN12_Service.getMemRuleRecordList(map);
				// 会员规则履历List不为空
				if (memRuleRecordList != null && !memRuleRecordList.isEmpty()) {
					// 统计总条数
					totalCount += memRuleRecordList.size();
					List<MemRuleExecRecord> memRuleExecRecordList = new ArrayList<MemRuleExecRecord>();
					for(Map<String, Object> memRuleRecordMap : memRuleRecordList) {
						MemRuleExecRecord memRuleExecRecord = new MemRuleExecRecord();
						// 组织代码
						String orgCode = (String)map.get("orgCode");
						// 品牌代码
						String brandCode = (String)map.get("brandCode");
						// 会员ID
						String memberInfoId = String.valueOf(memRuleRecordMap.get("memberInfoId"));
						// 履历区分
						String recordKbn = String.valueOf(memRuleRecordMap.get("recordKbn"));
						// 单据号
						String billCode = (String)memRuleRecordMap.get("billId");
						if(billCode == null) {
							billCode = "";
						}
						// 业务类型
						String billType = (String)memRuleRecordMap.get("tradeType");
						if(orgCode != null && !"".equals(orgCode) 
								&& brandCode != null && !"".equals(brandCode)
								&& memberInfoId != null && !"".equals(memberInfoId)
								&& recordKbn != null && !"".equals(recordKbn)
								&& billType != null && !"".equals(billType)) {
							String mark = "$";
							memRuleExecRecord.setId(orgCode + mark + brandCode + mark + memberInfoId + mark + billType + mark + billCode + mark + recordKbn);
						} else {
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							// 会员规则履历异常
							batchExceptionDTO.setErrorCode("EDR00044");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							batchExceptionDTO.addErrorParam(orgCode);
							batchExceptionDTO.addErrorParam(brandCode);
							batchExceptionDTO.addErrorParam(memberInfoId);
							batchExceptionDTO.addErrorParam(billCode);
							batchExceptionDTO.addErrorParam(billType);
							batchExceptionDTO.addErrorParam(recordKbn);
							throw new CherryBatchException(batchExceptionDTO);
						}
						// 组织代码
						memRuleExecRecord.setOrgCode((String)map.get("orgCode"));
						// 品牌代码
						memRuleExecRecord.setBrandCode((String)map.get("brandCode"));
						// 组织ID
						memRuleExecRecord.setOrganizationInfoId((Integer)memRuleRecordMap.get("organizationInfoId"));
						// 品牌ID
						memRuleExecRecord.setBrandInfoId((Integer)memRuleRecordMap.get("brandInfoId"));
						// 会员ID
						memRuleExecRecord.setMemberInfoId((Integer)memRuleRecordMap.get("memberInfoId"));
						// 会员卡号
						memRuleExecRecord.setMemCode((String)memRuleRecordMap.get("memCode"));
						// 单据号
						memRuleExecRecord.setBillCode((String)memRuleRecordMap.get("billId"));
						// 业务类型
						memRuleExecRecord.setBillType((String)memRuleRecordMap.get("tradeType"));
						// 单据产生日期
						memRuleExecRecord.setTicketDate((String)memRuleRecordMap.get("ticketDate"));
						// 部门ID
						Object organizationId = memRuleRecordMap.get("organizationId");
						if(organizationId != null) {
							memRuleExecRecord.setOrganizationId(organizationId.toString());
						}
						// 柜台号
						memRuleExecRecord.setCountercode((String)memRuleRecordMap.get("counterCode"));
						// 员工ID
						Object employeeId = memRuleRecordMap.get("employeeId");
						if(employeeId != null) {
							memRuleExecRecord.setEmployeeId(employeeId.toString());
						}
						// Ba卡号
						memRuleExecRecord.setBaCode((String)memRuleRecordMap.get("employeeCode"));
						// 数据来源
						memRuleExecRecord.setChannel((String)memRuleRecordMap.get("channel"));
						// 履历区分
						memRuleExecRecord.setRecordKbn((Integer)memRuleRecordMap.get("recordKbn"));
						// 更新前的值
						memRuleExecRecord.setOldValue((String)memRuleRecordMap.get("oldValue"));
						// 更新后的值
						memRuleExecRecord.setNewValue((String)memRuleRecordMap.get("newValue"));
						// 计算日期
						memRuleExecRecord.setCalcDate((String)memRuleRecordMap.get("calcDate"));
						// 理由
						memRuleExecRecord.setReason((Integer)memRuleRecordMap.get("reason"));
						// 规则ID
						memRuleExecRecord.setRuleIds((String)memRuleRecordMap.get("ruleIDs"));
						// 匹配的活动
						memRuleExecRecord.setSubCampaignCodes((String)memRuleRecordMap.get("subCampaignCodes"));
						// 变化类型
						memRuleExecRecord.setChangeType((String)memRuleRecordMap.get("changeType"));
						// 重算次数
						memRuleExecRecord.setReCalcCount((Integer)memRuleRecordMap.get("reCalcCount"));
						// 有效区分
						memRuleExecRecord.setValidFlag((String)memRuleRecordMap.get("validFlag"));
						// 创建时间
						memRuleExecRecord.setCreateTime((String)memRuleRecordMap.get("createTime"));
						// 创建者
						memRuleExecRecord.setCreatedBy((String)memRuleRecordMap.get("createdBy"));
						// 创建程序名
						memRuleExecRecord.setCreatePGM((String)memRuleRecordMap.get("createPGM"));
						// 更新时间
						memRuleExecRecord.setUpdateTime((String)memRuleRecordMap.get("updateTime"));
						// 更新者
						memRuleExecRecord.setUpdatedBy((String)memRuleRecordMap.get("updatedBy"));
						// 更新程序名
						memRuleExecRecord.setUpdatePGM((String)memRuleRecordMap.get("updatePGM"));
						// 更新次数
						memRuleExecRecord.setModifyCount((Integer)memRuleRecordMap.get("modifyCount"));
						memRuleExecRecordList.add(memRuleExecRecord);
					}
					memRuleExecRecordRepository.save(memRuleExecRecordList);
					// 会员规则履历少于一次抽取的数量，即为最后一页，跳出循环
					if(memRuleRecordList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			} catch (CherryBatchException cherryBatchException) {
				return flag;
			} catch (Exception e) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 规则履历添加到MongoDB失败
				batchLoggerDTO1.setCode("EDR00042");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				return flag;
			}
		}
		try {
			// 删除迁移完的会员规则履历
			binBEDRHAN12_Service.delMemRuleRecord(map);
			// 提交事务
			binBEDRHAN12_Service.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 删除迁移完的会员规则履历失败
			batchLoggerDTO1.setCode("EDR00043");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
	}
	
	/**
	 * 把旧的规则履历迁移到规则履历历史表处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等	
	 * @return BATCH处理标志
	 */
	public int tran_memRuleRecordMoveHandle(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总条数
		int totalCount = 0;
		try {
			String organizationInfoID = map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString();
			String brandInfoID = map.get(CherryBatchConstants.BRANDINFOID).toString();
			String months = binOLCM14_BL.getConfigValue("1053", organizationInfoID, brandInfoID);
			if(months != null && !"".equals(months)) {
				// 迁移会员规则履历到MongoDB的时间点（月数）
				int monthCount = Integer.parseInt(months);
				// 取得业务日期
				String bussinessDate = binBEDRHAN12_Service.getBussinessDate(map);
				// 设置迁移会员规则履历到MongoDB的时间点
				map.put("ticketDate", DateUtil.addDateByMonth("yyyyMMdd", bussinessDate, -monthCount));
			} else {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 会员规则履历迁移时间不存在
				batchLoggerDTO1.setCode("EDR00040");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
				return flag;
			}
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 会员规则履历迁移时间不存在
			batchLoggerDTO1.setCode("EDR00040");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		try {
			// 把迁移时间点前的规则履历的数据状态更新成处理中
			binBEDRHAN12_Service.updDataStatus(map);
			// 把需要保留的规则履历的数据状态更新成未处理
			binBEDRHAN12_Service.updKeepRuleRecord(map);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 规则履历更新成处理中状态失败
			batchLoggerDTO1.setCode("EDR00041");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		try {
			// 把旧的规则履历迁移到规则履历历史表
			binBEDRHAN12_Service.memRuleRecordMove(map);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 规则履历添加到MongoDB失败
			batchLoggerDTO1.setCode("EDR00048");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		try {
			// 删除迁移完的会员规则履历
			totalCount = binBEDRHAN12_Service.delMemRuleRecord(map);
			// 提交事务
			binBEDRHAN12_Service.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			// 删除迁移完的会员规则履历失败
			batchLoggerDTO1.setCode("EDR00043");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
	}
	
	/**
	 * 从MongoDB把规则履历迁移到数据库
	 * 
	 * @param map 查询条件
	 * @throws Exception
	 */
	public int tran_moveMongoDBToDataBase(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总件数
		int totalCount = 0;
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		
		QMemRuleExecRecord qMemRuleExecRecord = QMemRuleExecRecord.memRuleExecRecord;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(qMemRuleExecRecord.orgCode.eq((String)map.get("orgCode")));
		booleanBuilder.and(qMemRuleExecRecord.brandCode.eq((String)map.get("brandCode")));
		
		while (true) {
			try {
				// 查询开始位置
				startNum = dataSize * currentNum;
				// 数据抽出次数累加
				currentNum++;
				List<MemRuleExecRecord> result = memRuleExecRecordRepository.findAll(booleanBuilder, new PageRequest(startNum, dataSize, Direction.DESC, "TicketDate")).getContent();
				if(result != null && !result.isEmpty()) {
					totalCount += result.size();
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					for(MemRuleExecRecord memRuleExecRecord : result) {
						Map<String, Object> param = (Map)Bean2Map.toHashMap(memRuleExecRecord);
						list.add(param);
					}
					if(!list.isEmpty()) {
						// 把从MongoDB取得的履历迁移到数据库
						binBEDRHAN12_Service.addRuleExecRecord(list);
						binBEDRHAN12_Service.manualCommit();
					}
				} else {
					break;
				}
				// 会员履历数据少于一次抽取的数量，即为最后一页，跳出循环
				if(result.size() < dataSize) {
					break;
				}
			} catch (Exception e) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EDR00049");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
				break;
			}
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		
		return flag;
		
	}

}
