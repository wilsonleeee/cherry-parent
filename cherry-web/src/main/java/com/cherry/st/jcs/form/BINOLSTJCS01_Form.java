/*
 * @(#)BINOLSTJCS01_Form.java     1.0 2011/04/11
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 仓库管理Form
 * 
 * @author weisc
 * @version 1.0 2011.08.24
 */
public class BINOLSTJCS01_Form extends DataTable_BaseForm{
	
	/** 所属组织 */
	private String depotInfoID;
	
	/**品牌ID*/
	private String brandInfoId;
	
	/** 归属部门 ID*/
	private String organizationID;
	
	/** 归属部门名称*/
	private String departName;
	
	/** 仓库名称*/
	private String depotName;
	
	/** 仓库编号 */
	private String depotCode;
	
	/** 仓库中文名称  */
	private String depotNameCN;
	
	/** 仓库英文名称  */
	private String depotNameEN;
	
	/** 仓库地址  */
	private String address;
	
	/** 有效区分  */
	private String validFlag;

	/** 仓库编号  */
	private String depotInfoIDArr;
	
	/** 仓库List */
	private List<Map<String, Object>> inventoryInfoList;

	/** 仓库详细 */
	private List<Map<String, Object>> inventoryInfoDetail;
	
	/** 是否测试仓库  */
	private String testType;
	
	/**
	 * @author zhanggl
	 * @date 2011-12-1
	 * 
	 * */
	/**添加时的测试仓库区分*/
	private String testTypeAdd;
	
	private String isTestType;
	
	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDepotNameCN() {
		return depotNameCN;
	}

	public void setDepotNameCN(String depotNameCN) {
		this.depotNameCN = depotNameCN;
	}

	public String getDepotNameEN() {
		return depotNameEN;
	}

	public void setDepotNameEN(String depotNameEN) {
		this.depotNameEN = depotNameEN;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public void setDepotInfoIDArr(String depotInfoIDArr) {
		this.depotInfoIDArr = depotInfoIDArr;
	}

	public String getDepotInfoIDArr() {
		return depotInfoIDArr;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setDepotInfoID(String depotInfoID) {
		this.depotInfoID = depotInfoID;
	}

	public String getDepotInfoID() {
		return depotInfoID;
	}

	public void setInventoryInfoList(List<Map<String, Object>> inventoryInfoList) {
		this.inventoryInfoList = inventoryInfoList;
	}

	public List<Map<String, Object>> getInventoryInfoList() {
		return inventoryInfoList;
	}

	public void setInventoryInfoDetail(List<Map<String, Object>> inventoryInfoDetail) {
		this.inventoryInfoDetail = inventoryInfoDetail;
	}

	public List<Map<String, Object>> getInventoryInfoDetail() {
		return inventoryInfoDetail;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getTestType() {
		return testType;
	}

	public void setIsTestType(String isTestType) {
		this.isTestType = isTestType;
	}

	public String getIsTestType() {
		return isTestType;
	}

	public String getTestTypeAdd() {
		return testTypeAdd;
	}

	public void setTestTypeAdd(String testTypeAdd) {
		this.testTypeAdd = testTypeAdd;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
}
