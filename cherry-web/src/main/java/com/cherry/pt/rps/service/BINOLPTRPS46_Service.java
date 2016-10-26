/*	
 * @(#)BINOLPTRPS46_Service.java     1.0 2016.10.18	
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品加权平均价查询service
 * 
 * @author lianmh
 * @version 1.0 2016.10.18
 *
 */
public class BINOLPTRPS46_Service extends BaseService {

	/**
	 * 获取产品库存结账记录总数
	 * 
	 * @param map
	 * @return int
	 * 
	 */
	public int getPrtCheckoutInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS46.getPrtCheckoutInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 获取产品库存结账信息LIST
	 * 
	 * @param map
	 * @return list
	 * 
	 */
	public List<Map<String, Object>> getPrtCheckoutInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS46.getPrtCheckoutInfoList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得Excel产品库存结账信息List
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS46.getPrtCheckoutInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得Excel导出的产品库存结账信息总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS46.getPrtCheckoutInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}

}
