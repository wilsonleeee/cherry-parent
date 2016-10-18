/*  
 * @(#)BINBEMQMES14_BL.java     1.0 2014/11/25     
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
package com.cherry.mq.mes.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM31_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES03_Service;
import com.cherry.mq.mes.service.BINBEMQMES08_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 会员扩展信息（和俱乐部关联）接收处理BL
 * 
 * @author hub
 *
 */
public class BINBEMQMES14_BL {
	
	/** 消息数据接收共通处理Service */
	@Resource
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	/** 会员初始数据采集信息接收处理Service **/
	@Resource
	private BINBEMQMES08_Service binBEMQMES08_Service;
	
	/** 规则处理 BL */
	@Resource
	private BINOLCM31_BL binOLCM31_BL;
	
	@Resource(name="binBEMQMES03_Service")
	private BINBEMQMES03_Service binBEMQMES03_Service;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource(name="binBEMQMES98_BL")
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	public void handleMessage(Map<String, Object> map) throws Exception {
		String tradeType = ConvertUtil.getString(map.get("tradeType"));
		if("MZ".equalsIgnoreCase(tradeType)){
			// 取得会员初始数据明细数据
			List<Map<String,Object>> detailList = (List<Map<String,Object>>) map.get("detailDataDTOList");
			String clubcd = null;
			if (null != detailList && !detailList.isEmpty()) {
				clubcd = (String) detailList.get(0).get("clubCode");
			}
			if (!CherryChecker.isNullOrEmpty(clubcd, true)) {
				// 查询会员信息
				Map<String, Object> memberInfo = binBEMQMES08_Service.getMemberInfoID(map);
				// 会员信息不存在的场合，表示接收失败，结束处理
				if(memberInfo == null) {
					MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_34);
				}
				map.putAll(memberInfo);
				// 柜台号
				map.put("counterCode", map.get("tradeCounterCode"));
				// BA卡号
				map.put("BAcode", map.get("tradeBAcode"));
				// 查询员工信息
				Map<String, Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(map);
				if(employeeInfo != null) {
					map.putAll(employeeInfo);
				}
				// 查询柜台部门信息
				Map<String, Object> counterInfo = binBEMQMES99_Service.selCounterDepartmentInfo(map);
				if(counterInfo != null) {
					map.putAll(counterInfo);
				}
				// 会员信息ID
				int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
				// 查询会员俱乐部扩展信息
				List<Map<String, Object>> clubExtList = binBEMQMES08_Service.getClubExtList(map);
				
				List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> recalList = null;
				String brandCode = (String) map.get("brandCode");
				String orgCode = (String) map.get("orgCode");
				boolean isRef = null != binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
				if (isRef) {
					recalList = new ArrayList<Map<String, Object>>();
				}
				boolean mzflag = false;
				// 循环处理会员初始数据明细数据
				for(Map<String,Object> detailMap : detailList) {
					detailMap.put("memberInfoId", memberInfoId);
					detailMap.put("memberInfoID", memberInfoId);
					detailMap.put("counterCode", map.get("counterCode"));
					detailMap.put("organizationID", map.get("organizationID"));
					detailMap.put("BAcode", map.get("BAcode"));
					detailMap.put("employeeID", map.get("employeeID"));
					detailMap.put("sourse", map.get("sourse"));
					detailMap.put("machinecode", map.get("machineCode"));
					detailMap.put("joinTime", map.get("tradeTime"));
					setInsertInfoMapKey(detailMap);
					// 推荐会员
					String Referrer = (String) detailMap.get("referrer");
					int referrerId = 0;
					if(Referrer != null && !"".equals(Referrer)) {
						Map<String, Object> checkMap = new HashMap<String, Object>();
						checkMap.put("organizationInfoID", map.get("organizationInfoID"));
						checkMap.put("brandInfoID", map.get("brandInfoID"));
						checkMap.put("referrer", Referrer);
						// 查询推荐会员ID
						Map<String, Object> referrerMap = binBEMQMES03_Service.selReferrerID(checkMap);
						if (null == referrerMap || referrerMap.isEmpty()) {
							// 该推荐会员卡号不存在
							MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_79 +"\""+Referrer+"\"");
						}
						referrerId = Integer.parseInt(referrerMap.get("referrerID").toString());
						detailMap.putAll(referrerMap);
					}
					int version1 = Integer.parseInt(String.valueOf(detailMap.get("version")));
					boolean flag = true;
					if (null != clubExtList) {
						for (int j = 0; j < clubExtList.size(); j++) {
							Map<String, Object> clubInfo = clubExtList.get(j);
							if (detailMap.get("clubCode").equals(clubInfo.get("clubCode"))) {
								flag = false;
								int version = Integer.parseInt(clubInfo.get("version").toString());
								// 消息版本号高于数据库的版本号需要进行更新处理
								if (version1 > version) {
									detailMap.put("clubLevelId", clubInfo.get("clubLevelId"));
									upList.add(detailMap);
									if (0 == Integer.parseInt(String.valueOf(clubInfo.get("organizationId")))) {
										mzflag = true;
										version1++;
										detailMap.put("version", version1);
										detailMap.put("upCtbaFlag", "1");
									}
									if (isRef) {
										if (referrerId != Integer.parseInt(clubInfo.get("referrerId").toString())) {
											recalList.add(detailMap);
										}
									}
								}
								clubExtList.remove(j);
								break;
							}
						}
					}
					if (flag) {
						version1++;
						detailMap.put("version", version1);
						detailMap.put("memberClubId", binOLCM31_BL.selMemClubId(detailMap));
						newList.add(detailMap);
						if (isRef && 0 != referrerId) {
							recalList.add(detailMap);
						}
					}
				}
				if (!newList.isEmpty()) {
					mzflag = true;
					Map<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put("organizationInfoID", map.get("organizationInfoID"));
					searchMap.put("brandInfoID", map.get("brandInfoID"));
					String tradeTime = (String) map.get("tradeTime");
					for (Map<String, Object> newMap : newList) {
						searchMap.put("mClubId", newMap.get("memberClubId"));
						int memberLevel = binOLCM31_BL.getClubDefaultLevel(searchMap);
						if (0 != memberLevel) {
							newMap.put("memberLevel", memberLevel);
							newMap.put("levelStatus", CherryConstants.LEVELSTATUS_1);
							newMap.put("grantMemberLevel", memberLevel);
							newMap.put("levelAdjustDay", tradeTime);
						}
					}
					// 插入会员俱乐部扩展信息
					binBEMQMES08_Service.addClubExtList(newList);
				}
				if (!upList.isEmpty()) {
					// 更新会员俱乐部扩展信息
					binBEMQMES08_Service.updateClubExtList(upList);
				}
				if (isRef && !recalList.isEmpty()) {
					for (Map<String, Object> recalMap : recalList) {
						// 查询会员的最早销售时间
						String saleTime = binBEMQMES03_Service.getMinSaleTime(recalMap);
						if(saleTime != null) {
							Map<String, Object> paramMap = new HashMap<String, Object>();
							// 组织ID
							paramMap.put("organizationInfoID", map.get("organizationInfoID"));
							// 品牌ID
							paramMap.put("brandInfoID", map.get("brandInfoID"));
							// 组织代号
							paramMap.put("orgCode", map.get("orgCode"));
							// 品牌代码，即品牌简称
							paramMap.put("brandCode", map.get("brandCode"));
							paramMap.put("memberInfoID", memberInfoId);
							paramMap.put("newMemcode", map.get("memberCode"));
							paramMap.put("reCalcDate", saleTime);
							paramMap.put("reCalcType", "0");
							paramMap.put("memberClubID", recalMap.get("memberClubID"));
							// 插入重算信息表
							binBEMQMES03_Service.insertReCalcInfo(paramMap);
							// 发送MQ重算消息进行实时重算
							sendReCalcMsg(paramMap);
						}
					}
				}
				if (mzflag) {
					map.put("organizationInfoId", map.get("organizationInfoID"));
					map.put("brandInfoId", map.get("brandInfoID"));
					// 发送会员扩展信息MQ消息(全部记录)
					binOLCM31_BL.sendAllMZMQMsg(map);
					sendMZRuleMsg(map, memberInfoId, newList);
				}
			}
			//信息插入到MogoDB
			DBObject dbObject = new BasicDBObject();
			// 组织代号
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", map.get("tradeType"));
			// 单据号
			dbObject.put("TradeNoIF", map.get("tradeNoIF"));
			// 修改回数
			dbObject.put("ModifyCounts", "0");
			// 业务主体
			dbObject.put("TradeEntity", "0");
			// 业务主体代号
			dbObject.put("TradeEntityCode", map.get("memberCode"));
			//发生时间
			dbObject.put("OccurTime", map.get("tradeTime"));
			// 操作员工
			dbObject.put("UserCode", map.get("BAcode"));
			// 消息体
			dbObject.put("Content", map.get("messageBody"));
			map.put("dbObject", dbObject);
			map.put("modifyCounts", "0");
		} else {
			// 没有此业务类型
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
		}
	}
	
	private void setInsertInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "BINBEMQMES14");
		map.put("createPGM", "BINBEMQMES14");
		map.put("updatedBy", "BINBEMQMES14");
		map.put("updatePGM", "BINBEMQMES14");
	}
		
	private void sendMZRuleMsg(Map<String, Object> map, int memberInfoId, List<Map<String, Object>> newList) throws Exception {
		if (null == newList || newList.isEmpty()) {
			return;
		}
		String memberCode = (String) map.get("memberCode");
		// 消息的明细数据行
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> newMap : newList) {
			Map<String,Object> ruleMap = new HashMap<String,Object>();
			ruleMap.put("memberCode", memberCode);
			ruleMap.put("brandInfoID", map.get("brandInfoID"));
	    	ruleMap.put("organizationInfoID", map.get("organizationInfoID"));
	    	ruleMap.put("orgCode", map.get("orgCode"));
	    	ruleMap.put("brandCode", map.get("brandCode"));
	    	ruleMap.put("clubCode", newMap.get("clubCode"));
	    	ruleMap.put("tradeNoIF", map.get("tradeNoIF"));
	    	ruleMap.put("tradeType", "MB");
	    	ruleMap.put("sourse", map.get("sourse"));
	    	// 柜台号
	    	ruleMap.put("counterCode", map.get("tradeCounterCode"));
			// BA卡号
	    	ruleMap.put("BAcode", map.get("tradeBAcode"));
	    	detailDataList.add(ruleMap);
		}
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
		String billType = MessageConstants.MESSAGE_TYPE_RU;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
				Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(MessageConstants.CHERRY_RULE_MSGQUEUE);
		// JMS协议头中的JMSGROUPID
		mqInfoDTO.setJmsGroupId(brandCode+String.valueOf(memberInfoId/10000));
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_RU);
		// 设定消息命令类型
		msgDataMap.put("Type", MessageConstants.MESSAGE_TYPE_1003);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("BrandCode", map.get("brandCode"));
		mainData.put("TradeType", billType);
		mainData.put("TradeNoIF", billCode);
		mainData.put("ModifyCounts", "0");
		dataLine.put("MainData", mainData);
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 发送MQ重算消息进行实时重算
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendReCalcMsg(Map<String, Object> map) throws Exception {
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_MR;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
				Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MR);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1001);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 修改次数
		mainData.put("ModifyCounts", "0");
		// 会员ID
		mainData.put("memberInfoId", map.get("memberInfoID"));
		// 会员俱乐部ID
		mainData.put("memberClubId", map.get("memberClubID"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
}
