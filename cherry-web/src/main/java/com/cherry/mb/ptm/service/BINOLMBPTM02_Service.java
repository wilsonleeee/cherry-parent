/*
 * @(#)BINOLMBPTM02_Service.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 查询积分信息Service
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM02_Service extends BaseService {
	
	/**
	 * 取得积分信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分信息总数
	 */
	public int getPointInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getPointInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得积分信息List
	 * 
	 * @param map 检索条件
	 * @return 积分信息List
	 */
	public List<Map<String, Object>> getPointInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getPointInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得积分规则信息List
	 * 
	 * @param map 检索条件
	 * @return 积分规则信息List
	 */
	public List<Map<String, Object>> getCampaignNameList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getCampaignNameList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 获取积分记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getExportDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 积分详细导出信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM02.getExportDetailList");
		return baseServiceImpl.getList(paramMap);
	}

}
