/*
 * @(#)BINOLBSPOS06_Action.java     1.0 2010/10/27
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

package com.cherry.bs.pos.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS06_BL;
import com.cherry.bs.pos.form.BINOLBSPOS06_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 岗位类别一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS06_Action extends BaseAction implements ModelDriven<BINOLBSPOS06_Form> {
	
	private static final long serialVersionUID = 869968310510396534L;

	/** 岗位类别一览画面BL */
	@Resource
	private BINOLBSPOS06_BL binOLBSPOS06_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 岗位类别一览画面Form */
	private BINOLBSPOS06_Form form = new BINOLBSPOS06_Form();
	
	/**
	 * 岗位类别一览画面初期处理
	 * 
	 * @return 岗位类别一览画面
	 */
	public String init() {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 语言
			String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
			if(language != null) {
				map.put(CherryConstants.SESSION_LANGUAGE, language);
			}
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
	 * 查询岗位类别一览
	 * 
	 * @return 岗位类别一览画面 
	 */
	public String posCategoryList() {
		
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
		if(form.getCategoryCode() != null && !"".equals(form.getCategoryCode())) {
			// 类别代码
			map.put("categoryCode", form.getCategoryCode());
		}
		if(form.getCategoryName() != null && !"".equals(form.getCategoryName())) {
			// 类别名称
			map.put("categoryName", form.getCategoryName());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put("validFlag", form.getValidFlag());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		int count = binOLBSPOS06_BL.getPositionCategoryCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得岗位类别信息List
			form.setPositionCategoryList(binOLBSPOS06_BL.getPositionCategoryList(map));
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
	public BINOLBSPOS06_Form getModel() {
		return form;
	}

}
