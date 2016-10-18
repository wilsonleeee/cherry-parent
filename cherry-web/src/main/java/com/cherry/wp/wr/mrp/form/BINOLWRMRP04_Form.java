package com.cherry.wp.wr.mrp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 礼券使用报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/10/30
 */
public class BINOLWRMRP04_Form extends DataTable_BaseForm {
	
	/** 开始日期 **/
	private String startDate;
	
	/** 结束日期 **/
	private String endDate;
	
	/** 营业员ID **/
	private String employeeId;
	
	/** 会员手机 **/
	private String mobilePhone;
	
	/** 会员卡号 **/
	private String memCode;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 字符编码 **/
	private String charset;

	public String getStartDate() {
		return startDate;
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

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
