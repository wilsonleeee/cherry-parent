/*	
 * @(#)BINOLCPACT11_Service.java     1.0 @2014-01-13	
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
 * 活动履历一览Service
 * 
 * @author LUOHONG
 * 
 */
public class BINOLCPACT11_Service extends BaseService {
	
	/**
	 * 活动履历数
	 * 
	 * @param map
	 * @return
	 */
	public int getCampHistoryCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT11.getCampHistoryCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 活动履历List
	 * 
	 * @param map 
	 * @returnList
	 */
	public List<Map<String, Object>> getCampHistoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT11.getCampHistoryList");
		return baseServiceImpl.getList(parameterMap);
	}
}
