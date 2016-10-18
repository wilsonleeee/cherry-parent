package com.cherry.wp.wr.mrp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 新会员入会统计Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP02_Form extends DataTable_BaseForm {
	
	/** 入会时间模式 **/
	private String joinDateMode;
	
	/** 入会时间范围 **/
	private String joinDateRange;
	
	/** 入会时间范围单位 **/
	private String joinDateUnit;
	
	/** 入会时间范围单位（1：一段时间内，2：满一段时间） **/
	private String joinDateUnitFlag;
	
	/** 入会时间上限 **/
	private String joinDateStart;
	
	/** 入会时间下限 **/
	private String joinDateEnd;
	
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

	public String getJoinDateMode() {
		return joinDateMode;
	}

	public void setJoinDateMode(String joinDateMode) {
		this.joinDateMode = joinDateMode;
	}

	public String getJoinDateRange() {
		return joinDateRange;
	}

	public void setJoinDateRange(String joinDateRange) {
		this.joinDateRange = joinDateRange;
	}

	public String getJoinDateUnit() {
		return joinDateUnit;
	}

	public void setJoinDateUnit(String joinDateUnit) {
		this.joinDateUnit = joinDateUnit;
	}

	public String getJoinDateUnitFlag() {
		return joinDateUnitFlag;
	}

	public void setJoinDateUnitFlag(String joinDateUnitFlag) {
		this.joinDateUnitFlag = joinDateUnitFlag;
	}

	public String getJoinDateStart() {
		return joinDateStart;
	}

	public void setJoinDateStart(String joinDateStart) {
		this.joinDateStart = joinDateStart;
	}

	public String getJoinDateEnd() {
		return joinDateEnd;
	}

	public void setJoinDateEnd(String joinDateEnd) {
		this.joinDateEnd = joinDateEnd;
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
