/*
 * @(#)BINOLBSREG01_Service.java     1.0 2011/11/23
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
package com.cherry.bs.reg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域一览画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG01_Service extends BaseService {
	
	/**
	 * 取得顶层区域List
	 * 
	 * @param map 检索条件
	 * @return 顶层区域List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getFirstRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG01.getFirstRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得下级区域List
	 * 
	 * @param map 检索条件
	 * @return 下级区域List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNextRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG01.getNextRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的区域的所有上级区域位置
	 * 
	 * @param map 检索条件
	 * @return 定位到的区域的所有上级区域位置
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLocationHigher(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG01.getLocationHigher");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询定位到的区域ID
	 * 
	 * @param map 检索条件
	 * @return 定位到的区域ID
	 */
	public String getLocationRegionId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG01.getLocationRegionId");
		return (String)baseServiceImpl.get(parameterMap);
	}

}
