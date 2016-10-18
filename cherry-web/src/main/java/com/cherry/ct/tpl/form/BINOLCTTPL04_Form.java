/*
 * @(#)BINOLCTTPL04_Form.java     1.0 2013/11/19
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
package com.cherry.ct.tpl.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 沟通模板管理Form
 * 
 * @author ZhangLe
 * @version 1.0 2013.11.19
 */
public class BINOLCTTPL04_Form extends DataTable_BaseForm{

	/**品牌ID*/
	private String brandInfoId;
	/**品牌名称*/
	private String brandName;
	/**自增长ID*/
	private String charId;
	/**字符值（添加与编辑时用）*/
	private String charValue;
	/**沟通类型*/
	private String  commType;
	/**有效区分*/
	private String validFlag;
	/**备注*/
	private String remark;
	/**非法字符值（查询用）*/
	private String sCharValue;
	

	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getCharValue() {
		return charValue;
	}
	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}
	public String getCommType() {
		return commType;
	}
	public void setCommType(String commType) {
		this.commType = commType;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCharId() {
		return charId;
	}
	public void setCharId(String charId) {
		this.charId = charId;
	}
	public String getsCharValue() {
		return sCharValue;
	}
	public void setsCharValue(String sCharValue) {
		this.sCharValue = sCharValue;
	}
	
}
