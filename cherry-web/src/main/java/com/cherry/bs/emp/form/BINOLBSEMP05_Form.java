/*	
 * @(#)BINOLBSEMP05_Form.java     1.0 2011.05.17	
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

package com.cherry.bs.emp.form;

/**
 * 
 * 停用启用员工Form
 * 
 * @author WangCT
 * @version 1.0 2011.05.17
 */
public class BINOLBSEMP05_Form {
	
	/** 员工ID */
	private String[] employeeId;
	
	/** 用户ID */
	private String[] userId;
	
	/** 登录帐号 */
	private String[] longinName;
	
	/** 有效区分 */
	private String validFlag;

	public String[] getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String[] employeeId) {
		this.employeeId = employeeId;
	}

	public String[] getUserId() {
		return userId;
	}

	public void setUserId(String[] userId) {
		this.userId = userId;
	}

	public String[] getLonginName() {
		return longinName;
	}

	public void setLonginName(String[] longinName) {
		this.longinName = longinName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
