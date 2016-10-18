/*	
 * @(#)BINOLSSPRM42_Service.java     1.0 2010/11/23		
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 退库记录详细service
 * 
 * @author lipc
 * @version 1.0 2010.11.23
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM42_Service extends BaseService{
	
	/**
	 * 取得退库记录信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getReturnInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM42.getReturnInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得退库详细记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReturnList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM42.getReturnList");
		return baseServiceImpl.getList(map);
	}
}
