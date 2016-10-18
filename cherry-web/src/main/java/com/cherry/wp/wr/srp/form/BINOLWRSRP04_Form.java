package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 商品销售排行Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/29
 */
public class BINOLWRSRP04_Form extends DataTable_BaseForm {
	
	/** 销售开始时间 **/
	private String saleDateStart;
	
	/** 销售结束时间 **/
	private String saleDateEnd;
	
	/** 营业员 **/
	private String employeeId;
	
	/** 交易类型 */
	private String saleType;
	
	/** 支付方式Code */
	private String payTypeCode;
	
	/** 大分类ID */
	private String bigClassId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 大分类名称 **/
	private String bigClassName;
	
	/** 字符编码 **/
	private String charset;

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

	public String getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(String bigClassId) {
		this.bigClassId = bigClassId;
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

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
