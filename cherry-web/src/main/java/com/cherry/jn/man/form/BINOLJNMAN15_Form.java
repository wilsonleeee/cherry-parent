/*	
 * @(#)BINOLJNMAN15_Form.java     1.0 2013/08/28		
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 积分清零 Form
 * 
 * @author hub
 * @version 1.0 2013.08.28
 */
public class BINOLJNMAN15_Form extends DataTable_BaseForm{
	
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 会员俱乐部名称 */
	private String memberClubName;
	
	/** 积分活动List*/
	private List<Map<String, Object>> camtempList;
	
	/** 活动类型 */
	private String campaignType;
	
	/** 规则名称 */
	private String ruleName;
	
	/** 规则ID */
	private String campaignId;
	
	/** 有效区分 */
	private String validFlag;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
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

	public List<Map<String, Object>> getCamtempList() {
		return camtempList;
	}

	public void setCamtempList(List<Map<String, Object>> camtempList) {
		this.camtempList = camtempList;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getMemberClubName() {
		return memberClubName;
	}

	public void setMemberClubName(String memberClubName) {
		this.memberClubName = memberClubName;
	}
}
