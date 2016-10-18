/*		
 * @(#)BINOLBSCOM01_Form.java     1.0 2010/10/27		
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

package com.cherry.bs.common.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 基础模块POPUP画面共通Form
 * 
 * @author WangCT
 *
 */
public class BINOLBSCOM01_Form extends DataTable_BaseForm {
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 员工节点 */
	private String empPath;
	
	/** 员工ID */
	private String employeeId;
	
	/** 部门ID */
	private String departId;
	
	/** 部门节点 */
	private String departPath;
	
	/** 岗位类别ID */
	private String positionCategoryId;
	
	/** 部门类型 */
	private String type;
	
	/** 区域类型 */
	private String regionType;
	
	/** 来源 */
	private String fromPage;
	
	/** 员工List */
	private List<Map<String, Object>> employeeList;
	
	/** 部门List */
	private List<Map<String, Object>> departList;
	
	/** 区域List */
	private List<Map<String, Object>> regionList;
	
	/** 部门类型级别区分 	0：级别比参数中的部门类型级别高；1 级别比参数中的部门类型级别低*/
	private String gradeFlag;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Map<String, Object>> departList) {
		this.departList = departList;
	}

	public String getEmpPath() {
		return empPath;
	}

	public void setEmpPath(String empPath) {
		this.empPath = empPath;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getDepartPath() {
		return departPath;
	}

	public void setDepartPath(String departPath) {
		this.departPath = departPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Map<String, Object>> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<Map<String, Object>> regionList) {
		this.regionList = regionList;
	}

	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	public String getGradeFlag() {
		return gradeFlag;
	}

	public void setGradeFlag(String gradeFlag) {
		this.gradeFlag = gradeFlag;
	}
	
}
