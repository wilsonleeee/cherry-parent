/*
 * @(#)BINOLMBRPT02_Service.java     1.0 2014/07/17
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
 * 会员微信绑定数统计报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/07/17
 */
public class BINOLMBRPT02_Service extends BaseService {
	
	/**
	 * 统计会员绑定数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getBindCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		String joinDateFlag = (String)map.get("joinDateFlag");
		if(joinDateFlag != null && "1".equals(joinDateFlag)) {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getBindCount1");
		} else {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getBindCount");
		}
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 统计新会员数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getNewMemCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		String joinDateFlag = (String)map.get("joinDateFlag");
		if(joinDateFlag != null && "1".equals(joinDateFlag)) {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getNewMemCount1");
		} else {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getNewMemCount");
		}
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 按城市统计绑定会员数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public List<Map<String, Object>> getBindCountByCity(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		String joinDateFlag = (String)map.get("joinDateFlag");
		if(joinDateFlag != null && "1".equals(joinDateFlag)) {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getBindCountByCity1");
		} else {
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getBindCountByCity");
		}
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得微信绑定起止时间
	 * 
	 * @param map 检索条件
	 * @return 微信绑定起止时间
	 */
	public Map<String, Object> getWechatBindTimeRange(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getWechatBindTimeRange");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得指定活动申请总人数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getTotalOrderCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getTotalOrderCount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得指定活动领用总人数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getTotalGetCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getTotalGetCount");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得活动List
	 * 
	 * @param map 检索条件
	 * @return 活动List
	 */
	public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT02.getCampaignList");
		return baseServiceImpl.getList(parameterMap);
	}

}
