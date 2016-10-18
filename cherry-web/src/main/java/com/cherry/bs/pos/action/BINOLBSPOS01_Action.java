/*
 * @(#)BINOLBSDEP01_Action.java     1.0 2010/10/27
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

import com.cherry.bs.pos.bl.BINOLBSPOS01_BL;
import com.cherry.bs.pos.bl.BINOLBSPOS04_BL;
import com.cherry.bs.pos.form.BINOLBSPOS01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 岗位一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS01_Action extends BaseAction implements ModelDriven<BINOLBSPOS01_Form> {
	
	private static final long serialVersionUID = -5310544241186817763L;
	
	/** 岗位一览画面BL */
	@Resource
	private BINOLBSPOS01_BL binOLBSPOS01_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM00_BL binolcm00BL;
	
	/** 添加岗位画面BL */
	@Resource
	private BINOLBSPOS04_BL binOLBSPOS04_BL;
	
	/** 岗位一览画面Form */
	private BINOLBSPOS01_Form form = new BINOLBSPOS01_Form();
	
	
	/**
	 * 岗位一览画面初期处理
	 * 
	 * @return 岗位一览画面
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 岗位一览（树模式）画面初期处理
	 * 
	 * @return 岗位一览画面 
	 */
	public String treeInit() {
		
		return SUCCESS;
	}
	
	/**
	 * 岗位一览（列表模式）画面初期处理
	 * 
	 * @return 岗位一览画面 
	 */
	public String listInit() {
		
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
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 取得部门List
		form.setOrgList(binolcm00BL.getOrgList(map));
		//  取得上级岗位信息List
		form.setHigherPositionList(binOLBSPOS01_BL.getHigherPositionList(map));
		
		return SUCCESS;
	}
	
	/**
	 * 岗位一览（列表模式）
	 * 
	 * @return 岗位一览画面 
	 */
	public String positionList() {
		
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
		if(form.getOrganizationId() != null && !"".equals(form.getOrganizationId())) {
			// 所属部门ID
			map.put("organizationId", form.getOrganizationId());
		}
		if(form.getPositionName() != null && !"".equals(form.getPositionName())) {
			// 岗位名称
			map.put("positionName", form.getPositionName());
		}
		if(form.getPath() != null && !"".equals(form.getPath())) {
			// 上级岗位节点位置
			map.put("path", form.getPath());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put("validFlag", form.getValidFlag());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		int count = binOLBSPOS01_BL.getPositionCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得岗位信息List
			form.setPositionList(binOLBSPOS01_BL.getPositionList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX取得某一岗位的直属下级岗位
	 * 
	 * @return 岗位一览画面 
	 */
	public String next() throws Exception {
		
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
		// 岗位节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 取得某一岗位的直属下级部门
		String positionTree = binOLBSPOS01_BL.getNextPositionList(map);
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(positionTree);
		
		return null;
	}
	
	/**
	 * 取得品牌ID下的上级岗位List和部门List
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
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
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 取得部门List
		resultMap.put("orgList", binolcm00BL.getOrgList(map));
		//  取得上级岗位信息List
		resultMap.put("higherPositionList", binOLBSPOS01_BL.getHigherPositionList(map));
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
	}
	
	/**
	 * 根据部门ID过滤下拉列表数据
	 * 
	 */
	public String filterByOrg() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
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
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		if(form.getOrganizationId() != null && !"".equals(form.getOrganizationId())) {
			// 所属部门
			map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
			// 取得上级岗位信息List
			resultMap.put("higherPositionList", binOLBSPOS04_BL.getPositionByOrg(map));
		} else {
			// 取得上级岗位信息List
			resultMap.put("higherPositionList", binOLBSPOS01_BL.getHigherPositionList(map));
		}
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
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
	public BINOLBSPOS01_Form getModel() {
		return form;
	}

}
