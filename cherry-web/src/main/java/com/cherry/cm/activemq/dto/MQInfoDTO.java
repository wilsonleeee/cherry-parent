/*	
 * @(#)MQInfoDTO.java     1.0 2011/12/14	
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
package com.cherry.cm.activemq.dto;

import java.util.Map;

import com.mongodb.DBObject;

/**
 * MQ消息 DTO
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class MQInfoDTO {
	
	/** 数据插入方标志 */
	private String source;
	
	/** 消息方向 */
	private String sendOrRece;
	
	/** 所属组织 */
	private int organizationInfoId;
	
	/** 所属品牌 */
	private int brandInfoId;
	
	/** 单据类型 */
	private String billType;
	
	/** 单据号 */
	private String billCode;
	
	/** 销售记录修改次数 */
	private int saleRecordModifyCount;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 交易日期 */
	private String txdDate;
	
	/** 交易时间 */
	private String txdTime;
	
	/** Transactionlog表开始写入时间 */
	private String beginPuttime;
	
	/** Transactionlog表结束写入时间 */
	private String endPuttime;
	
	/** 是否促销品区分 */
	private String isPromotionFlag;
	
	/** 消息发送接收标志位 */
	private String receiveFlag;
	
	/** 消息发送队列名 */
	private String msgQueueName;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 作成日时 */
	private String createTime;
	
	/** 作成者 */
	private String createdBy;
	
	/** 作成程序名 */
	private String createPGM;
	
	/** 更新日时 */
	private String updateTime;
	
	/** 更新者 */
	private String updatedBy;
	
	/** 更新程序名 */
	private String updatePGM;
	
	/** MQ消息发送日志(mongodb用) */
	private DBObject dbObject;
	
	/** 消息体(已经封装好的消息体) */
	private String data;
	
	/** 消息体信息(还没封装过的消息体信息) */
	private Map<String, Object> msgDataMap;
	
	/** 组织代码 */
	private String orgCode;
	
	/** 品牌代码 */
	private String brandCode;
	
	/** 业务时间 */
	private String busTime;
	
	/** 插入时间 */
	private String insertTime;
	
	/** JMS协议头中的JMSGROUPID */
	private String jmsGroupId;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSendOrRece() {
		return sendOrRece;
	}

	public void setSendOrRece(String sendOrRece) {
		this.sendOrRece = sendOrRece;
	}

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public int getSaleRecordModifyCount() {
		return saleRecordModifyCount;
	}

	public void setSaleRecordModifyCount(int saleRecordModifyCount) {
		this.saleRecordModifyCount = saleRecordModifyCount;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getTxdDate() {
		return txdDate;
	}

	public void setTxdDate(String txdDate) {
		this.txdDate = txdDate;
	}

	public String getTxdTime() {
		return txdTime;
	}

	public void setTxdTime(String txdTime) {
		this.txdTime = txdTime;
	}

	public String getBeginPuttime() {
		return beginPuttime;
	}

	public void setBeginPuttime(String beginPuttime) {
		this.beginPuttime = beginPuttime;
	}

	public String getEndPuttime() {
		return endPuttime;
	}

	public void setEndPuttime(String endPuttime) {
		this.endPuttime = endPuttime;
	}

	public String getIsPromotionFlag() {
		return isPromotionFlag;
	}

	public void setIsPromotionFlag(String isPromotionFlag) {
		this.isPromotionFlag = isPromotionFlag;
	}

	public String getReceiveFlag() {
		return receiveFlag;
	}

	public void setReceiveFlag(String receiveFlag) {
		this.receiveFlag = receiveFlag;
	}

	public String getMsgQueueName() {
		return msgQueueName;
	}

	public void setMsgQueueName(String msgQueueName) {
		this.msgQueueName = msgQueueName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatePGM() {
		return createPGM;
	}

	public void setCreatePGM(String createPGM) {
		this.createPGM = createPGM;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatePGM() {
		return updatePGM;
	}

	public void setUpdatePGM(String updatePGM) {
		this.updatePGM = updatePGM;
	}

	public DBObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map<String, Object> getMsgDataMap() {
		return msgDataMap;
	}

	public void setMsgDataMap(Map<String, Object> msgDataMap) {
		this.msgDataMap = msgDataMap;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBusTime() {
		return busTime;
	}

	public void setBusTime(String busTime) {
		this.busTime = busTime;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getJmsGroupId() {
		return jmsGroupId;
	}

	public void setJmsGroupId(String jmsGroupId) {
		this.jmsGroupId = jmsGroupId;
	}
}
