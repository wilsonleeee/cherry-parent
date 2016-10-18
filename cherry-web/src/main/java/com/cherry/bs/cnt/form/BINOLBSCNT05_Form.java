/*	
 * @(#)BINOLBSCNT05_Form.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.form;

/**
 * 
 * 	停用启用柜台处理Form
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT05_Form {
	
	/** 柜台ID */
	private String[] counterInfoId;
	
	/** 部门ID */
	private String[] organizationId;
	
	/** 柜台编号 */
	private String[] counterCode;
	
	/** 有效区分 */
	private String validFlag;

	public String[] getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(String[] counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String[] getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String[] organizationId) {
		this.organizationId = organizationId;
	}
	
	public String[] getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String[] counterCode) {
		this.counterCode = counterCode;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
