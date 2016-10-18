/*
 * @(#)BINOLSSPRM28_Service.java     1.0 2010/11/09
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
 * 发货单明细Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.09
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM28_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getDeliverInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM28.getDeliverInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getDeliverDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM28.getDeliverDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
