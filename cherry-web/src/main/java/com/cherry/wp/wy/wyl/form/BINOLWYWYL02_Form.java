package com.cherry.wp.wy.wyl.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWYWYL02_Form extends DataTable_BaseForm{
	// 申领代码
	private String applyCoupon;
	
	// 单据号
	private String billCode;
	
	// 活动子类型
	private String subType;
	
	// 会员ID
	private String memberInfoId;
	
	// 微信号
	private String messageId;
	
	// 会员姓名
	private String memberName;
	
	// 手机号码
	private String mobile;
	
	// 会员生日月
	private String birthDayMonthQ;
	
	// 会员生日日期
	private String birthDayDateQ;
	
	// 会员性别
	private String gender;
	
	// 原会员姓名
	private String orgMemberName;
	
	// 原会员生日
	private String orgBirthDay;
	
	// 原会员性别
	private String orgGender;

	public String getApplyCoupon() {
		return applyCoupon;
	}

	public void setApplyCoupon(String applyCoupon) {
		this.applyCoupon = applyCoupon;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBirthDayMonthQ() {
		return birthDayMonthQ;
	}

	public void setBirthDayMonthQ(String birthDayMonthQ) {
		this.birthDayMonthQ = birthDayMonthQ;
	}

	public String getBirthDayDateQ() {
		return birthDayDateQ;
	}

	public void setBirthDayDateQ(String birthDayDateQ) {
		this.birthDayDateQ = birthDayDateQ;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOrgMemberName() {
		return orgMemberName;
	}

	public void setOrgMemberName(String orgMemberName) {
		this.orgMemberName = orgMemberName;
	}

	public String getOrgBirthDay() {
		return orgBirthDay;
	}

	public void setOrgBirthDay(String orgBirthDay) {
		this.orgBirthDay = orgBirthDay;
	}

	public String getOrgGender() {
		return orgGender;
	}

	public void setOrgGender(String orgGender) {
		this.orgGender = orgGender;
	}
	
}
