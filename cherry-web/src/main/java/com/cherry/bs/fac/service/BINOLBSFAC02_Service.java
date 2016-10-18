/*	
 * @(#)BINOLBSFAC02_Service.java     1.0 2011/02/16	
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
package com.cherry.bs.fac.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 	生产厂商详细Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.16
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC02_Service extends BaseService{
	
	/**
	 * 取得生产厂商基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFacInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC02.getFacInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得厂商地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAddList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC02.getAddList");
		return baseServiceImpl.getList(map);
	}
}
