/*
 * @(#)BINOLCMPL03_Service.java     1.0 2011.11.02
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
 * 柜台主管变更处理共通Service
 * 
 * @author WangCT
 * @version 1.0 2011.11.02
 */
public class BINOLCMPL03_Service extends BaseService {
	
	/**
	 * 查询柜台主管
	 * 
	 * @param map 查询条件
	 * @return 柜台主管
	 */
	public String getCounterHeader(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.getCounterHeader");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 删除柜台主管
	 * 
	 * @param map 删除条件
	 * @return 删除数
	 */
	public int deleteCounterHeader(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.deleteCounterHeader");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 添加柜台主管
	 * 
	 * @param map 添加内容
	 */
	public void insertCounterHeader(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.insertCounterHeader");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 查询需要更新部门数据权限的员工
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除指定柜台的所有部门权限
	 * 
	 * @param map 删除条件
	 * @return 删除数
	 */
	public int deleteCounterPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.deleteCounterPrivilege");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 查询部门结构变化引起部门权限变化的人员
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeeListByOrg(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.getEmployeeListByOrg");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询原柜台上级和新柜台上级的所有上级部门
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getHigherOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL03.getHigherOrgList");
		return baseServiceImpl.getList(parameterMap);
	}

}
