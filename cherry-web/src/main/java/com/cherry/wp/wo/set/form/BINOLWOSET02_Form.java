package com.cherry.wp.wo.set.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 考勤管理Form
 * 
 * @author WangCT
 * @version 1.0 2014/10/22
 */
public class BINOLWOSET02_Form extends DataTable_BaseForm {
	
	/** 员工ID **/
	private String employeeId;
	
	/** 开始时间 **/
	private String startDate;
	
	/** 结束时间 **/
	private String endDate;
	
	/** 员工ID（查询用） **/
	private String employeeIdQ;
	
	/** 考勤类型（1：上班 0：下班） **/
	private String attendanceType;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getEmployeeIdQ() {
		return employeeIdQ;
	}

	public void setEmployeeIdQ(String employeeIdQ) {
		this.employeeIdQ = employeeIdQ;
	}

	public String getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}

}
