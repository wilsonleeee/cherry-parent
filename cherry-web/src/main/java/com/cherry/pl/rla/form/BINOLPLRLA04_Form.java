/*
 * @(#)BINOLPLRLM01_Form.java     1.0 2010/10/27
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

package com.cherry.pl.rla.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 用户角色分配Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLA04_Form extends DataTable_BaseForm {
	
	/** 用户信息List */
	private List<Map<String, Object>> userInfoList;
	
	/** 员工姓名 */
	private String employeeName;
	
	/** 用户帐号 */
	private String longinName;
	
	/** 部门ID */
	private String organizationId;
	
	/** 岗位ID */
	private String positionId;
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 部门List */
	private List<Map<String, Object>> orgList;
	
	public List<Map<String, Object>> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<Map<String, Object>> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}

}
