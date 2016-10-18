package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 销售分类统计Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/19
 */
public class BINOLWRSRP03_Form extends DataTable_BaseForm {
	
	/** 开始日期 */
	private String startDate;
	
	/** 结束日期 */
	private String endDate;
	
	/** 员工ID **/
	private String employeeId;
	
	/** 交易类型 */
	private String saleType;
	
	/** 大分类ID */
	private String bigClassId;
	
	/** 小分类ID */
	private String smallClassId;
	
	/** 字符编码 **/
	private String charset;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 大分类名称 **/
	private String bigClassName;
	
	/** 小分类名称 **/
	private String smallClassName;

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

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(String bigClassId) {
		this.bigClassId = bigClassId;
	}

	public String getSmallClassId() {
		return smallClassId;
	}

	public void setSmallClassId(String smallClassId) {
		this.smallClassId = smallClassId;
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

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}

	public String getSmallClassName() {
		return smallClassName;
	}

	public void setSmallClassName(String smallClassName) {
		this.smallClassName = smallClassName;
	}

}
