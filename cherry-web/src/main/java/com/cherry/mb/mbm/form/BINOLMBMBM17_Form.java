/*
 * @(#)BINOLMBMBM16_Form.java     1.0 2013/05/15
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
 * 会员启用停用处理Form
 * 
 * @author WangCT
 * @version 1.0 2013/05/15
 */
public class BINOLMBMBM17_Form {
	
	/** 会员ID */
	private String[] memberInfoId;
	
	/** 会员卡号 */
	private String[] memCode;
	
	/** 会员当前有效区分 */
	private String[] memCurValidFlag;
	
	/** 会员信息版本号 */
	private String[] versionDb;
	
	/** 停用启用标志 */
	private String validFlag;

	public String[] getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String[] memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String[] getMemCode() {
		return memCode;
	}

	public void setMemCode(String[] memCode) {
		this.memCode = memCode;
	}

	public String[] getMemCurValidFlag() {
		return memCurValidFlag;
	}

	public void setMemCurValidFlag(String[] memCurValidFlag) {
		this.memCurValidFlag = memCurValidFlag;
	}

	public String[] getVersionDb() {
		return versionDb;
	}

	public void setVersionDb(String[] versionDb) {
		this.versionDb = versionDb;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
