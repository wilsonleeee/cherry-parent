/*
 * @(#)BINOLBSDEP09_Form.java     1.0 2011.2.10
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
 * 组织删除画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP09_Form {
	
	/** 组织ID */
	private String[] organizationInfoId;
	
	/** 有效区分 */
	private String validFlag;

	public String[] getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String[] organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
