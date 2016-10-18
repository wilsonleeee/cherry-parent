/*
 * @(#)BINOLBSREG02_Service.java     1.0 2011/11/23
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
package com.cherry.bs.reg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG02_Service extends BaseService {
	
	/**
	 * 取得区域详细信息
	 * 
	 * @param map 检索条件
	 * @return 区域详细信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRegionInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG02.getRegionInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	/**
	 * 省份或直辖市List
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市List
	 */
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG02.getProvinceList");
		return baseServiceImpl.getList(parameterMap);
	}
}
