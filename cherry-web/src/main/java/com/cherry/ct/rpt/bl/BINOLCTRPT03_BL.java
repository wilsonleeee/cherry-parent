/*
 * @(#)BINOLCTRPT03_BL.java     1.0 2013/08/06
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
package com.cherry.ct.rpt.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.bl.BINOLMQCOM01_BL;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT03_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT03_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 历史事件触发记录查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT03_BL implements BINOLCTRPT03_IF,BINOLCM37_IF{

	@Resource(name="binOLCTRPT03_Service")
	private BINOLCTRPT03_Service binOLCTRPT03_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_BL binOLMQCOM01_BL;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private CodeTable codeTable;
	
	@Override
	public int getMsgDetailCount(Map<String, Object> map) {
		return binOLCTRPT03_Service.getMsgDetailCount(map);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getMsgDetailList(Map<String, Object> map) throws Exception {
		// 获取沟通事件List
		List<Map<String, Object>> msgDetailList = binOLCTRPT03_Service.getMsgDetailList(map);
		List<Map<String, Object>> newmsgDetailList = new ArrayList<Map<String, Object>>();
		if(msgDetailList != null && !msgDetailList.isEmpty()){
			for(Map<String,Object> msgDetailMap : msgDetailList){
				Map<String,Object> newmsgDetailMap = new HashMap<String, Object>();
				String message = ConvertUtil.getString(msgDetailMap.get("message"));
				if(message.length() > 30){
					newmsgDetailMap.putAll(msgDetailMap);
					newmsgDetailMap.put("messageCut", message.substring(0, 30)+" ...");
				}else{
					newmsgDetailMap.putAll(msgDetailMap);
					newmsgDetailMap.put("messageCut", message);
				}
				newmsgDetailList.add(newmsgDetailMap);
			}
		}
		return newmsgDetailList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		// 获取导出数据List
		List<Map<String, Object>> msgDetailList = binOLCTRPT03_Service.getMsgDetailList(map);
		List<Map<String, Object>> newmsgDetailList = new ArrayList<Map<String, Object>>();
		if(msgDetailList != null && !msgDetailList.isEmpty()){
			for(Map<String,Object> msgDetailMap : msgDetailList){
				Map<String,Object> newmsgDetailMap = new HashMap<String, Object>();
				newmsgDetailMap.putAll(msgDetailMap);
				// 获取信息发送方式，如果为事件触发的情况则根据事件ID获取事件名称
				String runType = ConvertUtil.getString(msgDetailMap.get("runType"));
				if("3".equals(runType)){
					String planName = "";
					String planCode = ConvertUtil.getString(msgDetailMap.get("planCode"));
					if("9".equals(planCode)){
						planName = CherryConstants.EVENTNAME_CODE_9;
					}else if("10".equals(planCode)){
						planName = CherryConstants.EVENTNAME_CODE_10;
					}else if("99".equals(planCode)){
						planName = CherryConstants.EVENTNAME_CODE_99;
					}else if("100".equals(planCode)){
						planName = CherryConstants.EVENTNAME_CODE_100;
					}else{
						planName = codeTable.getVal("1219", planCode);
					}
					newmsgDetailMap.put("planName", planName);
				}
				newmsgDetailList.add(newmsgDetailMap);
			}
		}
		return newmsgDetailList;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		//Excel导出
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "planCode", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.planCode"), "20", "", "" },
				{ "planName", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.planName"), "20", "", "" },
				{ "memCode", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.memCode"), "20", "", "" },
				{ "memName", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.memName"), "20", "", "" },
				{ "joinDate", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.joinDate"), "15", "", "" },
				{ "birthDay", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.birthDay"), "15", "", "" },
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.counterCode"), "15", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.counterName"), "25", "", "" },
				{ "rsm", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.rsm"), "25", "", "" },
				{ "amm", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.amm"), "25", "", "" },
				{ "mobilephone", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.mobilePhone"), "15", "", "" },
				{ "message", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.message"), "30", "", "" },
				{ "sendTime", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.sendTime"), "25", "", "" },
				{ "couponCode", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "ctrpt.couponCode"), "15", "", "" },
		};
		int dataLen = binOLCTRPT03_Service.getMsgDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT03", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "joinDate desc");
		return map;
	}
	
	/**
     * 导出CSV处理
     */
	@Override
	public String export(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 下载文件名
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
        exportMap.put("tempFileName", downloadFileName);
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	// 压缩包名
        	String zipName = downloadFileName+".zip";
        	// 压缩文件处理
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
        return null;
	}

	@Override
	public void tran_sendMsgAgain(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String orgCode = ConvertUtil.getString(map.get("orgCode"));
		String sourse = ConvertUtil.getString(map.get("sourse"));
		String messageCode = ConvertUtil.getString(map.get("messageCode"));
		String sysTime = binOLCTRPT03_Service.getSYSDate();
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
		mainData.put("EventType", CherryConstants.SENDMSGAGAINEVENTTYPE);
		// 事件ID
		mainData.put("EventId", messageCode);
		// 沟通时间
		mainData.put("EventDate", sysTime);
		// 数据来源
		mainData.put("Sourse", sourse);
		
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
		dbObject.put("EventType", CherryConstants.SENDMSGEVENTTYPE);
		// 事件ID
		dbObject.put("EventId", messageCode);
		// 沟通时间
		dbObject.put("EventDate", sysTime);
		// 数据来源
		dbObject.put("Sourse", sourse);
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
	}
	
}
