package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT02_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String ccBrandInfoId;
	/** 客户ID */
	private String customerSysId;
	/** 客户类型 */
	private String customerType;

	public String getCcBrandInfoId() {
		return ccBrandInfoId;
	}

	public void setCcBrandInfoId(String ccBrandInfoId) {
		this.ccBrandInfoId = ccBrandInfoId;
	}

	public String getCustomerSysId() {
		return customerSysId;
	}

	public void setCustomerSysId(String customerSysId) {
		this.customerSysId = customerSysId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
}
