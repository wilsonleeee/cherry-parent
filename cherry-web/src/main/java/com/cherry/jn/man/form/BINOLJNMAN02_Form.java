/*
 * @(#)BINOLJNMAN02_Form.java     1.0 2011/4/18
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
package com.cherry.jn.man.form;

import com.cherry.jn.common.form.BINOLJNCOM01_Form;

/**
 * 设定规则条件Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN02_Form extends BINOLJNCOM01_Form{
	
	/** 活动信息 */
	private String camTemps;
	
	/** 模板关系 */
	private String relationInfo;
	
	/** 会员活动ID */
	private String campaignId;
	
	/** 会员子活动ID */
	private String campaignRuleId;
	
	/** 会员等级ID */
	private String memberLevelId;
	
	/** 会员等级级别 */
	private String memberLevelGrade;
	
	/** 活动有效期开始日期 */
	private String campaignFromDate;
	
	/** 活动有效期结束日期 */
	private String campaignToDate;
	
	/** 组ID */
	private String groupId;
	
	public String getCamTemps() {
		return camTemps;
	}

	public void setCamTemps(String camTemps) {
		this.camTemps = camTemps;
	}
	
	public String getRelationInfo() {
		return relationInfo;
	}

	public void setRelationInfo(String relationInfo) {
		this.relationInfo = relationInfo;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignRuleId() {
		return campaignRuleId;
	}

	public void setCampaignRuleId(String campaignRuleId) {
		this.campaignRuleId = campaignRuleId;
	}

	public String getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(String memberLevelId) {
		this.memberLevelId = memberLevelId;
	}
	
	public String getMemberLevelGrade() {
		return memberLevelGrade;
	}

	public void setMemberLevelGrade(String memberLevelGrade) {
		this.memberLevelGrade = memberLevelGrade;
	}

	public String getCampaignFromDate() {
		return campaignFromDate;
	}

	public void setCampaignFromDate(String campaignFromDate) {
		this.campaignFromDate = campaignFromDate;
	}

	public String getCampaignToDate() {
		return campaignToDate;
	}

	public void setCampaignToDate(String campaignToDate) {
		this.campaignToDate = campaignToDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
