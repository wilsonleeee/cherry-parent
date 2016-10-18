/*
 * @(#)BINOLBSDEP07_Form.java     1.0 2011.2.10
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

package com.cherry.bs.dep.form;

/**
 * 组织添加画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP07_Form {
	
	/** 组织Code */
	private String orgCode;
	
	/** 组织名称中文 */
	private String orgNameChinese;
	
	/** 组织名称中文简称 */
	private String orgNameShort;
	
	/** 组织名称外文 */
	private String orgNameForeign;
	
	/** 组织名称外文简称 */
	private String orgNameForeignShort;
	
	/** 成立日期 */
	private String foundationDate;
	
	/** 登录帐号 */
	private String longinName;
	
	/** 密码 */
	private String passWord;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgNameChinese() {
		return orgNameChinese;
	}

	public void setOrgNameChinese(String orgNameChinese) {
		this.orgNameChinese = orgNameChinese;
	}

	public String getOrgNameShort() {
		return orgNameShort;
	}

	public void setOrgNameShort(String orgNameShort) {
		this.orgNameShort = orgNameShort;
	}

	public String getOrgNameForeign() {
		return orgNameForeign;
	}

	public void setOrgNameForeign(String orgNameForeign) {
		this.orgNameForeign = orgNameForeign;
	}

	public String getOrgNameForeignShort() {
		return orgNameForeignShort;
	}

	public void setOrgNameForeignShort(String orgNameForeignShort) {
		this.orgNameForeignShort = orgNameForeignShort;
	}

	public String getFoundationDate() {
		return foundationDate;
	}

	public void setFoundationDate(String foundationDate) {
		this.foundationDate = foundationDate;
	}

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
