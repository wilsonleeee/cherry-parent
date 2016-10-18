/*
 * @(#)BINOLSSPRM44_Service.java     1.0 2012/04/06
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 收货单明细Service
 * 
 * 
 * 
 * @author LuoHong
 * @version 1.0 2012.04.06
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM44_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得收货单信息
	 * 
	 * @param map
	 * @return
	 */
	public List <Map<String, Object>> getDeliverInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收收货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM44.getDeliverInfo");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得收货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getDeliverDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM44.getDeliverDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
