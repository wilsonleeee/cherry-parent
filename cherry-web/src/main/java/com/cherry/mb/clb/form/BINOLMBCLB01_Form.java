/*	
 * @(#)BINOLMBCLB01_Form.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员俱乐部一览Form
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB01_Form extends DataTable_BaseForm{
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 停用理由 */
	private String reason;
	
	/** 俱乐部ID */
	private String memberClubId;
	
	/** 柜台ID */
	private String prmCounterId;
	
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;
	
	/** 俱乐部list */
	private List<Map<String, Object>> clubList;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPrmCounterId() {
		return prmCounterId;
	}

	public void setPrmCounterId(String prmCounterId) {
		this.prmCounterId = prmCounterId;
	}
}
