/*
 * @(#)BINOLBSREG01_Action.java     1.0 2011/11/23
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
package com.cherry.bs.reg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.form.BINOLBSREG01_Form;
import com.cherry.bs.reg.interfaces.BINOLBSREG01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 区域一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG01_Action extends BaseAction implements ModelDriven<BINOLBSREG01_Form> {
	
	private static final long serialVersionUID = 5291827955892269036L;
	
	/** 区域一览画面BL */
	@Resource
	private BINOLBSREG01_IF binOLBSREG01_BL;
	
	/**
	 * 
	 * 区域一览（树模式）画面初期处理
	 * 
	 * @return 区域一览画面 
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * AJAX区域树结构信息
	 * 
	 */
	public String treeNext() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		
		String regionId = form.getRegionId();
		if(regionId != null && !"".equals(regionId)) {
			map.put("regionId", regionId);
		} else {
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				map.put("regionType", "0");
			} else {
				map.put("regionType", "-1");
			}
		}
		// 取得区域树结构信息
		String regionTreeInfo = binOLBSREG01_BL.getRegionTree(map);
		ConvertUtil.setResponseByAjax(response, regionTreeInfo);
		return null;
	}
	
	/**
	 * 
	 * 查询定位到的区域的所有上级区域位置
	 * 
	 */
	public String locateHigher() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put("regionId", form.getRegionId());
		// 取得定位到的区域的所有上级区域位置
		List<String> locationHigher = binOLBSREG01_BL.getLocationHigher(map);
		ConvertUtil.setResponseByAjax(response, locationHigher);
		return null;
	}
	
	/**
	 * 
	 * 查询定位到的区域ID
	 * 
	 */
	public String locateRegionId() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put("locationPosition", form.getLocationPosition());
		// 取得定位到的区域ID
		String regionId = binOLBSREG01_BL.getLocationRegionId(map);
		ConvertUtil.setResponseByAjax(response, regionId);
		return null;
	}
	
	/** 区域一览画面Form */
	private BINOLBSREG01_Form form = new BINOLBSREG01_Form();

	@Override
	public BINOLBSREG01_Form getModel() {
		return form;
	}

}
