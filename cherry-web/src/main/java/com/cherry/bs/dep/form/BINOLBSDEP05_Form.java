/*
 * @(#)BINOLBSDEP05_Form.java     1.0 2010/10/27
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
 * 停用启用部门Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP05_Form {
	
	/** 部门ID */
	private String[] organizationId;
	
	/** 有效区分 */
	private String validFlag;

	public String[] getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String[] organizationId) {
		this.organizationId = organizationId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
