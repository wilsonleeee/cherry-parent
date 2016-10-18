package com.cherry.bs.wem.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * @ClassName: BINOLBSWEM07_Form 
 * @Description: TODO(银行汇款报表Form) 
 * @author menghao
 * @version v1.0.0 2015-12-7 
 *
 */
public class BINOLBSWEM07_Form extends DataTable_BaseForm{
	
	/** 单据号 */
	private String billCode;
	/** 开始日期*/
	private String startDate;
	/** 结束日期*/
	private String endDate;
	/** 会员卡号*/
	private String memCode;
	/** 销售人员Code **/
	private String employeeCode;
	/** 收益人code */
	private String commissionEmployeeCode;
	/**部门级别*/
	private String commissionEmployeeLevel;
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


	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCommissionEmployeeLevel() {
		return commissionEmployeeLevel;
	}

	public void setCommissionEmployeeLevel(String commissionEmployeeLevel) {
		this.commissionEmployeeLevel = commissionEmployeeLevel;
	}

	public String getCommissionEmployeeCode() {
		return commissionEmployeeCode;
	}

	public void setCommissionEmployeeCode(String commissionEmployeeCode) {
		this.commissionEmployeeCode = commissionEmployeeCode;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
}
