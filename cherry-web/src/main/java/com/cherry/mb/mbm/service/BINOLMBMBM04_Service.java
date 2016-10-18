/*
 * @(#)BINOLMBMBM04_Service.java     1.0 2012.10.10
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
 * 会员积分详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM04_Service extends BaseService {
	
	/**
	 * 查询会员积分信息
	 * 
	 * @param map 检索条件
	 * @return 会员积分信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberPointInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM04.getMemberPointInfo");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询积分明细信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息总数
	 */
	public int getPointDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM04.getPointDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询积分明细信息List
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM04.getPointDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询积分明细信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息总数
	 */
	public int getPointDetail2Count(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM04.getPointDetail2Count");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询积分明细信息List
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointDetail2List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM04.getPointDetail2List");
		return baseServiceImpl.getList(parameterMap);
	}

}
