package com.cherry.bs.cnt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 用于
 */
public class BINOLBSCNT10_Form extends DataTable_BaseForm {

	/**柜台编号Id**/
	private int counterInfoId;

	private String brandInfoId;

	public int getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(int counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
}
