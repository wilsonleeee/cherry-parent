/*
 * @(#)BINOLPLRLM99_Service.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 角色管理Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLM99_Service extends BaseService {
	
	/**
	 * 取得角色信息总数
	 * 
	 * @param Map
	 * @return 返回角色信息总数 
	 */
	public int getRoleInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得角色信息List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getRoleInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除角色
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deleteRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deleteRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除组织角色对应关系
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deleteOrganizationRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deleteOrganizationRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除岗位类别角色对应关系
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deletePositionCategoryRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deletePositionCategoryRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除岗位角色对应关系
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deletePositionRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deletePositionRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除用户角色对应关系
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deleteUserRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deleteUserRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 查询指定角色是否存在
	 * 
	 * @param Map
	 * @return 返回角色数 
	 */
	public Map<String, Object> getRoleByRoleName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleByRoleName");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加角色
	 * 
	 * @param Map
	 * @return 返回一个自增长id
	 */
	public int addRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.addRole");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 取得角色信息
	 * 
	 * @param Map
	 * @return Map 
	 */
	public Map<String, Object> getRoleInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新角色
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int updateRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.updateRole");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 取得所有的功能权限
	 * 
	 * @return List 
	 */
	public List<Map <String, Object>> getResourceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getResourceList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得指定角色的所有授权List
	 * 
	 * @param Map
	 * @return List  
	 */
	public List<Map<String, Object>> getRoleResourceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleResourceList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得画面对应的控件资源List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPageControlList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getPageControlList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得角色对应的某个画面的控件资源List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getRoleControlList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getRoleControlList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除角色已有的的功能资源
	 * 
	 * @param Map
	 * @return 处理件数
	 */
	public int deleteRoleResource(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.deleteRoleResource");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 保存角色功能资源
	 * 
	 * @param Map
	 */
	public void addRoleResource(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLRLM99.addRoleResource");
	}
	
	/**
	 * 查询所有的菜单资源
	 * 
	 * @return List 
	 */
	public List<Map <String, Object>> getMenuList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getMenuList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有的禁止菜单资源
	 * 
	 * @return List 
	 */
	public List<Map<String, Object>> getNotMenuList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getNotMenuList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有菜单的子菜单数
	 * @return
	 */
	public List<Map<String, Object>> getChildMenuCount () {
		HashMap parameterMap  = new HashMap();
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLM99.getChildMenuCount");
		return baseServiceImpl.getList(parameterMap);
	}

}
