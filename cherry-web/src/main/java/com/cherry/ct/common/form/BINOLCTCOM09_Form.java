package com.cherry.ct.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCTCOM09_Form extends DataTable_BaseForm{
	/** Cherry登陆用户名 */
	private String userName;
	
	/** 手机号码 */
	private String mobilePhone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
}
