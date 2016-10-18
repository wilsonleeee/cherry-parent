package com.cherry.bs.cnt.form;
/*  
 * @(#)BINOLBSCNT06_Form.java    1.0 2012-7-1     
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
public class BINOLBSCNT06_Form {

	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 柜台中文名称 */
	private String counterName;
	
	/** 柜台英文名称 */
	private String foreignName;
	
	/** 渠道名称*/
	private String channeName;
	
	/** 柜台地址 */
	private String address;
	
	/** 城市 */
	private String cityId;
	
	/** 省份 */
	private String provinceId;
	
	/** 所属区域 */
	private String regionId;
	
	/** 区域code*/
	private String regionCode;
	/** 区域名称*/
	private String regionName;
	/** 省code*/
	private String provinceCode;
	/** 省名称*/
	private String provinceName;
	/** 城市code*/
	private String cityCode;
	/** 城市名称*/
	private String cityName;
	/** 商场名称*/
	private String mallName;
	
	/** 经销商code*/
	private String resellerCode;
	
	/** 经销商名称*/
	private String resellerName;
	
	/** 柜台类型 */
	private String counterKind;
	
	/** 柜台分类*/
	private String counterCategory;
	
	/**柜台员工数*/
	private String employeeNum;
	
	/** 柜台面积 */
	private String counterSpace;
	
	/** 柜台主管 */
	private String counterHead;
	
	/** 关注该柜台的人 */
	private String[] likeCounterEmp;
	
	/** 柜台类型 */
	private String managingType2;
	
	/** 银联设备号 */
	private String equipmentCode;
	
	private String message;
	
	/** 柜台电话 */
	private String counterTelephone;
	/** 经度 */
	private String longitude;
	/** 纬度 */
	private String latitude;
	/** 是否有pos机 */
	private String posFlag;
	
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

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getChanneName() {
		return channeName;
	}

	public void setChanneName(String channeName) {
		this.channeName = channeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getMallName() {
		return mallName;
	}

	public void setMallName(String mallName) {
		this.mallName = mallName;
	}

	public String getResellerCode() {
		return resellerCode;
	}

	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}

	public String getResellerName() {
		return resellerName;
	}

	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}

	public String getCounterKind() {
		return counterKind;
	}

	public void setCounterKind(String counterKind) {
		this.counterKind = counterKind;
	}

	public String getCounterCategory() {
		return counterCategory;
	}

	public void setCounterCategory(String counterCategory) {
		this.counterCategory = counterCategory;
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

	public String getForeignName() {
		return foreignName;
	}

	public void setForeignName(String foreignName) {
		this.foreignName = foreignName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getCounterTelephone() {
		return counterTelephone;
	}

	public void setCounterTelephone(String counterTelephone) {
		this.counterTelephone = counterTelephone;
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
	
	public String getCntCodeRule() {
		return cntCodeRule;
	}

	public void setCntCodeRule(String cntCodeRule) {
		this.cntCodeRule = cntCodeRule;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
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

	public String getPosFlag() {
		return posFlag;
	}

	public void setPosFlag(String posFlag) {
		this.posFlag = posFlag;
	}

	
}
