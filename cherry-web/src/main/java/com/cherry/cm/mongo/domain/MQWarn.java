/*  
 * @(#)MQWarn.java    1.0 2012-9-18     
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
package com.cherry.cm.mongo.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * MQ消息警告表
 * 
 * @author WangCT
 * @version 1.0 2012-9-18
 */
@Document(collection="MGO_MQWarn")
@CompoundIndexes({
	@CompoundIndex(name="PutTime", def="{'PutTime':1}"),
	@CompoundIndex(name="TradeType", def="{'TradeType':1}"),
	@CompoundIndex(name="TradeEntityCode", def="{'TradeEntityCode':1}")
})
public class MQWarn {
	
	@Id
	private ObjectId id;
	
	/** 组织代码 **/
	@Field("OrgCode")
	private String orgCode;
	
	/** 品牌代码 **/
	@Field("BrandCode")
	private String brandCode;
	
	/** 业务类型 **/
	@Field("TradeType")
	private String tradeType;
	
	/** 单据号 **/
	@Field("TradeNoIF")
	private String tradeNoIF;
	
	/** 会员卡号 **/
	@Field("TradeEntityCode")
	private String tradeEntityCode;
	
	/** 消息体 **/
	@Field("MessageBody")
	private String messageBody;
	
	/** 错误信息 **/
	@Field("ErrInfo")
	private String errInfo;
	
	/** 错误类型 **/
	@Field("ErrType")
	private String errType;
	
	/** 生成时间 **/
	@Field("PutTime")
	private String putTime;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getTradeEntityCode() {
		return tradeEntityCode;
	}

	public void setTradeEntityCode(String tradeEntityCode) {
		this.tradeEntityCode = tradeEntityCode;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}

	public String getPutTime() {
		return putTime;
	}

	public void setPutTime(String putTime) {
		this.putTime = putTime;
	}
}
