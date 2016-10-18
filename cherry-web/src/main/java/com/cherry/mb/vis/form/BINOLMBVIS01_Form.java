/*
 * @(#)BINOLMBVIS01_Form.java     1.0 2012/12/14
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.mb.vis.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员回访任务Form
 * 
 * @author liumh
 * @version 1.0 2012/08/08
 */
public class BINOLMBVIS01_Form extends DataTable_BaseForm {

	/** 会员回访ID */
	private int visitTaskID;
	
	/** 会员回访ID集合 */
	private String[] visitTaskIDs;

	/** 所属组织ID */
	private int organizationInfoID;

	/** 所属品牌ID */
	private int brandInfoID;

	/** 回访类型 */
	private String visitType;

	/** 回访任务名称 */
	private String visitTaskName;

	/** 回访任务名称 */
	private String visitTaskCreateTime;

	/** 开始日 */
	private String startDate;

	/** 结束日 */
	private String endDate;

	/** 柜台对应的组织ID */
	private int organizationID;

	/** 柜台号 */
	private String counterCode;

	/** BA对应的员工ID */
	private int employeeID;
	
	/** BA对应的员工ID和代号 */
	private	String employeeIDs;

	/** BA对应的员工名称 */
	private String employeeName;

	/** BA代号 */
	private String BACode;

	/** 新后台会员ID */
	private int memberID;

	/** 会员卡号 */
	private String memberCode;

	/** 会员名字 */
	private String memberName;

	/** 入会时间 */
	private String joinTime;

	/** 任务进行状态 */
	private String taskState;

	/** 回访结果 */
	private String visitResult;

	/** 回访执行时间 */
	private String visitTime;
	
	/** 回访执行时间  开始 */
	private String visitTimeStart;
	
	/** 回访执行时间  结束*/
	private String visitTimeEnd;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;
	
	/** 回访柜台 */
	private String counterCodeName;

	public String getCounterCodeName() {
		return counterCodeName;
	}

	public void setCounterCodeName(String counterCodeName) {
		this.counterCodeName = counterCodeName;
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

	public String getVisitTimeStart() {
		return visitTimeStart;
	}

	public void setVisitTimeStart(String visitTimeStart) {
		this.visitTimeStart = visitTimeStart;
	}

	public String getVisitTimeEnd() {
		return visitTimeEnd;
	}

	public void setVisitTimeEnd(String visitTimeEnd) {
		this.visitTimeEnd = visitTimeEnd;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitResult() {
		return visitResult;
	}

	public void setVisitResult(String visitResult) {
		this.visitResult = visitResult;
	}

	public int getVisitTaskID() {
		return visitTaskID;
	}

	public void setVisitTaskID(int visitTaskID) {
		this.visitTaskID = visitTaskID;
	}

	public int getOrganizationInfoID() {
		return organizationInfoID;
	}

	public void setOrganizationInfoID(int organizationInfoID) {
		this.organizationInfoID = organizationInfoID;
	}

	public int getBrandInfoID() {
		return brandInfoID;
	}

	public void setBrandInfoID(int brandInfoID) {
		this.brandInfoID = brandInfoID;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitTaskName() {
		return visitTaskName;
	}

	public void setVisitTaskName(String visitTaskName) {
		this.visitTaskName = visitTaskName;
	}

	public String getVisitTaskCreateTime() {
		return visitTaskCreateTime;
	}

	public void setVisitTaskCreateTime(String visitTaskCreateTime) {
		this.visitTaskCreateTime = visitTaskCreateTime;
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

	public int getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(int organizationID) {
		this.organizationID = organizationID;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getBACode() {
		return BACode;
	}

	public void setBACode(String bACode) {
		BACode = bACode;
	}

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String[] getVisitTaskIDs() {
		return visitTaskIDs;
	}

	public void setVisitTaskIDs(String[] visitTaskIDs) {
		this.visitTaskIDs = visitTaskIDs;
	}

	public String getEmployeeIDs() {
		return employeeIDs;
	}

	public void setEmployeeIDs(String employeeIDs) {
		this.employeeIDs = employeeIDs;
	}

}