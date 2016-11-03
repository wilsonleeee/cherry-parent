/*
 * @(#)BINBEIFDPL01_Service.java     1.0 2010/07/04
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

package com.cherry.ia.dpl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 员工管辖部门数据导入Service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.07.04
 */
public class BINBEIFDPL01_Service extends BaseService {
	
	/**
	 * 
	 * 从接口数据库中查询员工管辖部门对应关系
	 * 
	 * @param map 查询条件
	 * @return 员工管辖部门对应关系List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmpDepartList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDPL01.getEmpDepartList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDepartInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDPL01.getDepartInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询员工信息
	 * 
	 * @param map 查询条件
	 * @return 员工信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEmpInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDPL01.getEmpInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 删除员工管辖部门对应关系
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 * 
	 */
	public int delEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDPL01.delEmployeeDepart");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 插入员工管辖部门对应表
	 * 
	 * @param 插入内容
	 */
	public void insertEmployeeDepart(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEIFDPL01.insertEmployeeDepart");
	}
	
	/**
	 * 删除指定部门的员工管辖部门对应关系
	 * 
	 * @param map
	 * @return
	 */
	public int delEmployeeDepartByOrg (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFDPL01.delEmployeeDepartByOrg");
		return baseServiceImpl.remove(map);
	}

}
