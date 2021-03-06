/*
 * @(#)BINOLCMPL01_Service.java     1.0 2010/11/04
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

package com.cherry.cm.privilege.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 部门数据过滤权限共通Service
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINOLCMPL01_Service extends BaseService {
	
	/**
	 * 查询所有的员工信息
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询权限类型配置信息
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPrivilegeTypeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getPrivilegeTypeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一用户管辖的所有部门ID(权限类型为0时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getOrganizationId0List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getOrganizationId0List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一用户管辖的所有部门ID(权限类型为1时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getOrganizationId1List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getOrganizationId1List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一用户管辖的所有部门ID(权限类型为2时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getOrganizationId2List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getOrganizationId2List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加数据过滤权限
	 * 
	 * @param List
	 */
	public void addDataPrivilege(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLCMPL01.addDataPrivilege");
	}
	
	/**
	 * 查询所有业务类型
	 * 
	 * @return List 
	 */
	public List<Map<String, Object>> getBusinessTypeList() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getBusinessTypeList");
		return baseConfServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除数据权限
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deleteDataPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.deleteDataPrivilege");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 查询管理员帐号
	 * 
	 * @return List 
	 */
	public List<Map<String, Object>> getAdminIdList() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getAdminIdList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定组织的所有部门
	 * 
	 * @return List 
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getDepartList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有非柜台部门
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getOrgInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getOrgInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定部门的所有下级部门
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getNextOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getNextOrgList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除部门从属关系表
	 * 
	 * @param map 删除条件
	 * @return 处理件数 
	 */
	public int deleteDepartRelation(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.deleteDepartRelation");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 添加部门从属关系表
	 * 
	 * @param list 添加内容
	 */
	public void addDepartRelation(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLCMPL01.addDepartRelation");
	}
	
	/**
	 * 查询所有柜台部门
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.getCounterList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除部门从属关系表中的所有柜台部门
	 * 
	 * @param map 删除条件
	 * @return 处理件数 
	 */
	public int deleteDepartRelationCou(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL01.deleteDepartRelationCou");
		return baseServiceImpl.remove(parameterMap);
	}

}
