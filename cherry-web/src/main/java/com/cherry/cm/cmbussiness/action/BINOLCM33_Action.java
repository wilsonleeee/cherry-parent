/*	
 * @(#)BINOLCM33_Action.java     1.0 2012/01/07		
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
package com.cherry.cm.cmbussiness.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM39_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM33_Form;
import com.cherry.cm.cmbussiness.service.BINOLCM33_Service;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员检索画面共通Action
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLCM33_Action extends BaseAction implements ModelDriven<BINOLCM33_Form> {

	private static final long serialVersionUID = 163661426322953623L;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 会员检索画面共通Service **/
	@Resource
	private BINOLCM33_Service binOLCM33_Service;
	
	/** 会员检索条件转换共通BL **/
	@Resource
	private BINOLCM39_BL binOLCM39_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	
	/**
	 * 
	 * 会员检索画面初期化
	 * 
	 */
	public String memSearchInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 俱乐部模式
		String clubMod = form.getClubMod();
		if (null == clubMod) {
			clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			form.setClubMod(clubMod);
		}
		String tagFlag = binOLCM14_BL.getConfigValue("1339", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		form.setTagFlag(tagFlag);
		if ("1".equals(tagFlag)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得产品分类List
			List<Map<String, Object>> cateList = binOLPTJCS03_IF.getCategoryList(map);
			if (null != cateList) {
				for (Map<String, Object> cateMap : cateList) {
					// 大分类
					if ("1".equals(cateMap.get("teminalFlag"))) {
						form.setBigPropId(Integer.parseInt(String.valueOf(cateMap.get("propId"))));
						// 中分类
					} else if ("3".equals(cateMap.get("teminalFlag"))) {
						form.setMidPropId(Integer.parseInt(String.valueOf(cateMap.get("propId"))));
					}
				}
			}
		}
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			clubList = new ArrayList<Map<String, Object>>();
			Map<String, Object> optionMap = new HashMap<String, Object>();
			optionMap.put("memberClubId", "");
			optionMap.put("clubName", getText("PMB01006"));
			clubList.add(optionMap);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				clubList.addAll(clubInfoList);
			}
		} else {
			// 查询会员等级信息List
			memLevelList = binOLCM33_Service.getMemberLevelInfoList(map);
		}
		// 月信息初始化处理
		monthList = new ArrayList<Integer>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(i);
		}
		form.setOrganizationInfoId(String.valueOf(userInfo.getBIN_OrganizationInfoID()));
		// 设置品牌ID
		form.setBrandInfoId(String.valueOf(userInfo.getBIN_BrandInfoID()));
		// 设置组织代码
		form.setOrgCode(userInfo.getOrganizationInfoCode());
		// 设置品牌代码
		form.setBrandCode(userInfo.getBrandCode());
		// 为支持一个画面显示多个检索条件画面而设置的索引序号，默认设置成0
		if(form.getIndex() == null || "".equals(form.getIndex())) {
			form.setIndex("0");
		}
		// 会员检索画面存在初始值的场合
		if(form.getReqContent() != null && !"".equals(form.getReqContent())) {
			Map<String, Object> reqContentMap = ConvertUtil.json2Map(form.getReqContent());
			String addrNotEmpty = (String)reqContentMap.get("addrNotEmpty");
			if(addrNotEmpty != null && !"".equals(addrNotEmpty)) {
				form.setAddrNotEmpty(addrNotEmpty);
			}
			String telNotEmpty = (String)reqContentMap.get("telNotEmpty");
			if(telNotEmpty != null && !"".equals(telNotEmpty)) {
				form.setTelNotEmpty(telNotEmpty);
			}
			String telCheck = (String)reqContentMap.get("telCheck");
			if(telCheck != null && !"".equals(telCheck)) {
				form.setTelCheck(telCheck);
			}
			String emailNotEmpty = (String)reqContentMap.get("emailNotEmpty");
			if(emailNotEmpty != null && !"".equals(emailNotEmpty)) {
				form.setEmailNotEmpty(emailNotEmpty);
			}
			String testType = (String)reqContentMap.get("testType");
			if(testType != null && !"".equals(testType)) {
				form.setTestType(testType);
			}
			String memCode = (String)reqContentMap.get("memCode");
			if(memCode != null && !"".equals(memCode)) {
				form.setMemCode(memCode);
			}
			String name = (String)reqContentMap.get("name");
			if(name != null && !"".equals(name)) {
				form.setName(name);
			}
			String mobilePhone = (String)reqContentMap.get("mobilePhone");
			if(mobilePhone != null && !"".equals(mobilePhone)) {
				form.setMobilePhone(mobilePhone);
			}
			String email = (String)reqContentMap.get("email");
			if(email != null && !"".equals(email)) {
				form.setEmail(email);
			}
			Object memType = (Object)reqContentMap.get("memType");
			if(memType != null) {
				List<String> _memType = new ArrayList<String>();
				if(memType instanceof String) {
					if(!"".equals(memType.toString())) {
						_memType.add(memType.toString());
					}
				} else {
					_memType = (List)memType;
				}
				if(!_memType.isEmpty()) {
					form.setMemType(_memType);
				}
			}
			String memberClubId = (String) reqContentMap.get("memberClubId");
			if (null != memberClubId && !"".equals(memberClubId)) {
				form.setMemberClubId(memberClubId);
				map.put("memberClubId", memberClubId);
				// 查询会员等级信息List(俱乐部)
				memLevelList = binOLCM33_Service.getMemberLevelInfoList(map);
			}
			Object memLevel = (Object)reqContentMap.get("memLevel");
			if(memLevel != null) {
				List<String> _memLevel = new ArrayList<String>();
				if(memLevel instanceof String) {
					if(!"".equals(memLevel.toString())) {
						_memLevel.add(memLevel.toString());
					}
				} else {
					_memLevel = (List)memLevel;
				}
				if(!_memLevel.isEmpty()) {
					form.setMemLevel(_memLevel);
				}
			}
			String ageStart = (String)reqContentMap.get("ageStart");
			if(ageStart != null && !"".equals(ageStart)) {
				form.setAgeStart(ageStart);
			}
			String ageEnd = (String)reqContentMap.get("ageEnd");
			if(ageEnd != null && !"".equals(ageEnd)) {
				form.setAgeEnd(ageEnd);
			}
			Object mebSex = (Object)reqContentMap.get("mebSex");
			if(mebSex != null) {
				List<String> _mebSex = new ArrayList<String>();
				if(mebSex instanceof String) {
					if(!"".equals(mebSex.toString())) {
						_mebSex.add(mebSex.toString());
					}
				} else {
					_mebSex = (List)mebSex;
				}
				if(!_mebSex.isEmpty()) {
					form.setMebSex(_mebSex);
				}
			}
			String memberPointStart = (String)reqContentMap.get("memberPointStart");
			if(memberPointStart != null && !"".equals(memberPointStart)) {
				form.setMemberPointStart(memberPointStart);
			}
			String memberPointEnd = (String)reqContentMap.get("memberPointEnd");
			if(memberPointEnd != null && !"".equals(memberPointEnd)) {
				form.setMemberPointEnd(memberPointEnd);
			}
			String changablePointStart = (String)reqContentMap.get("changablePointStart");
			if(changablePointStart != null && !"".equals(changablePointStart)) {
				form.setChangablePointStart(changablePointStart);
			}
			String changablePointEnd = (String)reqContentMap.get("changablePointEnd");
			if(changablePointEnd != null && !"".equals(changablePointEnd)) {
				form.setChangablePointEnd(changablePointEnd);
			}
			String birthDayMode = (String)reqContentMap.get("birthDayMode");
			if(birthDayMode != null && !"".equals(birthDayMode)) {
				form.setBirthDayMode(birthDayMode);
				if("0".equals(birthDayMode)) {
					String joinDateFlag = (String)reqContentMap.get("joinDateFlag");
					form.setJoinDateFlag(joinDateFlag);
					String birthDayDateStart = (String)reqContentMap.get("birthDayDateStart");
					form.setBirthDayDateStart(birthDayDateStart);
					String birthDayDateEnd = (String)reqContentMap.get("birthDayDateEnd");
					form.setBirthDayDateEnd(birthDayDateEnd);
				}  else if("1".equals(birthDayMode)) {
					String curDayJoinDateFlag = (String)reqContentMap.get("curDayJoinDateFlag");
					form.setCurDayJoinDateFlag(curDayJoinDateFlag);
				} else if("2".equals(birthDayMode)) {
					String birthDayRange = (String)reqContentMap.get("birthDayRange");
					form.setBirthDayRange(birthDayRange);
					String birthDayPath = (String)reqContentMap.get("birthDayPath");
					form.setBirthDayPath(birthDayPath);
					String birthDayUnit = (String)reqContentMap.get("birthDayUnit");
					form.setBirthDayUnit(birthDayUnit);
				} else if("9".equals(birthDayMode)) {
					String birthDayMonth = (String)reqContentMap.get("birthDayMonth");
					if(birthDayMonth != null && !"".equals(birthDayMonth)) {
						form.setBirthDayMonth(birthDayMonth);
						dateList = new ArrayList<Integer>();
						int max = 0;
						if("2".equals(birthDayMonth)) {
							max = 29;
						} else if("4".equals(birthDayMonth) || "6".equals(birthDayMonth) || "9".equals(birthDayMonth) || "11".equals(birthDayMonth)) {
							max = 30;
						} else {
							max = 31;
						}
						for(int i = 1; i <= max; i++) {
							dateList.add(i);
						}
					}
					String birthDayDate = (String)reqContentMap.get("birthDayDate");
					if(birthDayDate != null && !"".equals(birthDayDate)) {
						form.setBirthDayDate(birthDayDate);
					}
				} else if("3".equals(birthDayMode)) {
					String birthDayMonthRangeStart = (String)reqContentMap.get("birthDayMonthRangeStart");
					if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
						form.setBirthDayMonthRangeStart(birthDayMonthRangeStart);
						dateRangeStartList = new ArrayList<Integer>();
						int max = 0;
						if("2".equals(birthDayMonthRangeStart)) {
							max = 29;
						} else if("4".equals(birthDayMonthRangeStart) || "6".equals(birthDayMonthRangeStart) 
								|| "9".equals(birthDayMonthRangeStart) || "11".equals(birthDayMonthRangeStart)) {
							max = 30;
						} else {
							max = 31;
						}
						for(int i = 1; i <= max; i++) {
							dateRangeStartList.add(i);
						}
					}
					String birthDayDateRangeStart = (String)reqContentMap.get("birthDayDateRangeStart");
					if(birthDayDateRangeStart != null && !"".equals(birthDayDateRangeStart)) {
						form.setBirthDayDateRangeStart(birthDayDateRangeStart);
					}
					String birthDayMonthRangeEnd = (String)reqContentMap.get("birthDayMonthRangeEnd");
					if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
						form.setBirthDayMonthRangeEnd(birthDayMonthRangeEnd);
						dateRangeEndList = new ArrayList<Integer>();
						int max = 0;
						if("2".equals(birthDayMonthRangeEnd)) {
							max = 29;
						} else if("4".equals(birthDayMonthRangeEnd) || "6".equals(birthDayMonthRangeEnd) 
								|| "9".equals(birthDayMonthRangeEnd) || "11".equals(birthDayMonthRangeEnd)) {
							max = 30;
						} else {
							max = 31;
						}
						for(int i = 1; i <= max; i++) {
							dateRangeEndList.add(i);
						}
					}
					String birthDayDateRangeEnd = (String)reqContentMap.get("birthDayDateRangeEnd");
					if(birthDayDateRangeEnd != null && !"".equals(birthDayDateRangeEnd)) {
						form.setBirthDayDateRangeEnd(birthDayDateRangeEnd);
					}
				}
			}
			String birthDayDateMode = (String)reqContentMap.get("birthDayDateMode");
			if(birthDayDateMode != null && !"".equals(birthDayDateMode)) {
				form.setBirthDayDateMode(birthDayDateMode);
				if("0".equals(birthDayDateMode)) {
					String birthDayDateMoreStart = (String)reqContentMap.get("birthDayDateMoreStart");
					form.setBirthDayDateMoreStart(birthDayDateMoreStart);
					String birthDayDateMoreEnd = (String)reqContentMap.get("birthDayDateMoreEnd");
					form.setBirthDayDateMoreEnd(birthDayDateMoreEnd);
				}
			}
			
			String joinDateMode = (String)reqContentMap.get("joinDateMode");
			if(joinDateMode != null && !"".equals(joinDateMode)) {
				form.setJoinDateMode(joinDateMode);
				if("0".equals(joinDateMode)) {
					String joinDateRange = (String)reqContentMap.get("joinDateRange");
					if(joinDateRange != null && !"".equals(joinDateRange)) {
						form.setJoinDateRange(joinDateRange);
						String joinDateUnit = (String)reqContentMap.get("joinDateUnit");
						form.setJoinDateUnit(joinDateUnit);
						String joinDateUnitFlag = (String)reqContentMap.get("joinDateUnitFlag");
						form.setJoinDateUnitFlag(joinDateUnitFlag);
					}
				} else {
					String joinDateRangeJson = (String)reqContentMap.get("joinDateRangeJson");
					if(joinDateRangeJson != null && !"".equals(joinDateRangeJson)) {
						form.setJoinDateRangeJson(joinDateRangeJson);
						form.setJoinDateRangeList(ConvertUtil.json2List(joinDateRangeJson));
					}
				}
			}
			
			String isSaleFlag = (String)reqContentMap.get("isSaleFlag");
			if(isSaleFlag != null && !"".equals(isSaleFlag)) {
				form.setIsSaleFlag(isSaleFlag);
			}
			String notSaleTimeMode = (String)reqContentMap.get("notSaleTimeMode");
			if(notSaleTimeMode != null && !"".equals(notSaleTimeMode)) {
				form.setNotSaleTimeMode(notSaleTimeMode);
				if("0".equals(notSaleTimeMode)) {
					String notSaleTimeRange = (String)reqContentMap.get("notSaleTimeRange");
					if(notSaleTimeRange != null && !"".equals(notSaleTimeRange)) {
						form.setNotSaleTimeRange(notSaleTimeRange);
						String notSaleTimeUnit = (String)reqContentMap.get("notSaleTimeUnit");
						form.setNotSaleTimeUnit(notSaleTimeUnit);
					}
				} else if ("9".equals(notSaleTimeMode)){
					String notSaleTimeStart = (String)reqContentMap.get("notSaleTimeStart");
					if(notSaleTimeStart != null && !"".equals(notSaleTimeStart)) {
						form.setNotSaleTimeStart(notSaleTimeStart);
					}
					String notSaleTimeEnd = (String)reqContentMap.get("notSaleTimeEnd");
					if(notSaleTimeEnd != null && !"".equals(notSaleTimeEnd)) {
						form.setNotSaleTimeEnd(notSaleTimeEnd);
					}
				} else if ("8".equals(notSaleTimeMode)){
					String notSaleTimeRangeLast = (String)reqContentMap.get("notSaleTimeRangeLast");
					if(notSaleTimeRangeLast != null && !"".equals(notSaleTimeRangeLast)) {
						form.setNotSaleTimeRangeLast(notSaleTimeRangeLast);
					}
				}
			}
			String saleTimeMode = (String)reqContentMap.get("saleTimeMode");
			if(saleTimeMode != null && !"".equals(saleTimeMode)) {
				form.setSaleTimeMode(saleTimeMode);
				if("0".equals(saleTimeMode)) {
					String saleTimeRange = (String)reqContentMap.get("saleTimeRange");
					if(saleTimeRange != null && !"".equals(saleTimeRange)) {
						form.setSaleTimeRange(saleTimeRange);
						String saleTimeUnit = (String)reqContentMap.get("saleTimeUnit");
						form.setSaleTimeUnit(saleTimeUnit);
					}
				} else {
					String saleTimeStart = (String)reqContentMap.get("saleTimeStart");
					if(saleTimeStart != null && !"".equals(saleTimeStart)) {
						form.setSaleTimeStart(saleTimeStart);
					}
					String saleTimeEnd = (String)reqContentMap.get("saleTimeEnd");
					if(saleTimeEnd != null && !"".equals(saleTimeEnd)) {
						form.setSaleTimeEnd(saleTimeEnd);
					}
				}
			}
			String saleRegionId = (String)reqContentMap.get("saleRegionId");
			if(saleRegionId != null && !"".equals(saleRegionId)) {
				form.setSaleRegionId(saleRegionId);
			}
			String saleBelongId = (String)reqContentMap.get("saleBelongId");
			if(saleBelongId != null && !"".equals(saleBelongId)) {
				form.setSaleBelongId(saleBelongId);
			}
			String saleProvinceId = (String)reqContentMap.get("saleProvinceId");
			if(saleProvinceId != null && !"".equals(saleProvinceId)) {
				form.setSaleProvinceId(saleProvinceId);
			}
			String saleCityId = (String)reqContentMap.get("saleCityId");
			if(saleCityId != null && !"".equals(saleCityId)) {
				form.setSaleCityId(saleCityId);
			}
			String saleMemCounterId = (String)reqContentMap.get("saleMemCounterId");
			if(saleMemCounterId != null && !"".equals(saleMemCounterId)) {
				form.setSaleMemCounterId(saleMemCounterId);
			}
			String saleChannelRegionId = (String)reqContentMap.get("saleChannelRegionId");
			if(saleChannelRegionId != null && !"".equals(saleChannelRegionId)) {
				form.setSaleChannelRegionId(saleChannelRegionId);
			}
			String saleChannelId = (String)reqContentMap.get("saleChannelId");
			if(saleChannelId != null && !"".equals(saleChannelId)) {
				form.setSaleChannelId(saleChannelId);
			}
			String saleModeFlag = (String)reqContentMap.get("saleModeFlag");
			if(saleModeFlag != null && !"".equals(saleModeFlag)) {
				form.setSaleModeFlag(saleModeFlag);
			}
			String saleCouValidFlag = (String)reqContentMap.get("saleCouValidFlag");
			if(saleCouValidFlag != null && !"".equals(saleCouValidFlag)) {
				form.setSaleCouValidFlag(saleCouValidFlag);
			}
			String saleRegionName = (String)reqContentMap.get("saleRegionName");
			if(saleRegionName != null && !"".equals(saleRegionName)) {
				form.setSaleRegionName(saleRegionName);
			}
			String saleCounterId = (String)reqContentMap.get("saleCounterId");
			if(saleCounterId != null && !"".equals(saleCounterId)) {
				form.setSaleCounterId(saleCounterId);
			}
			String saleCounterCode = (String)reqContentMap.get("saleCounterCode");
			if(saleCounterCode != null && !"".equals(saleCounterCode)) {
				form.setSaleCounterCode(saleCounterCode);
			}
			String saleCounterName = (String)reqContentMap.get("saleCounterName");
			if(saleCounterName != null && !"".equals(saleCounterName)) {
				form.setSaleCounterName(saleCounterName);
			}
			String saleCountStart = (String)reqContentMap.get("saleCountStart");
			if(saleCountStart != null && !"".equals(saleCountStart)) {
				form.setSaleCountStart(saleCountStart);
			}
			String saleCountEnd = (String)reqContentMap.get("saleCountEnd");
			if(saleCountEnd != null && !"".equals(saleCountEnd)) {
				form.setSaleCountEnd(saleCountEnd);
			}
			String payQuantityStart = (String)reqContentMap.get("payQuantityStart");
			if(payQuantityStart != null && !"".equals(payQuantityStart)) {
				form.setPayQuantityStart(payQuantityStart);
			}
			String payQuantityEnd = (String)reqContentMap.get("payQuantityEnd");
			if(payQuantityEnd != null && !"".equals(payQuantityEnd)) {
				form.setPayQuantityEnd(payQuantityEnd);
			}
			String payAmountStart = (String)reqContentMap.get("payAmountStart");
			if(payAmountStart != null && !"".equals(payAmountStart)) {
				form.setPayAmountStart(payAmountStart);
			}
			String payAmountEnd = (String)reqContentMap.get("payAmountEnd");
			if(payAmountEnd != null && !"".equals(payAmountEnd)) {
				form.setPayAmountEnd(payAmountEnd);
			}
			String relation = (String)reqContentMap.get("relation");
			if(relation != null && !"".equals(relation)) {
				form.setRelation(relation);
			}
			String notRelation = (String)reqContentMap.get("notRelation");
			if(notRelation != null && !"".equals(notRelation)) {
				form.setNotRelation(notRelation);
			}
			Object prtVendorId = (Object)reqContentMap.get("buyPrtVendorId");
			if(prtVendorId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(prtVendorId instanceof String) {
					if(!"".equals(prtVendorId.toString())) {
						_prtVendorId.add(prtVendorId.toString());
					}
				} else {
					_prtVendorId = (List)prtVendorId;
				}
				if(!_prtVendorId.isEmpty()) {
					reqContentMap.put("prtVendorId", _prtVendorId);
					proInfoList = binOLCM33_Service.getProInfoList(reqContentMap);
				}
			}
			Object cateValId = (Object)reqContentMap.get("buyCateValId");
			if(cateValId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(cateValId instanceof String) {
					if(!"".equals(cateValId.toString())) {
						_cateValId.add(cateValId.toString());
					}
				} else {
					_cateValId = (List)cateValId;
				}
				if(!_cateValId.isEmpty()) {
					reqContentMap.put("cateValId", _cateValId);
					proCatInfoList = binOLCM33_Service.getProTypeInfoList(reqContentMap);
				}
			}
			
			Object notPrtVendorId = (Object)reqContentMap.get("notPrtVendorId");
			if(notPrtVendorId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(notPrtVendorId instanceof String) {
					if(!"".equals(notPrtVendorId.toString())) {
						_prtVendorId.add(notPrtVendorId.toString());
					}
				} else {
					_prtVendorId = (List)notPrtVendorId;
				}
				if(!_prtVendorId.isEmpty()) {
					reqContentMap.put("prtVendorId", _prtVendorId);
					notProInfoList = binOLCM33_Service.getProInfoList(reqContentMap);
				}
			}
			Object notCateValId = (Object)reqContentMap.get("notCateValId");
			if(notCateValId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(notCateValId instanceof String) {
					if(!"".equals(notCateValId.toString())) {
						_cateValId.add(notCateValId.toString());
					}
				} else {
					_cateValId = (List)notCateValId;
				}
				if(!_cateValId.isEmpty()) {
					reqContentMap.put("cateValId", _cateValId);
					notProCatInfoList = binOLCM33_Service.getProTypeInfoList(reqContentMap);
				}
			}
			
			String regionId = (String)reqContentMap.get("regionId");
			if(regionId != null && !"".equals(regionId)) {
				form.setRegionId(regionId);
			}
			String belongId = (String)reqContentMap.get("belongId");
			if(belongId != null && !"".equals(belongId)) {
				form.setBelongId(belongId);
			}
			String provinceId = (String)reqContentMap.get("provinceId");
			if(provinceId != null && !"".equals(provinceId)) {
				form.setProvinceId(provinceId);
			}
			String cityId = (String)reqContentMap.get("cityId");
			if(cityId != null && !"".equals(cityId)) {
				form.setCityId(cityId);
			}
			String memCounterId = (String)reqContentMap.get("memCounterId");
			if(memCounterId != null && !"".equals(memCounterId)) {
				form.setMemCounterId(memCounterId);
			}
			String channelRegionId = (String)reqContentMap.get("channelRegionId");
			if(channelRegionId != null && !"".equals(channelRegionId)) {
				form.setChannelRegionId(channelRegionId);
			}
			String channelId = (String)reqContentMap.get("channelId");
			if(channelId != null && !"".equals(channelId)) {
				form.setChannelId(channelId);
			}
			String exclusiveFlag = (String)reqContentMap.get("exclusiveFlag");
			if(exclusiveFlag != null && !"".equals(exclusiveFlag)) {
				form.setExclusiveFlag(exclusiveFlag);
			}
			String modeFlag = (String)reqContentMap.get("modeFlag");
			if(modeFlag != null && !"".equals(modeFlag)) {
				form.setModeFlag(modeFlag);
			}
			String couValidFlag = (String)reqContentMap.get("couValidFlag");
			if(couValidFlag != null && !"".equals(couValidFlag)) {
				form.setCouValidFlag(couValidFlag);
			}
			String regionName = (String)reqContentMap.get("regionName");
			if(regionName != null && !"".equals(regionName)) {
				form.setRegionName(regionName);
			}
			String clubRegionId = (String)reqContentMap.get("clubRegionId");
			if(clubRegionId != null && !"".equals(clubRegionId)) {
				form.setClubRegionId(clubRegionId);
			}
			String clubBelongId = (String)reqContentMap.get("clubBelongId");
			if(clubBelongId != null && !"".equals(clubBelongId)) {
				form.setClubBelongId(clubBelongId);
			}
			String clubProvinceId = (String)reqContentMap.get("clubProvinceId");
			if(clubProvinceId != null && !"".equals(clubProvinceId)) {
				form.setClubProvinceId(clubProvinceId);
			}
			String clubCityId = (String)reqContentMap.get("clubCityId");
			if(clubCityId != null && !"".equals(clubCityId)) {
				form.setClubCityId(clubCityId);
			}
			String clubMemCounterId = (String)reqContentMap.get("clubMemCounterId");
			if(clubMemCounterId != null && !"".equals(clubMemCounterId)) {
				form.setClubMemCounterId(clubMemCounterId);
			}
			String clubChannelRegionId = (String)reqContentMap.get("clubChannelRegionId");
			if(channelRegionId != null && !"".equals(clubChannelRegionId)) {
				form.setClubChannelRegionId(clubChannelRegionId);
			}
			String clubChannelId = (String)reqContentMap.get("clubChannelId");
			if(clubChannelId != null && !"".equals(clubChannelId)) {
				form.setClubChannelId(clubChannelId);
			}
			String clubExclusiveFlag = (String)reqContentMap.get("clubExclusiveFlag");
			if(clubExclusiveFlag != null && !"".equals(clubExclusiveFlag)) {
				form.setClubExclusiveFlag(clubExclusiveFlag);
			}
			String clubModeFlag = (String)reqContentMap.get("clubModeFlag");
			if(clubModeFlag != null && !"".equals(clubModeFlag)) {
				form.setClubModeFlag(clubModeFlag);
			}
			String clubCouValidFlag = (String)reqContentMap.get("clubCouValidFlag");
			if(clubCouValidFlag != null && !"".equals(clubCouValidFlag)) {
				form.setClubCouValidFlag(clubCouValidFlag);
			}
			String clubRegionName = (String)reqContentMap.get("clubRegionName");
			if(clubRegionName != null && !"".equals(clubRegionName)) {
				form.setClubRegionName(clubRegionName);
			}
			String clubJoinTimeStart = (String)reqContentMap.get("clubJoinTimeStart");
			if(clubJoinTimeStart != null && !"".equals(clubJoinTimeStart)) {
				form.setClubJoinTimeStart(clubJoinTimeStart);
			}
			String clubJoinTimeEnd = (String)reqContentMap.get("clubJoinTimeEnd");
			if(clubJoinTimeEnd != null && !"".equals(clubJoinTimeEnd)) {
				form.setClubJoinTimeEnd(clubJoinTimeEnd);
			}
			
			String joinDateSaleDateRel = (String)reqContentMap.get("joinDateSaleDateRel");
			if(joinDateSaleDateRel != null && !"".equals(joinDateSaleDateRel)) {
				form.setJoinDateSaleDateRel(joinDateSaleDateRel);
			}
			String referFlag = (String)reqContentMap.get("referFlag");
			if(referFlag != null && !"".equals(referFlag)) {
				form.setReferFlag(referFlag);
				if("1".equals(referFlag)) {
					String referredMemCode = (String)reqContentMap.get("referredMemCode");
					form.setReferredMemCode(referredMemCode);
				} else if("2".equals(referFlag)) {
					String referrerMemCode = (String)reqContentMap.get("referrerMemCode");
					form.setReferrerMemCode(referrerMemCode);
				}
			}
			String participateTimeStart = (String)reqContentMap.get("participateTimeStart");
			if(participateTimeStart != null && !"".equals(participateTimeStart)) {
				form.setParticipateTimeStart(participateTimeStart);
			}
			String participateTimeEnd = (String)reqContentMap.get("participateTimeEnd");
			if(participateTimeEnd != null && !"".equals(participateTimeEnd)) {
				form.setParticipateTimeEnd(participateTimeEnd);
			}
			String campaignCounterId = (String)reqContentMap.get("campaignCounterId");
			if(campaignCounterId != null && !"".equals(campaignCounterId)) {
				form.setCampaignCounterId(campaignCounterId);
			}
			String campaignCounterName = (String)reqContentMap.get("campaignCounterName");
			if(campaignCounterName != null && !"".equals(campaignCounterName)) {
				form.setCampaignCounterName(campaignCounterName);
			}
			String campaignMode = (String)reqContentMap.get("campaignMode");
			if(campaignMode != null && !"".equals(campaignMode)) {
				form.setCampaignMode(campaignMode);
			}
			String campaignCode = (String)reqContentMap.get("campaignCode");
			if(campaignCode != null && !"".equals(campaignCode)) {
				form.setCampaignCode(campaignCode);
			}
			String campaignName = (String)reqContentMap.get("campaignName");
			if(campaignName != null && !"".equals(campaignName)) {
				form.setCampaignName(campaignName);
			}
			Object campaignState = (Object)reqContentMap.get("campaignState");
			if(campaignState != null) {
				List<String> _campaignState = new ArrayList<String>();
				if(campaignState instanceof String) {
					if(!"".equals(campaignState.toString())) {
						_campaignState.add(campaignState.toString());
					}
				} else {
					_campaignState = (List)campaignState;
				}
				if(!_campaignState.isEmpty()) {
					form.setCampaignState(_campaignState);
				}
			}
			String couNotEmpty = (String)reqContentMap.get("couNotEmpty");
			if(couNotEmpty != null && !"".equals(couNotEmpty)) {
				form.setCouNotEmpty(couNotEmpty);
			}
			
			String levelAdjustDayFlag = (String)reqContentMap.get("levelAdjustDayFlag");
			if(levelAdjustDayFlag != null && !"".equals(levelAdjustDayFlag)) {
				form.setLevelAdjustDayFlag(levelAdjustDayFlag);
				if("2".equals(levelAdjustDayFlag)) {
					String levelAdjustDayRange = (String)reqContentMap.get("levelAdjustDayRange");
					if(levelAdjustDayRange != null && !"".equals(levelAdjustDayRange)) {
						form.setLevelAdjustDayRange(levelAdjustDayRange);
						String levelAdjustDayUnit = (String)reqContentMap.get("levelAdjustDayUnit");
						form.setLevelAdjustDayUnit(levelAdjustDayUnit);
					}
				} else if ("3".equals(levelAdjustDayFlag)) {
					String levelAdjustDayStart = (String)reqContentMap.get("levelAdjustDayStart");
					if(levelAdjustDayStart != null && !"".equals(levelAdjustDayStart)) {
						form.setLevelAdjustDayStart(levelAdjustDayStart);
					}
					String levelAdjustDayEnd = (String)reqContentMap.get("levelAdjustDayEnd");
					if(levelAdjustDayEnd != null && !"".equals(levelAdjustDayEnd)) {
						form.setLevelAdjustDayEnd(levelAdjustDayEnd);
					}
				}
				String levelChangeType = (String)reqContentMap.get("levelChangeType");
				if(levelChangeType != null && !"".equals(levelChangeType)) {
					form.setLevelChangeType(levelChangeType);
				}
			}
			
			String notSaleDays = (String)reqContentMap.get("notSaleDays");
			if(notSaleDays != null && !"".equals(notSaleDays)) {
				form.setNotSaleDays(notSaleDays);
			}
			String notSaleDaysRange = (String) reqContentMap.get("notSaleDaysRange");
			if(notSaleDaysRange != null && !"".equals(notSaleDaysRange)) {
				form.setNotSaleDaysRange(notSaleDaysRange);
			}
			String noSaleDaysMode = (String)reqContentMap.get("noSaleDaysMode");
			if(noSaleDaysMode != null && !"".equals(noSaleDaysMode)) {
				form.setNoSaleDaysMode(noSaleDaysMode);
			}
			String firstStartDay = (String)reqContentMap.get("firstStartDay");
			if(firstStartDay != null && !"".equals(firstStartDay)) {
				form.setFirstStartDay(firstStartDay);
			}
			String firstEndDay = (String)reqContentMap.get("firstEndDay");
			if(firstEndDay != null && !"".equals(firstEndDay)) {
				form.setFirstEndDay(firstEndDay);
			}
			String lastSaleDateStart = (String) reqContentMap.get("lastSaleDateStart");
			if(lastSaleDateStart != null && !"".equals(lastSaleDateStart)) {
				form.setLastSaleDateStart(lastSaleDateStart);
			}
			String lastSaleDateEnd = (String) reqContentMap.get("lastSaleDateEnd");
			if(lastSaleDateEnd != null && !"".equals(lastSaleDateEnd)) {
				form.setLastSaleDateEnd(lastSaleDateEnd);
			}
			String isNewMember = (String)reqContentMap.get("isNewMember");
			if(isNewMember != null && !"".equals(isNewMember)) {
				form.setIsNewMember(isNewMember);
			}
			String flagBuyCount = (String)reqContentMap.get("flagBuyCount");
			if(flagBuyCount != null && !"".equals(flagBuyCount)) {
				form.setFlagBuyCount(flagBuyCount);
			}
			String isActivityMember = (String)reqContentMap.get("isActivityMember");
			if(isActivityMember != null && !"".equals(isActivityMember)) {
				form.setIsActivityMember(isActivityMember);
			}
			String actiCountStart = (String)reqContentMap.get("actiCountStart");
			if(actiCountStart != null && !"".equals(actiCountStart)) {
				form.setActiCountStart(actiCountStart);
			}
			String actiCountEnd = (String)reqContentMap.get("actiCountEnd");
			if(actiCountEnd != null && !"".equals(actiCountEnd)) {
				form.setActiCountEnd(actiCountEnd);
			}
			String favActiType = (String)reqContentMap.get("favActiType");
			if(favActiType != null && !"".equals(favActiType)) {
				form.setFavActiType(favActiType);
			}
			String unBuyInterval = (String)reqContentMap.get("unBuyInterval");
			if(unBuyInterval != null && !"".equals(unBuyInterval)) {
				form.setUnBuyInterval(unBuyInterval);
			}
			String pctStart = (String)reqContentMap.get("pctStart");
			if(pctStart != null && !"".equals(pctStart)) {
				form.setPctStart(pctStart);
			}
			String pctEnd = (String)reqContentMap.get("pctEnd");
			if(pctEnd != null && !"".equals(pctEnd)) {
				form.setPctEnd(pctEnd);
			}
			Object mostPrtId = (Object)reqContentMap.get("mostPrtId");
			if(mostPrtId != null) {
				List<String> _prtVendorId = new ArrayList<String>();
				if(mostPrtId instanceof String) {
					if(!"".equals(mostPrtId.toString())) {
						_prtVendorId.add(mostPrtId.toString());
					}
				} else {
					_prtVendorId = (List)mostPrtId;
				}
				if(!_prtVendorId.isEmpty()) {
					reqContentMap.put("prtVendorId", _prtVendorId);
					mostPrtList = binOLCM33_Service.getProInfoList(reqContentMap);
				}
			}
			Object mostCateBClassId = (Object)reqContentMap.get("mostCateBClassId");
			if(mostCateBClassId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(mostCateBClassId instanceof String) {
					if(!"".equals(mostCateBClassId.toString())) {
						_cateValId.add(mostCateBClassId.toString());
					}
				} else {
					_cateValId = (List)mostCateBClassId;
				}
				if(!_cateValId.isEmpty()) {
					reqContentMap.put("cateValId", _cateValId);
					mostCateBClassList = binOLCM33_Service.getProTypeInfoList(reqContentMap);
				}
			}
			Object mostCateMClassId = (Object)reqContentMap.get("mostCateMClassId");
			if(mostCateMClassId != null) {
				List<String> _cateValId = new ArrayList<String>();
				if(mostCateMClassId instanceof String) {
					if(!"".equals(mostCateMClassId.toString())) {
						_cateValId.add(mostCateMClassId.toString());
					}
				} else {
					_cateValId = (List)mostCateMClassId;
				}
				if(!_cateValId.isEmpty()) {
					reqContentMap.put("cateValId", _cateValId);
					mostCateMClassList = binOLCM33_Service.getProTypeInfoList(reqContentMap);
				}
			}
			String memPointRangeJson = (String)reqContentMap.get("memPointRangeJson");
			if(memPointRangeJson != null && !"".equals(memPointRangeJson)) {
				form.setMemPointRangeList(ConvertUtil.json2List(memPointRangeJson));
				form.setMemPointRangeJson(memPointRangeJson);
			}
			String changablePointRangeJson = (String)reqContentMap.get("changablePointRangeJson");
			if(changablePointRangeJson != null && !"".equals(changablePointRangeJson)) {
				form.setChangablePointRangeList(ConvertUtil.json2List(changablePointRangeJson));
				form.setChangablePointRangeJson(changablePointRangeJson);
			}
			String lastSaleTimeRangeJson = (String)reqContentMap.get("lastSaleTimeRangeJson");
			if(lastSaleTimeRangeJson != null && !"".equals(lastSaleTimeRangeJson)) {
				form.setLastSaleTimeRangeJson(lastSaleTimeRangeJson);
				form.setLastSaleTimeRangeList(ConvertUtil.json2List(lastSaleTimeRangeJson));
			}
			String firstSaleTimeRangeJson = (String)reqContentMap.get("firstSaleTimeRangeJson");
			if(firstSaleTimeRangeJson != null && !"".equals(firstSaleTimeRangeJson)) {
				form.setFirstSaleTimeRangeJson(firstSaleTimeRangeJson);
				form.setFirstSaleTimeRangeList(ConvertUtil.json2List(firstSaleTimeRangeJson));
			}
		}
		
		// 存在禁止修改条件的场合
		if(form.getDisableCondition() != null && !"".equals(form.getDisableCondition())) {
			Map<String, Object> disableConditionMap = ConvertUtil.json2Map(form.getDisableCondition());
			// 剔除map中的空值
			disableConditionMap = CherryUtil.remEmptyVal(disableConditionMap);
			form.setDisableConditionMap(disableConditionMap);
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX 取得等级列表
	 * 
	 * @throws Exception
	 */
	public void searchLevel() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("memberClubId", form.getMemberClubId());
		// 会员等级List
		List<Map<String, Object>> levellist = binOLCM33_Service.getMemberLevelInfoList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, levellist);
	}
	
	/**
	 * 
	 * 查询会员搜索条件初始画面
	 * 
	 */
	public String searchInit() throws Exception {
		
		return SUCCESS;
	}
	
	
	/**
	 * 
	 * 查询会员搜索条件
	 * 
	 */
	public String searchSearchRequest() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put("userId", userInfo.getBIN_UserID());
		// form参数设置到map中
		ConvertUtil.setForm(form, map);		
		// 取得会员搜索条件记录数
		int count = binOLCM33_BL.getSearchRequestCount(map);
		if(count != 0) {
			// 取得会员搜索条件List
			searchRequestList = binOLCM33_BL.getSearchRequestList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加会员搜索条件初始画面
	 * 
	 */
	public String addInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加会员搜索条件
	 * 
	 */
	public String addSearchRequest() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		Map<String, Object> reqContent = new HashMap<String, Object>();
		if(form.getAddrNotEmpty() != null && !"".equals(form.getAddrNotEmpty())) {
			reqContent.put("addrNotEmpty", form.getAddrNotEmpty());
		}
		if(form.getTelNotEmpty() != null && !"".equals(form.getTelNotEmpty())) {
			reqContent.put("telNotEmpty", form.getTelNotEmpty());
		}
		if(form.getTelCheck() != null && !"".equals(form.getTelCheck())) {
			reqContent.put("telCheck", form.getTelCheck());
		}
		if(form.getEmailNotEmpty() != null && !"".equals(form.getEmailNotEmpty())) {
			reqContent.put("emailNotEmpty", form.getEmailNotEmpty());
		}
		if(form.getTestType() != null && !"".equals(form.getTestType())) {
			reqContent.put("testType", form.getTestType());
		}
		if(form.getMemCode() != null && !"".equals(form.getMemCode())) {
			reqContent.put("memCode", form.getMemCode());
		}
		if(form.getName() != null && !"".equals(form.getName())) {
			reqContent.put("name", form.getName());
		}
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			reqContent.put("mobilePhone", form.getMobilePhone());
		}
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			reqContent.put("email", form.getEmail());
		}
		if(form.getMemType() != null && !form.getMemType().isEmpty()) {
			reqContent.put("memType", form.getMemType());
		}
		if(form.getMemLevel() != null && !form.getMemLevel().isEmpty()) {
			reqContent.put("memLevel", form.getMemLevel());
		}
		if(form.getAgeStart() != null && !"".equals(form.getAgeStart())) {
			reqContent.put("ageStart", form.getAgeStart());
		}
		if(form.getAgeEnd() != null && !"".equals(form.getAgeEnd())) {
			reqContent.put("ageEnd", form.getAgeEnd());
		}
		if(form.getMebSex() != null && !form.getMebSex().isEmpty()) {
			reqContent.put("mebSex", form.getMebSex());
		}
		if(form.getMemberPointStart() != null && !"".equals(form.getMemberPointStart())) {
			reqContent.put("memberPointStart", form.getMemberPointStart());
		}
		if(form.getMemberPointEnd() != null && !"".equals(form.getMemberPointEnd())) {
			reqContent.put("memberPointEnd", form.getMemberPointEnd());
		}
		if(form.getChangablePointStart() != null && !"".equals(form.getChangablePointStart())) {
			reqContent.put("changablePointStart", form.getChangablePointStart());
		}
		if(form.getChangablePointEnd() != null && !"".equals(form.getChangablePointEnd())) {
			reqContent.put("changablePointEnd", form.getChangablePointEnd());
		}
		if(form.getBirthDayMode() != null && !"".equals(form.getBirthDayMode())) {
			reqContent.put("birthDayMode", form.getBirthDayMode());
			if("0".equals(form.getBirthDayMode())) {
				if(form.getJoinDateFlag() != null && !"".equals(form.getJoinDateFlag())) {
					reqContent.put("joinDateFlag", form.getJoinDateFlag());
				}
				if(form.getBirthDayDateStart() != null && !"".equals(form.getBirthDayDateStart())) {
					reqContent.put("birthDayDateStart", form.getBirthDayDateStart());
				}
				if(form.getBirthDayDateEnd() != null && !"".equals(form.getBirthDayDateEnd())) {
					reqContent.put("birthDayDateEnd", form.getBirthDayDateEnd());
				}
			} else if("1".equals(form.getBirthDayMode())) {
				if(form.getCurDayJoinDateFlag() != null && !"".equals(form.getCurDayJoinDateFlag())) {
					reqContent.put("curDayJoinDateFlag", form.getCurDayJoinDateFlag());
				}
			} else if("2".equals(form.getBirthDayMode())) {
				if(form.getBirthDayRange() != null && !"".equals(form.getBirthDayRange())) {
					reqContent.put("birthDayRange", form.getBirthDayRange());
				}
				if(form.getBirthDayPath() != null && !"".equals(form.getBirthDayPath())) {
					reqContent.put("birthDayPath", form.getBirthDayPath());
				}
				if(form.getBirthDayUnit() != null && !"".equals(form.getBirthDayUnit())) {
					reqContent.put("birthDayUnit", form.getBirthDayUnit());
				}
			} else if("9".equals(form.getBirthDayMode())) {
				if(form.getBirthDayMonth() != null && !"".equals(form.getBirthDayMonth())) {
					reqContent.put("birthDayMonth", form.getBirthDayMonth());
				}
				if(form.getBirthDayDate() != null && !"".equals(form.getBirthDayDate())) {
					reqContent.put("birthDayDate", form.getBirthDayDate());
				}
			} else if("3".equals(form.getBirthDayMode())) {
				if(form.getBirthDayMonthRangeStart() != null && !"".equals(form.getBirthDayMonthRangeStart())) {
					reqContent.put("birthDayMonthRangeStart", form.getBirthDayMonthRangeStart());
				}
				if(form.getBirthDayMonthRangeEnd() != null && !"".equals(form.getBirthDayMonthRangeEnd())) {
					reqContent.put("birthDayMonthRangeEnd", form.getBirthDayMonthRangeEnd());
				}
				if(form.getBirthDayDateRangeStart() != null && !"".equals(form.getBirthDayDateRangeStart())) {
					reqContent.put("birthDayDateRangeStart", form.getBirthDayDateRangeStart());
				}
				if(form.getBirthDayDateRangeEnd() != null && !"".equals(form.getBirthDayDateRangeEnd())) {
					reqContent.put("birthDayDateRangeEnd", form.getBirthDayDateRangeEnd());
				}
			}
		}
		if(form.getBirthDayDateMode() != null && !"".equals(form.getBirthDayDateMode())) {
			reqContent.put("birthDayDateMode", form.getBirthDayDateMode());
			if("0".equals(form.getBirthDayDateMode())) {
				if(form.getBirthDayDateMoreStart() != null && !"".equals(form.getBirthDayDateMoreStart())) {
					reqContent.put("birthDayDateMoreStart", form.getBirthDayDateMoreStart());
				}
				if(form.getBirthDayDateMoreEnd() != null && !"".equals(form.getBirthDayDateMoreEnd())) {
					reqContent.put("birthDayDateMoreEnd", form.getBirthDayDateMoreEnd());
				}
			}
		}
		if(form.getJoinDateMode() != null && !"".equals(form.getJoinDateMode())) {
			reqContent.put("joinDateMode", form.getJoinDateMode());
			if("0".equals(form.getJoinDateMode())) {
				if(form.getJoinDateRange()!= null && !"".equals(form.getJoinDateRange())) {
					reqContent.put("joinDateRange", form.getJoinDateRange());
					reqContent.put("joinDateUnit", form.getJoinDateUnit());
					reqContent.put("joinDateUnitFlag", form.getJoinDateUnitFlag());
				}
			} else if("9".equals(form.getJoinDateMode())) {
				if(form.getJoinDateRangeJson() != null && !"".equals(form.getJoinDateRangeJson())) {
					reqContent.put("joinDateRangeJson", form.getJoinDateRangeJson());
				}
			}
		}
		
		if(form.getIsSaleFlag() != null && !"".equals(form.getIsSaleFlag())) {
			reqContent.put("isSaleFlag", form.getIsSaleFlag());
			if("0".equals(form.getIsSaleFlag())) {
				if(form.getNotSaleTimeMode() != null && !"".equals(form.getNotSaleTimeMode())) {
					reqContent.put("notSaleTimeMode", form.getNotSaleTimeMode());
					if("0".equals(form.getNotSaleTimeMode())) {
						if(form.getNotSaleTimeRange()!= null && !"".equals(form.getNotSaleTimeRange())) {
							reqContent.put("notSaleTimeRange", form.getNotSaleTimeRange());
							reqContent.put("notSaleTimeUnit", form.getNotSaleTimeUnit());
						}
					} else if("9".equals(form.getNotSaleTimeMode())) {
						if(form.getNotSaleTimeStart() != null && !"".equals(form.getNotSaleTimeStart())) {
							reqContent.put("notSaleTimeStart", form.getNotSaleTimeStart());
						}
						if(form.getNotSaleTimeEnd() != null && !"".equals(form.getNotSaleTimeEnd())) {
							reqContent.put("notSaleTimeEnd", form.getNotSaleTimeEnd());
						}
					} else if("8".equals(form.getNotSaleTimeMode())) {
						if (form.getNotSaleTimeRangeLast() != null && !"".equals(form.getNotSaleTimeRangeLast())) {
							reqContent.put("notSaleTimeRangeLast", form.getNotSaleTimeRangeLast());
						}
					}
				}
			} else if("1".equals(form.getIsSaleFlag())) {
				if(form.getSaleTimeMode() != null && !"".equals(form.getSaleTimeMode())) {
					reqContent.put("saleTimeMode", form.getSaleTimeMode());
					if("0".equals(form.getSaleTimeMode())) {
						if(form.getSaleTimeRange()!= null && !"".equals(form.getSaleTimeRange())) {
							reqContent.put("saleTimeRange", form.getSaleTimeRange());
							reqContent.put("saleTimeUnit", form.getSaleTimeUnit());
						}
					} else if("9".equals(form.getSaleTimeMode())) {
						if(form.getSaleTimeStart() != null && !"".equals(form.getSaleTimeStart())) {
							reqContent.put("saleTimeStart", form.getSaleTimeStart());
						}
						if(form.getSaleTimeEnd() != null && !"".equals(form.getSaleTimeEnd())) {
							reqContent.put("saleTimeEnd", form.getSaleTimeEnd());
						}
					}
				}
				if(form.getSaleRegionId() != null && !"".equals(form.getSaleRegionId())) {
					reqContent.put("saleRegionId", form.getSaleRegionId());
				}
				if(form.getSaleProvinceId() != null && !"".equals(form.getSaleProvinceId())) {
					reqContent.put("saleProvinceId", form.getSaleProvinceId());
				}
				if(form.getSaleCityId() != null && !"".equals(form.getSaleCityId())) {
					reqContent.put("saleCityId", form.getSaleCityId());
				}
				if(form.getSaleMemCounterId() != null && !"".equals(form.getSaleMemCounterId())) {
					reqContent.put("saleMemCounterId", form.getSaleMemCounterId());
				}
				if(form.getSaleChannelRegionId() != null && !"".equals(form.getSaleChannelRegionId())) {
					reqContent.put("saleChannelRegionId", form.getSaleChannelRegionId());
				}
				if(form.getSaleChannelId() != null && !"".equals(form.getSaleChannelId())) {
					reqContent.put("saleChannelId", form.getSaleChannelId());
				}
				if(form.getSaleModeFlag() != null && !"".equals(form.getSaleModeFlag())) {
					reqContent.put("saleModeFlag", form.getSaleModeFlag());
				}
				if(form.getSaleCouValidFlag() != null && !"".equals(form.getSaleCouValidFlag())) {
					reqContent.put("saleCouValidFlag", form.getSaleCouValidFlag());
				}
				if(form.getSaleRegionName() != null && !"".equals(form.getSaleRegionName())) {
					reqContent.put("saleRegionName", form.getSaleRegionName());
				}
				if(form.getSaleCounterId() != null && !"".equals(form.getSaleCounterId())) {
					reqContent.put("saleCounterId", form.getSaleCounterId());
				}
				if(form.getSaleCounterCode() != null && !"".equals(form.getSaleCounterCode())) {
					reqContent.put("saleCounterCode", form.getSaleCounterCode());
				}
				if(form.getSaleCounterName() != null && !"".equals(form.getSaleCounterName())) {
					reqContent.put("saleCounterName", form.getSaleCounterName());
				}
				if(form.getSaleCountStart() != null && !"".equals(form.getSaleCountStart())) {
					reqContent.put("saleCountStart", form.getSaleCountStart());
				}
				if(form.getSaleCountEnd() != null && !"".equals(form.getSaleCountEnd())) {
					reqContent.put("saleCountEnd", form.getSaleCountEnd());
				}
				if(form.getPayQuantityStart() != null && !"".equals(form.getPayQuantityStart())) {
					reqContent.put("payQuantityStart", form.getPayQuantityStart());
				}
				if(form.getPayQuantityEnd() != null && !"".equals(form.getPayQuantityEnd())) {
					reqContent.put("payQuantityEnd", form.getPayQuantityEnd());
				}
				if(form.getPayAmountStart() != null && !"".equals(form.getPayAmountStart())) {
					reqContent.put("payAmountStart", form.getPayAmountStart());
				}
				if(form.getPayAmountEnd() != null && !"".equals(form.getPayAmountEnd())) {
					reqContent.put("payAmountEnd", form.getPayAmountEnd());
				}
				if(form.getRelation() != null && !"".equals(form.getRelation())) {
					reqContent.put("relation", form.getRelation());
				}
				if(form.getNotRelation() != null && !"".equals(form.getNotRelation())) {
					reqContent.put("notRelation", form.getNotRelation());
				}
				if(form.getBuyPrtVendorId() != null && !form.getBuyPrtVendorId().isEmpty()) {
					reqContent.put("buyPrtVendorId", form.getBuyPrtVendorId());
				}
				if(form.getBuyCateValId() != null && !form.getBuyCateValId().isEmpty()) {
					reqContent.put("buyCateValId", form.getBuyCateValId());
				}
				if(form.getNotPrtVendorId() != null && !form.getNotPrtVendorId().isEmpty()) {
					reqContent.put("notPrtVendorId", form.getNotPrtVendorId());
				}
				if(form.getNotCateValId() != null && !form.getNotCateValId().isEmpty()) {
					reqContent.put("notCateValId", form.getNotCateValId());
				}
			}
		}
		// 会员标签
		if(form.getIsNewMember() != null && !"".equals(form.getIsNewMember())) {
			reqContent.put("isNewMember", form.getIsNewMember());
		}
		if(form.getFlagBuyCount() != null && !"".equals(form.getFlagBuyCount())) {
			reqContent.put("flagBuyCount", form.getFlagBuyCount());
		}
		if(form.getIsActivityMember() != null && !"".equals(form.getIsActivityMember())) {
			reqContent.put("isActivityMember", form.getIsActivityMember());
		}
		if(form.getActiCountStart() != null && !"".equals(form.getActiCountStart())) {
			reqContent.put("actiCountStart", form.getActiCountStart());
		}
		if(form.getActiCountEnd() != null && !"".equals(form.getActiCountEnd())) {
			reqContent.put("actiCountEnd", form.getActiCountEnd());
		}
		if(form.getFavActiType() != null && !"".equals(form.getFavActiType())) {
			reqContent.put("favActiType", form.getFavActiType());
		}
		if(form.getUnBuyInterval() != null && !"".equals(form.getUnBuyInterval())) {
			reqContent.put("unBuyInterval", form.getUnBuyInterval());
		}
		if(form.getMostCateBClassId() != null && !form.getMostCateBClassId().isEmpty()) {
			reqContent.put("mostCateBClassId", form.getMostCateBClassId());
		}
		if(form.getMostCateMClassId()!= null && !form.getMostCateMClassId().isEmpty()) {
			reqContent.put("mostCateMClassId", form.getMostCateMClassId());
		}
		if(form.getMostPrtId() != null && !form.getMostPrtId().isEmpty()) {
			reqContent.put("mostPrtId", form.getMostPrtId());
		}
		if(form.getPctStart() != null && !"".equals(form.getPctStart())) {
			reqContent.put("pctStart", form.getPctStart());
		}
		if(form.getPctEnd() != null && !"".equals(form.getPctEnd())) {
			reqContent.put("pctEnd", form.getPctEnd());
		}
		if(form.getRegionId() != null && !"".equals(form.getRegionId())) {
			reqContent.put("regionId", form.getRegionId());
		}
		if(form.getProvinceId() != null && !"".equals(form.getProvinceId())) {
			reqContent.put("provinceId", form.getProvinceId());
		}
		if(form.getCityId() != null && !"".equals(form.getCityId())) {
			reqContent.put("cityId", form.getCityId());
		}
		if(form.getMemCounterId() != null && !"".equals(form.getMemCounterId())) {
			reqContent.put("memCounterId", form.getMemCounterId());
		}
		if(form.getChannelRegionId() != null && !"".equals(form.getChannelRegionId())) {
			reqContent.put("channelRegionId", form.getChannelRegionId());
		}
		if(form.getChannelId() != null && !"".equals(form.getChannelId())) {
			reqContent.put("channelId", form.getChannelId());
		}
		if(form.getExclusiveFlag() != null && !"".equals(form.getExclusiveFlag())) {
			reqContent.put("exclusiveFlag", form.getExclusiveFlag());
		}
		if(form.getModeFlag() != null && !"".equals(form.getModeFlag())) {
			reqContent.put("modeFlag", form.getModeFlag());
		}
		if(form.getCouValidFlag() != null && !"".equals(form.getCouValidFlag())) {
			reqContent.put("couValidFlag", form.getCouValidFlag());
		}
		if(form.getRegionName() != null && !"".equals(form.getRegionName())) {
			reqContent.put("regionName", form.getRegionName());
		}
		if(form.getMemberClubId() != null && !"".equals(form.getMemberClubId())) {
			reqContent.put("memberClubId", form.getMemberClubId());
		}
		if(form.getClubRegionId() != null && !"".equals(form.getClubRegionId())) {
			reqContent.put("clubRegionId", form.getClubRegionId());
		}
		if(form.getClubProvinceId() != null && !"".equals(form.getClubProvinceId())) {
			reqContent.put("clubProvinceId", form.getClubProvinceId());
		}
		if(form.getClubCityId() != null && !"".equals(form.getClubCityId())) {
			reqContent.put("clubCityId", form.getClubCityId());
		}
		if(form.getClubMemCounterId() != null && !"".equals(form.getClubMemCounterId())) {
			reqContent.put("clubMemCounterId", form.getClubMemCounterId());
		}
		if(form.getClubChannelRegionId() != null && !"".equals(form.getClubChannelRegionId())) {
			reqContent.put("clubChannelRegionId", form.getClubChannelRegionId());
		}
		if(form.getClubChannelId() != null && !"".equals(form.getClubChannelId())) {
			reqContent.put("clubChannelId", form.getClubChannelId());
		}
		if(form.getClubExclusiveFlag() != null && !"".equals(form.getClubExclusiveFlag())) {
			reqContent.put("clubExclusiveFlag", form.getClubExclusiveFlag());
		}
		if(form.getClubModeFlag() != null && !"".equals(form.getClubModeFlag())) {
			reqContent.put("clubModeFlag", form.getClubModeFlag());
		}
		if(form.getClubCouValidFlag() != null && !"".equals(form.getClubCouValidFlag())) {
			reqContent.put("clubCouValidFlag", form.getClubCouValidFlag());
		}
		if(form.getClubRegionName() != null && !"".equals(form.getClubRegionName())) {
			reqContent.put("clubRegionName", form.getClubRegionName());
		}
		
		if(form.getClubJoinTimeStart() != null && !"".equals(form.getClubJoinTimeStart())) {
			reqContent.put("clubJoinTimeStart", form.getClubJoinTimeStart());
		}
		if(form.getClubJoinTimeEnd() != null && !"".equals(form.getClubJoinTimeEnd())) {
			reqContent.put("clubJoinTimeEnd", form.getClubJoinTimeEnd());
		}
		
		if(form.getJoinDateSaleDateRel() != null && !"".equals(form.getJoinDateSaleDateRel())) {
			reqContent.put("joinDateSaleDateRel", form.getJoinDateSaleDateRel());
		}
		if(form.getReferFlag() != null && !"".equals(form.getReferFlag())) {
			reqContent.put("referFlag", form.getReferFlag());
			if("1".equals(form.getReferFlag())) {
				if(form.getReferredMemCode() != null && !"".equals(form.getReferredMemCode())) {
					reqContent.put("referredMemCode", form.getReferredMemCode());
				}
			} else if("2".equals(form.getReferFlag())) {
				if(form.getReferrerMemCode() != null && !"".equals(form.getReferrerMemCode())) {
					reqContent.put("referrerMemCode", form.getReferrerMemCode());
				}
			}
		}
		if(form.getParticipateTimeStart() != null && !"".equals(form.getParticipateTimeStart())) {
			reqContent.put("participateTimeStart", form.getParticipateTimeStart());
		}
		if(form.getParticipateTimeEnd() != null && !"".equals(form.getParticipateTimeEnd())) {
			reqContent.put("participateTimeEnd", form.getParticipateTimeEnd());
		}
		if(form.getCampaignCounterId() != null && !"".equals(form.getCampaignCounterId())) {
			reqContent.put("campaignCounterId", form.getCampaignCounterId());
		}
		if(form.getCampaignCounterName() != null && !"".equals(form.getCampaignCounterName())) {
			reqContent.put("campaignCounterName", form.getCampaignCounterName());
		}
		if(form.getCampaignMode() != null && !"".equals(form.getCampaignMode())) {
			reqContent.put("campaignMode", form.getCampaignMode());
		}
		if(form.getCampaignCode() != null && !"".equals(form.getCampaignCode())) {
			reqContent.put("campaignCode", form.getCampaignCode());
		}
		if(form.getCampaignName() != null && !"".equals(form.getCampaignName())) {
			reqContent.put("campaignName", form.getCampaignName());
		}
		if(form.getCampaignState() != null && !form.getCampaignState().isEmpty()) {
			reqContent.put("campaignState", form.getCampaignState());
		}
		if(form.getCouNotEmpty() != null && !"".equals(form.getCouNotEmpty())) {
			reqContent.put("couNotEmpty", form.getCouNotEmpty());
		}
		
		if(form.getLevelAdjustDayFlag() != null && !"".equals(form.getLevelAdjustDayFlag())) {
			reqContent.put("levelAdjustDayFlag", form.getLevelAdjustDayFlag());
			if("2".equals(form.getLevelAdjustDayFlag())) {
				if(form.getLevelAdjustDayRange() != null && !"".equals(form.getLevelAdjustDayRange())) {
					reqContent.put("levelAdjustDayRange", form.getLevelAdjustDayRange());
					reqContent.put("levelAdjustDayUnit", form.getLevelAdjustDayUnit());
				}
			} else if("3".equals(form.getLevelAdjustDayFlag())) {
				if(form.getLevelAdjustDayStart() != null && !"".equals(form.getLevelAdjustDayStart())) {
					reqContent.put("levelAdjustDayStart", form.getLevelAdjustDayStart());
				}
				if(form.getLevelAdjustDayEnd() != null && !"".equals(form.getLevelAdjustDayEnd())) {
					reqContent.put("levelAdjustDayEnd", form.getLevelAdjustDayEnd());
				}
			}
			if(form.getLevelChangeType() != null && !"".equals(form.getLevelChangeType())) {
				reqContent.put("levelChangeType", form.getLevelChangeType());
			}
		}
		
		if(form.getNotSaleDays() != null && !"".equals(form.getNotSaleDays())) {
			reqContent.put("notSaleDays", form.getNotSaleDays());
		}
		if(form.getNotSaleDaysRange() != null && !"".equals(form.getNotSaleDaysRange())) {
			reqContent.put("notSaleDaysRange", form.getNotSaleDaysRange());
		}
		if(form.getFirstStartDay() != null && !"".equals(form.getFirstStartDay())) {
			reqContent.put("firstStartDay", form.getFirstStartDay());
		}
		if(form.getFirstEndDay() != null && !"".equals(form.getFirstEndDay())) {
			reqContent.put("firstEndDay", form.getFirstEndDay());
		}
		if(form.getNoSaleDaysMode() != null && !"".equals(form.getNoSaleDaysMode())) {
			reqContent.put("noSaleDaysMode", form.getNoSaleDaysMode());
		}
		if (form.getLastSaleDateStart() != null && !"".equals(form.getLastSaleDateStart())) {
			reqContent.put("lastSaleDateStart", form.getLastSaleDateStart());
		}
		if (form.getLastSaleDateEnd() != null && !"".equals(form.getLastSaleDateEnd())) {
			reqContent.put("lastSaleDateEnd", form.getLastSaleDateEnd());
		}
		if(form.getPrivilegeFlag() != null && !"".equals(form.getPrivilegeFlag())) {
			reqContent.put("privilegeFlag", form.getPrivilegeFlag());
		}
		if(form.getMemPointRangeJson() != null && !"".equals(form.getMemPointRangeJson())) {
			reqContent.put("memPointRangeJson", form.getMemPointRangeJson());
		}
		if(form.getChangablePointRangeJson() != null && !"".equals(form.getChangablePointRangeJson())) {
			reqContent.put("changablePointRangeJson", form.getChangablePointRangeJson());
		}
		if(form.getLastSaleTimeRangeJson() != null && !"".equals(form.getLastSaleTimeRangeJson())) {
			reqContent.put("lastSaleTimeRangeJson", form.getLastSaleTimeRangeJson());
		}
		if(form.getFirstSaleTimeRangeJson() != null && !"".equals(form.getFirstSaleTimeRangeJson())) {
			reqContent.put("firstSaleTimeRangeJson", form.getFirstSaleTimeRangeJson());
		}
		
		map.put("reqContent", JSONUtil.serialize(reqContent));
		
		if(form.getUserId() != null && !"".equals(ConvertUtil.getString(form.getUserId()))){
			map.put(CherryConstants.USERID, form.getUserId());
		}else{
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			map.put("userId", userInfo.getBIN_UserID());
		}
		// 添加会员搜索条件
		binOLCM33_BL.addSearchRequest(map);
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}
	
	/**
	 * 
	 * 删除会员搜索条件
	 * 
	 */
	public String delSearchRequest() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 删除会员搜索条件
		binOLCM33_BL.deleteSearchRequest(map);
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}
	
	
	/**
	 * 
	 * 查询会员初始画面
	 * 
	 */
	public String searchMemInit() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 查询会员
	 * 
	 */
	public String searchMem() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
