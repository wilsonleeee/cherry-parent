/*	
 * @(#)BINOLJNMAN01_Action.java     1.0 2011/4/18		
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
package com.cherry.jn.man.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.common.bl.BINOLJNCOM01_BL;
import com.cherry.jn.common.interfaces.BINOLJNCOM01_IF;
import com.cherry.jn.man.form.BINOLJNMAN01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 入会条件查看Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNMAN01_Action extends BaseAction implements
ModelDriven<BINOLJNMAN01_Form> {

	private static final long serialVersionUID = 7989677590066258208L;
	
	/** 参数FORM */
	private BINOLJNMAN01_Form form = new BINOLJNMAN01_Form();
	
	@Resource
    private BINOLJNCOM01_IF binoljncom01IF;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNCOM01_BL binoljncom01_BL;
	
	/** 所管辖的品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 等级有效期List */
	private List<Map<String, Object>> levelDateList;
	
	/** 会员活动信息List */
	private List<Map<String, Object>> camtempList;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	/** 品牌名称 */
	private String brandName;
	
	@Override
	public BINOLJNMAN01_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public List<Map<String, Object>> getLevelDateList() {
		return levelDateList;
	}

	public void setLevelDateList(List<Map<String, Object>> levelDateList) {
		this.levelDateList = levelDateList;
	}

	public List<Map<String, Object>> getCamtempList() {
		return camtempList;
	}

	public void setCamtempList(List<Map<String, Object>> camtempList) {
		this.camtempList = camtempList;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * <p>
	 * 会员一览画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		form.setCampaignType("1");
		form.setCampaignTypeName(getText("PMB00019"));
		pageInit();
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 升降级画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String initUp() throws Exception {
		form.setCampaignType("2");
		form.setCampaignTypeName(getText("PMB00020"));
		pageInit();
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void pageInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		//int brandInfoId = CherryConstants.BRAND_INFO_ID_VALUE;
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandInfoList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
			// 取得品牌名称
			brandName = binOLCM05_BL.getBrandName(map);
			// 品牌ID
			form.setBrandInfoId(ConvertUtil.getString(brandInfoId));
		}
		clubList = binOLCM05_BL.getClubList(map);
		if (null != clubList && !clubList.isEmpty()) {
			String memberClubId = String.valueOf(clubList.get(0).get("memberClubId"));
			form.setMemberClubId(memberClubId);
			map.put("memberClubId", memberClubId);
		}
		// 取得会员活动等级List
		levelDateList = binoljncom01_BL.getMemberLevelList(map);
	}
	
	/**
	 * <p>
	 * 切换品牌
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void changeBrand() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", form.getBrandInfoId());
		// 活动类型
		map.put("campaignType", form.getCampaignType());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		// 取得会员活动等级List
		levelDateList = binoljncom01_BL.getMemberLevelList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, levelDateList);
		// 取得会员活动信息List
		camtempList = binoljncom01IF.convertMemberLevelList(map);
	}
	
	/**
	 * <p>
	 * 切换活动
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String changeCampaign() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", form.getBrandInfoId());
		// 会员俱乐部ID
		map.put("memberClubId", form.getMemberClubId());
		// 等级有效期
		map.put("levelDate", form.getLevelDate());
		// 活动类型
		map.put("campaignType", form.getCampaignType());
		// 取得会员活动等级信息
		camtempList = binoljncom01IF.convertMemberLevelList(map);
		return SUCCESS;
	}
	
}
