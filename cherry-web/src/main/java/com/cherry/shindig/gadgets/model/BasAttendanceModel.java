package com.cherry.shindig.gadgets.model;

public class BasAttendanceModel {
	
	/** 岗位名称 **/
	private String categoryName;
	
	/** 员工名称 **/
	private String employeeName;
	
	/** 员工代码 **/
	private String employeeCode;
	
	/** 员工ID **/
	private String employeeId;
	
	/** 发生时间 **/
	private String occurTime;
	
	/** 时间内容 **/
	private String content;
	
	/** 部门名称 **/
	private String departName;
	
	/** 部门ID **/
	private String organizationId;
	
	/** 部门代码 **/
	private String departCode;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

}
