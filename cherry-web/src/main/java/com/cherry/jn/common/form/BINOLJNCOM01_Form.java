/*	
 * @(#)BINOLJNCOM01_Form.java     1.0 2011/4/18		
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

/**
 * 会员入会共通 Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM01_Form {
	
	private String csrftoken;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 有效期信息  */
	private String levelDate;
	
	/** 会员活动类型  */
	private String campaignType;
	
	/** 会员活动类型名称  */
	private String campaignTypeName;

	public String getLevelDate() {
		return levelDate;
	}

	public void setLevelDate(String levelDate) {
		this.levelDate = levelDate;
	}

	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCampaignTypeName() {
		return campaignTypeName;
	}

	public void setCampaignTypeName(String campaignTypeName) {
		this.campaignTypeName = campaignTypeName;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
