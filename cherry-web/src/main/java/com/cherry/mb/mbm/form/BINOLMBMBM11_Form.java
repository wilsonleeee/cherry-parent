/*
 * @(#)BINOLMBMBM11_Action.java     1.0 2013/03/05
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
package com.cherry.mb.mbm.form;

import java.util.List;

import com.cherry.cm.dto.ExtendPropertyDto;

/**
 * 添加会员画面Form
 * 
 * @author WangCT
 * @version 1.0 2013/03/05
 */
public class BINOLMBMBM11_Form {
	
	/** 假登录会员ID **/
	private String memberInfoId;
	
	/** 变更前会员等级 **/
	private String oldMemberLevel;
	
	/** 变更前会员等级名称 **/
	private String oldLevelName;
	
	/** 会员等级是否维护确认 **/
	private String memLevelConfirm;
	
	/** 会员卡号 **/
	private String memCode;
	
	/** 发卡日期 **/
	private String joinDate;
	
	/** 发卡柜台 ID **/
	private String organizationId;
	
	/** 发卡柜台 CODE **/
	private String organizationCode;
	
	/** 发卡柜台测试区分 **/
	private String counterKind;
	
	/** 发卡BA ID **/
	private String employeeId;
	
	/** 发卡BA CODE **/
	private String employeeCode;
	
	/** 会员等级ID **/
	private String memberLevel;
	
	/** 昵称 */
	private String nickname;
	
	/** 信用等级 */
	private String creditRating;
	
	/** 会员QQ */
	private String tencentQQ;
	
	/** 会员姓名 **/
	private String memName;
	
	/** 会员生日 **/
	private String birth;
	
	/** 会员性别 **/
	private String gender;
	
	/** 会员身份证 **/
	private String identityCard;
	
	/** 婚姻状况 **/
	private String maritalStatus;
	
	/** 职业 **/
	private String profession;
	
	/** 会员手机 **/
	private String mobilePhone;
	
	/** 会员电话 **/
	private String telephone;
	
	/** 最佳联络方式 **/
	private List<String> connectTime;
	
	/** 会员email地址 **/
	private String email;
	
	/** 微博号 **/
	private String blogId;
	
	/** 微信号 **/
	private String messageId;
	
	/** 推荐会员卡号 **/
	private String referrer;
	
	/** 会员类型 **/
	private String memType;
	
	/** 是否接收通知 **/
	private String isReceiveMsg;
	
	/** 激活状态 **/
	private String active;
	
	/** 省ID **/
	private String provinceId;
	
	/** 城市ID **/
	private String cityId;
	
	/** 区县ID **/
	private String countyId;
	
	/** 地址 **/
	private String address;
	
	/** 邮编 **/
	private String postcode;
	
	/** 会员扩展属性 */
	private List<ExtendPropertyDto> propertyInfoList;
	
	/** 备注1 **/
	private String memo1;
	
	/** 备注2 **/
	private String memo2;
	
	/** 初始累计金额 **/
	private String initTotalAmount;
	
	/** 入会途径 **/
	private String channelCode;

	/** 回访方式 **/
	private String returnVisit;

	/** 收入 **/
	private String income;

	/** 肤质 **/
	private String skinType;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getOldMemberLevel() {
		return oldMemberLevel;
	}

	public void setOldMemberLevel(String oldMemberLevel) {
		this.oldMemberLevel = oldMemberLevel;
	}

	public String getOldLevelName() {
		return oldLevelName;
	}

	public void setOldLevelName(String oldLevelName) {
		this.oldLevelName = oldLevelName;
	}

	public String getMemLevelConfirm() {
		return memLevelConfirm;
	}

	public void setMemLevelConfirm(String memLevelConfirm) {
		this.memLevelConfirm = memLevelConfirm;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getCounterKind() {
		return counterKind;
	}

	public void setCounterKind(String counterKind) {
		this.counterKind = counterKind;
	}

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
		this.employeeCode = employeeCode;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<String> getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(List<String> connectTime) {
		this.connectTime = connectTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getMemType() {
		return memType;
	}

	public void setMemType(String memType) {
		this.memType = memType;
	}

	public String getIsReceiveMsg() {
		return isReceiveMsg;
	}

	public void setIsReceiveMsg(String isReceiveMsg) {
		this.isReceiveMsg = isReceiveMsg;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public List<ExtendPropertyDto> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<ExtendPropertyDto> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getInitTotalAmount() {
		return initTotalAmount;
	}

	public void setInitTotalAmount(String initTotalAmount) {
		this.initTotalAmount = initTotalAmount;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCreditRating() {
		return creditRating;
	}

	public void setCreditRating(String creditRating) {
		this.creditRating = creditRating;
	}

	public String getTencentQQ() {
		return tencentQQ;
	}

	public void setTencentQQ(String tencentQQ) {
		this.tencentQQ = tencentQQ;
	}

	public String getReturnVisit() {
		return returnVisit;
	}

	public void setReturnVisit(String returnVisit) {
		this.returnVisit = returnVisit;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}
}
