/*
 * @(#)BINOLMBMBM06_Form.java     1.0 2012.11.27
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
 * 会员资料修改画面Form
 * 
 * @author WangCT
 * @version 1.0 2012.11.27
 */
public class BINOLMBMBM06_Form {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 修改时间 */
	private String modifyTime;
	
	/** 修改次数 */
	private String modifyCount;
	
	/** 假登录会员ID */
	private String newMemberInfoId;
	
	/** 假登录会员地址ID */
	private String newAddressInfoId;
	
	/** 版本号 */
	private String version;
	
	/** 会员老卡号 */
	private String memCodeOld;
	
	/** 会员卡回目 */
	private String cardCount;
	
	/** 会员卡号 */
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
	
	/** 变更前生日 **/
	private String birthOld;
	
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
	
	/** 会员原手机 **/
	private String mobilePhoneOld;
	
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
	
	/** 变更前推荐会员ID **/
	private String referrerIdOld;
	
	/** 推荐会员卡号(俱乐部) **/
	private String referrerClub;
	
	/** 变更前推荐会员ID(俱乐部) **/
	private String clubReferIdOld;
	
	/** 会员类型 **/
	private String memType;
	
	/** 是否接收通知 **/
	private String isReceiveMsg;
	
	/** 激活状态 **/
	private String active;
	
	/** 原激活状态 **/
	private String activeOld;
	
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
	
	/** 地址ID **/
	private String addressInfoId;
	
	/** 会员扩展属性 */
	private List<ExtendPropertyDto> propertyInfoList;
	
	/** 备注1 **/
	private String memo1;
	
	/** 备注2 **/
	private String memo2;
	
	/** 初始累计金额 **/
	private String initTotalAmount;
	
	/** 变更前初始累计金额 **/
	private String initTotalAmountOld;
	
	/** 会员有效无效状态 **/
	private String status;
	
	/** 入会途径 **/
	private String channelCode;
	
	/** 会员俱乐部ID */
	private String memberClubId;
	
	/** 俱乐部发卡柜台ID */
	private String organizationIdClub;
	
	/** 俱乐部发卡柜台代号 */
	private String organizationCodeClub;
	
	/** 俱乐部发卡BA ID */
	private String employeeIdClub;
	
	/** 俱乐部发卡BA代号 */
	private String employeeCodeClub;
	
	/** 俱乐部发卡日期 */
	private String joinTimeClub;

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

	public String getNewMemberInfoId() {
		return newMemberInfoId;
	}

	public void setNewMemberInfoId(String newMemberInfoId) {
		this.newMemberInfoId = newMemberInfoId;
	}

	public String getNewAddressInfoId() {
		return newAddressInfoId;
	}

	public void setNewAddressInfoId(String newAddressInfoId) {
		this.newAddressInfoId = newAddressInfoId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMemCodeOld() {
		return memCodeOld;
	}

	public void setMemCodeOld(String memCodeOld) {
		this.memCodeOld = memCodeOld;
	}

	public String getCardCount() {
		return cardCount;
	}

	public void setCardCount(String cardCount) {
		this.cardCount = cardCount;
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

	public String getBirthOld() {
		return birthOld;
	}

	public void setBirthOld(String birthOld) {
		this.birthOld = birthOld;
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

	public String getMobilePhoneOld() {
		return mobilePhoneOld;
	}

	public void setMobilePhoneOld(String mobilePhoneOld) {
		this.mobilePhoneOld = mobilePhoneOld;
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

	public String getReferrerIdOld() {
		return referrerIdOld;
	}

	public void setReferrerIdOld(String referrerIdOld) {
		this.referrerIdOld = referrerIdOld;
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

	public String getActiveOld() {
		return activeOld;
	}

	public void setActiveOld(String activeOld) {
		this.activeOld = activeOld;
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

	public String getAddressInfoId() {
		return addressInfoId;
	}

	public void setAddressInfoId(String addressInfoId) {
		this.addressInfoId = addressInfoId;
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

	public String getInitTotalAmountOld() {
		return initTotalAmountOld;
	}

	public void setInitTotalAmountOld(String initTotalAmountOld) {
		this.initTotalAmountOld = initTotalAmountOld;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
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

	public String getClubReferIdOld() {
		return clubReferIdOld;
	}

	public void setClubReferIdOld(String clubReferIdOld) {
		this.clubReferIdOld = clubReferIdOld;
	}

	public String getOrganizationIdClub() {
		return organizationIdClub;
	}

	public void setOrganizationIdClub(String organizationIdClub) {
		this.organizationIdClub = organizationIdClub;
	}

	public String getOrganizationCodeClub() {
		return organizationCodeClub;
	}

	public void setOrganizationCodeClub(String organizationCodeClub) {
		this.organizationCodeClub = organizationCodeClub;
	}

	public String getEmployeeIdClub() {
		return employeeIdClub;
	}

	public void setEmployeeIdClub(String employeeIdClub) {
		this.employeeIdClub = employeeIdClub;
	}

	public String getEmployeeCodeClub() {
		return employeeCodeClub;
	}

	public void setEmployeeCodeClub(String employeeCodeClub) {
		this.employeeCodeClub = employeeCodeClub;
	}

	public String getJoinTimeClub() {
		return joinTimeClub;
	}

	public void setJoinTimeClub(String joinTimeClub) {
		this.joinTimeClub = joinTimeClub;
	}

	public String getReferrerClub() {
		return referrerClub;
	}

	public void setReferrerClub(String referrerClub) {
		this.referrerClub = referrerClub;
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
