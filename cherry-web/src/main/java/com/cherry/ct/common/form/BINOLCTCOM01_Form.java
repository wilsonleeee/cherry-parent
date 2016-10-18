/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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
package com.cherry.ct.common.form;

/**
 * 新建沟通计划Form
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public class BINOLCTCOM01_Form {
	/** 用户ID */
	private String userId;
	/** 品牌信息ID */
	private String brandInfoId;
	/** 品牌名称 */
	private String brandName;
	/** 活动编号 */
	private String campaignCode;
	/** 活动名称 */
	private String campaignName;
	/** 活动开始时间*/
	private String beginDate;
	/** 活动截止时间 */
	private String endDate;	
	/** 活动ID */
	private String campaignID;
	/** 沟通计划编号 */
	private String planCode;
	/** 沟通计划名称 */
	private String planName;
	/** 渠道ID */
	private String channelId;
	/** 柜台号 */
	private String counterCode;
	/** 渠道名称 */
	private String channelName;
	/** 柜台名称 */
	private String counterName;
	/** 沟通计划备注 */
	private String memo;
	/** 是否使用关联活动的对象条件 */
	private String conditionUseFlag;
	/** 沟通设置信息 */
	private String commResultInfo;
	/** 编辑模式（1:复制）*/
	private String editType;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getConditionUseFlag() {
		return conditionUseFlag;
	}

	public void setConditionUseFlag(String conditionUseFlag) {
		this.conditionUseFlag = conditionUseFlag;
	}

	public String getCommResultInfo() {
		return commResultInfo;
	}

	public void setCommResultInfo(String commResultInfo) {
		this.commResultInfo = commResultInfo;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}
	
}
