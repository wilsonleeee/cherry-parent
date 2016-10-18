/*		
 * @(#)BINOLBSEMP03_Form.java     1.0 2010/12/30		
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
 * 编辑员工form
 * 
 * @author lipc
 * @version 1.0 2010.12.30
 * 
 */
public class BINOLBSEMP03_Form {

	/** 雇员Id */
	private String employeeId;
	
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

	/** 手机 */
	private String mobilePhone;

	/** 电子邮箱 */
	private String email;

	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌Code【加解密参数】 */
	private String brandCode;
	
	/** 修改时间 */
	private String modifyTime;
	
	/** 修改次数 */
	private int modifyCount;

	/** 入退职信息 */
	private String quitInfo;

	/** 员工地址信息 */
	private String addressInfo;
	
	/** 员工节点 */
	private String empPath;
	
	/** 员工新上级节点 */
	private String higher;
	
	/** 员工上司的员工ID*/
	private String higherEmployeeId;
	
	/** 员工原上级节点 */
	private String oldHigher;
	
	/** 部门Id */
	private String departId;
	
	/** 岗位类别Id */
	private String positionCategoryId;
	
	/** 编辑前的岗位类别ID*/
	private String oldPositionCategoryId;
	
	/** 登录帐号 */
	private String longinName;
	
	/** 用户ID */
	private String userId;
	
	/** 登录密码 */
	private String password;
	
	/** 原登录密码 */
	private String oldPassword;
	
	/** 是否启用flag */
	private String validFlag;
	
	/** 管辖部门信息  */
	private String followDepart;
	
	/** 关注部门信息  */
	private String likeDepart;
	
	/** 迁移源 */
	private String fromPage;
	
	/** 关注用户 */
	private String[] likeEmployeeId;
	
	/** BI账号区分 */
	private String biFlag;
	
	/** 原管辖柜台  */
	private String[] oldfollowDepart;
	
	/** 原管辖和关注柜台  */
	private String[] oldfollowLikeDepart;
	
	/** 是否维护BA信息*/
	private boolean maintainBa;
	
	/** 是否需要自动添加柜台主管部门（1：自动添加）*/
	private String creatOrgFlag;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

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
		this.brandInfoId = brandInfoId.trim();
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(int modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getQuitInfo() {
		return quitInfo;
	}

	public void setQuitInfo(String quitInfo) {
		this.quitInfo = quitInfo;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

	public String getEmpPath() {
		return empPath;
	}

	public void setEmpPath(String empPath) {
		this.empPath = empPath;
	}

	public String getHigher() {
		return higher;
	}

	public void setHigher(String higher) {
		this.higher = higher;
	}

	public String getOldHigher() {
		return oldHigher;
	}

	public void setOldHigher(String oldHigher) {
		this.oldHigher = oldHigher;
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

	public String getLonginName() {
		return longinName;
	}

	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String[] getLikeEmployeeId() {
		return likeEmployeeId;
	}

	public void setLikeEmployeeId(String[] likeEmployeeId) {
		this.likeEmployeeId = likeEmployeeId;
	}

	public String getBiFlag() {
		return biFlag;
	}

	public void setBiFlag(String biFlag) {
		this.biFlag = biFlag;
	}

	public String[] getOldfollowDepart() {
		return oldfollowDepart;
	}

	public void setOldfollowDepart(String[] oldfollowDepart) {
		this.oldfollowDepart = oldfollowDepart;
	}

	public String[] getOldfollowLikeDepart() {
		return oldfollowLikeDepart;
	}

	public void setOldfollowLikeDepart(String[] oldfollowLikeDepart) {
		this.oldfollowLikeDepart = oldfollowLikeDepart;
	}

	public boolean isMaintainBa() {
		return maintainBa;
	}

	public void setMaintainBa(boolean maintainBa) {
		this.maintainBa = maintainBa;
	}

	public String getOldPositionCategoryId() {
		return oldPositionCategoryId;
	}

	public void setOldPositionCategoryId(String oldPositionCategoryId) {
		this.oldPositionCategoryId = oldPositionCategoryId;
	}

	public String getCreatOrgFlag() {
		return creatOrgFlag;
	}

	public void setCreatOrgFlag(String creatOrgFlag) {
		this.creatOrgFlag = creatOrgFlag;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getHigherEmployeeId() {
		return higherEmployeeId;
	}

	public void setHigherEmployeeId(String higherEmployeeId) {
		this.higherEmployeeId = higherEmployeeId;
	}
	
}
