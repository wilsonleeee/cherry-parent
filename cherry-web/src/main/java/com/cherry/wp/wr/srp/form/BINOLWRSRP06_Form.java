package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 销售月报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/10/29
 */
public class BINOLWRSRP06_Form extends DataTable_BaseForm {
	
	/** 年 */
	private String year;
	
	/** 月 */
	private String month;
	
	/** 销售日期 */
	private String saleDate;
	
	/** 营业员 **/
	private String employeeId;
	
	/** 交易类型 */
	private String saleType;
	
	/** 支付方式Code */
	private String payTypeCode;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 字符编码 **/
	private String charset;
	
	/** 来源 **/
	private String channel;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getPayTypeCode() {
		return payTypeCode;
	}

	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
