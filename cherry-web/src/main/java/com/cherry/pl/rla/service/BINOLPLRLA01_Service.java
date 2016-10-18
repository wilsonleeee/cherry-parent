/*
 * @(#)BINOLPLRLA01_Service.java     1.0 2010/10/27
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

package com.cherry.pl.rla.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 组织角色分配Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA01_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得某一组织的直属下级组织
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getNextOrganizationInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA01.getNextOrganizationInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得品牌下的顶层部门List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getFirstOrganizationList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA01.getFirstOrganizationList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得品牌下的顶层部门级别
	 * 
	 * @param Map
	 * @return 部门级别 
	 */
	public Integer getFirstOrganizationLevel(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA01.getFirstOrganizationLevel");
		return (Integer)baseServiceImpl.get(parameterMap);
	}

}
