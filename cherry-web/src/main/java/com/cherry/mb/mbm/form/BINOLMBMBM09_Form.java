/*	
 * @(#)BINOLMBMBM09_Form.java     1.0 2012/01/07		
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
import java.util.Map;

import com.cherry.cm.dto.ExtendPropertyDto;
import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员搜索画面Form
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLMBMBM09_Form extends DataTable_BaseForm {
	
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
	
	/** 测试区分 */
	private String testType;
	
	/** 会员类型 **/
	private List<String> memType;
	
	/** 会员等级 **/
	private List<String> memLevel;

	/** 会员卡号 */
	private String memCode;
	
	/** 昵称 */
	private String nickname;
	
	/** QQ*/
	private String tencentQQ;
	
	/** 会员信用等级 */
	private String creditRating;
	
	/** 会员手机 */
	private String mobilePhone;
	
	/** 会员姓名 */
	private String name;
	
	/** 会员性别 */
	private List<String> mebSex;
	
	/** 会员生日模式 */
	private String birthDayMode;
	
	/** 会员生日范围 */
	private String birthDayRange;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;
	
	/** 是否去除当月入会会员Flag */
	private String joinDateFlag;
	
	/** 是否去除当天入会会员Flag */
	private String curDayJoinDateFlag;
	
	/** 生日（日上限） */
	private String birthDayDateStart;
	
	/** 生日（日下限） */
	private String birthDayDateEnd;
	
	/** 生日前或者生日后（1：前，2：后） */
	private String birthDayPath;
	
	/** 生日单位（1：月，2：天） */
	private String birthDayUnit;
	
	/** 生日范围（月上限） */
	private String birthDayMonthRangeStart;
	
	/** 生日范围（月下限） */
	private String birthDayMonthRangeEnd;
	
	/** 生日范围（日上限） */
	private String birthDayDateRangeStart;
	
	/** 生日范围（日下限） */
	private String birthDayDateRangeEnd;
	
	/** 会员年龄下限 */
	private String ageStart;
	
	/** 会员年龄上限 */
	private String ageEnd;
	
	/** 入会时间模式 **/
	private String joinDateMode;
	
	/** 入会时间范围 **/
	private String joinDateRange;
	
	/** 入会时间范围单位 **/
	private String joinDateUnit;
	
	/** 入会时间范围单位（1：一段时间内，2：满一段时间） **/
	private String joinDateUnitFlag;
	
	/** 入会时间上限 **/
	private String joinDateStart;
	
	/** 入会时间下限 **/
	private String joinDateEnd;
	
	/** 入会时间和购买时间条件之间的关系（1：AND，2：OR） **/
	private String joinDateSaleDateRel;
	
	/** 积分上限 */
	private String memberPointStart;
	
	/** 积分下限 */
	private String memberPointEnd;
	
	/** 可兑换积分上限 **/
	private String changablePointStart;
	
	/** 可兑换积分下限 **/
	private String changablePointEnd;
	
	/** 发卡柜台所在区域ID **/
	private String regionId;
	
	/** 所属系统ID **/
	private String belongId;
	
	/** 发卡柜台所在省ID **/
	private String provinceId;
	
	/** 发卡柜台所在城市ID **/
	private String cityId;
	
	/** 发卡柜台所在部门ID */
	private String memCounterId;
	
	/** 发卡柜台所在区域ID（渠道相关的区域） */
	private String channelRegionId;
	
	/** 发卡柜台所在渠道ID */
	private String channelId;
	
	/** 选择的发卡柜台条件是包含还是排除flag（1：包含，2：排除） **/
	private String exclusiveFlag;
	
	/** 发卡柜台选择模式（1：按区域，2：按渠道） **/
	private String modeFlag;
	
	/** 发卡柜台有效区分 **/
	private String couValidFlag;
	
	/** 发卡柜台所在区域ID(俱乐部) **/
	private String clubRegionId;
	
	/** 所属系统ID **/
	private String clubBelongId;
	
	/** 发卡柜台所在省ID(俱乐部) **/
	private String clubProvinceId;
	
	/** 发卡柜台所在城市ID(俱乐部) **/
	private String clubCityId;
	
	/** 发卡柜台所在部门ID(俱乐部) */
	private String clubMemCounterId;
	
	/** 发卡柜台所在区域ID（渠道相关的区域）(俱乐部) */
	private String clubChannelRegionId;
	
	/** 发卡柜台所在渠道ID(俱乐部) */
	private String clubChannelId;
	
	/** 选择的发卡柜台条件是包含还是排除flag（1：包含，2：排除） (俱乐部)**/
	private String clubExclusiveFlag;
	
	/** 发卡柜台选择模式（1：按区域，2：按渠道）(俱乐部) **/
	private String clubModeFlag;
	
	/** 发卡柜台有效区分 (俱乐部)**/
	private String clubCouValidFlag;
	
	/** 购买条件查询方式 **/
	private String isSaleFlag;
	
	/** 无购买时间模式 **/
	private String notSaleTimeMode;
	
	/** 无购买时间范围 **/
	private String notSaleTimeRange;
	
	/** 无购买时间范围(参考最近购买时间) **/
	private String notSaleTimeRangeLast;
	
	/** 无购买时间范围单位 **/
	private String notSaleTimeUnit;
	
	/** 无购买时间上限 **/
	private String notSaleTimeStart;
	
	/** 无购买时间下限 **/
	private String notSaleTimeEnd;
	
	/** 购买时间模式 **/
	private String saleTimeMode;
	
	/** 购买时间范围 **/
	private String saleTimeRange;
	
	/** 购买时间范围单位 **/
	private String saleTimeUnit;
	
	/** 购买时间上限 **/
	private String saleTimeStart;
	
	/** 购买时间下限 **/
	private String saleTimeEnd;
	
	/** 购买柜台所在部门ID **/
	private String saleCounterId;
	
	/** 购买次数上限 */
	private String saleCountStart;
	
	/** 购买次数下限 */
	private String saleCountEnd;
	
	/** 购买金额上限 */
	private String payAmountStart;
	
	/** 购买金额下限 */
	private String payAmountEnd;
	
	/** 购买支数上限 */
	private String payQuantityStart;
	
	/** 购买支数下限 */
	private String payQuantityEnd;
	
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
	
	/** 积分到期日上限 **/
	private String curDealDateStart;
	
	/** 积分到期日下限 **/
	private String curDealDateEnd;
	
	/** 推荐会员（1：推荐过别人的会员，2：被别人推荐的会员） **/
	private String referFlag;
	
	/** 被推荐会员卡号 **/
	private String referredMemCode;
	
	/** 推荐者卡号 **/
	private String referrerMemCode;
	
	/** 活动类型（0：会员活动，1：促销活动） **/
	private String campaignMode;
	
	/** 活动代码 **/
	private String campaignCode;
	
	/** 活动时间上限 **/
	private String participateTimeStart;
	
	/** 活动时间下限 **/
	private String participateTimeEnd;
	
	/** 活动柜台 **/
	private String campaignCounterId;
	
	/** 会员发卡柜台非空 **/
	private String couNotEmpty;
	
	/** 导出文件格式 **/
	private String exportFormat;
	
	/** 字符编码 **/
	private String charset;
	
	/** 员工ID **/
	private String employeeId;
	
	/** 员工ID(俱乐部) **/
	private String clubEmployeeId;
	
	/** 激活途径 **/
	private String activeChannel;
	
	/** 购买柜台所在区域ID **/
	private String saleRegionId;
	
	/** 所属系统ID **/
	private String saleBelongId;
	
	/** 购买柜台所在省ID **/
	private String saleProvinceId;
	
	/** 购买柜台所在城市ID **/
	private String saleCityId;
	
	/** 购买柜台所在部门ID */
	private String saleMemCounterId;
	
	/** 购买柜台所在区域ID（渠道相关的区域） */
	private String saleChannelRegionId;
	
	/** 购买柜台所在渠道ID */
	private String saleChannelId;
	
	/** 购买柜台选择模式（1：按区域，2：按渠道） **/
	private String saleModeFlag;
	
	/** 购买柜台有效区分 **/
	private String saleCouValidFlag;
	
	/** 已绑定微信 **/
	private String bindWeChat;
	
	/** 导出模式 **/
	private String exportMode;
	
	/** 入会途径 **/
	private String channelCode;
	
	/** 停用会员备注信息 **/
	private String remark;
	
	/** 微信绑定开始时间 **/
	private String wechatBindTimeStart;
	
	/** 微信绑定结束时间 **/
	private String wechatBindTimeEnd;
	
	/** 等级变化类型 **/
	private String levelChangeType;
	
	/** 等级调整日模式  **/
	private String levelAdjustDayFlag;
	
	/** 等级调整日期下限 **/
	private String levelAdjustDayStart;
	
	/** 等级调整日期上限 **/
	private String levelAdjustDayEnd;
	
	/** 等级调整日期相对范围 **/
	private String levelAdjustDayRange;
	
	/** 等级调整日期相对范围单位 **/
	private String levelAdjustDayUnit;
	
	/** 最近一次购买时间开始 **/
	private String lastSaleDateStart;
	
	/** 最近一次购买时间结束 **/
	private String lastSaleDateEnd;
	
	/** 会员俱乐部ID */
	private String memberClubId;
	
	private String clubMod;
	
	/** 俱乐部发卡日期开始时间 */
	private String clubJoinTimeStart;
	
	/** 俱乐部发卡日期结束时间 */
	private String clubJoinTimeEnd;
	
	/** 是否接收通知 */
	private String isReceiveMsg;
	
	/** 是否新会员 */
	private String isNewMember;
	
	/** 本年购买次数 */
	private String flagBuyCount;
	
	/** 是否敏感会员 */
	private String isActivityMember;
	
	/** 活动次数(敏感度) */
	private String actiCountStart;
	
	/** 活动次数(敏感度) */
	private String actiCountEnd;
	
	/** 最喜欢的活动类型  */
	private String favActiType;
	
	/** 购买间隔周期  */
	private String unBuyInterval;
	
	/** 大分类ID  */
	private int bigPropId;
	
	/** 中分类ID  */
	private int midPropId;
	
	/** 最多购买的产品系列大类 */
	private List<String> mostCateBClassId;
	
	/** 最多购买的产品系列中类 */
	private List<String> mostCateMClassId;
	
	
	/** 购买子品牌*/
	private String saleSubBrand;

	public String getSaleSubBrand() {
		return saleSubBrand;
	}

	public void setSaleSubBrand(String saleSubBrand) {
		this.saleSubBrand = saleSubBrand;
	}

	public List<String> getMostCateBClassId() {
		return mostCateBClassId;
	}

	public void setMostCateBClassId(List<String> mostCateBClassId) {
		this.mostCateBClassId = mostCateBClassId;
	}
	
	public int getBigPropId() {
		return bigPropId;
	}

	public void setBigPropId(int bigPropId) {
		this.bigPropId = bigPropId;
	}

	public int getMidPropId() {
		return midPropId;
	}

	public void setMidPropId(int midPropId) {
		this.midPropId = midPropId;
	}

	public String getPayQuantityStart() {
		return payQuantityStart;
	}

	public void setPayQuantityStart(String payQuantityStart) {
		this.payQuantityStart = payQuantityStart;
	}

	public String getPayQuantityEnd() {
		return payQuantityEnd;
	}

	public void setPayQuantityEnd(String payQuantityEnd) {
		this.payQuantityEnd = payQuantityEnd;
	}

	public List<String> getMostCateMClassId() {
		return mostCateMClassId;
	}

	public void setMostCateMClassId(List<String> mostCateMClassId) {
		this.mostCateMClassId = mostCateMClassId;
	}

	/** 最多购买的产品 */
	private List<String> mostPrtId;
	
