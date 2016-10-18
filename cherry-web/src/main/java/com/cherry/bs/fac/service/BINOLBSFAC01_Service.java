/*	
 * @(#)BINOLBSFAC01_Service.java     1.0 2011/02/14	
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
 * 	生产厂商查询Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.14
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC01_Service extends BaseService{
	
	/**
	 * 取得生产厂商总数
	 * 
	 * @param map
	 * @return
	 */
	public int getFacCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC01.getFacCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得生产厂商信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getFactoryList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC01.getFactoryList");
		return baseServiceImpl.getList(map);
	}
}
