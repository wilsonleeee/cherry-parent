/*
 * @(#)BINOLBSDEP91_Service.java     1.0 2011.2.10
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

package com.cherry.bs.dep.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 组织信息管理共通Service
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP91_Service extends BaseService {
	
	/**
	 * 查询组织总数
	 * 
	 * @param map 检索条件
	 * @return 返回组织总数
	 */
	public int getOrganizationCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.getOrganizationCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得组织信息List
	 * 
	 * @param map 检索条件
	 * @return 组织信息List
	 */
	public List<Map<String, Object>> getOrganizationList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.getOrganizationList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询组织信息
	 * 
	 * @param map 查询条件
	 * @return 组织信息
	 */
	public Map<String, Object> getOrganization(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.getOrganization");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 判断组织代码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 组织ID
	 */
	public String getOrgIdByOrgCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.getOrgIdByOrgCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 判断组织名称是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 组织ID
	 */
	public String getOrganizationInfoID(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.getOrganizationInfoID");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加组织
	 * 
	 * @param map 添加内容
	 * @return 组织ID
	 */
	public int addOrganization(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.addOrganization");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 
	 * 更新组织
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateOrganizationInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.updateOrganizationInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 更新部门信息
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateOrganization(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.updateOrganization");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 伦理删除组织
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int deleteOrganization(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.deleteOrganization");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 添加管理员
	 * 
	 * @param map 添加内容
	 * @return 管理员ID
	 */
	public int addUser(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.addUser");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加管理员角色
	 * 
	 * @param map 添加内容
	 */
	public void addUserRole(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP91.addUserRole");
		baseServiceImpl.save(parameterMap);
	}

}
