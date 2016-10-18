package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM02_Form  extends DataTable_BaseForm{

	private List<Map<String,Object>> overTimeList;
	private String auditedState;
	private String fromDate;
	private String toDate;
	
	public List<Map<String, Object>> getOverTimeList() {
		return overTimeList;
	}

	public void setOverTimeList(List<Map<String, Object>> overTimeList) {
		this.overTimeList = overTimeList;
	}

	public String getAuditedState() {
		return auditedState;
	}

	public void setAuditedState(String auditedState) {
		this.auditedState = auditedState;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	
}
