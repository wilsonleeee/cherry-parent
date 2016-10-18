/*	
 * @(#)BINOLJNCOM03_Form.java     1.0 2011/4/18		
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
package com.cherry.jn.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员活动组添加 Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM03_Form extends DataTable_BaseForm {
	
	/** 会员活动组ID */
	private String campaignGrpId;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 页面ID */
	private String pageId;
	
	/** 活动组名称  */
	private String groupName;
	
	/** 活动类型  */
	private String campaignType;
	
	/** 以前的活动类型  */
	private String campaignTypeOld;
	
	/** 规则详细  */
	private String ruleDetail;
	
	/** 是否包含规则  */
	private String hasRule;
	
	/** 更新日时  */
	private String grpUpdateTime;
	
	/** 更新次数  */
	private String grpModifyCount;
	
	/** 配置操作区分 */
	private String configKbn;
	
	/** 积分规则类型*/
	private String pointRuleType;
	
	public String getCampaignGrpId() {
		return campaignGrpId;
	}

	public void setCampaignGrpId(String campaignGrpId) {
		this.campaignGrpId = campaignGrpId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
	}

	public String getHasRule() {
		return hasRule;
	}

	public void setHasRule(String hasRule) {
		this.hasRule = hasRule;
	}

	public String getCampaignTypeOld() {
		return campaignTypeOld;
	}

	public void setCampaignTypeOld(String campaignTypeOld) {
		this.campaignTypeOld = campaignTypeOld;
	}

	public String getGrpUpdateTime() {
		return grpUpdateTime;
	}

	public void setGrpUpdateTime(String grpUpdateTime) {
		this.grpUpdateTime = grpUpdateTime;
	}

	public String getGrpModifyCount() {
		return grpModifyCount;
	}

	public void setGrpModifyCount(String grpModifyCount) {
		this.grpModifyCount = grpModifyCount;
	}

	public String getConfigKbn() {
		return configKbn;
	}

	public void setConfigKbn(String configKbn) {
		this.configKbn = configKbn;
	}

	public String getPointRuleType() {
		return pointRuleType;
	}

	public void setPointRuleType(String pointRuleType) {
		this.pointRuleType = pointRuleType;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
