/*	
 * @(#)BINOLCPACT08_Service.java     1.0 @2013-07-16		
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
package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 礼品领用详细一览Service
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT08_Service extends BaseService {

	/**
	 * 取得礼品领用详细基本信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGiftDrawDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT08.getGiftDrawDetail");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}

	/**
	 * 取得礼品领用的礼品详细List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGiftDrawPrtDetail(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT08.getGiftDrawPrtDetail");
		return baseServiceImpl.getList(parameterMap);
	}
}
