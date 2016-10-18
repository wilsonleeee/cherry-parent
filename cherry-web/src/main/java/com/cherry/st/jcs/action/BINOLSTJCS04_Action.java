/*  
 * @(#)BINOLSTJCS04_Action.java    1.0 2011-8-23     
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
package com.cherry.st.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS04_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS04_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSTJCS04_Action extends BaseAction implements
		ModelDriven<BINOLSTJCS04_Form> {

	private BINOLSTJCS04_Form form = new BINOLSTJCS04_Form();

	private static final long serialVersionUID = 1L;

	private List<Map<String, Object>> brandInfoList;

	private List<Map<String, Object>> departList;

	private List<Map<String, Object>> inventoryList;

	private List<Map<String, Object>> ReaInvenDepartList;

	private String departString;

	private int count = 0;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLSTJCS04_IF binOLSTJCS04_BL;

	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		inventoryList = binOLSTJCS04_BL.getDepotInfoList(map);
		departList = binOLSTJCS04_BL.getDepartList(map);
		return SUCCESS;
	}

	public String search() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put("brandInfoId", form.getBrandInfoId());
		map.put("inventoryCode", form.getInventoryCode());
		map.put("departCode", form.getDepartCode());
		map.put("inventoryName", form.getInventoryName());
		map.put("departName", form.getDepartName());
		ConvertUtil.setForm(form, map);
		count = binOLSTJCS04_BL.getInventoryCount(map);
		ReaInvenDepartList = binOLSTJCS04_BL.getInventoryList(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTJCS04_01";
	}

	public void getAddTreeNodes() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("inventoryInfoId", form.getInventoryInfoId());
		departList = binOLSTJCS04_BL.getJsDepartList(map);
		ConvertUtil.setResponseByAjax(response, departList);
	}

	public String setInvDepRelation() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 作成者为当前用户
			map.put("createdBy", userInfo.getBIN_UserID());
			// 作成程序名为当前程序
			map.put("createPGM", "BINOLSTJCS04");
			// 更新者为当前用户
			map.put("updatedBy", userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put("updatePGM", "BINOLSTJCS04");
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("inventoryInfoId", form.getInventoryInfoId());
			map.put("defaultFlag", form.getDefaultFlag());
			map.put("comments", form.getComments());
			String departString = form.getDepartInfo();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(departString);
			binOLSTJCS04_BL.tran_setInvDepRelation(map, list);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}

	public String saveEditCom() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 作成者为当前用户
			map.put("createdBy", userInfo.getBIN_UserID());
			// 作成程序名为当前程序
			map.put("createPGM", "BINOLSTJCS04");
			// 更新者为当前用户
			map.put("updatedBy", userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put("updatePGM", "BINOLSTJCS04");
			map.put("brandInfoId", form.getBrandInfoId());
			map.put("inventoryInfoId", form.getInventoryInfoId());
			map.put("defaultFlag", form.getDefaultFlag());
			map.put("comments", form.getComments());
			map.put("identityId", form.getIdentityId());
			map.put("organizationId", form.getOrganizationId());
			binOLSTJCS04_BL.tran_saveEditInfo(map);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}

	public String deleteInvDepRelation() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("identityId", form.getIdentityId());
			String identityIdArr = form.getIdentityIdArr();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(identityIdArr);
			binOLSTJCS04_BL.tran_deleteInvDepRelation(list);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}

	@Override
	public BINOLSTJCS04_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Map<String, Object>> departList) {
		this.departList = departList;
	}

	public List<Map<String, Object>> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<Map<String, Object>> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public List<Map<String, Object>> getReaInvenDepartList() {
		return ReaInvenDepartList;
	}

	public void setReaInvenDepartList(
			List<Map<String, Object>> reaInvenDepartList) {
		ReaInvenDepartList = reaInvenDepartList;
	}

	public String getDepartString() {
		return departString;
	}

	public void setDepartString(String departString) {
		this.departString = departString;
	}

}
