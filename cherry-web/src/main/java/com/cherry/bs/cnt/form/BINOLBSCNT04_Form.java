/*	
 * @(#)BINOLBSCNT04_Form.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.form;

/**
 * 
 * 	创建柜台画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT04_Form {
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 柜台中文名称 */
	private String counterNameIF;
	
	/** 柜台简称 */
	private String counterNameShort;
	
	/** 柜台英文名称 */
	private String nameForeign;
	
	/** 柜台英文简称 */
	private String nameShortForeign;
	
	/** 柜台电话 */
	private String counterTelephone;
	
	/** 渠道ID */
	private String channelId;
	
	/** 柜台状态 */
	private String status;
	
	/** 柜台地址 */
	private String counterAddress;
	
	/** 柜台分类 */
	private String counterCategory;
	
	/** 县级市 */
	private String countyId;
	
	/** 城市 */
	private String cityId;
	
	/** 省份 */
	private String provinceId;
	
	/** 所属区域 */
	private String regionId;
	
	/** 商场 */
	private String mallInfoId;
	
	/** 经销商 */
	private String resellerInfoId;
	
	/** 柜台类型 */
	private String counterKind;
	
	/** 柜台级别 */
	private String counterLevel;
	
	/** 柜台升级状态 */
	private String updateStatus;
	
	/** 柜台面积 */
	private String counterSpace;
	
	/** 柜台主管 */
	private String counterHead;
	
	/** 关注该柜台的人 */
	private String[] likeCounterEmp;
	
	/** 上级部门节点 */
	private String path;
	
	/** 经销商部门ID */
	private String resellerDepartId;
	
	/** 是否维护柜台密码*/
	private boolean maintainPassWord;
	
	/** 是否支持柜台协同维护*/
	private boolean maintainCoutSynergy;
	
	/** 柜台密码*/
	private String passWord;
	
	/** 柜台协同区分*/
	private String counterSynergyFlag;
	
	/** 到期日_年月日 */
	private String expiringDateDate;
	
	/** 到期日_时分秒 */
	private String expiringDateTime;
	
	/** 经度 */
	private String longitude;
	
	/** 纬度 */
	private String latitude;
	
	/** 是否有POS机 */
	private String posFlag;
	
	/** 柜台所属系统 */
	private String belongFaction;

	/** 柜台所属系统*/
	private String belongFactionName;
	
	/** 运营模式 */
	private String operateMode;
	
	/** 开票单位 */
	private String invoiceCompany;
	
	/** 柜台员工数 */
	private String employeeNum;
	
	/** 商圈 */
	private String busDistrict;
	/**业务负责人*/
	private String busniessPrincipal;
	
	/**银联设备号*/
	private String equipmentCode;
	
	/**柜台类型*/
	private String managingType2;
	
	//-------------------------------------------------------------------------------------------------------------------------------//
	/** 柜台生成规则 */
	private String cntCodeRule;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterNameIF() {
		return counterNameIF;
	}

	public void setCounterNameIF(String counterNameIF) {
		this.counterNameIF = counterNameIF;
	}

	public String getCounterNameShort() {
		return counterNameShort;
	}

	public void setCounterNameShort(String counterNameShort) {
		this.counterNameShort = counterNameShort;
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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCounterAddress() {
		return counterAddress;
	}

	public void setCounterAddress(String counterAddress) {
		this.counterAddress = counterAddress;
	}

	public String getCounterCategory() {
		return counterCategory;
	}

	public void setCounterCategory(String counterCategory) {
		this.counterCategory = counterCategory;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getMallInfoId() {
		return mallInfoId;
	}

	public void setMallInfoId(String mallInfoId) {
		this.mallInfoId = mallInfoId;
	}

	public String getResellerInfoId() {
		return resellerInfoId;
	}

	public void setResellerInfoId(String resellerInfoId) {
		this.resellerInfoId = resellerInfoId;
	}

	public String getCounterKind() {
		return counterKind;
	}

	public void setCounterKind(String counterKind) {
		this.counterKind = counterKind;
	}

	public String getCounterLevel() {
		return counterLevel;
	}

	public void setCounterLevel(String counterLevel) {
		this.counterLevel = counterLevel;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String getCounterSpace() {
		return counterSpace;
	}

	public void setCounterSpace(String counterSpace) {
		this.counterSpace = counterSpace;
	}

	public String getCounterHead() {
		return counterHead;
	}

	public void setCounterHead(String counterHead) {
		this.counterHead = counterHead;
	}

	public String[] getLikeCounterEmp() {
		return likeCounterEmp;
	}

	public void setLikeCounterEmp(String[] likeCounterEmp) {
		this.likeCounterEmp = likeCounterEmp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getResellerDepartId() {
		return resellerDepartId;
	}

	public void setResellerDepartId(String resellerDepartId) {
		this.resellerDepartId = resellerDepartId;
	}

	public boolean isMaintainPassWord() {
		return maintainPassWord;
	}

	public void setMaintainPassWord(boolean maintainPassWord) {
		this.maintainPassWord = maintainPassWord;
	}

	public boolean isMaintainCoutSynergy() {
		return maintainCoutSynergy;
	}

	public void setMaintainCoutSynergy(boolean maintainCoutSynergy) {
		this.maintainCoutSynergy = maintainCoutSynergy;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getCounterSynergyFlag() {
		return counterSynergyFlag;
	}

	public void setCounterSynergyFlag(String counterSynergyFlag) {
		this.counterSynergyFlag = counterSynergyFlag;
	}

	public String getCounterTelephone() {
		return counterTelephone;
	}

	public void setCounterTelephone(String counterTelephone) {
		this.counterTelephone = counterTelephone;
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
	
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getPosFlag() {
		return posFlag;
	}

	public void setPosFlag(String posFlag) {
		this.posFlag = posFlag;
	}
	
	public String getCntCodeRule() {
		return cntCodeRule;
	}

	public void setCntCodeRule(String cntCodeRule) {
		this.cntCodeRule = cntCodeRule;
	}

	public String getBelongFaction() {
		return belongFaction;
	}

	public void setBelongFaction(String belongFaction) {
		this.belongFaction = belongFaction;
	}

	public String getOperateMode() {
		return operateMode;
	}

	public void setOperateMode(String operateMode) {
		this.operateMode = operateMode;
	}

	public String getInvoiceCompany() {
		return invoiceCompany;
	}

	public void setInvoiceCompany(String invoiceCompany) {
		this.invoiceCompany = invoiceCompany;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}
	public String getBusDistrict() {
		return busDistrict;
	}

	public void setBusDistrict(String busDistrict) {
		this.busDistrict = busDistrict;
	}

	public String getBusniessPrincipal() {
		return busniessPrincipal;
	}

	public void setBusniessPrincipal(String busniessPrincipal) {
		this.busniessPrincipal = busniessPrincipal;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getManagingType2() {
		return managingType2;
	}

	public void setManagingType2(String managingType2) {
		this.managingType2 = managingType2;
	}

	public String getBelongFactionName() {
		return belongFactionName;
	}

	public void setBelongFactionName(String belongFactionName) {
		this.belongFactionName = belongFactionName;
	}
	
	
}
