package com.cherry.pl.rla.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/*  
 * @(#)BINOLPLRLA05_IF.java    1.0 2012-4-5     
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
public interface BINOLPLRLA05_IF extends ICherryInterface {

	/**
	 * 根据员工查询角色页面	取得员工LIST
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getEmployeeList(Map<String,Object> map);
	
	
	/**
	 * 根据员工查询角色页面	取得员工总数
	 * 
	 * 
	 * */
	public int getEmployeeCount(Map<String,Object> map);
	
	/**
	 * 根据角色查询员工画面	取得角色LIST
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getRoleList(Map<String,Object> map);
	
	/**
	 * 根据角色查询员工画面	取得角色总数
	 * 
	 * 
	 * */
	public int getRoleCount(Map<String,Object> map);
	
	/**
	 * 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
	 * 
	 * */
	public List<Map<String,Object>> getRolesByEmployee(Map<String,Object> map);
	
	/**
	 * 根据某个角色ID查询出拥有这些角色的员工
	 * 
	 * */
	public Map<String,Object> getEmployeesByRole(Map<String,Object> map);
	
	/**
	 * 根据员工ID查询其所拥有的菜单资源
	 * 
	 * */
	public List<Map<String,Object>> getMenusByEmployee(Map<String,Object> map);
	
}
