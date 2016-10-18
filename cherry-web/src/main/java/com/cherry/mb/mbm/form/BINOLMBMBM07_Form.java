/*
 * @(#)BINOLMBMBM07_Form.java     1.0 2012.12.07
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
package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员活动记录画面Form
 * 
 * @author WangCT
 * @version 1.0 2012.12.07
 */
public class BINOLMBMBM07_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 活动名称 */
	private String activityName;
	
	/** 活动状态 */
	private String state;
	
	/** 参与活动时间上限 */
	private String participateTimeStart;
	
	/** 参与活动时间下限 */
	private String participateTimeEnd;
	
	/** 活动柜台 */
	private String departName;
	
	/** 单据号 */
	private String tradeNoIF;
	
	/** 批次号 */
	private String batchNo;
	
	/** 活动ID */
	private String subCampaignId;
	
	/** 活动类型（0：促销活动，1：会员活动） */
	private String actType;
	
	/** 活动预约ID */
	private String campOrderId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParticipateTimeStart() {
		return participateTimeStart;
	}

	public void setParticipateTimeStart(String participateTimeStart) {
		this.participateTimeStart = participateTimeStart;
	}

	public String getParticipateTimeEnd() {
		return participateTimeEnd;
	}

	public void setParticipateTimeEnd(String participateTimeEnd) {
		this.participateTimeEnd = participateTimeEnd;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSubCampaignId() {
		return subCampaignId;
	}

	public void setSubCampaignId(String subCampaignId) {
		this.subCampaignId = subCampaignId;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getCampOrderId() {
		return campOrderId;
	}

	public void setCampOrderId(String campOrderId) {
		this.campOrderId = campOrderId;
	}

}
