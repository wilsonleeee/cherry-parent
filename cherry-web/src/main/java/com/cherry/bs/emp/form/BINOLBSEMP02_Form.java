/*		
 * @(#)BINOLBSEMP02_Form.java     1.0 2010/12/07		
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
package com.cherry.bs.emp.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 员工信息详细form
 * 
 * @author lipc
 * @version 1.0 2010.12.07
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP02_Form extends DataTable_BaseForm{

	/** 雇员Id */
	private String employeeId;

	/** 员工信息 */
	private Map employeeInfo;
	
	/** 员工地址List */
	private List addressList;
	
	/** 员工入离职List */
	private List quitList;
	
	/** 员工岗位List */
	private List postList;
	
	/** 直属上级List */
	private List supervisorList;
	
	/** 直属下级List */
	private List juniorList;
	
	/** 用户管辖部门List */
	private List employeeDepartList;
	
	/** 关注用户List */
	private List likeEmployeeList;
	
	/** 列表和树模式迁移判断flg */
	private String modeFlg;
	
    /** BAS考勤List */
    private List attendanceInfoList;
    
    /** 雇员代号 */
	private String employeeCode;
	
	/** 权限类型 */
	private String privilegeType;
	
	/** 操作类型 */
	private String operationType;
	
	/** 业务类型 */
	private String businessType;
	
	/** 部门代码 */
	private String departCode;
	
	/** 部门名称 */
	private String departName;
	
	/** 部门权限List */
    private List departPrivilegeList;
    
    /** 人员权限List */
    private List employeePrivilegeList;
    
    /** 被关注用户List */
	private List beLikedEmployeeList;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Map getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(Map employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	public List getAddressList() {
		return addressList;
	}

	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}

	public List getQuitList() {
		return quitList;
	}

	public void setQuitList(List quitList) {
		this.quitList = quitList;
	}

	public List getPostList() {
		return postList;
	}

	public void setPostList(List postList) {
		this.postList = postList;
	}

	public List getSupervisorList() {
		return supervisorList;
	}

	public void setSupervisorList(List supervisorList) {
		this.supervisorList = supervisorList;
	}

	public List getJuniorList() {
		return juniorList;
	}

	public void setJuniorList(List juniorList) {
		this.juniorList = juniorList;
	}

	public List getEmployeeDepartList() {
		return employeeDepartList;
	}

	public void setEmployeeDepartList(List employeeDepartList) {
		this.employeeDepartList = employeeDepartList;
	}

	public String getModeFlg() {
		return modeFlg;
	}

	public void setModeFlg(String modeFlg) {
		this.modeFlg = modeFlg;
	}

	public List getLikeEmployeeList() {
		return likeEmployeeList;
	}

	public void setLikeEmployeeList(List likeEmployeeList) {
		this.likeEmployeeList = likeEmployeeList;
	}

    public void setAttendanceInfoList(List attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }

    public List getAttendanceInfoList() {
        return attendanceInfoList;
    }

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(String privilegeType) {
		this.privilegeType = privilegeType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public List getDepartPrivilegeList() {
		return departPrivilegeList;
	}

	public void setDepartPrivilegeList(List departPrivilegeList) {
		this.departPrivilegeList = departPrivilegeList;
	}

	public List getEmployeePrivilegeList() {
		return employeePrivilegeList;
	}

	public void setEmployeePrivilegeList(List employeePrivilegeList) {
		this.employeePrivilegeList = employeePrivilegeList;
	}

	public List getBeLikedEmployeeList() {
		return beLikedEmployeeList;
	}

	public void setBeLikedEmployeeList(List beLikedEmployeeList) {
		this.beLikedEmployeeList = beLikedEmployeeList;
	}

}
