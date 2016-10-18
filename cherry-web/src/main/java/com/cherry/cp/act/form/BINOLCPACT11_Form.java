/*	
 * @(#)BINOLCPACT11_Form.java     1.0 @2014-01-13		
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
package com.cherry.cp.act.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 活动履历一览
 * 
 * @author LUOHONG
 * 
 */
public class BINOLCPACT11_Form extends DataTable_BaseForm{

	/** 活动批次号码 */
	private String batchNo;

	/** 活动单据号 */
	private String tradeNoIF;
	
	/** 会员卡号 */
	private String memCode;

	/** 会员手机 */
	private String mobilePhone;
	
	/** 单据状态 */
	private String state;

	/** 开始日期 */
	private String startDate;

	/** 结束日期 */
	private String endDate;

	/** 活动Code */
	private String campCode;
	
	/** 活动柜台 */
	private String departName;
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

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

	public String getCampCode() {
		return campCode;
	}

	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}
	
}
