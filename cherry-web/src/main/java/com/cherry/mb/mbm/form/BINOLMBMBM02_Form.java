/*
 * @(#)BINOLMBMBM02_Form.java     1.0 2011.10.25
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

/**
 * 会员详细画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.10.25
 */
public class BINOLMBMBM02_Form {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 会员俱乐部ID */
	private String memberClubId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
