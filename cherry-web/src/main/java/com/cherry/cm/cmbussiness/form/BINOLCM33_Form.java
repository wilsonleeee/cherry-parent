/*	
 * @(#)BINOLCM33_Form.java     1.0 2012/01/07		
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
package com.cherry.cm.cmbussiness.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员检索画面共通Form
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLCM33_Form extends DataTable_BaseForm {
	
	/** 组织ID **/
	private String organizationInfoId;
	
	/** 品牌ID **/
	private String brandInfoId;
	
	/** 组织代码 **/
	private String orgCode;
	
	/** 品牌代码 **/
	private String brandCode;
	
	/** 搜索条件ID **/
	private String searchRequestId;
	
	/** 查询搜索条件的检索条件 **/
	private String searchKey;
	
	/** 搜索条件名称 **/
	private String requestName;
	
	/** 搜索条件描述 **/
	private String description;
	
	/** 搜索条件内容 **/
	private String reqContent;
	
	/** 会员ID **/
	private String memberInfoId;
	
	/** 会员地址非空 **/
	private String addrNotEmpty;
	
	/** 会员电话非空 **/
	private String telNotEmpty;
	
	/** 会员手机合法 **/
	private String telCheck;
	
	/** 会员Email非空 **/
	private String emailNotEmpty;
	
	/** 测试区分 */
	private String testType;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 昵称 */
	private String nickname;
	
	/** QQ*/
	private String tencentQQ;
	
	/** 会员信用等级 */
	private String creditRating;
	
	/** 会员类型 **/
	private List<String> memType;
	
	/** 会员等级 **/
	private List<String> memLevel;
	
	/** 会员年龄上限 **/
	private String ageStart;
	
	/** 会员年龄下限 **/
	private String ageEnd;
	
	/** 会员生日模式（0：当月生日，1：生日当天，2：相对生日，9：指定生日，3：生日范围） */
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
	
	/** 第二次选择时的生日模式（0：当月生日，1：生日当天） */
	private String birthDayDateMode;
	
	/** 第二次选择时的生日（日上限） */
	private String birthDayDateMoreStart;
	
	/** 第二次选择时的生日（日下限） */
	private String birthDayDateMoreEnd;
	
	/** 会员性别 **/
	private List<String> mebSex;
	
//	/** 会员积分上限 **/
//	private String memberPointStart;
//
//	/** 会员积分下限 **/
//	private String memberPointEnd;
//
//	/** 可兑换积分上限 **/
//	private String changablePointStart;
//
//	/** 可兑换积分下限 **/
//	private String changablePointEnd;
//
	/** 入会时间模式 **/
	private String joinDateMode;
	
	/** 入会时间范围 **/
	private String joinDateRange;
	
	/** 入会时间范围单位 **/
	private String joinDateUnit;
	
	/** 入会时间范围单位（1：一段时间内，2：满一段时间） **/
	private String joinDateUnitFlag;
	
//	/** 入会时间上限 **/
//	private String joinDateStart;
//
//	/** 入会时间下限 **/
//	private String joinDateEnd;
	
	/** 入会时间和购买时间条件之间的关系（1：AND，2：OR） **/
	private String joinDateSaleDateRel;
	
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
	
	/** 发卡柜台所在区域名称（画面显示用） **/
	private String regionName;
	
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
	
	/** 购买柜台CODE **/
	private String saleCounterCode;
	
	/** 购买柜台名称 **/
	private String saleCounterName;
	
	/** 购买次数上限 **/
	private String saleCountStart;
	
	/** 购买次数下限 **/
	private String saleCountEnd;
	
	/** 购买支数上限 */
	private String payQuantityStart;
	
	/** 购买支数下限 */
	private String payQuantityEnd;
	
	/** 购买金额上限 **/
	private String payAmountStart;
	
	/** 购买金额下限 **/
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
	
	/** 活动柜台名称 **/
	private String campaignCounterName;
	
	/** 活动名称 **/
	private String campaignName;
	
	/** 活动状态 **/
	private List<String> campaignState;
	
	/** 会员发卡柜台非空 **/
	private String couNotEmpty;
	
	/** 为支持一个画面显示多个检索条件画面而设置的索引序号 **/
	private String index;
	
	/** 禁止修改条件 **/
	private String disableCondition;
	
	/** 禁止修改条件map **/
	private Map disableConditionMap;
	
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
	
	/** 购买柜台所在区域名称（画面显示用） **/
	private String saleRegionName;
	
	/** 几天内未回访 **/
	private String notSaleDays;
	
	private String notSaleDaysRange;
	
	private String noSaleDaysMode;
	
