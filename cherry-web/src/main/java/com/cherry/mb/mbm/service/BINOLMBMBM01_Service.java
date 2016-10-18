/*
 * @(#)BINOLMBMBM01_Service.java     1.0 2011/03/22
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
 * 会员一览画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BINOLMBMBM01_Service extends BaseService {
	
	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM01.getMemberInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map 检索条件
	 * @return 会员信息List
	 */
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM01.getMemberInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员问卷信息
	 * 
	 * @param map 检索条件
	 * @return 会员问卷信息
	 */
	public List<Map<String, Object>> getMemPaperList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM01.getMemPaperList");
		return baseServiceImpl.getList(parameterMap);
	}
	
}
