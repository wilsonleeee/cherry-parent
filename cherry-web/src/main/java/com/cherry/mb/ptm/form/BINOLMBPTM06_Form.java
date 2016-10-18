/*
 * @(#)BINOLMBPTM06_Form.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 查询积分信息Form
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM06_Form extends DataTable_BaseForm {
	
	/** 扫描开始时间 */
	private String startDate;
	
	/** 扫描结束时间 */
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
