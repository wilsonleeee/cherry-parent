/*
 * @(#)BINOLMBMBM21_Service.java     1.0 2013.08.01
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
 * 推荐会员画面Service
 * 
 * @author WangCT
 * @version 1.0 2013.08.01
 */
public class BINOLMBMBM21_Service extends BaseService {
	
	/**
	 * 查询推荐者基本信息
	 * 
	 * @param map 检索条件
	 * @return 推荐者基本信息
	 */
	public Map<String, Object> getReferrerInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM21.getReferrerInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询推荐过的会员总数
	 * 
	 * @param map 检索条件
	 * @return 推荐过的会员总数
	 */
	public int getReferCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM21.getReferCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询推荐过的会员List
	 * 
	 * @param map 检索条件
	 * @return 推荐过的会员List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReferList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM21.getReferList");
		return baseServiceImpl.getList(parameterMap);
	}

}
