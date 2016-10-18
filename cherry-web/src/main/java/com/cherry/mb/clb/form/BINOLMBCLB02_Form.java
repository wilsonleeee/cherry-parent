/*	
 * @(#)BINOLMBCLB02_Form.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.form;

import java.util.List;
import java.util.Map;

/**
 * 会员俱乐部添加Form
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB02_Form {
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 俱乐部ID */
	private String memberClubId;
	
	/** 俱乐部名称 */
	private String clubName;
	
	/** 俱乐部英文名 */
	private String clubNameForeign;
	
	/** 俱乐部代码 */
	private String clubCode;
	
	/** 描述 */
	private String descriptionDtl;
	
	/** 所选择的子品牌 */
	private String origBrands;
	
	/** 更新时间 */
	private String upTime;
	
	/** 更新次数 */
	private String modCount;
	
	/** 俱乐部模式 */
	private String clubMod;
	
	/** 地点类型 */
	private String locationType;
	
	/** 选择的柜台 */
	private String placeJson;
	
	/** 保存的柜台 */
	private String saveJson;
	
	/** 创建者 */
	private String clubSetBy;
	
	/** 地点list */
	private List<Map<String, Object>> placeList;
	
	/** 所选择的子品牌列表 */
	private List<Map<String, Object>> origBrandList;
	
	/** 俱乐部信息*/
	private Map<String, Object> clubInfo;
	
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getClubCode() {
		return clubCode;
	}

	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}

	public String getDescriptionDtl() {
		return descriptionDtl;
	}

	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}

	public String getOrigBrands() {
		return origBrands;
	}

	public void setOrigBrands(String origBrands) {
		this.origBrands = origBrands;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getClubNameForeign() {
		return clubNameForeign;
	}

	public void setClubNameForeign(String clubNameForeign) {
		this.clubNameForeign = clubNameForeign;
	}

	public Map<String, Object> getClubInfo() {
		return clubInfo;
	}

	public void setClubInfo(Map<String, Object> clubInfo) {
		this.clubInfo = clubInfo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<Map<String, Object>> getOrigBrandList() {
		return origBrandList;
	}

	public void setOrigBrandList(List<Map<String, Object>> origBrandList) {
		this.origBrandList = origBrandList;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getModCount() {
		return modCount;
	}

	public void setModCount(String modCount) {
		this.modCount = modCount;
	}

	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getPlaceJson() {
		return placeJson;
	}

	public void setPlaceJson(String placeJson) {
		this.placeJson = placeJson;
	}

	public String getSaveJson() {
		return saveJson;
	}

	public void setSaveJson(String saveJson) {
		this.saveJson = saveJson;
	}

	public List<Map<String, Object>> getPlaceList() {
		return placeList;
	}

	public void setPlaceList(List<Map<String, Object>> placeList) {
		this.placeList = placeList;
	}

	public String getClubSetBy() {
		return clubSetBy;
	}

	public void setClubSetBy(String clubSetBy) {
		this.clubSetBy = clubSetBy;
	}
}
