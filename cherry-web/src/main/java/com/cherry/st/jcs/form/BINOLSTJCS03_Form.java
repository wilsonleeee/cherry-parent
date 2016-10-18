package com.cherry.st.jcs.form;
/*  
 * @(#)BINOLSTJCS03_Form.java    1.0 2012-6-18     
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
public class BINOLSTJCS03_Form {

	/**仓库ID*/
	private String depotId;
	
	/**所属品牌*/
	private String brandInfoId;
	
	/** 归属部门 */
	private String organizationID;
	
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

	/** 关联部门  */
	private String[] relOrganizationIDArr;
	
	/** 关联部门的测试区分*/
	private String[] relOrganizationTestType;
	
	/** 默认区分*/
	private String[] defaultFlagArr;
	
	/** 关联部门备注*/
	private String[] commentArr;
	
	/** 区域ID*/
	private String regionId;
	
	/** 省份ID*/
	private String provinceId;
	
	/** 市ID*/
	private String cityId;
	
	/** 县级市ID*/
	private String CountyId;
	
	/** 仓库类型*/
	private String depotType;
	
	/**测试区分*/
	private String testType;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

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

	public String[] getRelOrganizationIDArr() {
		return relOrganizationIDArr;
	}

	public void setRelOrganizationIDArr(String[] relOrganizationIDArr) {
		this.relOrganizationIDArr = relOrganizationIDArr;
	}

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String[] getRelOrganizationTestType() {
		return relOrganizationTestType;
	}

	public void setRelOrganizationTestType(String[] relOrganizationTestType) {
		this.relOrganizationTestType = relOrganizationTestType;
	}

	public String[] getDefaultFlagArr() {
		return defaultFlagArr;
	}

	public void setDefaultFlagArr(String[] defaultFlagArr) {
		this.defaultFlagArr = defaultFlagArr;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
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

	public String getCountyId() {
		return CountyId;
	}

	public void setCountyId(String countyId) {
		CountyId = countyId;
	}

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String[] getCommentArr() {
		return commentArr;
	}

	public void setCommentArr(String[] commentArr) {
		this.commentArr = commentArr;
	}
	
}
