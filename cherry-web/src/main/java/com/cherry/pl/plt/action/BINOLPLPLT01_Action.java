/*
 * @(#)BINOLPLPLT01_Action.java     1.0 2010/10/27
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

package com.cherry.pl.plt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.plt.bl.BINOLPLPLT01_BL;
import com.cherry.pl.plt.form.BINOLPLPLT01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 权限类型一览Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT01_Action extends BaseAction implements ModelDriven<BINOLPLPLT01_Form> {
	
	private static final long serialVersionUID = -3553137023395435541L;
	
	/** 权限类型一览BL */
	@Resource
	private BINOLPLPLT01_BL binOLPLPLT01_BL;
	
	/** 权限类型一览Form */
	private BINOLPLPLT01_Form form = new BINOLPLPLT01_Form();
	
	/**
	 * 权限类型一览初期表示
	 * 
	 * @return 权限类型一览画面
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
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 取得岗位类别信息List
		posCategoryList = binOLPLPLT01_BL.getPositionCategoryList(map);
		return SUCCESS;
	}
	
	/**
	 * 取得权限类型一览
	 * 
	 * @return 权限类型一览画面
	 */
	public String pltList() {
		
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
		}
		// 业务类型
		if(form.getBusinessType() != null && !"".equals(form.getBusinessType())) {
			map.put("businessType", form.getBusinessType());
		}
		// 数据过滤类别设定
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			if("departPl".equals(form.getSSearch())) {
				map.put("category", "0");
			} else if("positionPl".equals(form.getSSearch())) {
				map.put("category", "1");
			}
		}
		// 部门类型
		if(form.getDepartType() != null && !"".equals(form.getDepartType())) {
			map.put("departType", form.getDepartType());
		}
		// 岗位类别
		if(form.getPositionCategoryId() != null && !"".equals(form.getPositionCategoryId())) {
			map.put("positionCategoryId", form.getPositionCategoryId());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 查询权限类型总数
		int count = binOLPLPLT01_BL.getPrivilegeTypeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 查询权限类型List
			privilegeTypeList = binOLPLPLT01_BL.getPrivilegeTypeList(map);
		}
		
		return SUCCESS;
	}
	
	/** 岗位类别List */
	private List<Map<String, Object>> posCategoryList;
	
	/** 权限类型List */
	private List<Map<String, Object>> privilegeTypeList;

	public List<Map<String, Object>> getPosCategoryList() {
		return posCategoryList;
	}

	public void setPosCategoryList(List<Map<String, Object>> posCategoryList) {
		this.posCategoryList = posCategoryList;
	}

	public List<Map<String, Object>> getPrivilegeTypeList() {
		return privilegeTypeList;
	}

	public void setPrivilegeTypeList(List<Map<String, Object>> privilegeTypeList) {
		this.privilegeTypeList = privilegeTypeList;
	}

	@Override
	public BINOLPLPLT01_Form getModel() {
		return form;
	}

}
