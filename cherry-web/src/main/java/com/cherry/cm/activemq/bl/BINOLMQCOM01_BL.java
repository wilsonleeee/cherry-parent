/*	
 * @(#)BINOLMQCOM01_BL.java     1.0 2011/12/14
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
package com.cherry.cm.activemq.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.activemq.service.BINOLMQCOM01_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 发送MQ消息共通处理 BL
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class BINOLMQCOM01_BL implements BINOLMQCOM01_IF {
	
	/** ActiveMQ消息发送类 **/
	@Resource
	private MessageSender messageSender;
	
	/** 发送MQ消息共通处理 Service **/
	@Resource
	private BINOLMQCOM01_Service binOLMQCOM01_Service;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/**
	 * 
	 * 发送MQ消息处理（默认保存消息记录到数据库MQ收发日志表）
	 * 
	 * @param mqInfoDTO MQ消息 DTO
	 * @throws Exception 
	 */
	@Override
	public void sendMQMsg(MQInfoDTO mqInfoDTO) throws Exception {
		
		this.sendMQMsg(mqInfoDTO, true);
	}

	/**
	 * 
	 * 发送MQ消息处理
	 * 
	 * @param mqInfoDTO MQ消息 DTO
	 * @param saveLog 是否保存消息到数据库MQ收发日志表，true：保存，false：不保存
	 * @throws Exception 
	 */
	@Override
	public void sendMQMsg(MQInfoDTO mqInfoDTO, boolean saveLog)
			throws Exception {
		
		// MQ消息 DTO为null的场合抛出异常
		if(mqInfoDTO == null) {
			throw new CherryException("mq.messageError_8");
		}
		// 已经封装好的消息体
		String msg = mqInfoDTO.getData();
		// 已经封装好的消息体不存在的场合，封装还没封装过的消息体信息
		if(msg == null || "".equals(msg)) {
			// 封装还没封装过的消息体信息
			msg = this.getMQMessage(mqInfoDTO.getMsgDataMap());
		}
		// 消息体不存在的场合抛出异常
		if(msg == null || "".equals(msg)) {
			throw new CherryException("mq.messageError_7");
		}
		//将封装好的MQ消息体放到mqInfoDTO中
		mqInfoDTO.setData(msg);
		// 消息发送队列名
		String msgQueueName = mqInfoDTO.getMsgQueueName();
		// 消息发送队列名为空的场合默认设为新后台到witpos的队列名
		if(msgQueueName == null || "".equals(msgQueueName)) {
			if ("ML".equals(mqInfoDTO.getBillType()) ||
					"MP".equals(mqInfoDTO.getBillType()) ||
					"ME".equals(mqInfoDTO.getBillType())) {
				msgQueueName = CherryConstants.CHERRYTOPOSMEMBER;
				mqInfoDTO.setMsgQueueName(msgQueueName);
			} else {
				msgQueueName = CherryConstants.CHERRYTOPOSMSGQUEUE;
			}
		}
		// 下发终端MQ
		if (msgQueueName.equals(CherryConstants.CHERRYTOPOSMSGQUEUE) ||
				CherryConstants.CHERRYTOPOSMEMBER.equals(msgQueueName) ||
				CherryConstants.CHERRYTOPOSSP.equals(msgQueueName)) {
			int brandInfoId = mqInfoDTO.getBrandInfoId();
			int organizationInfoId = mqInfoDTO.getOrganizationInfoId();
			if (0 == brandInfoId || 0 == organizationInfoId) {
				Map<String, Object> searchMap = new HashMap<String, Object>();
				searchMap.put("brandCode", mqInfoDTO.getBrandCode());
				// 查询品牌信息
				Map<String, Object> brandInfo = binOLMQCOM01_Service.selBrandInfo(searchMap);
				if (null != brandInfo && !brandInfo.isEmpty()) {
					brandInfoId = Integer.parseInt(String.valueOf(brandInfo.get("brandInfoId")));
					organizationInfoId = Integer.parseInt(String.valueOf(brandInfo.get("organizationInfoId")));
				}
			}
			// 不需要下发终端
			if (0 != brandInfoId && 0 != organizationInfoId &&
					"2".equals(binOLCM14_BL.getConfigValue("1303", String.valueOf(organizationInfoId), String.valueOf(brandInfoId)))) {
				return;
			}
		}
		// 发送MQ消息
		messageSender.sendGroupMessage(msg, msgQueueName, mqInfoDTO.getJmsGroupId());
		// 需要保存消息日志的场合，保存日志处理
		if(saveLog) {
			// 非新后台内部发送的消息
			if(CherryConstants.CHERRYTOPOSMSGQUEUE.equals(msgQueueName) ||
					CherryConstants.CHERRYTOPOSMEMBER.equals(msgQueueName) ||
					CherryConstants.CHERRYTOPOSSP.equals(msgQueueName)) {
				if(mqInfoDTO.getSource() == null || "".equals(mqInfoDTO.getSource())) {
					// 数据插入方标志:CHERRY
					mqInfoDTO.setSource(CherryConstants.MQ_SOURCE_CHERRY);
				}
				// 消息方向:发送
				mqInfoDTO.setSendOrRece(CherryConstants.MQ_SENDORRECE_S);
				if(mqInfoDTO.getReceiveFlag() == null || "".equals(mqInfoDTO.getReceiveFlag())) {
					// 消息发送接收标志位:未比对
					mqInfoDTO.setReceiveFlag(CherryConstants.MQ_RECEIVEFLAG_0);
				}
				if(mqInfoDTO.getCreatedBy() == null || "".equals(mqInfoDTO.getCreatedBy())) {
					// 作成者
					mqInfoDTO.setCreatedBy("BINOLMQCOM01");
				}
				if(mqInfoDTO.getUpdatedBy() == null || "".equals(mqInfoDTO.getUpdatedBy())) {
					// 更新者
					mqInfoDTO.setUpdatedBy("BINOLMQCOM01");
				}
				if(mqInfoDTO.getCreatePGM() == null || "".equals(mqInfoDTO.getCreatePGM())) {
					// 做成程序名
					mqInfoDTO.setCreatePGM("BINOLMQCOM01");
				}
				if(mqInfoDTO.getUpdatePGM() == null || "".equals(mqInfoDTO.getUpdatePGM())) {
					// 更新程序名
					mqInfoDTO.setUpdatePGM("BINOLMQCOM01");
				}
				// 插入MQ收发日志表
				binOLMQCOM01_Service.insertMQLog(mqInfoDTO);
			} else { // 新后台内部发送的消息
				DBObject mqLog = new BasicDBObject();
				if(mqInfoDTO.getSource() == null || "".equals(mqInfoDTO.getSource())) {
					// 数据插入方标志
					mqLog.put("Source", CherryConstants.MQ_SOURCE_CHERRY);
				} else {
					// 数据插入方标志
					mqLog.put("Source", mqInfoDTO.getSource());
				}
				// 消息方向
				mqLog.put("SendOrRece", CherryConstants.MQ_SENDORRECE_S);
				// 组织代号
				mqLog.put("OrgCode", mqInfoDTO.getOrgCode());
				// 品牌代码
				mqLog.put("BrandCode", mqInfoDTO.getBrandCode());
				// 单据类型
				mqLog.put("BillType", mqInfoDTO.getBillType());
				// 单据号
				mqLog.put("BillCode", mqInfoDTO.getBillCode());
				// 修改次数
				mqLog.put("ModifyCount", String.valueOf(mqInfoDTO.getSaleRecordModifyCount()));
				// 柜台号
				mqLog.put("CounterCode", mqInfoDTO.getCounterCode());
				// 业务时间
				mqLog.put("BusTime", mqInfoDTO.getBusTime());
				if(mqInfoDTO.getInsertTime() == null || "".equals(mqInfoDTO.getInsertTime())) {
					// 插入时间
					mqLog.put("InsertTime", binOLMQCOM01_Service.getSYSDate());
				} else {
					// 插入时间
					mqLog.put("InsertTime", mqInfoDTO.getInsertTime());
				}
				if(mqInfoDTO.getReceiveFlag() == null || "".equals(mqInfoDTO.getReceiveFlag())) {
					// 消息发送接收标志位:未比对
					mqLog.put("ReceiveFlag", CherryConstants.MQ_RECEIVEFLAG_0);
				} else {
					// 消息发送接收标志位:未比对
					mqLog.put("ReceiveFlag", mqInfoDTO.getReceiveFlag());
				}
				// 消息发送队列名
				mqLog.put("MsgQueueName", msgQueueName);
				// 消息体
				mqLog.put("Data", mqInfoDTO.getData());
				// JMS协议头中的JMSGROUPID
				mqLog.put("JmsGroupId", mqInfoDTO.getJmsGroupId());
				// 插入MQ收发日志表（新后台内部发送消息用）
				binOLMQCOM01_Service.addMongoDBMqLog(mqLog);
			}
		}
		// 需要保存日志到mongodb的场合
		if(mqInfoDTO.getDbObject() != null) {
			String occurTime = (String)mqInfoDTO.getDbObject().get("OccurTime");
			if(occurTime == null || "".equals(occurTime)) {
				mqInfoDTO.getDbObject().put("OccurTime", binOLMQCOM01_Service.getForwardSYSDate());
			}
			//设定MQ消息体
			String content = ConvertUtil.getString(mqInfoDTO.getDbObject().get("Content"));
			if("".equals(content)){
				mqInfoDTO.getDbObject().put("Content", mqInfoDTO.getData());
			}
			
			// 插入MQ消息发送日志表
			binOLMQCOM01_Service.addMongoDBBusLog(mqInfoDTO.getDbObject());
		}
	}
	
	/**
	 * 包装MQ消息体，将参数Map中的数据包装成MQ消息
	 * @param 
	 * 		Map
	 * 			"Version":必须字段，类型为String，消息体版本
	 * 			"Type":可选必须字段，类型为String，消息命令类型，如果消息是新后台发往老后台的该字段为空
	 * 			"DataType":必须字段，类型为String，对应的值为消息体业务数据的格式，有"text/plain"（不推荐使用，逐渐废弃，并且该共通不处理此类型的数据），"application/json"，"application/xml"以及"BLOBMessage"
	 * 			"DataLine":必须字段，类型为Map<String,Object>，存放的是业务数据，此Map中存放的是Key为"MainData"（必须字段，类型为Map）的主数据以及其的非必须的数据，例如Key为"DetailDataDTOList"（可选，类型为List<Map>）的明细数据
	 *@return
	 * 		String 包装好的MQ消息体，可以直接发送
	 * */
	@SuppressWarnings("unchecked")
	private String getMQMessage(Map<String,Object> MessageMap) throws Exception{
		try{
			if(null == MessageMap || MessageMap.isEmpty()){
				return null;
			}
			
			//数据类型
			String dataType = "";
			//如果没有设定DataType不处理，直接返回null
			if(!MessageMap.containsKey(CherryConstants.MESSAGE_DATATYPE_TITLE)){
				return null;
			}else{
				dataType = ConvertUtil.getString(MessageMap.get(CherryConstants.MESSAGE_DATATYPE_TITLE));
				//不处理的数据类型
				if("".equals(dataType) || CherryConstants.DATATYPE_TEXT_PLAIN.equals(dataType)){
					return null;
				}//暂不提供xml数据类型的封装
				else if(CherryConstants.DATATYPE_APPLICATION_XML.equals(dataType)){
					throw new CherryException("mq.messageError_1");
				}//抛出异常
				else if(!CherryConstants.DATATYPE_APPLICATION_JSON.equals(dataType)){
					throw new CherryException("mq.messageError_2");
				}
			}
			
			StringBuffer message = new StringBuffer();
			//版本号
			String version = null;
			//MQ数据类型
			String type = null;
			
			//如果没有设定Version的值或者它的值设定为空则抛出异常
			try{
				version = ConvertUtil.getString(MessageMap.get(CherryConstants.MESSAGE_VERSION_TITLE));
				if(("").equals(version)){
					throw new Exception();
				}
				message.append(CherryConstants.MESSAGE_VERSION_SIGN);
				message.append(version);
				message.append(CherryConstants.MESSAGE_LINE_BREAK);
			}catch(Exception e){
				throw new CherryException("mq.messageError_3",e);
			}
			
			//如果设定了Type则将Type封装进去
			type = ConvertUtil.getString(MessageMap.get(CherryConstants.MESSAGE_TYPE_TITLE));
			if(!("").equals(type)){
				message.append(CherryConstants.MESSAGE_TYPE_SIGN);
				message.append(type);
				message.append(CherryConstants.MESSAGE_LINE_BREAK);
			}
			
			//消息数据类型
			message.append(CherryConstants.MESSAGE_DATATYPE_SIGN);
			message.append(dataType);
			message.append(CherryConstants.MESSAGE_LINE_BREAK);
			
			//处理业务数据（DataLine）
			Map<String,Object> dataLineMap = null;
			try{
				dataLineMap = (Map<String, Object>) MessageMap.get(CherryConstants.DATALINE_JSON_XML);
				//如果dataLine设定为空抛出自定义异常
				if(null == dataLineMap || dataLineMap.isEmpty()){
					throw new Exception("");
				}
				
				String dataLine = "";
				try{
					dataLine = CherryUtil.map2Json(dataLineMap);
				}catch(Exception e){
					throw new CherryException("mq.messageError_5",e);
				}
				message.append(CherryConstants.DATALINE_JSON_XML_SIGN);
				message.append(dataLine);
				message.append(CherryConstants.MESSAGE_LINE_BREAK);
				
			}catch(Exception e){
				if(e instanceof CherryException){
					throw (CherryException)e;
				}else{
					throw new CherryException("mq.messageError_6",e);
				}
			}
			
			//结束标志
			message.append(CherryConstants.END_MESSAGE_SIGN);
			
			return message.toString();
		}catch(Exception e){
			if(e instanceof CherryException){
				throw (CherryException)e;
			}else{
				throw new CherryException("mq.messageError_7",e);
			}
		}
	}
	
}
