/*	
 * @(#)BINOLPTRPS15_Service.java     1.0 2012/10/31		
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
package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 统计销售信息service
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public class BINOLPTRPS15_Service extends BaseService {
	
	/**
	 * 查询销售统计总数
	 * 
	 * @param map 查询条件
	 * @return 销售统计总数
	 */
	public int getSaleCountInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getSaleCountInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息
	 */
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getSaleCountInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询销售统计信息List
	 * 
	 * @param map 检索条件
	 * @return 销售统计信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleCountInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getSaleCountInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定月份是否存在统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售月度统计ID
	 */
	public String getSaleCountHistoryID(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getSaleCountHistoryID");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询未进行销售统计且为销售记录修改的记录数
	 * 
	 * @param map 查询条件
	 * @return 未进行销售统计且为销售记录修改的记录数
	 */
	public int getModifiedSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getModifiedSaleCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 根据指定日期取得其所处财务年的最小最大自然日及对应的财务年
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFiscalYearAndMinMaxDate(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS15.getFiscalYearAndMinMaxDate");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}

}
