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
 * @author menghao
 * @version 1.0 2014/12/02
 */
public class BINOLMBRPT06_Service extends BaseService {
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT06.getSumInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得会员推荐会员信息LIST的行数
	 * @param map
	 * @return
	 */
	public int getMemRecommendedRptCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT06.getMemRecommendedRptCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得会员推荐会员信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemRecommendedRptList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT06.getMemRecommendedRptList");
		return baseServiceImpl.getList(paramMap);
	}
	
	
}
