/*		
 * @(#)BINOLBSEMP04_Form.java     1.0 2010/12/22		
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
package com.cherry.bs.emp.form;


/**
 * 添加员工form
 * 
 * @author lipc
 * @version 1.0 2010.12.22
 * 
 */
public class BINOLBSEMP04_Form{
	
	/** 雇员code */
	private String employeeCode;
	
	/** 雇员name */
	private String employeeName;
	
	/** 雇员外文名 */
	private String employeeNameForeign;
	
	/** 雇员性别 */
	private String gender;
	
	/** 婚姻状况 */
	private String maritalStatus;
	
	/** 学历 */
	private String academic;
	
	/** 出生日期 */
	private String birthDay;
	
	/** 身份证号 */
	private String identityCard;
	
	/** 联系电话 */
	private String phone;
	
	/** 手机*/
	private String mobilePhone;
	
	/** 电子邮箱 */
	private String email;
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 部门Id */
	private String departId;
	
	/** 岗位类别Id */
	private String positionCategoryId;
	
	/** 员工入职日期  */
	private String commtDate;
	
	/** 员工地址信息 */
	private String addressInfo;
	
	/** 管辖部门信息  */
	private String followDepart;
	
	/** 关注部门信息  */
	private String likeDepart;
	
	/** 上级 */
	private String higher;
	
	/** 员工上司的员工ID*/
	private String higherEmployeeId;
	
	/** 登录帐号 */
	private String longinName;
	
	/** 登录密码 */
	private String password;
	
	/** BI账号区分 */
	private String biFlag;
	
	/** 是否启用flag */
	private String validFlag;
	
	/** 关注用户 */
	private String[] likeEmployeeId;
	
	/** 是否维护BA信息*/
	private boolean maintainBa;
	
	/** 是否需要自动添加柜台主管部门（1：自动添加）*/
	private String creatOrgFlag;

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode.trim();
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName.trim();
	}

	public String getEmployeeNameForeign() {
		return employeeNameForeign;
	}

	public void setEmployeeNameForeign(String employeeNameForeign) {
		this.employeeNameForeign = employeeNameForeign.trim();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getAcademic() {
		return academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay.trim();
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone.trim();
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
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

	public String getCommtDate() {
		return commtDate;
	}

	public void setCommtDate(String commtDate) {
		this.commtDate = commtDate.trim();
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo.trim();
	}

	public String getFollowDepart() {
		return followDepart;
	}

	public void setFollowDepart(String followDepart) {
		this.followDepart = followDepart;
	}

	public String getLikeDepart() {
		return likeDepart;
	}

	public void setLikeDepart(String likeDepart) {
		this.likeDepart = likeDepart;
	}

	public String getHigher() {
		return higher;
	}

	public void setHigher(String higher) {
		this.higher = higher;
	}

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiFlag() {
		return biFlag;
	}

	public void setBiFlag(String biFlag) {
		this.biFlag = biFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String[] getLikeEmployeeId() {
		return likeEmployeeId;
	}

	public void setLikeEmployeeId(String[] likeEmployeeId) {
		this.likeEmployeeId = likeEmployeeId;
	}

	public boolean isMaintainBa() {
		return maintainBa;
	}

	public void setMaintainBa(boolean maintainBa) {
		this.maintainBa = maintainBa;
	}

	public String getCreatOrgFlag() {
		return creatOrgFlag;
	}

	public void setCreatOrgFlag(String creatOrgFlag) {
		this.creatOrgFlag = creatOrgFlag;
	}

	public String getHigherEmployeeId() {
		return higherEmployeeId;
	}

	public void setHigherEmployeeId(String higherEmployeeId) {
		this.higherEmployeeId = higherEmployeeId;
	}
	
}
