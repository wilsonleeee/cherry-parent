/*
 * @(#)BINOLPLSCF02_Form.java     1.0 2010/10/27
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

package com.cherry.pl.scf.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 审核审批配置一览Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF02_Form extends DataTable_BaseForm {
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 业务类型代码 */
	private String bussinessTypeCode;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBussinessTypeCode() {
		return bussinessTypeCode;
	}

	public void setBussinessTypeCode(String bussinessTypeCode) {
		this.bussinessTypeCode = bussinessTypeCode;
	}

}
