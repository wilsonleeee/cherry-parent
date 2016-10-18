package com.cherry.wp.wr.crp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 客户预约登记查询Form
 * 
 * @author menghao
 * @version 1.0 2014/12/24
 */
public class BINOLWRCRP01_Form extends DataTable_BaseForm {
	
	private String campaignOrderDateStart;
	
	private String campaignOrderDateEnd;
	
	private String customerName;
	
	private String subType;
	
	private String state;
	
	private String bookDateStart;
	
	private String bookDateEnd;
	
	private String finishTimeStart;
	
	private String finishTimeEnd;
	
	private String mobilePhone;
	
	private String employeeName;
	
	private String employeeId;

	public String getCampaignOrderDateStart() {
		return campaignOrderDateStart;
	}

	public void setCampaignOrderDateStart(String campaignOrderDateStart) {
		this.campaignOrderDateStart = campaignOrderDateStart;
	}

	public String getCampaignOrderDateEnd() {
		return campaignOrderDateEnd;
	}

	public void setCampaignOrderDateEnd(String campaignOrderDateEnd) {
		this.campaignOrderDateEnd = campaignOrderDateEnd;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBookDateStart() {
		return bookDateStart;
	}

	public void setBookDateStart(String bookDateStart) {
		this.bookDateStart = bookDateStart;
	}

	public String getBookDateEnd() {
		return bookDateEnd;
	}

	public void setBookDateEnd(String bookDateEnd) {
		this.bookDateEnd = bookDateEnd;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFinishTimeStart() {
		return finishTimeStart;
	}

	public void setFinishTimeStart(String finishTimeStart) {
		this.finishTimeStart = finishTimeStart;
	}

	public String getFinishTimeEnd() {
		return finishTimeEnd;
	}

	public void setFinishTimeEnd(String finishTimeEnd) {
		this.finishTimeEnd = finishTimeEnd;
	}
	
}
