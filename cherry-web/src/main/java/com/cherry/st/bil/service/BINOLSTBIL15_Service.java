/*  
 * @(#)BINOLSTBIL15_Service.java     1.0 2012/8/23      
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
package com.cherry.st.bil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品盘点申请单详细Service
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL15_Service extends BaseService{

	/**
	 * 取得盘点申请单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStocktakeRequestCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getProStocktakeRequestCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得盘点申请单List
	 * 
	 * @param map
	 * @return
	 */
	public List getProStocktakeRequestList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getProStocktakeRequestList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品盘点申请单记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品盘点申请单详细导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtStocktakeReqExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getPrtStocktakeReqExportList");
		return baseServiceImpl.getList(parameterMap);
	}
	
}

