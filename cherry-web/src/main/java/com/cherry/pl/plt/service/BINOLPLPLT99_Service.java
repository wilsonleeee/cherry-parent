/*
 * @(#)BINOLPLPLT99_Service.java     1.0 2010/10/27
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

package com.cherry.pl.plt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 权限类型管理Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLPLT99_Service extends BaseService {
	
	/**
	 * 查询权限类型总数
	 * 
	 * @param map 查询条件
	 * @return 权限类型总数
	 */
	public int getPrivilegeTypeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPrivilegeTypeCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询权限类型List
	 * 
	 * @param map 查询条件
	 * @return 权限类型List
	 */
	public List<Map<String, Object>> getPrivilegeTypeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPrivilegeTypeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPositionCategoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加权限类型
	 * 
	 * @param map 添加内容
	 */
	public void addPlt(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPLPLT99.addPlt");
	}
	
	/**
	 * 查询权限类型是否存在
	 * 
	 * @param map 查询条件
	 * @return 权限类型数
	 */
	public String getPltCountVal(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPltCountVal");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询权限类型是否存在(添加画面用)
	 * 
	 * @param map 查询条件
	 * @return 权限类型数
	 */
	public int getPltCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPltCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询权限类型信息
	 * 
	 * @param map 查询条件
	 * @return 权限类型信息
	 */
	public Map<String, Object> getPrivilegeTypeInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.getPrivilegeTypeInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新权限类型
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePlt(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.updatePlt");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除权限类型
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int deletePlt(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLPLT99.deletePlt");
		return baseServiceImpl.remove(parameterMap);
	}

}
