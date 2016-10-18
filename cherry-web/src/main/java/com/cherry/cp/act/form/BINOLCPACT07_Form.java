/*	
 * @(#)BINOLCPACT07_Form.java     1.0 @2013-07-15		
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
 * 礼品领用报表Form
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT07_Form extends DataTable_BaseForm {

	/** 开始时间 */
	private String startDate;

	/** 结束时间 */
	private String endDate;

	/** 品牌 */
	private String brandInfoId;

	/** 单据号 */
	private String billNoIF;

	/** 会员手机号 */
	private String mobilePhone;

	/** 会员卡号 */
	private String memberCode;
	
	/**测试区分*/
	private String  testType;

	/** 领用柜台号 */
	private String counterCode;

	/** coupon码 */
	private String couponCode;

	/** 主题活动代号 */
	private String activityCode;

	/** 主题活动名称 */
	private String activityName;

	/** 活动下拉框显示条数 */
	private int number;
	
	/** 主题活动查询条件显示项*/
	private String selected;
	
	/** 礼品领用id */
	private String giftDrawId;

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
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

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBillNoIF() {
		return billNoIF;
	}

	public void setBillNoIF(String billNoIF) {
		this.billNoIF = billNoIF;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getGiftDrawId() {
		return giftDrawId;
	}

	public void setGiftDrawId(String giftDrawId) {
		this.giftDrawId = giftDrawId;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

}
