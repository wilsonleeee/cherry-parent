/*
 * @(#)BINOLPLRLA04_Action.java     1.0 2010/10/27
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

package com.cherry.pl.rla.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.rla.bl.BINOLPLRLA04_BL;
import com.cherry.pl.rla.form.BINOLPLRLA04_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户角色分配Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA04_Action extends BaseAction implements ModelDriven<BINOLPLRLA04_Form> {
	
	private static final long serialVersionUID = -1801372807729741268L;

	/** 用户角色分配BL */
	@Resource
	private BINOLPLRLA04_BL binOLPLRLA04_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 用户角色分配Form */
	private BINOLPLRLA04_Form form = new BINOLPLRLA04_Form();
	
	/**
	 * 用户一览初期表示
	 * 
	 * @return String 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
		// 取得部门List
		form.setOrgList(binOLCM00_BL.getOrgList(map));
		
		return SUCCESS;
	}
	
	/**
	 * 用户一览
	 * 
	 * @return String 
	 */
	public String userList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		if(form.getLonginName() != null && !"".equals(form.getLonginName())) {
			// 用户帐号
			map.put("longinName", form.getLonginName());
		}
		if(form.getEmployeeName() != null && !"".equals(form.getEmployeeName())) {
			// 员工名称
			map.put("employeeName", form.getEmployeeName());
		}
		if(form.getOrganizationId() != null && !"".equals(form.getOrganizationId())) {
			// 部门ID
			map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
		}
		if(form.getPositionId() != null && !"".equals(form.getPositionId())) {
			// 岗位ID
			map.put(CherryConstants.POSITIONID, form.getPositionId());
		}
		// 默认排序
		//form.setSort("BIN_UserID"+CherryConstants.SPACE+"asc");
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得用户总数
		int count = binOLPLRLA04_BL.getUserInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得用户信息List
			form.setUserInfoList(binOLPLRLA04_BL.getUserInfoList(map));
		}
		return SUCCESS;
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLPLRLA04_Form getModel() {
		return form;
	}

}