//	private String firstStartDay;
//
//	private String firstEndDay;
	
	private String privilegeFlag;
	
	private String userId;
	
	/** 会员俱乐部ID */
	private String memberClubId;
	
	private String clubMod;
	
	/** 俱乐部发卡日期开始时间 */
	private String clubJoinTimeStart;
	
	/** 俱乐部发卡日期结束时间 */
	private String clubJoinTimeEnd;
	
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
	
	/** 俱乐部发卡柜台所在区域名称（画面显示用） **/
	private String clubRegionName;
	
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
	
	/** 最多购买的产品中列 */
	private List<String> mostCateMClassId;
	
	/** 最多购买的产品大列 */
	private List<String> mostCateBClassId;
	
	public List<String> getMostCateBClassId() {
		return mostCateBClassId;
	}

	public void setMostCateBClassId(List<String> mostCateBClassId) {
		this.mostCateBClassId = mostCateBClassId;
	}

	/** 最多购买的产品 */
	private List<String> mostPrtId;
	
	public List<String> getMostCateMClassId() {
		return mostCateMClassId;
	}

	public void setMostCateMClassId(List<String> mostCateMClassId) {
		this.mostCateMClassId = mostCateMClassId;
	}

	/** 客单价 */
	private String pctStart;
	
	/** 客单价*/
	private String pctEnd;
	
	/** 会员标签是否显示*/
	private String tagFlag;
	
