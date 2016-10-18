/*
 * @(#)BINOLPTRPS28_Form.java     1.0 2013/08/12
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 销售完成进度报表Form
 * 
 * @author WangCT
 * @version 1.0 2013/08/12
 */
public class BINOLPTRPS28_Form extends BINOLCM13_Form {
	
	/** 财务年 **/
	private String fiscalYear;
	
	/** 财务月 **/
	private String fiscalMonth;
	
	/** 统计模式 **/
	private String countModel;

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public String getFiscalMonth() {
		return fiscalMonth;
	}

	public void setFiscalMonth(String fiscalMonth) {
		this.fiscalMonth = fiscalMonth;
	}

	public String getCountModel() {
		return countModel;
	}

	public void setCountModel(String countModel) {
		this.countModel = countModel;
	}

}
