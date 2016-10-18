/*
 * @(#)BINOLJNMAN01_Form.java     1.0 2011/4/18
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
 * 入会条件查看Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN01_Form extends BINOLJNCOM01_Form{
	
	/** TAB区分 */
	private String tabKbn;
	
	/** 组ID */
	private String groupId;
	
	private String campaignRuleId;
	
	private String memberLevelId;
	
	private String campaignType;
	// 会员活动id
	private String campaignId;

	public String getTabKbn() {
		return tabKbn;
	}

	public void setTabKbn(String tabKbn) {
		this.tabKbn = tabKbn;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

}
