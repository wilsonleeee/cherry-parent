/*
 * @(#)BINOLPLRLM03_Action.java     1.0 2010/10/27
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

import com.cherry.cm.cmbeans.RoleInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rlm.bl.BINOLPLRLM03_BL;

/**
 * 角色授权Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM03_Action extends BaseAction {
	
	private static final long serialVersionUID = 7455706460057710002L;

	/** 角色管理BL */
	@Resource
	private BINOLPLRLM03_BL binOLPLRLM03_BL;
	
	/**
	 * 角色授权初期表示
	 * 
	 * @return String 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户不是系统内置管理员的场合，需要按权限查看
		if(userInfo.getBIN_OrganizationInfoID() != -9999) {
//			List<RoleInfo> roleInfoList = userInfo.getRolelist();
//			if(roleInfoList != null && !roleInfoList.isEmpty()) {
//				List<Object> isRoleList = new ArrayList<Object>();
//				List<Object> notRoleList = new ArrayList<Object>();
//				for(RoleInfo roleInfo : roleInfoList) {
//					String privilegeFlag = roleInfo.getPrivilegeFlag();
//					if(privilegeFlag != null && "0".equals(privilegeFlag)) {
//						notRoleList.add(roleInfo.getBIN_RoleID());
//					} else {
//						isRoleList.add(roleInfo.getBIN_RoleID());
//					}
//				}
//				if(isRoleList != null && !isRoleList.isEmpty()) {
//					// 用户拥有的角色(允许权限)
//					map.put("isRoleList", isRoleList);
//				}
//				if(notRoleList != null && !notRoleList.isEmpty()) {
//					// 用户拥有的角色(禁止权限)
//					map.put("notRoleList", notRoleList);
//				}
//			}
			// 品牌用户在组织管理员权限下看数据
			map.put("brandRole", "2");
		}
		// 角色ID
		map.put(PrivilegeConstants.ROLE_ID, roleId);
		// 取得所有的功能权限
		resourceList = binOLPLRLM03_BL.getResourceList(map);
		
		return SUCCESS;
	}
	
//	/**
//	 * 取得画面对应的控件资源
//	 * 
//	 * @return String 
//	 */
//	public String searchControl() {
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 角色ID
//		map.put(PrivilegeConstants.ROLE_ID, roleId);
//		// 画面ID
//		map.put(PrivilegeConstants.PAGE_ID, pageId[0]);
//		// 取得画面对应的控件资源List
//		controlList = binOLPLRLM03_BL.getPageControlList(map);
//		return SUCCESS;
//	}
	
	/**
	 * 角色授权处理
	 * 
	 * @return String 
	 */
	public String roleAuthorize() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLRLM03");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLRLM03");
		// 角色ID
		map.put(PrivilegeConstants.ROLE_ID, roleId);
//		// 子系统ID
//		map.put(PrivilegeConstants.SUB_SYS_ID, subSysId);
//		// 模块ID
//		map.put(PrivilegeConstants.MODULE_ID, moduleId);
//		// 功能ID
//		map.put(PrivilegeConstants.FUNCTION_ID, functionId);
//		// 画面ID
//		map.put(PrivilegeConstants.PAGE_ID, pageId);
//		// 控件ID
//		map.put(PrivilegeConstants.CONTROL_ID, controlId);
		// 菜单资源ID
		map.put("menuId", menuId);
		// 角色授权处理
		binOLPLRLM03_BL.tran_RoleAuthorize(map);
		return SUCCESS;
	}
	
	/** 子系统ID */
	private String[] subSysId;
	
	/** 模块ID */
	private String[] moduleId;
	
	/** 功能ID */
	private String[] functionId;
	
	/** 画面ID */
	private String[] pageId;
	
	/** 控件ID */
	private String[] controlId;
	
	/** 角色ID */
	private String roleId;
	
	/** 菜单资源ID */
	private String[] menuId;
	
	/** 控件List */
	private List<Map<String, Object>> controlList;
	
	/** 功能资源List */
	private List<Map<String, Object>> resourceList;

	public String[] getSubSysId() {
		return subSysId;
	}

	public void setSubSysId(String[] subSysId) {
		this.subSysId = subSysId;
	}

	public String[] getModuleId() {
		return moduleId;
	}

	public void setModuleId(String[] moduleId) {
		this.moduleId = moduleId;
	}

	public String[] getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String[] functionId) {
		this.functionId = functionId;
	}

	public String[] getPageId() {
		return pageId;
	}

	public void setPageId(String[] pageId) {
		this.pageId = pageId;
	}

	public String[] getControlId() {
		return controlId;
	}

	public void setControlId(String[] controlId) {
		this.controlId = controlId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<Map<String, Object>> getControlList() {
		return controlList;
	}

	public void setControlList(List<Map<String, Object>> controlList) {
		this.controlList = controlList;
	}

	public List<Map<String, Object>> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Map<String, Object>> resourceList) {
		this.resourceList = resourceList;
	}

	public String[] getMenuId() {
		return menuId;
	}

	public void setMenuId(String[] menuId) {
		this.menuId = menuId;
	}
	
}
