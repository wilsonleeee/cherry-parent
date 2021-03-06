/*
 * @(#)BINOLPLPLT03_Form.java     1.0 2010/10/27
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

package com.cherry.pl.plt.form;

/**
 * 更新权限类型Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT03_Form {
	
	/** 权限类型记录ID */
	private String privilegeTypeId;
	
	/** 数据过滤类别 */
	private String category;
	
	/** 部门类型 */
	private String departType;
	
	/** 岗位类别 */
	private String positionCategoryId;
	
	/** 业务类型 */
	private String businessType;
	
	/** 操作类型 */
	private String operationType;
	
	/** 权限类型 */
	private String privilegeType;
	
	/** 是否排他 */
	private String exclusive;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	public String getPrivilegeTypeId() {
		return privilegeTypeId;
	}

	public void setPrivilegeTypeId(String privilegeTypeId) {
		this.privilegeTypeId = privilegeTypeId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(String privilegeType) {
		this.privilegeType = privilegeType;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getExclusive() {
		return exclusive;
	}

	public void setExclusive(String exclusive) {
		this.exclusive = exclusive;
	}

}
