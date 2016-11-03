package com.webconsole.form;

import java.util.List;

import com.cherry.cm.form.DataTable_BaseForm;

public class ViewWorkFlow_Form extends DataTable_BaseForm{
	private List currentSteps;
	
	private String brandInfo;

	public List getCurrentSteps() {
		return currentSteps;
	}

	public void setCurrentSteps(List currentSteps) {
		this.currentSteps = currentSteps;
	}

	public String getBrandInfo() {
		return brandInfo;
	}

	public void setBrandInfo(String brandInfo) {
		this.brandInfo = brandInfo;
	}
}
