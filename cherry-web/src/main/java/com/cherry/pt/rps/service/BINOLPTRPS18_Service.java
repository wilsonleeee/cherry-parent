/*	
 * @(#)BINOLPTRPS18_Service.java     1.0 2010/11/29		
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品调出记录详细service
 * 
 * @author weisc
 * @version 1.0 2011.4.2
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS18_Service extends BaseService {

	/**
	 * 取得调拨单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAllocationInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS18.getAllocationInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}

	/**
	 * 取得调拨单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS18.getAllocationList");
		return baseServiceImpl.getList(map);
	}
}
