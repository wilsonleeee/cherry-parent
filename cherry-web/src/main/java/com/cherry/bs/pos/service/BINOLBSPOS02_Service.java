/*
 * @(#)BINOLBSPOS02_Service.java     1.0 2010/10/27
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

package com.cherry.bs.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 岗位详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS02_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 查询岗位信息
	 * 
	 * @param map 查询条件
	 * @return 岗位信息
	 */
	public Map<String, Object> getPositionInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getPositionInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询直属上级岗位名称
	 * 
	 * @param map 查询条件
	 * @return 岗位名称
	 */
	public String getHigherPositionName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getHigherPositionName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询当前岗位所在的员工
	 * 
	 * @param map 查询条件
	 * @return 当前岗位所在的员工
	 */
	public List<Map<String, Object>> getEmployeeByPos(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getEmployeeByPos");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询员工信息
	 * 
	 * @param map 查询条件
	 * @return 员工信息
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getEmployeeInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得员工管辖的柜台List
	 * 
	 * @param map 查询条件
	 * @return 柜台List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getCounterList");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS02.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}

}
