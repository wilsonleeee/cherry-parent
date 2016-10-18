/*
 * @(#)BINOLSSPRM12_Service.java     1.0 2010/11/29
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 促销品类别Service
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM12_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得促销品类别信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getPrmCategoryInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品类别ID
		paramMap.put("prmCategoryId", map.get("prmCategoryId"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM12.getPrmCategoryInfo");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询直属上级类别名称
	 * 
	 * @param map 查询条件
	 * @return 类别名称
	 */
	public String getHigherCategoryName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM12.getHigherCategoryName");
		return (String)baseServiceImpl.get(parameterMap);
	}
}
