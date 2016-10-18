package com.cherry.wp.wr.mrp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员生日情况统计Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP03_Form extends DataTable_BaseForm {
	
	/** 会员生日模式 */
	private String birthDayMode;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;
	
	/** 生日范围（月上限） */
	private String birthDayMonthRangeStart;
	
	/** 生日范围（月下限） */
	private String birthDayMonthRangeEnd;
	
	/** 生日范围（日上限） */
	private String birthDayDateRangeStart;
	
	/** 生日范围（日下限） */
	private String birthDayDateRangeEnd;
	
	/** 会员等级 **/
	private String memberLevel;
	
	/** 营业员 **/
	private String employeeId;
	
	/** 会员等级名称 **/
	private String levelName;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 字符编码 **/
	private String charset;

	public String getBirthDayMode() {
		return birthDayMode;
	}

	public void setBirthDayMode(String birthDayMode) {
		this.birthDayMode = birthDayMode;
	}

	public String getBirthDayMonth() {
		return birthDayMonth;
	}

	public void setBirthDayMonth(String birthDayMonth) {
		this.birthDayMonth = birthDayMonth;
	}

	public String getBirthDayDate() {
		return birthDayDate;
	}

	public void setBirthDayDate(String birthDayDate) {
		this.birthDayDate = birthDayDate;
	}

	public String getBirthDayMonthRangeStart() {
		return birthDayMonthRangeStart;
	}

	public void setBirthDayMonthRangeStart(String birthDayMonthRangeStart) {
		this.birthDayMonthRangeStart = birthDayMonthRangeStart;
	}

	public String getBirthDayMonthRangeEnd() {
		return birthDayMonthRangeEnd;
	}

	public void setBirthDayMonthRangeEnd(String birthDayMonthRangeEnd) {
		this.birthDayMonthRangeEnd = birthDayMonthRangeEnd;
	}

	public String getBirthDayDateRangeStart() {
		return birthDayDateRangeStart;
	}

	public void setBirthDayDateRangeStart(String birthDayDateRangeStart) {
		this.birthDayDateRangeStart = birthDayDateRangeStart;
	}

	public String getBirthDayDateRangeEnd() {
		return birthDayDateRangeEnd;
	}

	public void setBirthDayDateRangeEnd(String birthDayDateRangeEnd) {
		this.birthDayDateRangeEnd = birthDayDateRangeEnd;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
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

}
