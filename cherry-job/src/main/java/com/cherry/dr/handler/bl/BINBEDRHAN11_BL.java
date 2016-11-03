/*
 * @(#)BINBEDRHAN11_BL.java     1.0 2012/05/28
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
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.handler.service.BINBEDRHAN11_Service;
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
public class BINBEDRHAN11_BL {
	
	/** 处理会员的规则履历记录service */
	@Resource
	private BINBEDRHAN11_Service binBEDRHAN11_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
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
	public int tran_memRuleRecordHandle(Map<String, Object> map) throws Exception {
		
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
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
		
		// 所有的等级信息
		Map<String, Object> allMemLevelMap = new HashMap<String, Object>();
		// 查询会员等级信息List
		List<Map<String, Object>> memberLevelInfoList = binBEDRHAN11_Service.getMemberLevelInfoList(map);
		if(memberLevelInfoList != null && !memberLevelInfoList.isEmpty()) {
			for(int i = 0; i < memberLevelInfoList.size(); i++) {
				Map<String, Object> allLevelMap = memberLevelInfoList.get(i);
				allMemLevelMap.put(allLevelMap.get("memberLevelId").toString(), allLevelMap);
			}
		}
		
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
			
			// 查询会员规则履历List
			List<Map<String, Object>> memRuleRecordList = binBEDRHAN11_Service.getMemRuleRecordList(map);
			// 会员履历数据不为空
			if (memRuleRecordList != null && !memRuleRecordList.isEmpty()) {
				// 统计处理总件数
				totalCount += memRuleRecordList.size();
				List<DBObject> dbObjectList = new ArrayList<DBObject>();
				// 循环处理每个会员的规则履历记录，把规则履历从数据库迁移到MongoDB，同时把等级变化的履历发送到老后台
				for(int i = 0; i < memRuleRecordList.size(); i++) {
					Map<String, Object> memberInfoMap = memRuleRecordList.get(i);
					memberInfoMap.putAll(map);
					int memberInfoId = (Integer)memberInfoMap.get("memberInfoId");
					try {
						// 变更前等级
						String oldValue = (String)memberInfoMap.get("oldValue");
						if(oldValue != null) {
							String levelCode = this.getMemLevelCode(oldValue, allMemLevelMap);
							memberInfoMap.put("oldLevelCode", levelCode);
						}
						// 变更后等级
						String newValue = (String)memberInfoMap.get("newValue");
						// 变更后等级级别
						if(newValue != null) {
							String levelCode = this.getMemLevelCode(newValue, allMemLevelMap);
							memberInfoMap.put("newLevelCode", levelCode);
						}
						memberInfoMap.put("totalAmount", "0");
						// 生成添加MongoDB用的规则履历对象
						dbObjectList.add(this.createMongoRuleRecord(memberInfoMap));
						// 发送等级下发MQ消息
						this.sendLevelMes(memberInfoMap, allMemLevelMap);
						// 统计成功件数
						successCount++;
					} catch (Exception e) {
						// 统计失败件数
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EDR00031");
						// 会员ID
						batchLoggerDTO1.addParam(String.valueOf(memberInfoId));
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
				}
				// 把规则履历添加到MongoDB中
				this.addRuleRecordToMongDB(dbObjectList);
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
	 * 生成添加MongoDB用的规则履历对象
	 * 
	 * @param memRuleRecordMap 规则履历信息
	 * @return MongoDB用的规则履历对象
	 */
	public DBObject createMongoRuleRecord(Map<String, Object> memberInfoMap) {
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", memberInfoMap.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", memberInfoMap.get("brandCode"));
		// 会员ID
		dbObject.put("BIN_MemberInfoID", memberInfoMap.get("memberInfoId"));
		// 会员卡号
		dbObject.put("MemCode", memberInfoMap.get("memCode"));
		// 单据号
		dbObject.put("BillID", memberInfoMap.get("billId"));
		// 业务类型
		dbObject.put("TradeType", memberInfoMap.get("tradeType"));
		// 履历区分
		dbObject.put("RecordKbn", memberInfoMap.get("recordKbn"));
		// 更新前的值
		dbObject.put("OldValue", memberInfoMap.get("oldValue"));
		// 更新后的值
		dbObject.put("NewValue", memberInfoMap.get("newValue"));
		// 单据产生日期
		dbObject.put("TicketDate", memberInfoMap.get("ticketDate"));
		// 计算日期
		dbObject.put("CalcDate", memberInfoMap.get("calcDate"));
		// 规则ID
		dbObject.put("BIN_RuleIDs", memberInfoMap.get("ruleIDs"));
		// 重算次数
		dbObject.put("ReCalcCount", memberInfoMap.get("reCalcCount"));
		// 理由
		dbObject.put("Reason", memberInfoMap.get("reason"));
		// 变化类型
		dbObject.put("ChangeType", memberInfoMap.get("changeType"));
		// 匹配的活动
		dbObject.put("SubCampaignCodes", memberInfoMap.get("subCampaignCodes"));
		// 柜台号
		dbObject.put("Countercode", memberInfoMap.get("countercode"));
		// Ba卡号
		dbObject.put("EmployeeCode", memberInfoMap.get("baCode"));
		// 数据来源
		dbObject.put("Channel", memberInfoMap.get("channel"));
		// 有效区分
		dbObject.put("ValidFlag", memberInfoMap.get("validFlag"));
		return dbObject;
	}
	
	/**
	 * 把规则履历添加到MongoDB中
	 * 
	 * @param dbObjectList 规则履历信息List
	 * @throws Exception 
	 */
	public void addRuleRecordToMongDB(List<DBObject> dbObjectList) throws Exception {
		
		MongoDB.insert("MGO_RuleExecRecord", dbObjectList);
	}
	
	/**
	 * 发送等级下发MQ消息
	 * 
	 * @param memberInfoMap 会员等级变化信息
	 * @throws Exception 
	 */
	public void sendLevelMes(Map<String, Object> memberInfoMap, Map<String, Object> allMemLevelMap) throws Exception {
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
		String caltime = binBEDRHAN11_Service.getForwardSYSDate();
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
		// 等级MQ明细业务 DTO
		LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
		// 会员卡号
		levelDetailDTO.setMemberCode((String)memberInfoMap.get("memCode"));
		// 业务类型
		levelDetailDTO.setBizType((String)memberInfoMap.get("tradeType"));
		// 关联单据时间
		levelDetailDTO.setRelevantTicketDate((String)memberInfoMap.get("ticketDate"));
		// 关联单号
		levelDetailDTO.setRelevantNo((String)memberInfoMap.get("billId"));
		// 变动渠道
		levelDetailDTO.setChannel((String)memberInfoMap.get("channel"));
		// 重算次数
		levelDetailDTO.setReCalcCount(String.valueOf(memberInfoMap.get("reCalcCount")));
		// 操作类型
		levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 柜台号
		levelDetailDTO.setCounterCode((String)memberInfoMap.get("countercode"));
		// 员工编号
		levelDetailDTO.setEmployeeCode((String)memberInfoMap.get("baCode"));
		// 变更前等级
		levelDetailDTO.setMemberlevelOld((String)memberInfoMap.get("oldLevelCode"));
		// 变更后等级
		levelDetailDTO.setMemberlevelNew((String)memberInfoMap.get("newLevelCode"));
		// 变化类型
		levelDetailDTO.setChangeType((String)memberInfoMap.get("changeType"));
		// 累积金额
		levelDetailDTO.setTotalAmount((String)memberInfoMap.get("totalAmount"));
		// 变化原因
		levelDetailDTO.setReason(DroolsConstants.MQ_INITIMPORT_REASON);
		levelDetailList.add(levelDetailDTO);
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

}
