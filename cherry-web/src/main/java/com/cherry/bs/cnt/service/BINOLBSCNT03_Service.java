/*	
 * @(#)BINOLBSCNT03_Service.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	更新柜台画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT03_Service extends BaseService {
	
	/**
	 * 更新柜台信息
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int updateCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.updateCounterInfo");
		return baseServiceImpl.update(parameterMap);
	}
	

	/**
	 * 更新柜台信息
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int updateOrganizationSynergyFlag(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.updateOrganizationSynergyFlag");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除柜台主管和关注柜台的人
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.delEmployeeDepart");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 取得所属部门的员工
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeInDepartList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.getEmployeeInDepartList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得员工节点
	 * 
	 * @param map 查询条件
	 * @return 员工节点
	 */
	public String getEmployeePath(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.getEmployeePath");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新员工节点信息
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updateEmpSuperiors(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT03.updateEmpSuperiors");
		return baseServiceImpl.update(parameterMap);
	}

}
