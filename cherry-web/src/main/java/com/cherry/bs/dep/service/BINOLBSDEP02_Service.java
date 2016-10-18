/*
 * @(#)BINOLBSDEP02_Service.java     1.0 2010/10/27
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
 * 部门详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP02_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 查询部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门信息
	 */
	public Map<String, Object> getOrganizationInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getOrganizationInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询部门地址List
	 * 
	 * @param map 查询条件
	 * @return 部门地址List
	 */
	public List<Map<String, Object>> getDepartAddressList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getDepartAddressList");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门联系人List
	 * 
	 * @param map 查询条件
	 * @return 部门联系人List
	 */
	public List<Map<String, Object>> getDepartContactList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getDepartContactList");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDepartType(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getDepartType");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得所属部门的员工
	 * 
	 * @param map 查询条件
	 * @return 部门的员工
	 */
	public List<Map<String, Object>> getEmployeeInDepartList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getEmployeeInDepartList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得管辖或者关注指定部门的人的信息
	 * 
	 * @param map 查询条件
	 * @return 管辖或者关注指定部门的人的信息
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP02.getEmployeeList");
		return (List)baseServiceImpl.getList(parameterMap);
	}

}
