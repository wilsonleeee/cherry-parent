package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO11_Form extends DataTable_BaseForm {
	
	//考核问卷ID
	private String paperId;
	//考核问卷问题List转换成的字符串
	private String questionStr;
	
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getQuestionStr() {
		return questionStr;
	}
	public void setQuestionStr(String questionStr) {
		this.questionStr = questionStr;
	}
	
}
