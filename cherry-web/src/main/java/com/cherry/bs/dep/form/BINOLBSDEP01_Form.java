/*
 * @(#)BINOLBSDEP01_Form.java     1.0 2010/10/27
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

package com.cherry.bs.dep.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 部门一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP01_Form extends DataTable_BaseForm {
	
	/** 部门信息List */
	private List<Map<String, Object>> organizationList;
	
	/** 上级部门节点位置 */
	private String path;
	
	/** 上级部门List */
	private List<Map<String, Object>> higherOrganizationList;
	
	/** 部门代码 */
	private String departCode;
	
	/** 部门名称 */
	private String departName;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 部门类型 */
	private String type;
	
	/** 部门状态 */
	private String status;
	
	/** 定位条件 */
	private String locationPosition;

	
	/** 是否支持部门协同维护*/
	private boolean maintainOrgSynergy;
	
	public List<Map<String, Object>> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<Map<String, Object>> organizationList) {
		this.organizationList = organizationList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Map<String, Object>> getHigherOrganizationList() {
		return higherOrganizationList;
	}

	public void setHigherOrganizationList(
			List<Map<String, Object>> higherOrganizationList) {
		this.higherOrganizationList = higherOrganizationList;
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

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocationPosition() {
		return locationPosition;
	}

	public void setLocationPosition(String locationPosition) {
		this.locationPosition = locationPosition;
	}

	public boolean isMaintainOrgSynergy() {
		return maintainOrgSynergy;
	}

	public void setMaintainOrgSynergy(boolean maintainOrgSynergy) {
		this.maintainOrgSynergy = maintainOrgSynergy;
	}
	
}
