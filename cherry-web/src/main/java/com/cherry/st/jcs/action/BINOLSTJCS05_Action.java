
/*  
 * @(#)BINOLSTJCS05_Action.java    1.0 2011-9-5     
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS05_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS04_IF;
import com.cherry.st.jcs.interfaces.BINOLSTJCS05_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSTJCS05_Action extends BaseAction implements
		ModelDriven<BINOLSTJCS05_Form> {

	
	private static final long serialVersionUID = 1L;

	private BINOLSTJCS05_Form form = new BINOLSTJCS05_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLSTJCS05_IF binOLSTJCS05_BL;
	@Resource
	private BINOLSTJCS04_IF binOLSTJCS04_BL;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private List<Map<String, Object>> brandInfoList;
	//仓库业务关系List
	private List<Map<String,Object>> depotBusinessList;
	//非柜台仓库LIST
	private List<Map<String, Object>> inventoryList;
	//实体仓库业务配置模式
	private String configModel;
	
	private int count = 0;
	
	/**
	 * 页面期初加载
	 * 
	 * */
	public String init()
	{
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
		//取得系统基本配置信息中的"仓库业务配置"
		configModel = binOLCM14_BL.getConfigValue("1028",String.valueOf(userInfo.getBIN_OrganizationInfoID()),String.valueOf(userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * 
	 * */
	public String search()
	{
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
		map.put("name", form.getName().trim());
		map.put("code", form.getCode().trim());
		map.put("businessType", form.getBusinessType().trim());
		ConvertUtil.setForm(form, map);
		//取得系统基本配置信息中的"仓库业务配置"
		configModel = binOLCM14_BL.getConfigValue("1028",String.valueOf(userInfo.getBIN_OrganizationInfoID()),String.valueOf(userInfo.getBIN_BrandInfoID()));
		map.put("configModel", configModel);
		//取得配置List
		depotBusinessList = binOLSTJCS05_BL.getDepotBusinessList(map);
		//取得配置总数
		count = binOLSTJCS05_BL.getDepotBusinessCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return "BINOLSTJCS05_01";
	}
	
	/**
	 * 获取添加时的根节点
	 * 
	 * 
	 * */
	public void getAddTreeNodes() throws Exception{
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
		map.put("configByDepOrg", form.getConfigByDepOrg());
		List<Map<String,Object>> resultList = binOLSTJCS05_BL.getAddTree(map);
		ConvertUtil.setResponseByAjax(response, resultList);
	}
	
	/**
	 * 保存添加
	 * 
	 * 
	 * */
	public String saveAdd()throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
			map.put("BusinessType", form.getBusinessType());
			map.put("OutID", form.getDeportId());
			map.put("OutIdFlag", "0");
			map.put("CreatedBy", userInfo.getBIN_UserID());
			map.put("UpdatedBy", userInfo.getBIN_UserID());
			map.put("flag", form.getFlag());
			String deportOrRegionStr = form.getDeportOrRegionStr();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(deportOrRegionStr);
			binOLSTJCS05_BL.tran_saveAdd(map, list);
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
	
	/**
	 * 编辑
	 * 
	 * 
	 * */
	public String saveEdit()throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			//组织ID
			map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			//品牌ID
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
			//业务类型
			String businessType = ConvertUtil.getString(form.getBusinessType());
			map.put("BusinessType", businessType);
			//根据业务类型决定入出库方
			map.put(CherryConstants.OPERATE_RR.equals(businessType)? "InID":"OutID", form.getDeportId());
			map.put(CherryConstants.OPERATE_RR.equals(businessType)? "InIdFlag":"OutIdFlag", "0");
			map.put("CreatedBy", userInfo.getBIN_UserID());
			map.put("UpdatedBy", userInfo.getBIN_UserID());
			map.put("flag", form.getFlag());
			map.put("configByDepOrg", form.getConfigByDepOrg());
			String deportOrRegionStr = form.getDeportOrRegionStr();
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(deportOrRegionStr);
			binOLSTJCS05_BL.tran_saveEdit(map, list);
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
	
	
	/**
	 * 逻辑删除
	 * 
	 * */
	public String deleteDepotBusiness() throws Exception{
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.CREATEPGM, "BINOLSTJCS05");
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			map.put(CherryConstants.UPDATEPGM, "BINOLSTJCS05");
			String deportBusinessStr = form.getDeportBusinessStr();
			List<Map<String,Object>> deportBusinessList = (List<Map<String, Object>>) JSONUtil.deserialize(deportBusinessStr);
			binOLSTJCS05_BL.tran_deleteDepotBusiness(deportBusinessList, map);
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
	
	/**
	 * 获取要编辑的仓库信息，包括树节点
	 * 
	 * 
	 * */
	public void getEditInfo()throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//业务关系ID
		map.put("BIN_DepotBusinessID", form.getDepotBusinessId());
		//业务类型
		map.put("BusinessType", form.getBusinessType());
		//用户组织
		map.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
		//用户组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 按部门组织架构配置
		map.put("configByDepOrg", form.getConfigByDepOrg());
		Map<String,Object> resultMap = binOLSTJCS05_BL.getEditInfo(map);
		
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	@Override
	public BINOLSTJCS05_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Map<String, Object>> getDepotBusinessList() {
		return depotBusinessList;
	}

	public void setDepotBusinessList(List<Map<String, Object>> depotBusinessList) {
		this.depotBusinessList = depotBusinessList;
	}

	public List<Map<String, Object>> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<Map<String, Object>> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public String getConfigModel() {
		return configModel;
	}

	public void setConfigModel(String configModel) {
		this.configModel = configModel;
	}
	
}
