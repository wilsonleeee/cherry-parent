/*
 * @(#)BINOLPLRLA99_Service.java     1.0 2010/10/27
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

package com.cherry.pl.rla.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 角色分配Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA99_Service extends BaseService {
	
	/**
	 * 取得角色信息List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getRoleInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.getRoleInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得组织角色List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getOrganizationRoleList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.getOrganizationRoleList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加组织角色
	 * 
	 * @param List
	 */
	public void addOrganizationRole(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLRLA99.addOrganizationRole");
	}
	
	/**
	 * 删除组织角色
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deleteOrganizationRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.deleteOrganizationRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 取得岗位类别角色List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionCategoryRoleList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.getPositionCategoryRoleList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加岗位类别角色
	 * 
	 * @param List
	 */
	public void addPositionCategoryRole(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLRLA99.addPositionCategoryRole");
	}
	
	/**
	 * 删除岗位类别角色
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deletePositionCategoryRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.deletePositionCategoryRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 取得岗位角色List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionRoleList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.getPositionRoleList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加岗位角色
	 * 
	 * @param List
	 */
	public void addPositionRole(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLRLA99.addPositionRole");
	}
	
	/**
	 * 删除岗位角色
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deletePositionRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.deletePositionRole");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 取得用户角色List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getUserRoleList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.getUserRoleList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加用户角色
	 * 
	 * @param List
	 */
	public void addUserRole(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLRLA99.addUserRole");
	}
	
	/**
	 * 删除用户角色
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deleteUserRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA99.deleteUserRole");
		return baseServiceImpl.remove(parameterMap);
	}

}
