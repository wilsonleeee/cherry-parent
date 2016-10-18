/*
 * @(#)BINOLCTCOM06_Form.java     1.0 2013/06/09
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
package com.cherry.ct.common.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 沟通对象明细显示Form
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.09
 */
public class BINOLCTCOM06_Form extends DataTable_BaseForm{
	/** 搜索记录编号 */
	private String recordCode;
	
	private String recordName;
	
	private String recordType;
	
	private String customerType;
	
	private String conditionInfo;
	
	private String memCode;
	
	private String memName;
	
	private String mobilePhone;
	
	private String userId;
	
	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getConditionInfo() {
		return conditionInfo;
	}

	public void setConditionInfo(String conditionInfo) {
		this.conditionInfo = conditionInfo;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
