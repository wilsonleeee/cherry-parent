/*
 * @(#)BINOLCTCOM05_BL.java     1.0 2013/06/06
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
package com.cherry.ct.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM38_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.interfaces.BINOLCTCOM05_IF;
import com.cherry.ct.common.service.BINOLCTCOM04_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * 发送测试信息BL
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.06
 */
public class BINOLCTCOM05_BL implements BINOLCTCOM05_IF{
	@Resource
	private BINOLCTCOM04_Service binolctcom04_Service;
	
	@Resource
	private BINOLCM38_BL binolcm38_BL;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 取得各种业务类型的单据流水号 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	@Override
	public void tran_sendTestMsg(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String orgCode = ConvertUtil.getString(map.get("orgCode"));
		String messageType = ConvertUtil.getString(map.get("messageType"));
		String contents = ConvertUtil.getString(map.get("contents"));
		String smsChannel=ConvertUtil.getString(map.get("smsChannel"));
		String messageContents = "";
		// 获取沟通模板变量List
		List<Map<String, Object>> variableList = binOLCM32_BL.getVariableList(map);
		for(Map<String,Object> variableMap : variableList){
			if(contents.contains(ConvertUtil.getString(variableMap.get("variableValue")))){
				contents = contents.replaceAll(ConvertUtil.getString(variableMap.get("variableValue")), ConvertUtil.getString(variableMap.get("comments")));
			}
		}
		messageContents = contents;
		contents = toUtf8String(contents);
		String resCodeText = ConvertUtil.getString(map.get("resCodeList"));
		resCodeText = resCodeText.replace("；", ";");
		resCodeText = resCodeText.replace("，", ";");
		resCodeText = resCodeText.replace(",", ";");
		String[] resCodes = resCodeText.split(";");
		for (String resCode : resCodes) {
			if(!"".equals(resCode) && resCode.length()>0){
				if("1".equals(messageType)){
					Map<String, Object> sendParamMap = new HashMap<String, Object>();
					String sendInterfaceType = PropertiesUtil.pps.getProperty("sendInterfaceType");
					if("WEBIF".equals(sendInterfaceType)){
						// 发送测试短信
						String webServiceUrl = PropertiesUtil.pps.getProperty("sendIntelfaceUrl");
						sendParamMap.put("userid", PropertiesUtil.pps.getProperty("sendIntelfaceUserId"));
						sendParamMap.put("password", PropertiesUtil.pps.getProperty("sendIntelfacePwd"));
						sendParamMap.put("destnumbers", resCode);
						sendParamMap.put("msg", contents);
						sendParamMap.put("sendtime", "");
						binolcm38_BL.sendMsgWebService(webServiceUrl, sendParamMap);
					}else{
						String sysTime = binolctcom04_Service.getSYSDate();
						// 发送MQ
						MQInfoDTO mqInfoDTO = new MQInfoDTO();
						// 品牌代码
						mqInfoDTO.setBrandCode(brandCode);
						
						String billType = CherryConstants.MESSAGE_TYPE_ES;
						
						String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))), 
								Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))), "", billType);
						// 业务类型
						mqInfoDTO.setBillType(billType);
						// 单据号
						mqInfoDTO.setBillCode(billCode);
						// 消息发送队列名
						mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);
						
						// 设定消息内容
						Map<String,Object> msgDataMap = new HashMap<String,Object>();
						// 设定消息版本号
						msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
						// 设定消息命令类型
						msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
						// 设定消息数据类型
						msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
						// 设定消息的数据行
						Map<String,Object> dataLine = new HashMap<String,Object>();
						// 消息的主数据行
						Map<String,Object> mainData = new HashMap<String,Object>();
						// 品牌代码
						mainData.put("BrandCode", brandCode);
						// 业务类型
						mainData.put("TradeType", billType);
						// 单据号
						mainData.put("TradeNoIF", billCode);
						// 事件类型
						mainData.put("EventType", CherryConstants.TESTMSGEVENTTYPE);
						// 事件ID
						mainData.put("EventId", resCode);
						// 沟通内容
						mainData.put("MessageContents", messageContents);
						// 沟通时间
						mainData.put("EventDate", sysTime);
						// 数据来源
						mainData.put("Sourse", "Cherry");
						//短信通道类型
						mainData.put("smsChannel", smsChannel);
						dataLine.put("MainData", mainData);
						
						msgDataMap.put("DataLine", dataLine);
						
						mqInfoDTO.setMsgDataMap(msgDataMap);
						
						// 设定插入到MongoDB的信息
						DBObject dbObject = new BasicDBObject();
						// 组织代码
						dbObject.put("OrgCode", orgCode);
						// 品牌代码
						dbObject.put("BrandCode", brandCode);
						// 业务类型
						dbObject.put("TradeType", billType);
						// 单据号
						dbObject.put("TradeNoIF", billCode);
						// 事件类型
						dbObject.put("EventType", CherryConstants.TESTMSGEVENTTYPE);
						// 事件ID
						dbObject.put("EventId", resCode);
						// 沟通内容
						dbObject.put("MessageContents", messageContents);
						// 沟通时间
						dbObject.put("EventDate", sysTime);
						// 数据来源
						dbObject.put("Sourse", "Cherry");
						mqInfoDTO.setDbObject(dbObject);
						
						// 发送MQ消息
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
					}
				}else if("2".equals(messageType)){
					// 发送测试邮件
				}
			}
		}
	}	
	
	public String toUtf8String(String s) throws Exception{  
		if (s == null || s.equals("")){  
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;  
			for(int i = 0; i < s.length(); i++){
				c = s.charAt(i);
				if (c >= 0 && c <= 255){
					sb.append(c);
				}else{
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		}catch (Exception e){
			throw e;
		}
		return sb.toString();
	}

	
}
