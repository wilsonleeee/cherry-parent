/*  
 * @(#)BINOLSTIOS01_Action.java     1.0 2011/09/29      
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
/**
 * 报损单一览
 * @author LuoHong
 *
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


@SuppressWarnings("unchecked")
public class BINOLSTBIL05_Service extends BaseService{

	/**
	 * 取得报损单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getOutboundCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL05.getOutboundCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得报损单List
	 * 
	 * @param map
	 * @return
	 */
	public List getOutboundList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL05.getOutboundList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL05.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品报损单记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL05.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品报损单详细导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtOutboundExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL05.getPrtOutboundExportList");
		return baseServiceImpl.getList(parameterMap);
	}
}

