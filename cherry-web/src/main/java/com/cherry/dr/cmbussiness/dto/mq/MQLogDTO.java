/*	
 * @(#)MQLogDTO.java     1.0 2011/09/02	
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
package com.cherry.dr.cmbussiness.dto.mq;

import com.cherry.dr.cmbussiness.dto.core.BaseDTO;
import com.mongodb.DBObject;

/**
 * MQ收发日志DTO
 * 
 * @author hub
 * @version 1.0 2010.09.02
 */
public class MQLogDTO extends BaseDTO{
	
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
	
	/** 消息体 */
	private String data;
	
	/** 业务流水 */
	private DBObject dbObject;

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public DBObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}
}
