package com.cherry.wp.wr.mrp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员信息查询Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP01_Form extends DataTable_BaseForm {
	
	/** 会员ID **/
	private String memberInfoId;
	
	/** 会员卡号 **/
	private String memCode;
	
	/** 会员姓名 **/
	private String memName;
	
	/** 会员手机 **/
	private String mobilePhone;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
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

	public String getBirthDayMonth() {
		return birthDayMonth;
	}

	public void setBirthDayMonth(String birthDayMonth) {
		this.birthDayMonth = birthDayMonth;
	}

	public String getBirthDayDate() {
		return birthDayDate;
	}

	public void setBirthDayDate(String birthDayDate) {
		this.birthDayDate = birthDayDate;
	}

}
