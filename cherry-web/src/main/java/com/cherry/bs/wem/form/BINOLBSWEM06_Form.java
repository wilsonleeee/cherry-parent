package com.cherry.bs.wem.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSWEM06_Form extends DataTable_BaseForm{
	private String saleRecordCodeList;
	private String employeeCode;
	private String channel;
	private String saleRecordCode;
	private String billCodePre;
	private String employeeName;
	private String startDate;
	private String endDate;
	private String saleType;
	private List<Map<String, Object>> salList;
	private String rebateFlag;
	private String billCode;

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getRebateFlag() {
		return rebateFlag;
	}

	public void setRebateFlag(String rebateFlag) {
		this.rebateFlag = rebateFlag;
	}

	public String getSaleRecordCodeList() {
		return saleRecordCodeList;
	}

	public void setSaleRecordCodeList(String saleRecordCodeList) {
		this.saleRecordCodeList = saleRecordCodeList;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getSaleRecordCode() {
		return saleRecordCode;
	}

	public void setSaleRecordCode(String saleRecordCode) {
		this.saleRecordCode = saleRecordCode;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBillCodePre() {
		return billCodePre;
	}

	public void setBillCodePre(String billCodePre) {
		this.billCodePre = billCodePre;
	}

	public List<Map<String, Object>> getSalList() {
		return salList;
	}

	public void setSalList(List<Map<String, Object>> salList) {
		this.salList = salList;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
}
