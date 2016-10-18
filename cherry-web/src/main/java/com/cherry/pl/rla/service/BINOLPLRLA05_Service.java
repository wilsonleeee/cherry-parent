package com.cherry.pl.rla.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINOLPLRLA05_Service.java    1.0 2012-4-5     
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

@SuppressWarnings("unchecked")
public class BINOLPLRLA05_Service extends BaseService {
	
	/**
	 * 取得员工list
	 * 
	 * */
	public List<Map<String,Object>> getEmployeeList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getEmployeeList");
	}
	
	/**
	 * 取得员工总数
	 * 
	 * */
	public int getEmployeeCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getEmployeeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得角色LIST
	 * 
	 * */
	public List<Map<String,Object>> getRoleList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getRoleList");
	}
	
	/**
	 * 取得角色总数
	 * 
	 * */
	public int getRoleCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getRoleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
	 * 
	 * */
	public List<Map<String,Object>> getRolesByEmployee(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getRolesByEmployee");
	}
	
	/**
	 * 查询某个岗位类型角色对应的员工LIST
	 * 
	 * */
	public List<Map<String,Object>> getEmpByPostCatRoleList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getEmpByPostCatRoleList");
	}
	
	/**
	 * 查询某个岗位类型角色对应的员工总数
	 * 
	 * */
	public int getEmpByPostCatRoleCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getEmpByPostCatRoleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询某个部门角色对应的员工LIST
	 * 
	 * */
	public List<Map<String,Object>> getEmpByOrgRoleList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getEmpByOrgRoleList");
	}
	
	/**
	 * 查询某个部门角色对应的员工总数
	 * 
	 * */
	public int getEmpByOrgRoleCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getEmpByOrgRoleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询某个用户角色对应的员工LIST
	 * 
	 * */
	public List<Map<String,Object>> getEmpByUserRoleList(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLPLRLA05.getEmpByUserRoleList");
	}
	
	/**
	 * 查询某个用户角色对应的员工总数
	 * 
	 * */
	public int getEmpByUserRoleCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getEmpByUserRoleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询所有的菜单资源
	 * @param roleArray
	 * @return
	 */
	public List<Map<String, Object>> getMenuList (Map<String, Object> map) {
		HashMap parameterMap  = new HashMap();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getMenuList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有菜单的子菜单数
	 * @return
	 */
	public List<Map<String, Object>> getChildMenuCount () {
		HashMap parameterMap  = new HashMap();
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA05.getChildMenuCount");
		return baseServiceImpl.getList(parameterMap);
	}
}
