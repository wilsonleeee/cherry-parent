/*  
 * @(#)BINOLSTJCS04_Form.java    1.0 2011-8-23     
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
package com.cherry.st.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTJCS04_Form extends DataTable_BaseForm {

	// 品牌ID
	private String brandInfoId;
	// 部门ID
	private String organizationId;
	// 部门名称
	private String departName;
	// 部门Code
	private String departCode;
	// 仓库ID
	private String inventoryInfoId;
	// 仓库名称
	private String inventoryName;
	// 仓库Code
	private String inventoryCode;
	// 默认仓库
	private String defaultFlag;
	// 备注
	private String comments;
	// 仓库信息
	private String inventoryInfo;
	// 部门
	private String departInfo;
	// 仓库部门关系ID组
	private String identityIdArr;
	// 仓库部门关系ID
	private String identityId;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getIdentityIdArr() {
		return identityIdArr;
	}

	public void setIdentityIdArr(String identityIdArr) {
		this.identityIdArr = identityIdArr;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInventoryInfo() {
		return inventoryInfo;
	}

	public void setInventoryInfo(String inventoryInfo) {
		this.inventoryInfo = inventoryInfo;
	}

	public String getDepartInfo() {
		return departInfo;
	}

	public void setDepartInfo(String departInfo) {
		this.departInfo = departInfo;
	}

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	public String getInventoryInfoId() {
		return inventoryInfoId;
	}

	public void setInventoryInfoId(String inventoryInfoId) {
		this.inventoryInfoId = inventoryInfoId;
	}

}
