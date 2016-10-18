/*
 * @(#)BINOLBSDEP10_Form.java     1.0 2011.2.10
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 品牌一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP10_Form extends DataTable_BaseForm {
	
	/** 品牌代码 */
	private String brandCode;
	
	/** 品牌名称 */
	private String brandNameKw;
	
	/** 有效区分 */
	private String validFlag;

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandNameKw() {
		return brandNameKw;
	}

	public void setBrandNameKw(String brandNameKw) {
		this.brandNameKw = brandNameKw;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
