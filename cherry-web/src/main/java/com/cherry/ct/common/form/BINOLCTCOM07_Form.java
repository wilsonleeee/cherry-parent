package com.cherry.ct.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCTCOM07_Form extends DataTable_BaseForm{
	
	/** 信息接收号码 */
	private String mobilePhone;
	
	/** 客户ID */
	private String customerSysId;
	
	/** 会员卡号 */
	private String memberCode;
	
	/** 数据来源 */
	private String sourse;
	
	/** 信息内容 */
	private String msgContents;

	/** 信息签名 */
	private String signature;
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCustomerSysId() {
		return customerSysId;
	}

	public void setCustomerSysId(String customerSysId) {
		this.customerSysId = customerSysId;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getSourse() {
		return sourse;
	}

	public void setSourse(String sourse) {
		this.sourse = sourse;
	}

	public String getMsgContents() {
		return msgContents;
	}

	public void setMsgContents(String msgContents) {
		this.msgContents = msgContents;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
