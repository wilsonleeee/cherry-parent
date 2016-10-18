/*
 * @(#)LoginService.java     1.0 2010/10/12
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
package com.cherry.lg.lgn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

@SuppressWarnings("unchecked")
public class LoginService {

	@Resource
	private BaseServiceImpl baseServeceImpl;	
	@Resource
	protected BaseConfServiceImpl baseConfServiceImpl;
	/**
	 * 取得数据源
	 * @param loginName
	 * @return
	 */
	public List getDBByName (String loginName) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("InputName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getDBByName");
		return baseConfServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得指定登录名的相关信息
	 * @param loginName
	 * @return
	 */
	public List checkAccount (String loginName) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("InputName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.checkAccount");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 取得指定登录名的账号安全配置信息
	 * @param loginName
	 * @return
	 */	
	public List getUserSecurityInfo (String userID) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_UserID",userID);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getUserSecurityInfo");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 解锁账号
	 * 将指定账号的已失败登录次数清为0,上次被锁定的时间置为空
	 * @param loginName
	 * @return
	 */
	public int unLockUser(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "login.unLockUser");
		return baseServeceImpl.update(map);
	}
	
	/**
	 * 锁定账号
	 * @param loginName
	 * @return
	 */
	public int lockUser(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "login.lockUser");
		return baseServeceImpl.update(map);
	}
	
	/**
	 * 更新失败次数
	 * @param loginName
	 * @return
	 */
	public int updateFailureCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "login.updateFailureCount");
		return baseServeceImpl.update(map);
	}
	
	/**
	 * 取得指定用户的相关信息
	 * @param loginName
	 * @return
	 */
	public List getUserInfo (String userID,String lanuage) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_UserID",userID);
		parameterMap.put("language",lanuage);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getUserInfo");
		return baseServeceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得用户所属的组织，品牌，岗位等信息
	 * @param employeeID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getControlOrganizationList (int employeeID,String language) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_EmployeeID",employeeID);
		parameterMap.put("language",language);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getControlOrganizationList");
		return baseServeceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得用户拥有的角色信息
	 * @param userID
	 * @param employeeID
	 * @return
	 */
	public List getRoleList (int userID,int organizationID,int positionCategoryID) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_UserID",userID);
		parameterMap.put("BIN_OrganizationID",organizationID);
		parameterMap.put("BIN_PositionCategoryID",positionCategoryID);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getRoleList");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 取得topmenu菜单
	 * @param roleArray
	 * @return
	 */
	public List getPrivilege(int[] roleArray,int userID){
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_UserID",userID);
		parameterMap.put("BIN_RoleID",roleArray);	
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getPrivilege");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 取得topmenu菜单s
	 * @param roleArray
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getTopMenu (int[] roleArray) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_RoleID",roleArray);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getTopMenu");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 取得禁止的topmenu菜单s
	 * @param roleArray
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getForbidTopMenu (int[] roleArray) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("BIN_RoleID",roleArray);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getForbidTopMenu");
		return baseServeceImpl.getList(parameterMap);
	}
	/**
	 * 取得指定topmenu下的左菜单
	 * @param roleArray
	 * @param argMenuId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getLeftMenu (int[] roleArray,String argMenuId) {		
		HashMap parameterMap  = new HashMap();
		parameterMap.put("TopMenuID",argMenuId);
		parameterMap.put("BIN_RoleID",roleArray);	
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getLeftMenu");
		return baseServeceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有的菜单资源
	 * @param roleArray
	 * @return
	 */
	public List getMenuList (int[] roleArray) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("roleList",roleArray);	
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getMenuList");
		return baseServeceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有菜单的子菜单数
	 * @return
	 */
	public List getChildMenuCount () {
		HashMap parameterMap  = new HashMap();
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getChildMenuCount");
		return baseServeceImpl.getList(parameterMap);
	}
	
	/**
     * 更新用户登录信息
     * @param loginName
     * @return
     */
    public int updateLoginInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "login.updateLoginInfo");
        return baseServeceImpl.update(map);
    }
}
