/*
 * @(#)BINOLBSDEP01_Service.java     1.0 2010/10/27
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

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 部门一览画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP01_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得某一部门的直属下级部门
	 * 
	 * @param map 检索条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getNextOrganizationInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getNextOrganizationInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得某一用户能访问的顶层部门List
	 * 
	 * @param map 检索条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getFirstOrganizationList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getFirstOrganizationList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得未知节点下的部门List
	 * 
	 * @param map 检索条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getRootNextOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getRootNextOrgList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询部门总数
	 * 
	 * @param map 检索条件
	 * @return 返回部门总数
	 */
	public int getOrganizationInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getOrganizationInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得部门信息List
	 * 
	 * @param map 检索条件
	 * @return 部门信息List
	 */
	public List<Map<String, Object>> getOrganizationInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getOrganizationInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的部门的所有上级部门位置
	 * 
	 * @param map 检索条件
	 * @return 定位到的部门的所有上级部门位置
	 */
	public List<Map<String, Object>> getLocationHigher(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getLocationHigher");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的部门ID
	 * 
	 * @param map 检索条件
	 * @return 定位到的部门ID
	 */
	public String getLocationOrgId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getLocationOrgId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得存在下级节点的部门List
	 * 
	 * @param map 检索条件
	 * @return 部门List
	 */
	public List<String> getHasLowerOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getHasLowerOrgList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门信息List(報表导出)
	 * 
	 * @param map 检索条件
	 * @return 部门信息List
	 */
	public List<Map<String, Object>> getOrganizationList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.organizationInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得用户权限部门类型
	 * 
	 * @param map 检索条件
	 * @return 部门类型List
	 */
	public List<String> getDepartType(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP01.getDepartType");
		return baseServiceImpl.getList(parameterMap);
	}

}
