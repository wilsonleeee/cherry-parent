/*
 * @(#)RuleCalStateService.java     1.0 2012/08/02
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
package com.cherry.shindig.gadgets.service.mb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 查看积分信息Service
 * 
 * @author WangCT
 * @version 1.0 2012/08/02
 */
public class RuleCalStateService extends BaseService {
	
	/**
	 * 统计积分规则累计计算笔数
	 * 
	 * @param map 查询条件
	 * @return 积分规则累计计算笔数List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRuleCalState(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getRuleCalState");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得积分规则信息List
	 * 
	 * @param map 查询条件
	 * @return 积分规则信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCampaignNameList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getCampaignNameList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 按天统计会员积分计算笔数
	 * 
	 * @param map 查询条件
	 * @return 每天会员积分计算笔数List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRuleCalCountByDay(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getRuleCalCountByDay");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计会员当天积分计算笔数
	 * 
	 * @param map 查询条件
	 * @return 会员当天积分计算笔数
	 */
	public int getPointCalCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getPointCalCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询积分计算信息List
	 * 
	 * @param map 查询条件
	 * @return 积分计算信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointCalInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getPointCalInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得会员总数和新会员数
	 * 
	 * @param map 查询条件
	 * @return 会员总数和新会员数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getMemCount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得活跃会员数
	 * 
	 * @param map 查询条件
	 * @return 活跃会员数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActiveMemCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getActiveMemCount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得总会员销售额和新会员销售额
	 * 
	 * @param map 查询条件
	 * @return 总会员销售额和新会员销售额
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemAmount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getMemAmount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得活跃会员销售额
	 * 
	 * @param map 查询条件
	 * @return 活跃会员销售额
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActiveMemAmount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "RuleCalState.getActiveMemAmount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}

}
