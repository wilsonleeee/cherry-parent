package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 英雄榜Form
 * 
 * @author songka
 * @version 1.0 2015/09/07
 */
public class BINOLWRSRP08_Form extends DataTable_BaseForm {
	
	/** 销售开始时间 **/
	private String saleDateStart;
	
	/** 销售结束时间 **/
	private String saleDateEnd;
	
	/** 营业员 **/
	private String employeeId;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	public String getSaleDateStart() {
		return saleDateStart;
	}

	public void setSaleDateStart(String saleDateStart) {
		this.saleDateStart = saleDateStart;
	}

	public String getSaleDateEnd() {
		return saleDateEnd;
	}

	public void setSaleDateEnd(String saleDateEnd) {
		this.saleDateEnd = saleDateEnd;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
