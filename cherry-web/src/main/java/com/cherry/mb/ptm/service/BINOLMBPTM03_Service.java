/*
 * @(#)BINOLMBPTM03_Service.java     1.0 2012/08/08
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
 * 查询积分明细信息Service
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM03_Service extends BaseService {
	
	/**
	 * 取得积分信息
	 * 
	 * @param map 检索条件
	 * @return 积分信息
	 */
	public Map<String, Object> getPointInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM03.getPointInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得积分明细信息
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息
	 */
	public List<Map<String, Object>> getPointInfoDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM03.getPointInfoDetail");
		return baseServiceImpl.getList(parameterMap);
	}

}
