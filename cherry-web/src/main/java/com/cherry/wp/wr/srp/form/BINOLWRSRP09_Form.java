package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 支付构成报表Form
 * 
 * @author fengxuebo
 * @version 1.0 2016/10/31
 */
public class BINOLWRSRP09_Form extends DataTable_BaseForm {

	/** 销售开始时间 **/
	private String saleDateStart;

	/** 销售结束时间 **/
	private String saleDateEnd;

	/** 营业员 **/
	private String employeeId;

	/** 柜台 **/
	private String organizationId;

	/** 字符编码 **/
	private String charset;

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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
