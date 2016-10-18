/*	
 * @(#)BINOLBSEMP02_Service.java     1.0 2010/12/07		
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
 * 	员工详细Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.12.07
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP02_Service extends BaseService{
	
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmployeeInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得员工地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpAddressList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmpAddressList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得员工入离职List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpQuitList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmpQuitList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得员工部门、岗位信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPostDistList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getPostDistList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得直属上级
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSupervisor (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getSupervisor");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得直属下级List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJuniorList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getJuniorList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得管辖部门List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeDepartList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmployeeDepartList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得关注用户List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLikeEmployeeList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getLikeEmployeeList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 取得BAS考勤信息总数
     * 
     * @param map 查询条件
     * @return 返回BAS考勤信息总数
     */
    public int getBASAttendanceCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getBASAttendanceCount");
        return baseServiceImpl.getSum(parameterMap);
    }
	
    /**
     * 取得BAS考勤List
     * 
     * @param map
     * @return 返回BAS考勤信息List
     */
    public List<Map<String, Object>> getBASAttendanceList (Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getBASAttendanceList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得部门权限总数
     * 
     * @param map 查询条件
     * @return 返回部门权限总数
     */
    public int getDepartPrivilegeCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getDepartPrivilegeCount");
        return baseServiceImpl.getSum(parameterMap);
    }
	
    /**
     * 取得部门权限List
     * 
     * @param map 查询条件
     * @return 返回部门权限List
     */
    public List<Map<String, Object>> getDepartPrivilegeList (Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getDepartPrivilegeList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得人员权限总数
     * 
     * @param map 查询条件
     * @return 返回人员权限总数
     */
    public int getEmployeePrivilegeCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmployeePrivilegeCount");
        return baseServiceImpl.getSum(parameterMap);
    }
	
    /**
     * 取得人员权限List
     * 
     * @param map 查询条件
     * @return 返回人员权限List
     */
    public List<Map<String, Object>> getEmployeePrivilegeList (Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmployeePrivilegeList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
	 * 取得被关注用户List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBeLikedEmployeeList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getBeLikedEmployeeList");
		return baseServiceImpl.getList(map);
	}
}
