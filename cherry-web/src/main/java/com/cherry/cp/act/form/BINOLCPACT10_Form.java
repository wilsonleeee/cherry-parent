/*	
 * @(#)BINOLCPACT10_BL.java     1.0 @2013-08-15		
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
 * 兑换结果一览FORM
 * 
 * @author LuoHong
 * 
 */
public class BINOLCPACT10_Form extends DataTable_BaseForm{
	
	/** 活动Id*/
	private String campaignId;
	
	/** 兑换单据*/
	private String billCode;
	
	/** 会员卡号*/
	private String memberCode;
	
	/** 兑换柜台*/
	private String departCode;
	
	/** 顾客类型*/
	private String testType;
	
	/** 开始日期*/
	private String startDate;
	
	/** 开始日期*/
	private String endDate;
	
	/** 会员手机*/
	private String mobilePhone;
	
	/** 明细Id*/
	private String saleRecordId;
	
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getDepartCode() {
		return departCode;
	}
	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}
	
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
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
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getSaleRecordId() {
		return saleRecordId;
	}
	public void setSaleRecordId(String saleRecordId) {
		this.saleRecordId = saleRecordId;
	}
	
}
