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
import com.cherry.pl.rla.form.BINOLPLRLA05_Form;
import com.cherry.pl.rla.interfaces.BINOLPLRLA05_IF;
import com.opensymphony.xwork2.ModelDriven;

/*  
 * @(#)BINOLPLRLA05_Action.java    1.0 2012-4-1     
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

/**
 * 角色人员关系查询
 * 
 * @author zhanggl
 * @version 1.0 2012/04/01
 * 
 * */
public class BINOLPLRLA05_Action extends BaseAction implements ModelDriven<BINOLPLRLA05_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLPLRLA05_Form form = new BINOLPLRLA05_Form();
	
	/**品牌List*/
	private List<Map<String,Object>> brandInfoList = null;
	
	/** 岗位类别信息List */
	private List<Map<String,Object>> positionCategoryList = null;
	
	/**根据人员查询角色画面	员工LIST*/
	private List<Map<String,Object>> employeesList = null;
	
	private List<Map<String,Object>> rolesList = null;
	
	private List<Map<String,Object>> resourceList = null;
	
	/**根据人员查询角色画面	员工总数*/
	private int count = 0;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	@Resource
	private BINOLPLRLA05_IF binOLPLRLA05_BL;
	
	/**
	 * 期初页面加载
	 * 
	 * */
	public String init(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
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
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		
		// 参数MAP
		Map<String, Object> map1 = new HashMap<String, Object>();
		// 所属组织
		map1.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map1.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map1.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		// 取得岗位类别信息List
		positionCategoryList = binOLCM00_BL.getPositionCategoryList(map1);
		
		return SUCCESS;
	}

	
	/**
	 * 根据员工查询角色画面	查询员工信息
	 * 
	 * */
	public String getEmployees(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.Brand_CODE_ALL){
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}else{
			map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		}
		// 当前用户的所属组织
		map.put("BIN_OrganizationInfoID", userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		//员工CODE
		map.put("EmployeeCode", form.getEmployeeCode());
		//员工姓名
		map.put("EmployeeName", form.getEmployeeName());
		//登录帐号
		map.put("LonginName", form.getUserName());
		//岗位类别
		map.put("BIN_PositionCategoryID", form.getPostId());
		
		employeesList = binOLPLRLA05_BL.getEmployeeList(map);
		count = binOLPLRLA05_BL.getEmployeeCount(map);
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLPLRLA05_1";
	}
	
	/**
	 * 根据角色查询员工画面	查询角色信息
	 * 
	 * */
	public String getRoles(){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.Brand_CODE_ALL){
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}else{
			map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		}
		// 当前用户的所属组织
		map.put("BIN_OrganizationInfoID", userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		//角色名称
		map.put("RoleName", form.getRoleName());
		//角色类型
		map.put("RoleKind", form.getRoleKind());
		
		rolesList = binOLPLRLA05_BL.getRoleList(map);
		count = binOLPLRLA05_BL.getRoleCount(map);
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return "BINOLPLRLA05_3";
	}
	
	/**
	 * 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
	 * 
	 * */
	public String getRolesByEmployee(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		//员工ID
		map.put("BIN_EmployeeID", form.getEmployeeId());
		
		rolesList = binOLPLRLA05_BL.getRolesByEmployee(map);
		
		return "BINOLPLRLA05_2";
	}
	
	/**
	 * 根据某个角色查询出拥有该角色的员工
	 * 
	 * */
	public String getEmployeesByRole(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.Brand_CODE_ALL){
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
		}else{
			map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		}
		// 当前用户的所属组织
		map.put("BIN_OrganizationInfoID", userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		//角色ID
		map.put("BIN_RoleID", form.getRoleId());
		//角色类型
		map.put("RoleKind", form.getRoleKind());
		//查询输入
		map.put("inputting", form.getSSearch());
		
		Map<String,Object> resultMap = binOLPLRLA05_BL.getEmployeesByRole(map);
		
		employeesList = (List<Map<String, Object>>) resultMap.get("list");
		count = (Integer) resultMap.get("count");
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return "BINOLPLRLA05_4";
	}
	
	/**
	 * 根据员工ID查询其所拥有的菜单资源
	 * 
	 **/
	public String getMenusByEmployee() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		//员工ID
		map.put("BIN_EmployeeID", form.getEmployeeId());
		
		resourceList = binOLPLRLA05_BL.getMenusByEmployee(map);
		
		return "BINOLPLRLA05_5";
	}
	
	
	@Override
	public BINOLPLRLA05_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String,Object>> getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(List<Map<String,Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}
	
	public List<Map<String, Object>> getEmployeesList() {
		return employeesList;
	}

	public void setEmployeesList(List<Map<String, Object>> employeesList) {
		this.employeesList = employeesList;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Map<String, Object>> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Map<String, Object>> rolesList) {
		this.rolesList = rolesList;
	}


	public List<Map<String, Object>> getResourceList() {
		return resourceList;
	}


	public void setResourceList(List<Map<String, Object>> resourceList) {
		this.resourceList = resourceList;
	}
	
}