//	/** 最近一次购买时间开始 **/
//	private String lastSaleDateStart;
//
//	/** 最近一次购买时间结束 **/
//	private String lastSaleDateEnd;

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

	/** 会员邮箱 */
	private String email;

	/** 会员手机 */
	private String mobilePhone;

	/** 会员姓名 */
	private String name;
	
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

	public String getClubRegionName() {
		return clubRegionName;
	}

	public void setClubRegionName(String clubRegionName) {
		this.clubRegionName = clubRegionName;
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

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSearchRequestId() {
		return searchRequestId;
	}

	public void setSearchRequestId(String searchRequestId) {
		this.searchRequestId = searchRequestId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReqContent() {
		return reqContent;
	}

	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
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

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
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

	public List<String> getMebSex() {
		return mebSex;
	}

	public void setMebSex(List<String> mebSex) {
		this.mebSex = mebSex;
	}

//	public String getMemberPointStart() {
//		return memberPointStart;
//	}
//
//	public void setMemberPointStart(String memberPointStart) {
//		this.memberPointStart = memberPointStart;
//	}
//
//	public String getMemberPointEnd() {
//		return memberPointEnd;
//	}
//
//	public void setMemberPointEnd(String memberPointEnd) {
//		this.memberPointEnd = memberPointEnd;
//	}
//
//	public String getChangablePointStart() {
//		return changablePointStart;
//	}
//
//	public void setChangablePointStart(String changablePointStart) {
//		this.changablePointStart = changablePointStart;
//	}
//
//	public String getChangablePointEnd() {
//		return changablePointEnd;
//	}
//
//	public void setChangablePointEnd(String changablePointEnd) {
//		this.changablePointEnd = changablePointEnd;
//	}

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

//	public String getJoinDateStart() {
//		return joinDateStart;
//	}
//
//	public void setJoinDateStart(String joinDateStart) {
//		this.joinDateStart = joinDateStart;
//	}
//
//	public String getJoinDateEnd() {
//		return joinDateEnd;
//	}
//
//	public void setJoinDateEnd(String joinDateEnd) {
//		this.joinDateEnd = joinDateEnd;
//	}
	
	public String getJoinDateSaleDateRel() {
		return joinDateSaleDateRel;
	}

	public void setJoinDateSaleDateRel(String joinDateSaleDateRel) {
		this.joinDateSaleDateRel = joinDateSaleDateRel;
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

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
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

	public String getSaleCounterCode() {
		return saleCounterCode;
	}

	public void setSaleCounterCode(String saleCounterCode) {
		this.saleCounterCode = saleCounterCode;
	}

	public String getSaleCounterName() {
		return saleCounterName;
	}

	public void setSaleCounterName(String saleCounterName) {
		this.saleCounterName = saleCounterName;
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

	public String getCampaignCounterName() {
		return campaignCounterName;
	}

	public void setCampaignCounterName(String campaignCounterName) {
		this.campaignCounterName = campaignCounterName;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public List<String> getCampaignState() {
		return campaignState;
	}

	public void setCampaignState(List<String> campaignState) {
		this.campaignState = campaignState;
	}

	public String getCouNotEmpty() {
		return couNotEmpty;
	}

	public void setCouNotEmpty(String couNotEmpty) {
		this.couNotEmpty = couNotEmpty;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getDisableCondition() {
		return disableCondition;
	}

	public void setDisableCondition(String disableCondition) {
		this.disableCondition = disableCondition;
	}

	public Map getDisableConditionMap() {
		return disableConditionMap;
	}

	public void setDisableConditionMap(Map disableConditionMap) {
		this.disableConditionMap = disableConditionMap;
	}

	public String getBirthDayDateMode() {
		return birthDayDateMode;
	}

	public void setBirthDayDateMode(String birthDayDateMode) {
		this.birthDayDateMode = birthDayDateMode;
	}

	public String getBirthDayDateMoreStart() {
		return birthDayDateMoreStart;
	}

	public void setBirthDayDateMoreStart(String birthDayDateMoreStart) {
		this.birthDayDateMoreStart = birthDayDateMoreStart;
	}

	public String getBirthDayDateMoreEnd() {
		return birthDayDateMoreEnd;
	}

	public void setBirthDayDateMoreEnd(String birthDayDateMoreEnd) {
		this.birthDayDateMoreEnd = birthDayDateMoreEnd;
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

	public String getSaleRegionName() {
		return saleRegionName;
	}

	public void setSaleRegionName(String saleRegionName) {
		this.saleRegionName = saleRegionName;
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
	
	public String getNotSaleDays() {
		return notSaleDays;
	}

	public void setNotSaleDays(String notSaleDays) {
		this.notSaleDays = notSaleDays;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBelongId() {
		return belongId;
	}

	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}

	public String getSaleBelongId() {
		return saleBelongId;
	}

	public void setSaleBelongId(String saleBelongId) {
		this.saleBelongId = saleBelongId;
	}

	public String getClubBelongId() {
		return clubBelongId;
	}

	public void setClubBelongId(String clubBelongId) {
		this.clubBelongId = clubBelongId;
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

	public String getTagFlag() {
		return tagFlag;
	}

	public void setTagFlag(String tagFlag) {
		this.tagFlag = tagFlag;
	}

//	public String getLastSaleDateStart() {
//		return lastSaleDateStart;
//	}
//
//	public void setLastSaleDateStart(String lastSaleDateStart) {
//		this.lastSaleDateStart = lastSaleDateStart;
//	}
//
//	public String getLastSaleDateEnd() {
//		return lastSaleDateEnd;
//	}
//
//	public void setLastSaleDateEnd(String lastSaleDateEnd) {
//		this.lastSaleDateEnd = lastSaleDateEnd;
//	}

	public String getNoSaleDaysMode() {
		return noSaleDaysMode;
	}

	public void setNoSaleDaysMode(String noSaleDaysMode) {
		this.noSaleDaysMode = noSaleDaysMode;
	}

//	public String getFirstStartDay() {
//		return firstStartDay;
//	}
//
//	public void setFirstStartDay(String firstStartDay) {
//		this.firstStartDay = firstStartDay;
//	}
//
//	public String getFirstEndDay() {
//		return firstEndDay;
//	}
//
//	public void setFirstEndDay(String firstEndDay) {
//		this.firstEndDay = firstEndDay;
//	}

	public String getNotSaleDaysRange() {
		return notSaleDaysRange;
	}

	public void setNotSaleDaysRange(String notSaleDaysRange) {
		this.notSaleDaysRange = notSaleDaysRange;
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

	public List<Map<String, Object>> getFirstSaleTimeRangeList() {
		return firstSaleTimeRangeList;
	}

	public void setFirstSaleTimeRangeList(List<Map<String, Object>> firstSaleTimeRangeList) {
		this.firstSaleTimeRangeList = firstSaleTimeRangeList;
	}

	public String getFirstSaleTimeRangeJson() {
		return firstSaleTimeRangeJson;
	}

	public void setFirstSaleTimeRangeJson(String firstSaleTimeRangeJson) {
		this.firstSaleTimeRangeJson = firstSaleTimeRangeJson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
