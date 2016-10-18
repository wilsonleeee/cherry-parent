package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT04_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String ccBrandInfoId;
	
	/** 手机号码 */
	private String ccMobilePhone;
	
	/** 固定电话号码 */
	private String ccTelephone;
	
	/** 会员卡号 */
	private String ccMemCode;
	
	/** 客户姓名 */
	private String ccMemName;
	
	/** 客户生日 */
	private String ccBirth;
	
	/** 呼叫标识号 */
	private String callId;
	
	/** 客户系统Id */
	private String customerSysId;
	
	/** 是否会员标识 */
	private int isMember;
	
	public String getCcBrandInfoId() {
		return ccBrandInfoId;
	}

	public void setCcBrandInfoId(String ccBrandInfoId) {
		this.ccBrandInfoId = ccBrandInfoId;
	}

	public String getCcMobilePhone() {
		return ccMobilePhone;
	}

	public void setCcMobilePhone(String ccMobilePhone) {
		this.ccMobilePhone = ccMobilePhone;
	}

	public String getCcTelephone() {
		return ccTelephone;
	}

	public void setCcTelephone(String ccTelephone) {
		this.ccTelephone = ccTelephone;
	}

	public String getCcMemCode() {
		return ccMemCode;
	}

	public void setCcMemCode(String ccMemCode) {
		this.ccMemCode = ccMemCode;
	}

	public String getCcMemName() {
		return ccMemName;
	}

	public void setCcMemName(String ccMemName) {
		this.ccMemName = ccMemName;
	}

	public String getCcBirth() {
		return ccBirth;
	}

	public void setCcBirth(String ccBirth) {
		this.ccBirth = ccBirth;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCustomerSysId() {
		return customerSysId;
	}

	public void setCustomerSysId(String customerSysId) {
		this.customerSysId = customerSysId;
	}

	public int getIsMember() {
		return isMember;
	}

	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}
	
}
