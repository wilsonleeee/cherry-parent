/*
 * @(#)BINOLJNMAN05_Form.java     1.0 2012/10/30
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
 * 积分规则配置一览Form
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN05_Form extends DataTable_BaseForm{
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
	
	/** 配置名称 */
	private String groupName;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 配置ID */
	private String campaignGrpId;
	
	
	/** 规则配置List*/
	private List<Map<String, Object>> campGrpList;

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

	public List<Map<String, Object>> getCampGrpList() {
		return campGrpList;
	}

	public void setCampGrpList(List<Map<String, Object>> campGrpList) {
		this.campGrpList = campGrpList;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getCampaignGrpId() {
		return campaignGrpId;
	}

	public void setCampaignGrpId(String campaignGrpId) {
		this.campaignGrpId = campaignGrpId;
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
