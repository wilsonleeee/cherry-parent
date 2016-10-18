/*	
 * @(#)BINOLPTRPS20_Service.java     1.0 2010/11/29		
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
 * 发货记录详细service
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS20_Service extends BaseService {

	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDeliverMainInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS20.getDeliverMainInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 取得发货单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverDetailList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS20.getDeliverDetailList");
		return baseServiceImpl.getList(map);
	}
}
