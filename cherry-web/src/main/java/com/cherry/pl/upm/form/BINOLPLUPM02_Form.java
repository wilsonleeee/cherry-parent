/*
 * @(#)BINOLPLUPM02_Form.java   1.0 2010/12/29
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
package com.cherry.pl.upm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 用户查询实体类
 * 用户信息DTO
 * 
 */
public class BINOLPLUPM02_Form extends DataTable_BaseForm {
	
	private String employeeId;   //员工信息ID
    
	private String loginName; //用户名
    
    private String passWord; //密码
    
    private String brandInfoId; //品牌
    
    private String confirmPW; //确认密码

   //员工信息ID
    public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	//用户名
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

	//密码
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPassWord() {
		return passWord;
	}

	//品牌
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	//确认密码
	public void setConfirmPW(String confirmPW) {
		this.confirmPW = confirmPW;
	}

	public String getConfirmPW() {
		return confirmPW;
	}
}
