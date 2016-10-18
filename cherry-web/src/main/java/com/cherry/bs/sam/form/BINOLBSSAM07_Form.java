package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM07_Form extends DataTable_BaseForm{
	private String employeeName;
	private String attendanceType;
	private String attendanceDateTime;
	private String startDateTime;
	private String endDateTime;
	private String departName;
	private List<Map<String, Object>> BAAttendanceList;
	public String getAttendanceType() {
		return attendanceType;
	}
	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}
	public List<Map<String, Object>> getBAAttendanceList() {
		return BAAttendanceList;
	}
	public void setBAAttendanceList(List<Map<String, Object>> bAAttendanceList) {
		BAAttendanceList = bAAttendanceList;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public String getAttendanceDateTime() {
		return attendanceDateTime;
	}
	public void setAttendanceDateTime(String attendanceDateTime) {
		this.attendanceDateTime = attendanceDateTime;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	
}
