/*	
 * @(#)BINOLPTRPS05_Service.java     1.0 2010/11/25		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品调出单查询service
 * 
 * @author weisc
 * @version 1.0 2010.11.25
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS05_Service extends BaseService {

	/**
	 * 取得调拨记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getAllocationCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getAllocationCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得调拨记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getAllocationList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取需要导出的调拨记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getExportDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取需要导出的调拨记录明细LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getExportDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
