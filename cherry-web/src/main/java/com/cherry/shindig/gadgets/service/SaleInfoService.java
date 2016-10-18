/*
 * @(#)SaleInfoService.java     1.0 2011/11/11
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
 * 销售信息取得Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/11
 */
public class SaleInfoService extends BaseService {
	
	/**
	 * 查询销售信息List
	 * 
	 * @param map 查询条件
	 * @return 销售信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getSaleInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询销售BA信息List
	 * 
	 * @param map 查询条件
	 * @return 销售BA信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计销售总金额和总数量
	 * 
	 * @param map 查询条件
	 * @return 销售总金额和总数量
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleAmountSum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getSaleAmountSum");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 按时间统计销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 销售总金额和总数量
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleByHours(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getSaleByHours");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按渠道统计销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 按渠道销售金额和数量List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleByChannel(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getSaleByChannel");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得渠道柜台信息List
	 * 
	 * @param map 查询条件
	 * @return 渠道柜台信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChannelCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getChannelCounterList");
		return baseServiceImpl.getList(parameterMap);
	}

}
