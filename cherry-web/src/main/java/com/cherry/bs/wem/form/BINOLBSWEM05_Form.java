package com.cherry.bs.wem.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLBSWEM05_Form extends BINOLCM13_Form{

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
	
	/** 返点分成列表 */
	private List<Map<String, Object>> bonusList;

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

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

	public String getCommissionEmployeeCode() {
		return commissionEmployeeCode;
	}

	public void setCommissionEmployeeCode(String commissionEmployeeCode) {
		this.commissionEmployeeCode = commissionEmployeeCode;
	}

	public String getCommissionEmployeeLevel() {
		return commissionEmployeeLevel;
	}

	public void setCommissionEmployeeLevel(String commissionEmployeeLevel) {
		this.commissionEmployeeLevel = commissionEmployeeLevel;
	}

	public List<Map<String, Object>> getBonusList() {
		return bonusList;
	}

	public void setBonusList(List<Map<String, Object>> bonusList) {
		this.bonusList = bonusList;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
}
