/*
 * @(#)BINBEDRHAN11_Service.java     1.0 2012/05/28
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
package com.cherry.dr.handler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 处理会员的规则履历记录service
 * 
 * 
 * @author WangCT
 * @version 1.0 2012/05/28
 */
public class BINBEDRHAN11_Service extends BaseService {
	
	/**
	 * 查询会员规则履历List
	 * 
	 * @param map 查询条件
	 * @return 会员规则履历List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemRuleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN11.getMemRuleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN11.getMemberLevelInfoList");
		return baseServiceImpl.getList(paramMap);
	}

}
