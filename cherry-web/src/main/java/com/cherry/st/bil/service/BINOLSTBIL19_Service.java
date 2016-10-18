/*  
 * @(#)BINOLSTBIL13_Service.java     1.0 2012/7/24      
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
 * 销售退货申请单详细Service
 * @author nanjunbo
 * @version 1.0 2016.08.29
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL19_Service extends BaseService{

	/**
	 * 取得退库申请单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleReturnRequestCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getSaleReturnRequestCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得退库申请单List
	 * 
	 * @param map
	 * @return
	 */
	public List getSaleReturnRequestList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getSaleReturnRequestList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品退库申请单记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品退库申请单详细导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleReturnReqExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL19.getSaleReturnReqExportList");
		return baseServiceImpl.getList(parameterMap);
	}
}

