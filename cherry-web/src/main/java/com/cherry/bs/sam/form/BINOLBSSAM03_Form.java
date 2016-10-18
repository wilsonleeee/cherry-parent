package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM03_Form   extends DataTable_BaseForm{

	private String employeeName;
	private String wagesYear;
	private String wagesMonth;
	private List<Map<String,Object>> payrollList;
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getWagesYear() {
		return wagesYear;
	}
	public void setWagesYear(String wagesYear) {
		this.wagesYear = wagesYear;
	}
	public String getWagesMonth() {
		return wagesMonth;
	}
	public void setWagesMonth(String wagesMonth) {
		this.wagesMonth = wagesMonth;
	}
	public List<Map<String, Object>> getPayrollList() {
		return payrollList;
	}
	public void setPayrollList(List<Map<String, Object>> payrollList) {
		this.payrollList = payrollList;
	}
	
	
}
