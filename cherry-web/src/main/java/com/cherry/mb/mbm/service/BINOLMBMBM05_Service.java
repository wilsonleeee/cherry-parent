/*
 * @(#)BINOLMBMBM05_Service.java     1.0 2012.10.10
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员销售详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM05_Service extends BaseService {

	/**
	 * 查询会员销售总数
	 * 
	 * @param map 检索条件
	 * @return 会员销售总数
	 */
	public int getSaleRecordCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleRecordCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @param map 检索条件
	 * @return 会员销售信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleRecordList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员销售明细信息
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleRecordDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleRecordDetail");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询会员销售明细List
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleRecordDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleRecordDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计会员销售信息
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleCount");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 获取支付方式详细信息
	 * 
	 * @param map 检索条件
	 * @return 支付方式详细信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPayTypeDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getPayTypeDetail");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 获取操作员姓名
	 * 
	 * @param map 检索条件
	 * @return 操作员姓名
	 */
	public String getEmployeeName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getEmployeeName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询会员销售明细总数
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细总数
	 */
	public int getSaleDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询会员销售明细List
	 * 
	 * @param map 检索条件
	 * @return 会员销售明细List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM05.getSaleDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
}