//		// 是否带权限查询
//		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		Map<String, Object> resultMap = binOLCM33_BL.searchMemList(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memberInfoList = (List)resultMap.get("list");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 把会员搜索条件转换为文字说明
	 * 
	 */
	public String conditionDisplay() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		String conditionContent = "";
		if(form.getReqContent() != null && !"".equals(form.getReqContent())) {
			Map<String, Object> reqContentMap = ConvertUtil.json2Map(form.getReqContent());
			// 剔除map中的空值
			reqContentMap = CherryUtil.remEmptyVal(reqContentMap);
			map.putAll(reqContentMap);
			// 把会员搜索条件转换为文字说明
			conditionContent = binOLCM39_BL.conditionDisplay(map);
		}
		ConvertUtil.setResponseByAjax(response, conditionContent);
		return null;
	}
	
	/** 历史查询条件List **/
	private List<Map<String, Object>> searchRequestList;
	
	/** 会员等级List **/
	private List<Map<String, Object>> memLevelList;
	
	/** 产品类别List **/
	private List<Map<String, Object>> prtCatPropList;
	
	/** 产品List **/
	private List<Map<String, Object>> proInfoList;
	
	/** 产品分类List **/
	private List<Map<String, Object>> proCatInfoList;
	
	/** 未购买产品List **/
	private List<Map<String, Object>> notProInfoList;
	
	/** 未购买产品分类List **/
	private List<Map<String, Object>> notProCatInfoList;
	
	/** 最多购买的产品大列List **/
	private List<Map<String, Object>> mostCateBClassList;
	
	public List<Map<String, Object>> getMostCateBClassList() {
		return mostCateBClassList;
	}

	public void setMostCateBClassList(List<Map<String, Object>> mostCateBClassList) {
		this.mostCateBClassList = mostCateBClassList;
	}

	/** 最多购买的产品中类List **/
	private List<Map<String, Object>> mostCateMClassList;
	
	public List<Map<String, Object>> getMostCateMClassList() {
		return mostCateMClassList;
	}

	public void setMostCateMClassList(List<Map<String, Object>> mostCateMClassList) {
		this.mostCateMClassList = mostCateMClassList;
	}

	/** 最多购买的产品List **/
	private List<Map<String, Object>> mostPrtList;

	/** 会员信息List **/
	private List<Map<String, Object>> memberInfoList;
	
	/** 月信息List */
	private List<Integer> monthList;
	
	/** 日信息List */
	private List<Integer> dateList;
	
	/** 生日范围开始日信息List */
	private List<Integer> dateRangeStartList;
	
	/** 生日范围结束日信息List */
	private List<Integer> dateRangeEndList;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	public List<Map<String, Object>> getSearchRequestList() {
		return searchRequestList;
	}

	public void setSearchRequestList(List<Map<String, Object>> searchRequestList) {
		this.searchRequestList = searchRequestList;
	}

	public List<Map<String, Object>> getMemLevelList() {
		return memLevelList;
	}

	public void setMemLevelList(List<Map<String, Object>> memLevelList) {
		this.memLevelList = memLevelList;
	}

	public List<Map<String, Object>> getPrtCatPropList() {
		return prtCatPropList;
	}

	public void setPrtCatPropList(List<Map<String, Object>> prtCatPropList) {
		this.prtCatPropList = prtCatPropList;
	}

	public List<Map<String, Object>> getProInfoList() {
		return proInfoList;
	}

	public void setProInfoList(List<Map<String, Object>> proInfoList) {
		this.proInfoList = proInfoList;
	}

	public List<Map<String, Object>> getProCatInfoList() {
		return proCatInfoList;
	}

	public void setProCatInfoList(List<Map<String, Object>> proCatInfoList) {
		this.proCatInfoList = proCatInfoList;
	}

	public List<Map<String, Object>> getNotProInfoList() {
		return notProInfoList;
	}

	public void setNotProInfoList(List<Map<String, Object>> notProInfoList) {
		this.notProInfoList = notProInfoList;
	}

	public List<Map<String, Object>> getNotProCatInfoList() {
		return notProCatInfoList;
	}

	public void setNotProCatInfoList(List<Map<String, Object>> notProCatInfoList) {
		this.notProCatInfoList = notProCatInfoList;
	}

	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	public List<Integer> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<Integer> monthList) {
		this.monthList = monthList;
	}

	public List<Integer> getDateList() {
		return dateList;
	}

	public void setDateList(List<Integer> dateList) {
		this.dateList = dateList;
	}

	public List<Integer> getDateRangeStartList() {
		return dateRangeStartList;
	}

	public void setDateRangeStartList(List<Integer> dateRangeStartList) {
		this.dateRangeStartList = dateRangeStartList;
	}

	public List<Integer> getDateRangeEndList() {
		return dateRangeEndList;
	}

	public void setDateRangeEndList(List<Integer> dateRangeEndList) {
		this.dateRangeEndList = dateRangeEndList;
	}

	public List<Map<String, Object>> getMostPrtList() {
		return mostPrtList;
	}

	public void setMostPrtList(List<Map<String, Object>> mostPrtList) {
		this.mostPrtList = mostPrtList;
	}


	private BINOLCM33_Form form = new BINOLCM33_Form();

	@Override
	public BINOLCM33_Form getModel() {
		return form;
	}

}

