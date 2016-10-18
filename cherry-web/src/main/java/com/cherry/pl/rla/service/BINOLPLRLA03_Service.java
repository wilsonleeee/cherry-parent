/*
 * @(#)BINOLPLRLA03_Service.java     1.0 2010/11/01
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
 * 岗位角色分配Service
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA03_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得某一岗位的直属下级岗位
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getNextPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA03.getNextPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得品牌下的顶层岗位List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getFirstPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA03.getFirstPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得品牌下的顶层岗位级别
	 * 
	 * @param Map
	 * @return 岗位级别
	 */
	public Integer getFirstPositionLevel(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA03.getFirstPositionLevel");
		return (Integer)baseServiceImpl.get(parameterMap);
	}

}
