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

package com.cherry.bs.dep.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP01_BL;
import com.cherry.bs.dep.form.BINOLBSDEP01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 部门一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP01_Action extends BaseAction implements ModelDriven<BINOLBSDEP01_Form> {
	
	private static final long serialVersionUID = -709727328524831027L;

	/** 部门一览画面BL */
	@Resource
	private BINOLBSDEP01_BL binOLBSDEP01_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
    
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 部门一览画面Form */
	private BINOLBSDEP01_Form form = new BINOLBSDEP01_Form();
	
	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;

	/**
	 * 部门一览画面初期处理
	 * 
	 * @return 部门一览画面 
	 */
	public String init() {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 是否支持部门协同
		form.setMaintainOrgSynergy(binOLCM14_BL.isConfigOpen("1371", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * 部门一览（树模式）画面初期处理
	 * 
	 * @return 部门一览画面 
	 */
	public String treeInit() {
		
		return SUCCESS;
	}
	
	/**
	 * 部门一览（列表模式）画面初期处理
	 * 
	 * @return 部门一览画面 
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
		}
		
		// 是否支持部门协同
		form.setMaintainOrgSynergy(binOLCM14_BL.isConfigOpen("1371", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()));
				
		return SUCCESS;
	}
	
	/**
	 * 部门一览（列表模式）
	 * 
	 * @return 部门一览画面 
	 */
	public String organizationList() {
		
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
		if(form.getDepartCode() != null && !"".equals(form.getDepartCode())) {
			// 部门代码
			map.put("departCode", form.getDepartCode());
		}
		if(form.getDepartName() != null && !"".equals(form.getDepartName())) {
			// 部门名称
			map.put("departName", form.getDepartName());
		}
		if(form.getPath() != null && !"".equals(form.getPath())) {
			// 上级部门节点位置
			map.put("path", form.getPath());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put("validFlag", form.getValidFlag());
		}
		if(form.getType() != null && !"".equals(form.getType())) {
			// 部门类型
			map.put("type", form.getType());
		}
		if(form.getStatus() != null && !"".equals(form.getStatus())) {
			// 部门状态
			map.put("status", form.getStatus());
		}
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		int count = binOLBSDEP01_BL.getOrganizationInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门信息List
			form.setOrganizationList(binOLBSDEP01_BL.getOrganizationInfoList(map));
		}
		// 是否支持部门协同
		form.setMaintainOrgSynergy(binOLCM14_BL.isConfigOpen("1371", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * AJAX取得某一部门的直属下级部门
	 * 
	 * @return 部门一览画面 
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
		// 部门节点位置
		map.put(CherryConstants.PATH, form.getPath());
		// 有效区分
		map.put("validFlag", form.getValidFlag());
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		// 取得某一部门的直属下级部门
		String organizationTree = binOLBSDEP01_BL.getNextOrganizationList(map);
		
		ConvertUtil.setResponseByAjax(response, organizationTree);
		return null;
	}
	
	/**
	 * 查询定位到的部门的所有上级部门位置
	 * 
	 */
	public String locateOrg() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
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
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		List<String> locationHigher = binOLBSDEP01_BL.getLocationHigher(map);
		ConvertUtil.setResponseByAjax(response, locationHigher);
		return null;
	}
	
	/**
	 * 导出Excel
	 * 
	 * @throws JSONException
	 */
	public String export() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
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
		if(form.getDepartCode()!=null){
	        //部门代码
	        map.put("departCode", form.getDepartCode());
		}
		if(form.getDepartName()!=null){
	        //部门名称
	        map.put("departName", form.getDepartName());
		}
		if(form.getType()!=null){
	        //部门类型
	        map.put("type", form.getType());
		}
		if(form.getStatus()!=null){
	        //部门状态
	        map.put("status", form.getStatus());
		}
		// 取得信息List
		try {
			exportName = binOLBSDEP01_BL.getExportName(map);
			excelStream = new ByteArrayInputStream(
					binOLBSDEP01_BL.exportExcel(map));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLBSDEP01_Form getModel() {
		return form;
	}
	
}
