/*
 * @(#)BINOLPLUPM01_Form.java   1.0 2010/12/24
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
public class BINOLPLUPM01_Form extends DataTable_BaseForm {
	
    private String loginName; //用户名
    
    private String employeeCode; //员工代号
    
	private String brandInfoId; //品牌信息ID 
	
	private String employeeName; //员工姓名
	
	private String userId;   //用户ID
	
	private String modifyTime;	//更新时间

    private String modifyCount;  //更新次数
    
    private String validFlag; //有效区分
    
    private String optFlag; // 促销品操作区分1：开启 0：停用 

    //用户名
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

	//员工代号
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	//品牌ID
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	//员工姓名
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	//用户ID
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	//更新时间
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	//更新次数
	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	 //有效区分
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getOptFlag() {
		return optFlag;
	}


}
