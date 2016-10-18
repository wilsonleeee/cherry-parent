/*		
 * @(#)BINOLBSEMP01_Form.java     1.0 2010/10/12		
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 员工信息查询form
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 * 
 */
public class BINOLBSEMP01_Form extends DataTable_BaseForm{

	/** 有效区分 + 雇员ID + 更新日期 + 更新次数  */
	private String[] employeeInfo;

	/** 雇员代号 */
	private String employeeCode;
	
	/** 登录帐号 */
	private String longinName;
	
	/** 雇员名称 */
	private String employeeName;
	
	/** 性别 */
	private String gender;

	/** 有效区分 */
	private String validFlag;
	
	/** 省ID  */
	private String provinceId;
	
	/**市Id  */
	private String cityId;
	
	/** 部门ID  */
	private String organizationId;
	
	/** 岗位Id  */
	private String positionCategoryId;

	/** 员工操作区分1：开启 0：停用  */
	private String optFlag;
	
	/** 雇员节点  */
	private String path;
	
	/** 定位条件 */
	private String locationPosition;
	
	public String[] getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(String[] employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getOptFlag() {
		return optFlag;
	}

	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLocationPosition() {
		return locationPosition;
	}

	public void setLocationPosition(String locationPosition) {
		this.locationPosition = locationPosition;
	}
	
}
