package com.cherry.bs.wem.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSWEM01_Form extends DataTable_BaseForm {

	private String agentApplyId; 
	
	/*单据号*/
	private String billCode;

	/*申请类型*/
	private String applyType;

	/*申请等级*/
	private String applyLevel;

	/*申请人姓名*/
	private String applyName;

	/*申请人手机号*/
	private String applyMobile;

	/*申请人微信号*/
	private String applyOpenID;

	/*申请人省份*/
	private String applyProvince;

	/*申请人城市*/
	private String applyCity;

	/*单据状态*/
	private String status;

	/*申请描述*/
	private String applyDesc;

	/*上级手机号*/
	private String superMobile;

	/*原上级手机号*/
	private String oldSuperMobile;

	/*申请时间*/
	private String applyTime;

	/*分配人*/
	private String assigner;

	/*分配时间*/
	private String assignTime;

	/*审核人手机号*/
	private String auditor;

	/*审核等级*/
	private String auditLevel;

	/*不通过理由*/
	private String reason;
	
	/*审核时间*/
	private String auditTime;

	/*有效区分*/
	private String validFlag;

	private String employeeName;
	
	private String employeeCode;
	
	private String mobilePhone;
	
	private String csrftoken;
	
	private String startDate;
	
	private String endDate;
	
	private String holidays;
	
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

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyMobile() {
		return applyMobile;
	}

	public void setApplyMobile(String applyMobile) {
		this.applyMobile = applyMobile;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyOpenID() {
		return applyOpenID;
	}

	public void setApplyOpenID(String applyOpenID) {
		this.applyOpenID = applyOpenID;
	}

	public String getApplyProvince() {
		return applyProvince;
	}

	public void setApplyProvince(String applyProvince) {
		this.applyProvince = applyProvince;
	}

	public String getApplyCity() {
		return applyCity;
	}

	public void setApplyCity(String applyCity) {
		this.applyCity = applyCity;
	}

	public String getApplyDesc() {
		return applyDesc;
	}

	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}

	public String getApplyLevel() {
		return applyLevel;
	}

	public void setApplyLevel(String applyLevel) {
		this.applyLevel = applyLevel;
	}

	public String getSuperMobile() {
		return superMobile;
	}

	public void setSuperMobile(String superMobile) {
		this.superMobile = superMobile;
	}

	public String getOldSuperMobile() {
		return oldSuperMobile;
	}

	public void setOldSuperMobile(String oldSuperMobile) {
		this.oldSuperMobile = oldSuperMobile;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditLevel() {
		return auditLevel;
	}

	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getAgentApplyId() {
		return agentApplyId;
	}

	public void setAgentApplyId(String agentApplyId) {
		this.agentApplyId = agentApplyId;
	}
	
}
