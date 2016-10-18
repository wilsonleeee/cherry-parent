/*
 * @(#)SaleTargetRptService.java     1.0 2013/08//08
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
package com.cherry.shindig.gadgets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 销售完成进度报表Service
 * 
 * @author WangCT
 * @version 1.0 2013/08//08
 */
public class SaleTargetRptService extends BaseService {
	
	/**
	 * 查询销售完成进度报表信息总件数
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息总件数
	 */
	public int getSaleTargetRptCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getSaleTargetRptCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询销售完成进度报表统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表统计信息
	 */
	public Map<String, Object> getSaleTargetRptCountInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getSaleTargetRptCountInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询销售完成进度报表信息List
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleTargetRptList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getSaleTargetRptList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得渠道信息List
	 * 
	 * @param map 查询条件
	 * @return 渠道信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getChannelList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得区域信息List
	 * 
	 * @param map 查询条件
	 * @return 区域信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得省市信息List
	 * 
	 * @param map 查询条件
	 * @return 省市信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProCityList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetRpt.getProCityList");
		return baseServiceImpl.getList(parameterMap);
	}

}
