/*
 * @(#)BINOLMBMBM16_BL.java     1.0 2013/05/20
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
 * 会员升降级履历
 * 
 * @author Luohong
 * @version 1.0 2013/05/20
 */
public class BINOLMBMBM16_Form {
	
	/** 会员Id*/
	private String  memberInfoId;
	
	/** 会员俱乐部ID */
	private String memberClubId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
