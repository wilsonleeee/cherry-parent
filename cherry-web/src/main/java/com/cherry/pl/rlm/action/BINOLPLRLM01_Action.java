/*
 * @(#)BINOLPLRLM01_Action.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rlm.bl.BINOLPLRLM01_BL;
import com.cherry.pl.rlm.form.BINOLPLRLM01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 角色一览Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLM01_Action extends BaseAction implements ModelDriven<BINOLPLRLM01_Form> {
	
	private static final long serialVersionUID = -6480730530764515094L;
	
	/** 角色一览BL */
	@Resource
	private BINOLPLRLM01_BL binOLPLRLM01_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 角色一览Form */
	private BINOLPLRLM01_Form form = new BINOLPLRLM01_Form();

	/**
	 * 角色一览初期表示
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
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
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
		return SUCCESS;
	}
	
	/**
	 * 取得角色信息
	 * 
	 * @return String 
	 */
	public String roleList() {
		
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
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		// 角色分类
		if(form.getRoleKind() != null && !"".equals(form.getRoleKind())) {
			map.put(PrivilegeConstants.ROLE_KIND, form.getRoleKind());
		}
		// 角色名称或描述
		if(form.getRoleKw() != null && !"".equals(form.getRoleKw())) {
			map.put(PrivilegeConstants.ROLE_KW, form.getRoleKw());
		}
		// 默认排序
		form.setSort("BIN_RoleID"+CherryConstants.SPACE+"asc");
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 取得角色信息总数
		int count = binOLPLRLM01_BL.getRoleInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得角色信息List
			form.setRoleInfoList(binOLPLRLM01_BL.getRoleInfoList(map));
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
	public BINOLPLRLM01_Form getModel() {
		return form;
	}

}
