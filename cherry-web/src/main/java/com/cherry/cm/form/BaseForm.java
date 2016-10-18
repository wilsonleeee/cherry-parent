/*	
 * @(#)BaseForm.java     1.0 2014/02/17		
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
package com.cherry.cm.form;

/**
 * Form基类（所有cherry中的form都需要继承于该类）
 * 
 * @author WangCT
 * @version 1.0 2014/02/17
 */
public class BaseForm {
	
	/** 品牌ID **/
	private String commonBrandId;
	
	/** 品牌代码 **/
	private String commonBrandCode;

	public String getCommonBrandId() {
		return commonBrandId;
	}

	public void setCommonBrandId(String commonBrandId) {
		this.commonBrandId = commonBrandId;
	}

	public String getCommonBrandCode() {
		return commonBrandCode;
	}

	public void setCommonBrandCode(String commonBrandCode) {
		this.commonBrandCode = commonBrandCode;
	}
}
