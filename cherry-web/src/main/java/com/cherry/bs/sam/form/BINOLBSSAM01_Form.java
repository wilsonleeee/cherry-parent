package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM01_Form extends DataTable_BaseForm{

	private List<Map<String,Object>> scheduleList;
	private String fromDate;
	private String toDate;
	private String type;

	public List<Map<String, Object>> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<Map<String, Object>> scheduleList) {
		this.scheduleList = scheduleList;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
