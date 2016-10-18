package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM06_Form extends DataTable_BaseForm{
	private String employeeID;
	private String employeeName;
	private String attendanceType;
	private String attendanceDateTime;
	private String startDateTime;
	private String endDateTime;
	private String departName;
	private String stime;
	private String xtime;
	private String time1;
	private String time2;
	private String time3;
	private String time4;
	private List<Map<String, Object>> BAAttendanceList;
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
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
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public String getTime3() {
		return time3;
	}
	public void setTime3(String time3) {
		this.time3 = time3;
	}
	public String getTime4() {
		return time4;
	}
	public void setTime4(String time4) {
		this.time4 = time4;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	
}
