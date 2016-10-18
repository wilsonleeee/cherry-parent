/*
 * @(#)BINOLBSDEP03_Form.java     1.0 2010/10/27
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

/**
 * 
 * 更新部门画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP03_Form {
	
	/** 部门ID */
	private String organizationId;
	
	/** 部门节点位置 */
	private String departPath;
	
	/** 原上级部门 */
	private String higherDepartPath;
	
	/** 新上级部门 */
	private String path;
	
	/** 部门代码 */
	private String departCode;
	
	/** 部门名称 */
	private String departName;
	
	/** 部门简称 */
	private String departNameShort;
	
	/** 部门英文名称 */
	private String nameForeign;
	
	/** 部门英文简称 */
	private String nameShortForeign;
	
	/** 部门类型 */
	private String type;
	
	/** 状态 */
	private String status;
	
	/** 原部门地址ID */
	private String[] oldAddressInfoId;
	
	/** 部门地址 */
	private String departAddress;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 迁移源 */
	private String fromPage;
	
	/** 部门联系人 */
	private String departContact;
	
	/** 原部门联系人 */
	private String[] oldContactInfoId;
	
	/** 柜台path */
	private String[] counterPath;
	
	/** 测试区分 */
	private String testType;
	
	/** 所属区域 */
	private String regionId;
	
	/** 所属省份 */
	private String provinceId;
	
	/** 所属城市 */
	private String cityId;
	
	/** 所属县级市 */
	private String countyId;
	
	/** 到期日 */
	private String expiringDate;
	
	/** 到期日_年月日 */
	private String expiringDateDate;
	
	/** 到期日_时分秒 */
	private String expiringDateTime;
	
	/** 部门协同区分 */
	private String orgSynergyFlag;
	
	/** 是否支持部门协同维护*/
	private boolean maintainOrgSynergy;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getDepartPath() {
		return departPath;
	}

	public void setDepartPath(String departPath) {
		this.departPath = departPath;
	}

	public String getHigherDepartPath() {
		return higherDepartPath;
	}

	public void setHigherDepartPath(String higherDepartPath) {
		this.higherDepartPath = higherDepartPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public String getDepartNameShort() {
		return departNameShort;
	}

	public void setDepartNameShort(String departNameShort) {
		this.departNameShort = departNameShort;
	}

	public String getNameForeign() {
		return nameForeign;
	}

	public void setNameForeign(String nameForeign) {
		this.nameForeign = nameForeign;
	}

	public String getNameShortForeign() {
		return nameShortForeign;
	}

	public void setNameShortForeign(String nameShortForeign) {
		this.nameShortForeign = nameShortForeign;
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

	public String[] getOldAddressInfoId() {
		return oldAddressInfoId;
	}

	public void setOldAddressInfoId(String[] oldAddressInfoId) {
		this.oldAddressInfoId = oldAddressInfoId;
	}

	public String getDepartAddress() {
		return departAddress;
	}

	public void setDepartAddress(String departAddress) {
		this.departAddress = departAddress;
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

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getDepartContact() {
		return departContact;
	}

	public void setDepartContact(String departContact) {
		this.departContact = departContact;
	}

	public String[] getOldContactInfoId() {
		return oldContactInfoId;
	}

	public void setOldContactInfoId(String[] oldContactInfoId) {
		this.oldContactInfoId = oldContactInfoId;
	}

	public String[] getCounterPath() {
		return counterPath;
	}

	public void setCounterPath(String[] counterPath) {
		this.counterPath = counterPath;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
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
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	
	public String getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(String expiringDate) {
		this.expiringDate = expiringDate;
	}
	
	public String getExpiringDateDate() {
		return expiringDateDate;
	}

	public void setExpiringDateDate(String expiringDateDate) {
		this.expiringDateDate = expiringDateDate;
	}

	public String getExpiringDateTime() {
		return expiringDateTime;
	}

	public void setExpiringDateTime(String expiringDateTime) {
		this.expiringDateTime = expiringDateTime;
	}

	public String getOrgSynergyFlag() {
		return orgSynergyFlag;
	}

	public void setOrgSynergyFlag(String orgSynergyFlag) {
		this.orgSynergyFlag = orgSynergyFlag;
	}

	public boolean isMaintainOrgSynergy() {
		return maintainOrgSynergy;
	}

	public void setMaintainOrgSynergy(boolean maintainOrgSynergy) {
		this.maintainOrgSynergy = maintainOrgSynergy;
	}
	

}
