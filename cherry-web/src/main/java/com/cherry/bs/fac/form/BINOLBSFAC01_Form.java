/*		
 * @(#)BINOLBSFAC01_Form.java     1.0 2011/02/14		
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
package com.cherry.bs.fac.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 生产厂商查询form
 * 
 * @author lipc
 * @version 1.0 2011.02.14
 * 
 */
public class BINOLBSFAC01_Form extends DataTable_BaseForm {
	
	/** 生产厂商代码 */
	private String manufacturerCode;
	
	/** 生产厂商名称 */
	private String factoryName;
	
	/** 有效区分 */
	private String validFlag;

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
