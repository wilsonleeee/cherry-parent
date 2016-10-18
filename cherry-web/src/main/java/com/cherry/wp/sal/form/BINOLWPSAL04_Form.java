package com.cherry.wp.sal.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL04_Form extends DataTable_BaseForm {
	
	private String baName;
	
	private String memberName;
	
	private String originalAmount;
	
	private String totalDiscountRate;
	
	public String getBaName() {
		return baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getTotalDiscountRate() {
		return totalDiscountRate;
	}

	public void setTotalDiscountRate(String totalDiscountRate) {
		this.totalDiscountRate = totalDiscountRate;
	}
	
}