//	/** 连带购买产品系列 */
//	private List<String> jointCateId;
//	
//	/** 连带购买产品系列大类 */
//	private List<String> jointCateBClassId;
//	
//	/** 连带购买产品系列中类 */
//	private List<String> jointCateMClassId;
	
//	public List<String> getJointCateBClassId() {
//		return jointCateBClassId;
//	}
//
//	public void setJointCateBClassId(List<String> jointCateBClassId) {
//		this.jointCateBClassId = jointCateBClassId;
//	}
//
//	public List<String> getJointCateMClassId() {
//		return jointCateMClassId;
//	}
//
//	public void setJointCateMClassId(List<String> jointCateMClassId) {
//		this.jointCateMClassId = jointCateMClassId;
//	}

//	/** 连带购买的产品 */
//	private List<String> jointPrtId;
	
	/** 客单价 */
	private String pctStart;
	
	/** 客单价*/
	private String pctEnd;
	
	/** 会员标签是否显示*/
	private String tagFlag;

	/** 多个入会时间段JSON **/
	private String joinDateRangeJson;

	/** 多个入会时间段List **/
	private List<Map<String, Object>> joinDateRangeList;

	/** 多个总积分JSON **/
	private String memPointRangeJson;

	/** 多个总积分List **/
	private List<Map<String, Object>> memPointRangeList;

	/** 多个可兑换积分JSON **/
	private String changablePointRangeJson;

	/** 多个可兑换积分List **/
	private List<Map<String, Object>> changablePointRangeList;

	/** 多个最近购买时间段JSON **/
	private String lastSaleTimeRangeJson;

	/** 多个最近购买时间段List **/
	private List<Map<String, Object>> lastSaleTimeRangeList;

	/** 多个首次购买时间段JSON **/
	private String firstSaleTimeRangeJson;

	/** 多个首次购买时间段List **/
	private List<Map<String, Object>> firstSaleTimeRangeList;

	private String notSaleDays;

	private String notSaleDaysRange;

	private String noSaleDaysMode;

	private String email;
	
	public String getTagFlag() {
		return tagFlag;
	}

	public void setTagFlag(String tagFlag) {
		this.tagFlag = tagFlag;
	}

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

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
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

	public String getBirthDayMode() {
		return birthDayMode;
	}

	public void setBirthDayMode(String birthDayMode) {
		this.birthDayMode = birthDayMode;
	}

	public String getBirthDayRange() {
		return birthDayRange;
	}

	public void setBirthDayRange(String birthDayRange) {
		this.birthDayRange = birthDayRange;
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

	public String getJoinDateFlag() {
		return joinDateFlag;
	}

	public void setJoinDateFlag(String joinDateFlag) {
		this.joinDateFlag = joinDateFlag;
	}

	public String getCurDayJoinDateFlag() {
		return curDayJoinDateFlag;
	}

	public void setCurDayJoinDateFlag(String curDayJoinDateFlag) {
		this.curDayJoinDateFlag = curDayJoinDateFlag;
	}

	public String getBirthDayDateStart() {
		return birthDayDateStart;
	}

	public void setBirthDayDateStart(String birthDayDateStart) {
		this.birthDayDateStart = birthDayDateStart;
	}

	public String getBirthDayDateEnd() {
		return birthDayDateEnd;
	}

	public void setBirthDayDateEnd(String birthDayDateEnd) {
		this.birthDayDateEnd = birthDayDateEnd;
	}

	public String getBirthDayPath() {
		return birthDayPath;
	}

	public void setBirthDayPath(String birthDayPath) {
		this.birthDayPath = birthDayPath;
	}

	public String getBirthDayUnit() {
		return birthDayUnit;
	}

	public void setBirthDayUnit(String birthDayUnit) {
		this.birthDayUnit = birthDayUnit;
	}

	public String getBirthDayMonthRangeStart() {
		return birthDayMonthRangeStart;
	}

	public void setBirthDayMonthRangeStart(String birthDayMonthRangeStart) {
		this.birthDayMonthRangeStart = birthDayMonthRangeStart;
	}

	public String getBirthDayMonthRangeEnd() {
		return birthDayMonthRangeEnd;
	}

	public void setBirthDayMonthRangeEnd(String birthDayMonthRangeEnd) {
		this.birthDayMonthRangeEnd = birthDayMonthRangeEnd;
	}

	public String getBirthDayDateRangeStart() {
		return birthDayDateRangeStart;
	}

	public void setBirthDayDateRangeStart(String birthDayDateRangeStart) {
		this.birthDayDateRangeStart = birthDayDateRangeStart;
	}

	public String getBirthDayDateRangeEnd() {
		return birthDayDateRangeEnd;
	}

	public void setBirthDayDateRangeEnd(String birthDayDateRangeEnd) {
		this.birthDayDateRangeEnd = birthDayDateRangeEnd;
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

	public String getJoinDateMode() {
		return joinDateMode;
	}

	public void setJoinDateMode(String joinDateMode) {
		this.joinDateMode = joinDateMode;
	}

	public String getJoinDateRange() {
		return joinDateRange;
	}

	public void setJoinDateRange(String joinDateRange) {
		this.joinDateRange = joinDateRange;
	}

	public String getJoinDateUnit() {
		return joinDateUnit;
	}

	public void setJoinDateUnit(String joinDateUnit) {
		this.joinDateUnit = joinDateUnit;
	}

	public String getJoinDateUnitFlag() {
		return joinDateUnitFlag;
	}

	public void setJoinDateUnitFlag(String joinDateUnitFlag) {
		this.joinDateUnitFlag = joinDateUnitFlag;
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

	public String getJoinDateSaleDateRel() {
		return joinDateSaleDateRel;
	}

	public void setJoinDateSaleDateRel(String joinDateSaleDateRel) {
		this.joinDateSaleDateRel = joinDateSaleDateRel;
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

	public String getChangablePointStart() {
		return changablePointStart;
	}

	public void setChangablePointStart(String changablePointStart) {
		this.changablePointStart = changablePointStart;
	}

	public String getChangablePointEnd() {
		return changablePointEnd;
	}

	public void setChangablePointEnd(String changablePointEnd) {
		this.changablePointEnd = changablePointEnd;
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

	public String getMemCounterId() {
		return memCounterId;
	}

	public void setMemCounterId(String memCounterId) {
		this.memCounterId = memCounterId;
	}

	public String getChannelRegionId() {
		return channelRegionId;
	}

	public void setChannelRegionId(String channelRegionId) {
		this.channelRegionId = channelRegionId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getExclusiveFlag() {
		return exclusiveFlag;
	}

	public void setExclusiveFlag(String exclusiveFlag) {
		this.exclusiveFlag = exclusiveFlag;
	}

	public String getModeFlag() {
		return modeFlag;
	}

	public void setModeFlag(String modeFlag) {
		this.modeFlag = modeFlag;
	}

	public String getCouValidFlag() {
		return couValidFlag;
	}

	public void setCouValidFlag(String couValidFlag) {
		this.couValidFlag = couValidFlag;
	}

	public String getIsSaleFlag() {
		return isSaleFlag;
	}

	public void setIsSaleFlag(String isSaleFlag) {
		this.isSaleFlag = isSaleFlag;
	}

	public String getNotSaleTimeMode() {
		return notSaleTimeMode;
	}

	public void setNotSaleTimeMode(String notSaleTimeMode) {
		this.notSaleTimeMode = notSaleTimeMode;
	}

	public String getNotSaleTimeRange() {
		return notSaleTimeRange;
	}

	public void setNotSaleTimeRange(String notSaleTimeRange) {
		this.notSaleTimeRange = notSaleTimeRange;
	}
	
	public String getNotSaleTimeRangeLast() {
		return notSaleTimeRangeLast;
	}

	public void setNotSaleTimeRangeLast(String notSaleTimeRangeLast) {
		this.notSaleTimeRangeLast = notSaleTimeRangeLast;
	}
	
	public String getNotSaleTimeUnit() {
		return notSaleTimeUnit;
	}

	public void setNotSaleTimeUnit(String notSaleTimeUnit) {
		this.notSaleTimeUnit = notSaleTimeUnit;
	}

	public String getNotSaleTimeStart() {
		return notSaleTimeStart;
	}

	public void setNotSaleTimeStart(String notSaleTimeStart) {
		this.notSaleTimeStart = notSaleTimeStart;
	}

	public String getNotSaleTimeEnd() {
		return notSaleTimeEnd;
	}

	public void setNotSaleTimeEnd(String notSaleTimeEnd) {
		this.notSaleTimeEnd = notSaleTimeEnd;
	}

	public String getSaleTimeMode() {
		return saleTimeMode;
	}

	public void setSaleTimeMode(String saleTimeMode) {
		this.saleTimeMode = saleTimeMode;
	}

	public String getSaleTimeRange() {
		return saleTimeRange;
	}

	public void setSaleTimeRange(String saleTimeRange) {
		this.saleTimeRange = saleTimeRange;
	}

	public String getSaleTimeUnit() {
		return saleTimeUnit;
	}

	public void setSaleTimeUnit(String saleTimeUnit) {
		this.saleTimeUnit = saleTimeUnit;
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

	public String getSaleCounterId() {
		return saleCounterId;
	}

	public void setSaleCounterId(String saleCounterId) {
		this.saleCounterId = saleCounterId;
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

	public List<ExtendPropertyDto> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<ExtendPropertyDto> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public String getCurDealDateStart() {
		return curDealDateStart;
	}

	public void setCurDealDateStart(String curDealDateStart) {
		this.curDealDateStart = curDealDateStart;
	}

	public String getCurDealDateEnd() {
		return curDealDateEnd;
	}

	public void setCurDealDateEnd(String curDealDateEnd) {
		this.curDealDateEnd = curDealDateEnd;
	}

	public String getReferFlag() {
		return referFlag;
	}

	public void setReferFlag(String referFlag) {
		this.referFlag = referFlag;
	}

	public String getReferredMemCode() {
		return referredMemCode;
	}

	public void setReferredMemCode(String referredMemCode) {
		this.referredMemCode = referredMemCode;
	}

	public String getReferrerMemCode() {
		return referrerMemCode;
	}

	public void setReferrerMemCode(String referrerMemCode) {
		this.referrerMemCode = referrerMemCode;
	}

	public String getCampaignMode() {
		return campaignMode;
	}

	public void setCampaignMode(String campaignMode) {
		this.campaignMode = campaignMode;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getParticipateTimeStart() {
		return participateTimeStart;
	}

	public void setParticipateTimeStart(String participateTimeStart) {
		this.participateTimeStart = participateTimeStart;
	}

	public String getParticipateTimeEnd() {
		return participateTimeEnd;
	}

	public void setParticipateTimeEnd(String participateTimeEnd) {
		this.participateTimeEnd = participateTimeEnd;
	}

	public String getCampaignCounterId() {
		return campaignCounterId;
	}

	public void setCampaignCounterId(String campaignCounterId) {
		this.campaignCounterId = campaignCounterId;
	}

	public String getCouNotEmpty() {
		return couNotEmpty;
	}

	public void setCouNotEmpty(String couNotEmpty) {
		this.couNotEmpty = couNotEmpty;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getActiveChannel() {
		return activeChannel;
	}

	public void setActiveChannel(String activeChannel) {
		this.activeChannel = activeChannel;
	}

	public String getSaleRegionId() {
		return saleRegionId;
	}

	public void setSaleRegionId(String saleRegionId) {
		this.saleRegionId = saleRegionId;
	}

	public String getSaleProvinceId() {
		return saleProvinceId;
	}

	public void setSaleProvinceId(String saleProvinceId) {
		this.saleProvinceId = saleProvinceId;
	}

	public String getSaleCityId() {
		return saleCityId;
	}

	public void setSaleCityId(String saleCityId) {
		this.saleCityId = saleCityId;
	}

	public String getSaleMemCounterId() {
		return saleMemCounterId;
	}

	public void setSaleMemCounterId(String saleMemCounterId) {
		this.saleMemCounterId = saleMemCounterId;
	}

	public String getSaleChannelRegionId() {
		return saleChannelRegionId;
	}

	public void setSaleChannelRegionId(String saleChannelRegionId) {
		this.saleChannelRegionId = saleChannelRegionId;
	}

	public String getSaleChannelId() {
		return saleChannelId;
	}

	public void setSaleChannelId(String saleChannelId) {
		this.saleChannelId = saleChannelId;
	}

	public String getSaleModeFlag() {
		return saleModeFlag;
	}

	public void setSaleModeFlag(String saleModeFlag) {
		this.saleModeFlag = saleModeFlag;
	}

	public String getSaleCouValidFlag() {
		return saleCouValidFlag;
	}

	public void setSaleCouValidFlag(String saleCouValidFlag) {
		this.saleCouValidFlag = saleCouValidFlag;
	}

	public String getBindWeChat() {
		return bindWeChat;
	}

	public void setBindWeChat(String bindWeChat) {
		this.bindWeChat = bindWeChat;
	}

	public String getExportMode() {
		return exportMode;
	}

	public void setExportMode(String exportMode) {
		this.exportMode = exportMode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWechatBindTimeStart() {
		return wechatBindTimeStart;
	}

	public void setWechatBindTimeStart(String wechatBindTimeStart) {
		this.wechatBindTimeStart = wechatBindTimeStart;
	}

	public String getWechatBindTimeEnd() {
		return wechatBindTimeEnd;
	}

	public void setWechatBindTimeEnd(String wechatBindTimeEnd) {
		this.wechatBindTimeEnd = wechatBindTimeEnd;
	}

	public String getLevelChangeType() {
		return levelChangeType;
	}

	public void setLevelChangeType(String levelChangeType) {
		this.levelChangeType = levelChangeType;
	}

	public String getLevelAdjustDayFlag() {
		return levelAdjustDayFlag;
	}

	public void setLevelAdjustDayFlag(String levelAdjustDayFlag) {
		this.levelAdjustDayFlag = levelAdjustDayFlag;
	}

	public String getLevelAdjustDayStart() {
		return levelAdjustDayStart;
	}

	public void setLevelAdjustDayStart(String levelAdjustDayStart) {
		this.levelAdjustDayStart = levelAdjustDayStart;
	}

	public String getLevelAdjustDayEnd() {
		return levelAdjustDayEnd;
	}

	public void setLevelAdjustDayEnd(String levelAdjustDayEnd) {
		this.levelAdjustDayEnd = levelAdjustDayEnd;
	}

	public String getLevelAdjustDayRange() {
		return levelAdjustDayRange;
	}

	public void setLevelAdjustDayRange(String levelAdjustDayRange) {
		this.levelAdjustDayRange = levelAdjustDayRange;
	}

	public String getLevelAdjustDayUnit() {
		return levelAdjustDayUnit;
	}

	public void setLevelAdjustDayUnit(String levelAdjustDayUnit) {
		this.levelAdjustDayUnit = levelAdjustDayUnit;
	}

	public String getLastSaleDateStart() {
		return lastSaleDateStart;
	}

	public void setLastSaleDateStart(String lastSaleDateStart) {
		this.lastSaleDateStart = lastSaleDateStart;
	}

	public String getLastSaleDateEnd() {
		return lastSaleDateEnd;
	}

	public void setLastSaleDateEnd(String lastSaleDateEnd) {
		this.lastSaleDateEnd = lastSaleDateEnd;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTencentQQ() {
		return tencentQQ;
	}

	public void setTencentQQ(String tencentQQ) {
		this.tencentQQ = tencentQQ;
	}

	public String getCreditRating() {
		return creditRating;
	}

	public void setCreditRating(String creditRating) {
		this.creditRating = creditRating;
	}

	public String getClubRegionId() {
		return clubRegionId;
	}

	public void setClubRegionId(String clubRegionId) {
		this.clubRegionId = clubRegionId;
	}

	public String getClubProvinceId() {
		return clubProvinceId;
	}

	public void setClubProvinceId(String clubProvinceId) {
		this.clubProvinceId = clubProvinceId;
	}

	public String getClubCityId() {
		return clubCityId;
	}

	public void setClubCityId(String clubCityId) {
		this.clubCityId = clubCityId;
	}

	public String getClubMemCounterId() {
		return clubMemCounterId;
	}

	public void setClubMemCounterId(String clubMemCounterId) {
		this.clubMemCounterId = clubMemCounterId;
	}

	public String getClubChannelRegionId() {
		return clubChannelRegionId;
	}

	public void setClubChannelRegionId(String clubChannelRegionId) {
		this.clubChannelRegionId = clubChannelRegionId;
	}

	public String getClubChannelId() {
		return clubChannelId;
	}

	public void setClubChannelId(String clubChannelId) {
		this.clubChannelId = clubChannelId;
	}

	public String getClubExclusiveFlag() {
		return clubExclusiveFlag;
	}

	public void setClubExclusiveFlag(String clubExclusiveFlag) {
		this.clubExclusiveFlag = clubExclusiveFlag;
	}

	public String getClubModeFlag() {
		return clubModeFlag;
	}

	public void setClubModeFlag(String clubModeFlag) {
		this.clubModeFlag = clubModeFlag;
	}

	public String getClubCouValidFlag() {
		return clubCouValidFlag;
	}

	public void setClubCouValidFlag(String clubCouValidFlag) {
		this.clubCouValidFlag = clubCouValidFlag;
	}

	public String getClubEmployeeId() {
		return clubEmployeeId;
	}

	public void setClubEmployeeId(String clubEmployeeId) {
		this.clubEmployeeId = clubEmployeeId;
	}

	public String getClubJoinTimeStart() {
		return clubJoinTimeStart;
	}

	public void setClubJoinTimeStart(String clubJoinTimeStart) {
		this.clubJoinTimeStart = clubJoinTimeStart;
	}

	public String getClubJoinTimeEnd() {
		return clubJoinTimeEnd;
	}

	public void setClubJoinTimeEnd(String clubJoinTimeEnd) {
		this.clubJoinTimeEnd = clubJoinTimeEnd;
	}

	public String getIsReceiveMsg() {
		return isReceiveMsg;
	}

	public void setIsReceiveMsg(String isReceiveMsg) {
		this.isReceiveMsg = isReceiveMsg;
	}

	public String getBelongId() {
		return belongId;
	}

	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}

	public String getClubBelongId() {
		return clubBelongId;
	}

	public void setClubBelongId(String clubBelongId) {
		this.clubBelongId = clubBelongId;
	}

	public String getSaleBelongId() {
		return saleBelongId;
	}

	public void setSaleBelongId(String saleBelongId) {
		this.saleBelongId = saleBelongId;
	}

	public String getIsNewMember() {
		return isNewMember;
	}

	public void setIsNewMember(String isNewMember) {
		this.isNewMember = isNewMember;
	}

	public String getFlagBuyCount() {
		return flagBuyCount;
	}

	public void setFlagBuyCount(String flagBuyCount) {
		this.flagBuyCount = flagBuyCount;
	}

	public String getIsActivityMember() {
		return isActivityMember;
	}

	public void setIsActivityMember(String isActivityMember) {
		this.isActivityMember = isActivityMember;
	}

	public String getActiCountStart() {
		return actiCountStart;
	}

	public void setActiCountStart(String actiCountStart) {
		this.actiCountStart = actiCountStart;
	}

	public String getActiCountEnd() {
		return actiCountEnd;
	}

	public void setActiCountEnd(String actiCountEnd) {
		this.actiCountEnd = actiCountEnd;
	}

	public String getFavActiType() {
		return favActiType;
	}

	public void setFavActiType(String favActiType) {
		this.favActiType = favActiType;
	}

	public String getUnBuyInterval() {
		return unBuyInterval;
	}

	public void setUnBuyInterval(String unBuyInterval) {
		this.unBuyInterval = unBuyInterval;
	}

	public List<String> getMostPrtId() {
		return mostPrtId;
	}

	public void setMostPrtId(List<String> mostPrtId) {
		this.mostPrtId = mostPrtId;
	}

//	public List<String> getJointCateId() {
//		return jointCateId;
//	}
//
//	public void setJointCateId(List<String> jointCateId) {
//		this.jointCateId = jointCateId;
//	}
//
//	public List<String> getJointPrtId() {
//		return jointPrtId;
//	}
//
//	public void setJointPrtId(List<String> jointPrtId) {
//		this.jointPrtId = jointPrtId;
//	}

	public String getPctStart() {
		return pctStart;
	}

	public void setPctStart(String pctStart) {
		this.pctStart = pctStart;
	}

	public String getPctEnd() {
		return pctEnd;
	}

	public void setPctEnd(String pctEnd) {
		this.pctEnd = pctEnd;
	}

	public String getJoinDateRangeJson() {
		return joinDateRangeJson;
	}

	public void setJoinDateRangeJson(String joinDateRangeJson) {
		this.joinDateRangeJson = joinDateRangeJson;
	}

	public List<Map<String, Object>> getJoinDateRangeList() {
		return joinDateRangeList;
	}

	public void setJoinDateRangeList(List<Map<String, Object>> joinDateRangeList) {
		this.joinDateRangeList = joinDateRangeList;
	}

	public String getMemPointRangeJson() {
		return memPointRangeJson;
	}

	public void setMemPointRangeJson(String memPointRangeJson) {
		this.memPointRangeJson = memPointRangeJson;
	}

	public List<Map<String, Object>> getMemPointRangeList() {
		return memPointRangeList;
	}

	public void setMemPointRangeList(List<Map<String, Object>> memPointRangeList) {
		this.memPointRangeList = memPointRangeList;
	}

	public String getChangablePointRangeJson() {
		return changablePointRangeJson;
	}

	public void setChangablePointRangeJson(String changablePointRangeJson) {
		this.changablePointRangeJson = changablePointRangeJson;
	}

	public List<Map<String, Object>> getChangablePointRangeList() {
		return changablePointRangeList;
	}

	public void setChangablePointRangeList(List<Map<String, Object>> changablePointRangeList) {
		this.changablePointRangeList = changablePointRangeList;
	}

	public String getLastSaleTimeRangeJson() {
		return lastSaleTimeRangeJson;
	}

	public void setLastSaleTimeRangeJson(String lastSaleTimeRangeJson) {
		this.lastSaleTimeRangeJson = lastSaleTimeRangeJson;
	}

	public List<Map<String, Object>> getLastSaleTimeRangeList() {
		return lastSaleTimeRangeList;
	}

	public void setLastSaleTimeRangeList(List<Map<String, Object>> lastSaleTimeRangeList) {
		this.lastSaleTimeRangeList = lastSaleTimeRangeList;
	}

	public String getFirstSaleTimeRangeJson() {
		return firstSaleTimeRangeJson;
	}

	public void setFirstSaleTimeRangeJson(String firstSaleTimeRangeJson) {
		this.firstSaleTimeRangeJson = firstSaleTimeRangeJson;
	}

	public List<Map<String, Object>> getFirstSaleTimeRangeList() {
		return firstSaleTimeRangeList;
	}

	public void setFirstSaleTimeRangeList(List<Map<String, Object>> firstSaleTimeRangeList) {
		this.firstSaleTimeRangeList = firstSaleTimeRangeList;
	}

	public String getNotSaleDays() {
		return notSaleDays;
	}

	public void setNotSaleDays(String notSaleDays) {
		this.notSaleDays = notSaleDays;
	}

	public String getNotSaleDaysRange() {
		return notSaleDaysRange;
	}

	public void setNotSaleDaysRange(String notSaleDaysRange) {
		this.notSaleDaysRange = notSaleDaysRange;
	}

	public String getNoSaleDaysMode() {
		return noSaleDaysMode;
	}

	public void setNoSaleDaysMode(String noSaleDaysMode) {
		this.noSaleDaysMode = noSaleDaysMode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
