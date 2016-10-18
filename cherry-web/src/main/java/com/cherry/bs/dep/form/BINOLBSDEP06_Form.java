/*
 * @(#)BINOLBSDEP06_Form.java     1.0 2011.2.10
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
 * 组织一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP06_Form extends DataTable_BaseForm {
	
	/** 组织名称 */
	private String orgNameKw;
	
	/** 有效区分 */
	private String validFlag;

	public String getOrgNameKw() {
		return orgNameKw;
	}

	public void setOrgNameKw(String orgNameKw) {
		this.orgNameKw = orgNameKw;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
