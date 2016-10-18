/*	
 * @(#)BINOLBSEMP01_Service.java     1.0 2010/10/12		
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
package com.cherry.bs.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 	员工管理Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP01_Service extends BaseService{
	
	/**
	 * 取得员工总数
	 * 
	 * @param map
	 * @return
	 */
	public int getEmpCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getEmpCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得员工信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getEmployeeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得直属下级雇员List
	 * 
	 * @param map 检索条件
	 * @return 雇员List
	 */
	public List<Map<String, Object>> getNextEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getNextEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得顶层雇员List
	 * 
	 * @param map 检索条件
	 * @return 雇员List
	 */
	public List<Map<String, Object>> getFirstEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getFirstEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得未知节点的下级雇员List
	 * 
	 * @param map 检索条件
	 * @return 雇员List
	 */
	public List<Map<String, Object>> getRootNextEmpByplList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getRootNextEmpByplList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得用户权限中的所有岗位级别信息List
	 * 
	 * @param map 检索条件
	 * @return 岗位级别信息List
	 */
	public List<Map<String, Object>> getPosCategoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getPosCategoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的员工的所有上级员工位置
	 * 
	 * @param map 检索条件
	 * @return 定位到的员工的所有上级员工位置
	 */
	public List<String> getLocationHigher(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getLocationHigher");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的员工ID
	 * 
	 * @param map 检索条件
	 * @return 定位到的部门ID
	 */
	public String getLocationEmployeeId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getLocationEmployeeId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得存在下级节点的员工List
	 * 
	 * @param map 检索条件
	 * @return 雇员List
	 */
	public List<String> getHasLowerEmpList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getHasLowerEmpList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得员工信息List（报表导出）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeExcelList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP01.getEmployeeExcelList");
		return baseServiceImpl.getList(map);
	}
}
