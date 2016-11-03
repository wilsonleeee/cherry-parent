/*
 * @(#)BINBEDRHAN10_BL.java     1.0 2012/05/28
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.mq.BTimesDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.handler.service.BINBEDRHAN01_Service;
import com.cherry.dr.handler.service.BINBEDRHAN10_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 处理会员的规则履历记录BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2012/05/28
 */
public class BINBEDRHAN10_BL {
	
	/** 处理会员的规则履历记录service */
	@Resource
	private BINBEDRHAN10_Service binBEDRHAN10_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 会员等级和化妆次数重算Service */
	@Resource
	private BINBEDRHAN01_Service binBEDRHAN01_Service;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 处理会员的规则履历记录，把规则履历从数据库迁移到MongoDB，同时把等级变化的履历发送到老后台
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_memRuleRecordHandle(Map<String, Object> map) throws CherryBatchException {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总件数
		int totalCount = 0;
		// 成功件数
		int successCount = 0;
		// 失败件数
		int failCount = 0;
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		
		// 取得业务日期
		String bussinessDate = binBEDRHAN10_Service.getBussinessDate(map);
		// 所有的等级信息
		Map<String, Object> allMemLevelMap = new HashMap<String, Object>();
		// 查询会员等级信息List
		List<Map<String, Object>> memberLevelInfoList = binBEDRHAN10_Service.getMemberLevelInfoList(map);
		if(memberLevelInfoList != null && !memberLevelInfoList.isEmpty()) {
			for(int i = 0; i < memberLevelInfoList.size(); i++) {
				Map<String, Object> allLevelMap = memberLevelInfoList.get(i);
				allMemLevelMap.put(allLevelMap.get("memberLevelId").toString(), allLevelMap);
			}
		}
		// 所有的规则内容
		Map<String, Object> allRulesMap = new HashMap<String, Object>();
		// 取得所有的规则内容
		List<Map<String, Object>> allRuleContent = binBEDRHAN01_Service.getAllRuleContent(map);
		if(allRuleContent != null && !allRuleContent.isEmpty()) {
			for(int i = 0; i < allRuleContent.size(); i++) {
				Map<String, Object> allRuleMap = allRuleContent.get(i);
				allRulesMap.put(allRuleMap.get("ruleId").toString(), allRuleMap.get("ruleContent"));
			}
		}
		map.put("bussinessDate", bussinessDate);
		while (true) {
			// 查询会员信息List
			List<Map<String, Object>> memberInfoList = binBEDRHAN10_Service.getMemberInfoList(map);
			// 会员数据不为空
			if (memberInfoList != null && !memberInfoList.isEmpty()) {
				// 统计处理总件数
				totalCount += memberInfoList.size();
				// 循环处理每个会员的规则履历记录，把规则履历从数据库迁移到MongoDB，同时把等级变化的履历发送到老后台
				for(int i = 0; i < memberInfoList.size(); i++) {
					Map<String, Object> memberInfoMap = memberInfoList.get(i);
					memberInfoMap.putAll(map);
					int memberInfoId = (Integer)memberInfoMap.get("memberInfoId");
					try {
						// 查询会员规则履历List
						List<Map<String, Object>> memRuleRecordList = binBEDRHAN10_Service.getMemRuleRecordList(memberInfoMap);
						if(memRuleRecordList != null && !memRuleRecordList.isEmpty()) {
							// 过滤无用的履历，返回有用的履历
							List<Map<String, Object>> _memRuleRecordList = this.filterRuleRecord(memRuleRecordList);
							List<String> billIdNSList = new ArrayList<String>();
							List<String> billIdNOTNSList = new ArrayList<String>();
							for(Map<String, Object> recordMap : _memRuleRecordList) {
								String billId = (String)recordMap.get("billId");
								String tradeType = (String)recordMap.get("tradeType");
								if("NS".equals(tradeType) || "SR".equals(tradeType) || "PX".equals(tradeType)) {
									if(!billIdNSList.contains(billId)) {
										billIdNSList.add(billId);
									}
								} else {
									if(!billIdNOTNSList.contains(billId)) {
										billIdNOTNSList.add(billId);
									}
								}
							}
							List<Map<String, Object>> businessList = new ArrayList<Map<String,Object>>();
							if(!billIdNSList.isEmpty()) {
								memberInfoMap.put("billIdList", billIdNSList);
								// 查询会员规则履历中关联单号对应的销售记录List
								List<Map<String, Object>> saleRecordList = binBEDRHAN10_Service.getSaleRecordList(memberInfoMap);
								if(saleRecordList != null) {
									businessList.addAll(saleRecordList);
								}
							}
							if(!billIdNOTNSList.isEmpty()) {
								memberInfoMap.put("billIdList", billIdNOTNSList);
								// 查询会员规则履历中关联单号对应的化妆次数积分使用记录List
								List<Map<String, Object>> memUsedCountList = binBEDRHAN10_Service.getMemUsedCountList(memberInfoMap);
								if(memUsedCountList != null) {
									businessList.addAll(memUsedCountList);
								}
							}
							// 会员等级变化履历List
							List<Map<String, Object>> memLevelRecordList = new ArrayList<Map<String,Object>>();
							for(int j = 0; j < _memRuleRecordList.size(); j++) {
								Map<String, Object> memRuleRecordMap = _memRuleRecordList.get(j);
								String billId = (String)memRuleRecordMap.get("billId");
								Map<String, Object> businessMap = this.getBusinessMap(billId, businessList);
								if(businessMap != null) {
									memRuleRecordMap.putAll(businessMap);
								}
								// 履历区分
								int recordKbn = (Integer)memRuleRecordMap.get("recordKbn");
								// 履历区分为等级的场合
								if(recordKbn == 0) {
									// 变更前等级
									String oldValue = (String)memRuleRecordMap.get("oldValue");
									// 变更前等级级别
									int oldGrade = 0;
									if(oldValue != null) {
										String levelCode = this.getMemLevelCode(oldValue, allMemLevelMap);
										oldGrade = this.getMemLevelGrade(oldValue, allMemLevelMap);
										memRuleRecordMap.put("oldLevelCode", levelCode);
									}
									// 变更后等级
									String newValue = (String)memRuleRecordMap.get("newValue");
									int newGrade = 0;
									// 变更后等级级别
									if(newValue != null) {
										String levelCode = this.getMemLevelCode(newValue, allMemLevelMap);
										newGrade = this.getMemLevelGrade(newValue, allMemLevelMap);
										memRuleRecordMap.put("newLevelCode", levelCode);
									}
									// 变更后会员等级级别比变更前等级级别大的场合，表示会员升级，小的表示降级
									if(newGrade > oldGrade) {
										// 变化类型
										memRuleRecordMap.put("changeType", "1");
									} else if(newGrade < oldGrade) {
										// 变化类型
										memRuleRecordMap.put("changeType", "0");
									}
									// 业务日期
									String ticketDate = (String)memRuleRecordMap.get("ticketDate");
									// 取得等级变化时的累积金额
									String totalAmount = this.getTotalAmount(ticketDate, _memRuleRecordList);
									memRuleRecordMap.put("totalAmount", totalAmount);
									memLevelRecordList.add(memRuleRecordMap);
								}
							}
							memberInfoMap.put("memRuleRecordList", _memRuleRecordList);
							// 把规则履历添加到MongoDB中
							this.addRuleRecordToMongDB(memberInfoMap);
							// 会员等级变化履历存在的场合
							if(!memLevelRecordList.isEmpty()) {
								memberInfoMap.put("memLevelRecordList", memLevelRecordList);
								// 发送等级下发MQ消息
								this.sendLevelMes(memberInfoMap, allMemLevelMap, allRulesMap);
							}
						}
						memberInfoMap.put("status", "1");
						// 统计成功件数
						successCount++;
					} catch (Exception e) {
						memberInfoMap.put("status", "0");
						// 统计失败件数
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EDR00021");
						// 会员ID
						batchLoggerDTO1.addParam(String.valueOf(memberInfoId));
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
				}
				try {
					// 更新已经处理过的规则履历记录
					binBEDRHAN10_Service.updateCompleteRuleRecord(memberInfoList);
					// 提交事务
					binBEDRHAN10_Service.manualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EDR00022");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					break;
				}
				// 会员数据数据少于一次抽取的数量，即为最后一页，跳出循环
				if(memberInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		return flag;
		
	}
	
	/**
	 * 过滤无用的履历，返回有用的履历
	 * 
	 * @param memRuleRecordList 待过滤的履历List
	 * @return 过滤好的履历List
	 */
	public List<Map<String, Object>> filterRuleRecord(List<Map<String, Object>> memRuleRecordList) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < memRuleRecordList.size(); i++) {
			Map<String, Object> memRuleRecordMap = memRuleRecordList.get(i);
			// 单据时间
			String ticketDate = (String)memRuleRecordMap.get("ticketDate");
			// 计算时间
			String calcDate = (String)memRuleRecordMap.get("calcDate");
			if(!list.isEmpty()) {
				Map<String, Object> map = list.get(list.size()-1);
				// 单据时间
				String _ticketDate = (String)map.get("ticketDate");
				// 计算时间
				String _calcDate = (String)memRuleRecordMap.get("calcDate");
				if(ticketDate.compareTo(_ticketDate) < 0) {
					list.add(memRuleRecordMap);
				} else if(ticketDate.compareTo(_ticketDate) == 0) {
					if(calcDate.equals(_calcDate)) {
						list.add(memRuleRecordMap);
					}
				}
			} else {
				list.add(memRuleRecordMap);
			}
		}
		return list;
	}
	
	/**
	 * 把规则履历添加到MongoDB中
	 * 
	 * @param memRuleRecordMap 规则履历信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addRuleRecordToMongDB(Map<String, Object> memberInfoMap) throws Exception {
		
		DBObject query = new BasicDBObject();
		// 组织代码
		query.put("OrgCode", memberInfoMap.get("orgCode"));
		// 品牌代码
		query.put("BrandCode", memberInfoMap.get("brandCode"));
		// 会员ID
		query.put("BIN_MemberInfoID", memberInfoMap.get("memberInfoId"));
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put("$set", new BasicDBObject("ValidFlag", "0"));
		MongoDB.update("MGO_RuleExecRecord", query, updatedValue,false,true);
		List<Map<String, Object>> memRuleRecordList = (List)memberInfoMap.get("memRuleRecordList");
		for(Map<String, Object> memRuleRecordMap : memRuleRecordList) {
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", memberInfoMap.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", memberInfoMap.get("brandCode"));
			// 会员ID
			dbObject.put("BIN_MemberInfoID", memberInfoMap.get("memberInfoId"));
			// 会员卡号
			dbObject.put("MemCode", memRuleRecordMap.get("memCode"));
			// 单据号
			dbObject.put("BillID", memRuleRecordMap.get("billId"));
			// 业务类型
			dbObject.put("TradeType", memRuleRecordMap.get("tradeType"));
			// 履历区分
			dbObject.put("RecordKbn", memRuleRecordMap.get("recordKbn"));
			// 更新前的值
			dbObject.put("OldValue", memRuleRecordMap.get("oldValue"));
			// 更新后的值
			dbObject.put("NewValue", memRuleRecordMap.get("newValue"));
			// 单据产生日期
			dbObject.put("TicketDate", memRuleRecordMap.get("ticketDate"));
			// 计算日期
			dbObject.put("CalcDate", memRuleRecordMap.get("calcDate"));
			// 规则ID
			dbObject.put("BIN_RuleIDs", memRuleRecordMap.get("ruleIDs"));
			// 重算次数
			dbObject.put("ReCalcCount", memRuleRecordMap.get("reCalcCount"));
			// 理由
			dbObject.put("Reason", memRuleRecordMap.get("reason"));
			// 变化类型
			dbObject.put("ChangeType", memRuleRecordMap.get("changeType"));
			// 匹配的活动
			dbObject.put("SubCampaignCodes", memRuleRecordMap.get("subCampaignCodes"));
			// 柜台号
			dbObject.put("Countercode", memRuleRecordMap.get("countercode"));
			// Ba卡号
			dbObject.put("EmployeeCode", memRuleRecordMap.get("baCode"));
			// 数据来源
			dbObject.put("Channel", memRuleRecordMap.get("channel"));
			// 有效区分
			dbObject.put("ValidFlag", "1");
			
			// 单据号
			query.put("BillID", memRuleRecordMap.get("billId"));
			// 履历区分
			query.put("RecordKbn", memRuleRecordMap.get("recordKbn"));
			updatedValue.put("$set", dbObject);
			int count = MongoDB.update("MGO_RuleExecRecord", query, updatedValue);
			if(count == 0) {
				MongoDB.insert("MGO_RuleExecRecord", dbObject);
			}
		}
		query = new BasicDBObject();
		// 组织代码
		query.put("OrgCode", memberInfoMap.get("orgCode"));
		// 品牌代码
		query.put("BrandCode", memberInfoMap.get("brandCode"));
		// 会员ID
		query.put("BIN_MemberInfoID", memberInfoMap.get("memberInfoId"));
		// 履历区分
		query.put("RecordKbn", 0);
		// 有效区分
		query.put("ValidFlag", "0");
		List<DBObject> invalidMemLevelRecordList = MongoDB.findAll("MGO_RuleExecRecord", query);
		if(invalidMemLevelRecordList != null && !invalidMemLevelRecordList.isEmpty()) {
			memberInfoMap.put("invalidMemLevelRecordList", invalidMemLevelRecordList);
		}
	}
	
	/**
	 * 发送等级下发MQ消息
	 * 
	 * @param memberInfoMap 会员等级变化信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void sendLevelMes(Map<String, Object> memberInfoMap, Map<String, Object> allMemLevelMap, Map<String, Object> allRulesMap) throws Exception {
		// 等级MQ主业务 DTO
		LevelMainDTO levelMainDTO = new LevelMainDTO();
		String orgCode = (String)memberInfoMap.get("orgCode");
		String brandCode = (String)memberInfoMap.get("brandCode");
		String organizationInfoId = memberInfoMap.get("organizationInfoId").toString();
		String brandInfoId = memberInfoMap.get("brandInfoId").toString();
		// 品牌代码
		levelMainDTO.setBrandCode(brandCode);
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, 
				DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_ML);
		levelMainDTO.setTradeNoIF(ticketNumber);
		
		// 会员卡号
		levelMainDTO.setMemberCode((String)memberInfoMap.get("memCode"));
		// 计算时间
		String caltime = binBEDRHAN10_Service.getForwardSYSDate();
		levelMainDTO.setCaltime(caltime);
		// 当前等级
		Object memberLevel = memberInfoMap.get("memberLevel");
		if(memberLevel != null) {
			String levelCode = this.getMemLevelCode(memberLevel.toString(), allMemLevelMap);
			levelMainDTO.setMember_level(levelCode);
		}
		// 开卡等级
		Object grantMemberLevel = memberInfoMap.get("grantMemberLevel");
		if(grantMemberLevel != null) {
			String grantMemberLevelCode = this.getMemLevelCode(grantMemberLevel.toString(), allMemLevelMap);
			levelMainDTO.setGrantMemberLevel(grantMemberLevelCode);
		}
		// 上一次等级
		Object upgradeFromLevel = memberInfoMap.get("upgradeFromLevel");
		if(upgradeFromLevel != null) {
			String oldLevelCode = this.getMemLevelCode(upgradeFromLevel.toString(), allMemLevelMap);
			levelMainDTO.setPrevLevel(oldLevelCode);
		}
		// 本次等级变化时间
		levelMainDTO.setLevelAdjustTime((String)memberInfoMap.get("levelAdjustDay"));
		// 入会时间调整准则
		String jnDateKbn = binOLCM14_BL.getConfigValue("1076", organizationInfoId, brandInfoId);
		// 以首单销售为准
		if ("2".equals(jnDateKbn)) {
			// 入会时间
			levelMainDTO.setJoinDate(ConvertUtil.getString(memberInfoMap.get("joinDate")));
		} else {
			// 入会时间
			levelMainDTO.setJoinDate("");
		}
		// 等级MQ明细业务List
		List<LevelDetailDTO> levelDetailList = new ArrayList<LevelDetailDTO>();
		List<Map<String, Object>> memLevelRecordList = (List)memberInfoMap.get("memLevelRecordList");
		for(Map<String, Object> memLevelRecordMap : memLevelRecordList) {
			// 等级MQ明细业务 DTO
			LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
			// 会员卡号
			levelDetailDTO.setMemberCode((String)memLevelRecordMap.get("memCode"));
			// 业务类型
			levelDetailDTO.setBizType((String)memLevelRecordMap.get("tradeType"));
			// 关联单据时间
			levelDetailDTO.setRelevantTicketDate((String)memLevelRecordMap.get("ticketDate"));
			// 关联单号
			levelDetailDTO.setRelevantNo((String)memLevelRecordMap.get("billId"));
			// 变动渠道
			levelDetailDTO.setChannel((String)memLevelRecordMap.get("channel"));
			// 重算次数
			levelDetailDTO.setReCalcCount(String.valueOf(memLevelRecordMap.get("reCalcCount")));
			// 操作类型
			levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_U);
			// 柜台号
			levelDetailDTO.setCounterCode((String)memLevelRecordMap.get("countercode"));
			// 员工编号
			levelDetailDTO.setEmployeeCode((String)memLevelRecordMap.get("baCode"));
			// 变更前等级
			levelDetailDTO.setMemberlevelOld((String)memLevelRecordMap.get("oldLevelCode"));
			// 变更后等级
			levelDetailDTO.setMemberlevelNew((String)memLevelRecordMap.get("newLevelCode"));
			// 变化类型
			levelDetailDTO.setChangeType((String)memLevelRecordMap.get("changeType"));
			// 累积金额
			levelDetailDTO.setTotalAmount((String)memLevelRecordMap.get("totalAmount"));
			
			// 理由
			int reason = (Integer)memLevelRecordMap.get("reason");
			// 变化原因
			String reasonS = "";
			// 理由为规则计算的场合
			if(reason == 0 || reason == 2) {
				String allRules = (String)memLevelRecordMap.get("ruleIDs");
				if(allRules != null && !"".equals(allRules)) {
					String[] allRuleArray = allRules.split(",");
					StringBuffer buffer = new StringBuffer();
					for(int i = 0; i < allRuleArray.length; i++) {
						if(allRulesMap.get(allRuleArray[i]) != null) {
							buffer.append(i + 1).append(DroolsConstants.MQ_REASON_COMMA).
							append(allRulesMap.get(allRuleArray[i])).
							append(DroolsConstants.LINE_SPACE);
						}
					}
					reasonS = buffer.toString();
				}
			} else if(reason == 1) { // 理由为初始数据导入的场合
				reasonS = DroolsConstants.MQ_INITDATA_REASON;
			} else if(reason == 3) { // 理由为化妆次数使用的场合
				reasonS = DroolsConstants.MQ_USEDTIMES_REASON;
			} else if(reason == 4) { // 会员等级化妆次数重算的场合
				reasonS = DroolsConstants.MQ_RECALCMEMLEVEL_REASON;
			}
			if (reasonS.length() > DroolsConstants.REASON_MAX_LENGTH) {
				reasonS = reasonS.substring(0, DroolsConstants.REASON_MAX_LENGTH);
			}
			// 变化原因
			levelDetailDTO.setReason(reasonS);
			levelDetailList.add(levelDetailDTO);
		}
		List<DBObject> invalidMemLevelRecordList = (List)memberInfoMap.get("invalidMemLevelRecordList");
		if(invalidMemLevelRecordList != null && !invalidMemLevelRecordList.isEmpty()) {
			for(DBObject invalidMemLevelRecord : invalidMemLevelRecordList) {
				// 等级MQ明细业务 DTO
				LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
				// 会员卡号
				levelDetailDTO.setMemberCode((String)invalidMemLevelRecord.get("MemCode"));
				// 关联单号
				levelDetailDTO.setRelevantNo((String)invalidMemLevelRecord.get("BillID"));
				// 重算次数
				levelDetailDTO.setReCalcCount(String.valueOf(invalidMemLevelRecord.get("ReCalcCount")));
				// 操作类型
				levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_D);
				levelDetailList.add(levelDetailDTO);
			}
		}
		// 柜台号
		levelMainDTO.setCountercode(levelDetailList.get(0).getCounterCode());
		// 员工编号
		levelMainDTO.setBacode(levelDetailList.get(0).getEmployeeCode());
		// 等级MQ明细业务List
		levelMainDTO.setLevelDetailList(levelDetailList);
		// 取得MQ消息体
		String msg = levelMainDTO.getMQMsg();
		// MQ收发日志DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(organizationInfoId));
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandInfoId));
		// 单据类型
		mqInfoDTO.setBillType(DroolsConstants.MQ_BILLTYPE_ML);
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(msg);
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", orgCode);
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_ML);
		// 单据号
		dbObject.put("TradeNoIF", ticketNumber);
		// 修改次数
		dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务主体
	    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
	    // 业务主体代码
	    dbObject.put("TradeEntityCode", (String)memberInfoMap.get("memCode"));
	    // 业务主体名称
	    dbObject.put("TradeEntityName", (String)memberInfoMap.get("memName"));
	    // 发生时间
	    dbObject.put("OccurTime", caltime);
	    // 事件内容
	    dbObject.put("Content", msg);
	    mqInfoDTO.setDbObject(dbObject);
	    // MQ消息发送处理
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 根据会员ID取得会员等级代码
	 * 
	 * @param memberInfoId 会员ID
	 * @param allLevelMap 所有的等级信息
	 * @return 会员等级代码
	 */
	@SuppressWarnings("unchecked")
	public String getMemLevelCode(String memberInfoId, Map<String, Object> allMemLevelMap) {
		
		Map<String, Object> levelMap = (Map)allMemLevelMap.get(memberInfoId);
		if(levelMap != null) {
			return (String)levelMap.get("levelCode");
		}
		return null;
	}
	
	/**
	 * 根据会员ID取得会员等级级别
	 * 
	 * @param memberInfoId 会员ID
	 * @param allLevelMap 所有的等级信息
	 * @return 会员等级级别
	 */
	@SuppressWarnings("unchecked")
	public int getMemLevelGrade(String memberInfoId, Map<String, Object> allMemLevelMap) {
		
		Map<String, Object> levelMap = (Map)allMemLevelMap.get(memberInfoId);
		if(levelMap != null) {
			return (Integer)levelMap.get("grade");
		}
		return 0;
	}
	
	/**
	 * 根据关联单号取得相应的业务信息
	 * 
	 * @param billId 单据号
	 * @param businessList 业务List
	 * @return 关联单号对应的业务信息
	 */
	public Map<String, Object> getBusinessMap(String billId, List<Map<String, Object>> businessList) {
		
		if(businessList != null && !businessList.isEmpty()) {
			for(Map<String, Object> businessMap : businessList) {
				String _billId = (String)businessMap.get("billId");
				if(billId.equals(_billId)) {
					return businessMap;
				}
			}
		}
		return null;
	}
	
	/**
	 * 取得等级变化时的累积金额
	 * 
	 * @param ticketDate 等级变化时的业务时间
	 * @param memRuleRecordList 履历记录List
	 * @return 等级变化时的累积金额
	 */
	public String getTotalAmount(String ticketDate, List<Map<String, Object>> memRuleRecordList) {
		
		for(Map<String, Object> memRuleRecordMap : memRuleRecordList) {
			// 履历区分
			int recordKbn = (Integer)memRuleRecordMap.get("recordKbn");
			// 累积金额的场合
			if(recordKbn == 1) {
				String _ticketDate = (String)memRuleRecordMap.get("ticketDate");
				if(ticketDate.compareTo(_ticketDate) >= 0) {
					return (String)memRuleRecordMap.get("newValue");
				}
			}
		}
		return "0";
	}
	
	/**
	 * 处理会员的规则履历记录，把规则履历从MongoDB迁移到数据库，同时更新会员扩展属性表
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_memRuleRecordHandle2(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总件数
		int totalCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		
		DBObject query = new BasicDBObject();
		// 组织代码
		query.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		query.put("BrandCode", map.get("brandCode"));
		DBObject sort = new BasicDBObject();
		sort.put("TicketDate", 1);
		while (true) {
			// 查询开始位置
			startNum = dataSize * currentNum + 1;
			// 数据抽出次数累加
			currentNum++;
			// 从MongDB中取得会员履历List
			List<DBObject> memRuleRecordList = MongoDB.find("MGO_RuleExecRecord", query, null, sort, startNum, dataSize);
			if(memRuleRecordList != null && !memRuleRecordList.isEmpty()) {
				List<Map<String, Object>> _memRuleRecordList = new ArrayList<Map<String,Object>>();
				for(DBObject dbObject : memRuleRecordList) {
					Map<String, Object> memRuleRecordMap = new HashMap<String, Object>();
					memRuleRecordMap.putAll(map);
					// 作成者
					memRuleRecordMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 作成程序名
					memRuleRecordMap.put(CherryBatchConstants.CREATEPGM, "BINBEDRHAN10");
					// 更新者
					memRuleRecordMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
					// 更新程序名
					memRuleRecordMap.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN10");
					// 会员ID
					memRuleRecordMap.put("memberInfoId", dbObject.get("BIN_MemberInfoID"));
					// 会员卡号
					memRuleRecordMap.put("memCode", dbObject.get("MemCode"));
					// 单据号
					memRuleRecordMap.put("billId", dbObject.get("BillID"));
					// 业务类型
					memRuleRecordMap.put("tradeType", dbObject.get("TradeType"));
					// 履历区分
					memRuleRecordMap.put("recordKbn", dbObject.get("RecordKbn"));
					// 更新前的值
					memRuleRecordMap.put("oldValue", dbObject.get("OldValue"));
					// 更新后的值
					memRuleRecordMap.put("newValue", dbObject.get("NewValue"));
					// 单据产生日期
					memRuleRecordMap.put("ticketDate", dbObject.get("TicketDate"));
					// 计算日期
					memRuleRecordMap.put("calcDate", dbObject.get("CalcDate"));
					// 规则ID
					memRuleRecordMap.put("ruleIds", dbObject.get("BIN_RuleIDs"));
					// 重算次数
					memRuleRecordMap.put("reCalcCount", dbObject.get("ReCalcCount"));
					// 理由
					memRuleRecordMap.put("reason", dbObject.get("Reason"));
					// 变化类型
					memRuleRecordMap.put("changeType", dbObject.get("ChangeType"));
					// 匹配的活动
					memRuleRecordMap.put("subCampCodes", dbObject.get("SubCampaignCodes"));
					_memRuleRecordList.add(memRuleRecordMap);
				}
				try {
					binBEDRHAN10_Service.addRuleExecRecord(_memRuleRecordList);
					binBEDRHAN10_Service.manualCommit();
					totalCount += _memRuleRecordList.size();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EDR00023");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					break;
				}
				// 会员履历数据少于一次抽取的数量，即为最后一页，跳出循环
				if(memRuleRecordList.size() < dataSize) {
					break;
				}
			} else {
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
	
	/**
	 * 把所有会员的化妆次数履历全部发送到老后台
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_memCurBtimesHandle(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理总件数
		int totalCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		// 所有的规则内容
		Map<String, Object> allRulesMap = new HashMap<String, Object>();
		// 取得所有的规则内容
		List<Map<String, Object>> allRuleContent = binBEDRHAN01_Service.getAllRuleContent(map);
		if(allRuleContent != null && !allRuleContent.isEmpty()) {
			for(int i = 0; i < allRuleContent.size(); i++) {
				Map<String, Object> allRuleMap = allRuleContent.get(i);
				allRulesMap.put(allRuleMap.get("ruleId").toString(), allRuleMap.get("ruleContent"));
			}
		}
		
		// 从接口数据库中分批取得会员列表，并处理
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
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
			
			// 查询会员信息List
			List<Map<String, Object>> memberInfoList = binBEDRHAN10_Service.getAllMemberInfoList(map);
			// 会员数据不为空
			if (!CherryBatchUtil.isBlankList(memberInfoList)) {
				// 统计总条数
				totalCount += memberInfoList.size();
				for(int i = 0; i < memberInfoList.size(); i++) {
					Map<String, Object> memberInfoMap = memberInfoList.get(i);
					memberInfoMap.putAll(map);
					DBObject query = new BasicDBObject();
					// 组织代码
					query.put("OrgCode", memberInfoMap.get("orgCode"));
					// 品牌代码
					query.put("BrandCode", memberInfoMap.get("brandCode"));
					// 会员ID
					query.put("BIN_MemberInfoID", memberInfoMap.get("memberInfoId"));
					// 履历区分
					query.put("RecordKbn", 2);
					DBObject sort = new BasicDBObject();
					sort.put("TicketDate", 1);
					// 从MongoDB中取得会员的化妆次数履历List
					List<DBObject> memBtimesRecordList = MongoDB.find("MGO_RuleExecRecord", query, sort);
					// 存在化妆次数变化履历的场合
					if(memBtimesRecordList != null && !memBtimesRecordList.isEmpty()) {
						memberInfoMap.put("memBtimesRecordList", memBtimesRecordList);
						// 发送化妆次数下发MQ消息
						this.sendBTimesMes(memberInfoMap, allRulesMap);
					}
				}
				// 会员数据少于一次抽取的数量，即为最后一页，跳出循环
				if(memberInfoList.size() < dataSize) {
					break;
				}
			} else {
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
	
	/**
	 * 
	 * 发送化妆次数下发MQ消息
	 * 
	 * @param memberInfoMap 会员化妆次数变化信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void sendBTimesMes(Map<String, Object> memberInfoMap, Map<String, Object> allRulesMap) throws Exception {
		
		BTimesMainDTO btimesMainDTO = new BTimesMainDTO();
		String orgCode = (String)memberInfoMap.get("orgCode");
		String brandCode = (String)memberInfoMap.get("brandCode");
		String organizationInfoId = memberInfoMap.get("organizationInfoId").toString();
		String brandInfoId = memberInfoMap.get("brandInfoId").toString();
		// 品牌代码
		btimesMainDTO.setBrandCode(brandCode);
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(organizationInfoId, 
				brandInfoId, DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_MG);
		btimesMainDTO.setTradeNoIF(ticketNumber);
		// 修改回数
		btimesMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		btimesMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_MG);
		// 计算时间
		String caltime = binBEDRHAN10_Service.getForwardSYSDate();
		// 计算时间
		btimesMainDTO.setCaltime(caltime);
		
		// 化妆次数MQ明细业务List
		List<BTimesDetailDTO> btimesDetailList = new ArrayList<BTimesDetailDTO>();
		List<DBObject> memBtimesRecordList = (List)memberInfoMap.get("memBtimesRecordList");
		for(DBObject memBtimesRecordMap : memBtimesRecordList) {
			BTimesDetailDTO btimesDetailDTO = new BTimesDetailDTO();
			// 有效区分
			String validFlag = (String)memBtimesRecordMap.get("ValidFlag");
			if("1".equals(validFlag)) {
				// 操作类型
				btimesDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
			} else {
				// 操作类型
				btimesDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_D);
			}
			// 变更前化妆次数
			String oldValue = (String)memBtimesRecordMap.get("OldValue");
			// 变更后化妆次数
			String newValue = (String)memBtimesRecordMap.get("NewValue");
			// 变更前化妆次数
			btimesDetailDTO.setBtimesOld(oldValue);
			// 变更后化妆次数
			btimesDetailDTO.setCurBtimesNew(newValue);
			int oldBtimes = 0;
			if(oldValue != null && !"".equals(oldValue)) {
				oldBtimes = Integer.parseInt(oldValue);
			}
			int newBtimes = 0;
			if(newValue != null && !"".equals(newValue)) {
				newBtimes = Integer.parseInt(newValue);
			}
			// 化妆次数差分
			btimesDetailDTO.setDiffBtimes(String.valueOf(newBtimes-oldBtimes));
			// 会员卡号
			btimesDetailDTO.setMemberCode((String)memBtimesRecordMap.get("MemCode"));
			// 柜台号
			btimesDetailDTO.setCounterCode((String)memBtimesRecordMap.get("Countercode"));
			// 员工编号
			btimesDetailDTO.setEmployeeCode((String)memBtimesRecordMap.get("EmployeeCode"));
			// 业务类型
			btimesDetailDTO.setBizType((String)memBtimesRecordMap.get("TradeType"));
			// 关联单据时间
			btimesDetailDTO.setRelevantTicketDate((String)memBtimesRecordMap.get("TicketDate"));
			// 关联单号
			btimesDetailDTO.setRelevantNo((String)memBtimesRecordMap.get("BillID"));
			// 变动渠道
			btimesDetailDTO.setChannel((String)memBtimesRecordMap.get("Channel"));
			// 重算次数
			btimesDetailDTO.setReCalcCount(String.valueOf(memBtimesRecordMap.get("ReCalcCount")));
			// 理由
			int reason = (Integer)memBtimesRecordMap.get("Reason");
			// 变化原因
			String reasonS = "";
			// 理由为规则计算的场合
			if(reason == 0 || reason == 2) {
				String allRules = (String)memBtimesRecordMap.get("BIN_RuleIDs");
				if(allRules != null && !"".equals(allRules)) {
					String[] allRuleArray = allRules.split(",");
					StringBuffer buffer = new StringBuffer();
					for(int i = 0; i < allRuleArray.length; i++) {
						if(allRulesMap.get(allRuleArray[i]) != null) {
							buffer.append(i + 1).append(DroolsConstants.MQ_REASON_COMMA).
							append(allRulesMap.get(allRuleArray[i])).
							append(DroolsConstants.LINE_SPACE);
						}
					}
					reasonS = buffer.toString();
				}
			} else if(reason == 1) { // 理由为初始数据导入的场合
				reasonS = DroolsConstants.MQ_INITDATA_REASON;
			} else if(reason == 3) { // 理由为化妆次数使用的场合
				reasonS = DroolsConstants.MQ_USEDTIMES_REASON;
			} else if(reason == 4) { // 会员等级化妆次数重算的场合
				reasonS = DroolsConstants.MQ_RECALCMEMLEVEL_REASON;
			}
			if (reasonS.length() > DroolsConstants.REASON_MAX_LENGTH) {
				reasonS = reasonS.substring(0, DroolsConstants.REASON_MAX_LENGTH);
			}
			// 变化原因
			btimesDetailDTO.setReason(reasonS);
			btimesDetailList.add(btimesDetailDTO);
		}
		
		// 当前化妆次数
		btimesMainDTO.setCurBtimes(btimesDetailList.get(btimesDetailList.size()-1).getCurBtimesNew());
		// 会员卡号
		btimesMainDTO.setMemberCode(btimesDetailList.get(btimesDetailList.size()-1).getMemberCode());
		
		// 化妆次数MQ明细业务List
		btimesMainDTO.setBtimesDetailList(btimesDetailList);
		// MQ消息 DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(organizationInfoId));
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandInfoId));
		// 单据类型
		mqInfoDTO.setBillType(btimesMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(btimesMainDTO.getMQMsg());
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", orgCode);
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MG);
		// 单据号
		dbObject.put("TradeNoIF", ticketNumber);
		// 修改次数
		dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务主体
	    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
	    // 业务主体代码
	    dbObject.put("TradeEntityCode", btimesMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", (String)memberInfoMap.get("memName"));
	    // 发生时间
	    dbObject.put("OccurTime", btimesMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
	    // 发送MQ消息
	    binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}

}
