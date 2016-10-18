/*  
 * @(#)BINOLMOCOM01_Service.java    1.0 2011-8-3     
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

package com.cherry.mo.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLMOCOM01_Service extends BaseService{
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getEmployeeCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getDepartCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getDepartCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getDepartList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得柜台上级部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getHigherOrgCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getHigherOrgCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得柜台上级部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getHigherOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getHigherOrgList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门联系人总数
	 * 
	 * @param map 查询条件
	 * @return 部门联系人总数
	 */
	public int getDepartEmpCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getDepartEmpCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门联系人List
	 * 
	 * @param map 查询条件
	 * @return 部门联系人List
	 */
	
	public List<Map<String, Object>> getDepartEmpList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getDepartEmpList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDeparyType(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getDeparyType");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别等级
	 * 
	 * @param map 查询条件
	 * @return 岗位类别等级
	 */
	public String getPosCategoryGrade(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getPosCategoryGrade");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询柜台对应的大区信息List(柜台按渠道模式显示时用到)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得与导入柜台下发类型相对立的柜台的组织ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getContraryOrgID(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getContraryOrgID");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 带权限查询指定的柜台CODE信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getCounterInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
}
