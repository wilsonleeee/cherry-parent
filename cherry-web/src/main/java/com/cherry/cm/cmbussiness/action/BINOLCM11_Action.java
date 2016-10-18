/*
 * @(#)BINOLCM11_Action.java     1.0 2010/11/19
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM11_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 促销品分类共通 
 * 
 * @author zj
 */
@SuppressWarnings("unchecked")
public class BINOLCM11_Action extends BaseAction{

	private static final long serialVersionUID = 395966372586167187L;
	
	@Resource
	private BINOLCM11_BL binOLCM11_BL;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 大分类代码 */
	private String primaryCategoryCode;
	
	/** 中分类代码 */
	private String secondryCategoryCode;
	
	/** 小分类代码 */
	private String smallCategoryCode;
	
	/** 品牌信息ID */
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	/** 大分类代码 */
	public void setPrimaryCategoryCode(String primaryCategoryCode) {
		this.primaryCategoryCode = primaryCategoryCode;
	}

	public String getPrimaryCategoryCode() {
		return primaryCategoryCode;
	}

	/** 中分类代码 */
	public void setSecondryCategoryCode(String secondryCategoryCode) {
		this.secondryCategoryCode = secondryCategoryCode;
	}

	public String getSecondryCategoryCode() {
		return secondryCategoryCode;
	}

	/** 小分类代码 */
	public void setSmallCategoryCode(String smallCategoryCode) {
		this.smallCategoryCode = smallCategoryCode;
	}

	public String getSmallCategoryCode() {
		return smallCategoryCode;
	}

	/**
	 * AJAX 取得大分类中英文名称并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryPrimaryCateName() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 大分类代码
		map.put("primaryCategoryCode", primaryCategoryCode);
		// 大分类中英文名称map
		Map primaryMap = binOLCM11_BL.getPrimaryCategoryName(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, primaryMap);
	}
	
	/**
	 * AJAX 取得中分类中英文名称并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySecondryCateName() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 中分类代码
		map.put("secondryCategoryCode", secondryCategoryCode);
		// 取得中分类中英文名称map
		Map secondryMap = binOLCM11_BL.getSecondryCategoryName(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, secondryMap);
	}
	
	/**
	 * AJAX 取得小分类中英文名称并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySmallCateName() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 小分类代码
		map.put("smallCategoryCode", smallCategoryCode);
		// 取得小分类List
		Map smallMap = binOLCM11_BL.getSmallCategoryName(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, smallMap);
	}
}
