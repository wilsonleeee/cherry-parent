/*
 * @(#)BINOLMBRPT01_Service.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员销售报表Service
 * 
 * @author WangCT
 * @version 1.0 2013/10/12
 */
public class BINOLMBRPT01_Service extends BaseService {
	
	/**
	 * 按柜台统计总销售金额、数量、单数，会员销售金额、数量、单数 ，非会员销售金额、数量、单数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getMemSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getMemSaleCount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按柜台统计新会员销售金额、数量、单数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getNewMemSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getNewMemSaleCount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按柜台统计会员购买人数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getMemSalePeopleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getMemSalePeopleCount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按柜台统计非会员销售人数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getNonMemSalePeopleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getNonMemSalePeopleCount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按柜台统计销售天数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getSaleDaysList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getSaleDaysList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按柜台统计会员人数
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getMemPeopleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getMemPeopleCount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询柜台信息List
	 * 
	 * @param map 检索条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getCounterList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 会员等级统计
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getMemLevelCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT01.getMemLevelCount");
		return baseServiceImpl.getList(parameterMap);
	}

}
