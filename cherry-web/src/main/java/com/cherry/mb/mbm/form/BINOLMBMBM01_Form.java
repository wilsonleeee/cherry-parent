/*
 * @(#)BINOLMBMBM01_Form.java     1.0 2011/03/22
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
import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BINOLMBMBM01_Form extends DataTable_BaseForm {
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 会员地址非空 **/
	private String addrNotEmpty;
	
	/** 会员电话非空 **/
	private String telNotEmpty;
	
	/** 会员手机合法 **/
	private String telCheck;
	
	/** 会员Email非空 **/
	private String emailNotEmpty;
	
	/** 会员无效flg */
	private String validFlag;
	
	/** 会员信息登记区分 */
	private String memInfoRegFlg;
	
	/** 会员类型 **/
	private List<String> memType;
	
	/** 会员等级 **/
	private List<String> memLevel;

	/** 会员卡号 */
	private String memCode;
	
	/** 会员手机 */
	private String mobilePhone;
	
	/** 会员姓名 */
	private String name;
	
	/** 会员性别 */
	private List<String> mebSex;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;
	
	/** 会员年龄下限 */
	private String ageStart;
	
	/** 会员年龄上限 */
	private String ageEnd;
	
	/** 会员入会时间下限 */
	private String joinDateStart;
	
	/** 会员入会时间上限 */
	private String joinDateEnd;
	
	/** 积分上限 */
	private String memberPointStart;
	
	/** 积分下限 */
	private String memberPointEnd;
	
	/** 所属柜台号 */
	private String counterCode;
	
	/** 推荐会员卡号 */
	private String referrerMemCode;
	
	/** 购买时间上限 */
	private String saleTimeStart;
	
	/** 购买时间下限 */
	private String saleTimeEnd;
	
	/** 购买柜台 */
	private String saleCounterCode;
	
	/** 购买次数上限 */
	private String saleCountStart;
	
	/** 购买次数下限 */
	private String saleCountEnd;
	
	/** 购买金额上限 */
	private String payAmountStart;
	
	/** 购买金额下限 */
	private String payAmountEnd;
	
	/** 购买产品 **/
	private List<String> buyPrtVendorId;
	
	/** 购买某类产品 **/
	private List<String> buyCateValId;
	
	/** 未购买产品 **/
	private List<String> notPrtVendorId;
	
	/** 未购买某类产品 **/
	private List<String> notCateValId;
	
	/** 多个已购买产品条件之间的关系（1：AND，2：OR） **/
	private String relation;
	
	/** 多个未购买产品条件之间的关系（1：AND，2：OR） **/
	private String notRelation;

	/** 会员扩展属性 */
	private List<ExtendPropertyDto> propertyInfoList;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getAddrNotEmpty() {
		return addrNotEmpty;
	}

	public void setAddrNotEmpty(String addrNotEmpty) {
		this.addrNotEmpty = addrNotEmpty;
	}

	public String getTelNotEmpty() {
		return telNotEmpty;
	}

	public void setTelNotEmpty(String telNotEmpty) {
		this.telNotEmpty = telNotEmpty;
	}

	public String getTelCheck() {
		return telCheck;
	}

	public void setTelCheck(String telCheck) {
		this.telCheck = telCheck;
	}

	public String getEmailNotEmpty() {
		return emailNotEmpty;
	}

	public void setEmailNotEmpty(String emailNotEmpty) {
		this.emailNotEmpty = emailNotEmpty;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getMemInfoRegFlg() {
		return memInfoRegFlg;
	}

	public void setMemInfoRegFlg(String memInfoRegFlg) {
		this.memInfoRegFlg = memInfoRegFlg;
	}

	public List<String> getMemType() {
		return memType;
	}

	public void setMemType(List<String> memType) {
		this.memType = memType;
	}

	public List<String> getMemLevel() {
		return memLevel;
	}

	public void setMemLevel(List<String> memLevel) {
		this.memLevel = memLevel;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMebSex() {
		return mebSex;
	}

	public void setMebSex(List<String> mebSex) {
		this.mebSex = mebSex;
	}

	public String getBirthDayMonth() {
		return birthDayMonth;
	}

	public void setBirthDayMonth(String birthDayMonth) {
		this.birthDayMonth = birthDayMonth;
	}

	public String getBirthDayDate() {
		return birthDayDate;
	}

	public void setBirthDayDate(String birthDayDate) {
		this.birthDayDate = birthDayDate;
	}

	public String getAgeStart() {
		return ageStart;
	}

	public void setAgeStart(String ageStart) {
		this.ageStart = ageStart;
	}

	public String getAgeEnd() {
		return ageEnd;
	}

	public void setAgeEnd(String ageEnd) {
		this.ageEnd = ageEnd;
	}

	public String getJoinDateStart() {
		return joinDateStart;
	}

	public void setJoinDateStart(String joinDateStart) {
		this.joinDateStart = joinDateStart;
	}

	public String getJoinDateEnd() {
		return joinDateEnd;
	}

	public void setJoinDateEnd(String joinDateEnd) {
		this.joinDateEnd = joinDateEnd;
	}

	public String getMemberPointStart() {
		return memberPointStart;
	}

	public void setMemberPointStart(String memberPointStart) {
		this.memberPointStart = memberPointStart;
	}

	public String getMemberPointEnd() {
		return memberPointEnd;
	}

	public void setMemberPointEnd(String memberPointEnd) {
		this.memberPointEnd = memberPointEnd;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getReferrerMemCode() {
		return referrerMemCode;
	}

	public void setReferrerMemCode(String referrerMemCode) {
		this.referrerMemCode = referrerMemCode;
	}

	public String getSaleTimeStart() {
		return saleTimeStart;
	}

	public void setSaleTimeStart(String saleTimeStart) {
		this.saleTimeStart = saleTimeStart;
	}

	public String getSaleTimeEnd() {
		return saleTimeEnd;
	}

	public void setSaleTimeEnd(String saleTimeEnd) {
		this.saleTimeEnd = saleTimeEnd;
	}

	public String getSaleCounterCode() {
		return saleCounterCode;
	}

	public void setSaleCounterCode(String saleCounterCode) {
		this.saleCounterCode = saleCounterCode;
	}

	public String getSaleCountStart() {
		return saleCountStart;
	}

	public void setSaleCountStart(String saleCountStart) {
		this.saleCountStart = saleCountStart;
	}

	public String getSaleCountEnd() {
		return saleCountEnd;
	}

	public void setSaleCountEnd(String saleCountEnd) {
		this.saleCountEnd = saleCountEnd;
	}

	public String getPayAmountStart() {
		return payAmountStart;
	}

	public void setPayAmountStart(String payAmountStart) {
		this.payAmountStart = payAmountStart;
	}

	public String getPayAmountEnd() {
		return payAmountEnd;
	}

	public void setPayAmountEnd(String payAmountEnd) {
		this.payAmountEnd = payAmountEnd;
	}

	public List<String> getBuyPrtVendorId() {
		return buyPrtVendorId;
	}

	public void setBuyPrtVendorId(List<String> buyPrtVendorId) {
		this.buyPrtVendorId = buyPrtVendorId;
	}

	public List<String> getBuyCateValId() {
		return buyCateValId;
	}

	public void setBuyCateValId(List<String> buyCateValId) {
		this.buyCateValId = buyCateValId;
	}

	public List<ExtendPropertyDto> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<ExtendPropertyDto> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public List<String> getNotPrtVendorId() {
		return notPrtVendorId;
	}

	public void setNotPrtVendorId(List<String> notPrtVendorId) {
		this.notPrtVendorId = notPrtVendorId;
	}

	public List<String> getNotCateValId() {
		return notCateValId;
	}

	public void setNotCateValId(List<String> notCateValId) {
		this.notCateValId = notCateValId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getNotRelation() {
		return notRelation;
	}

	public void setNotRelation(String notRelation) {
		this.notRelation = notRelation;
	}
}
