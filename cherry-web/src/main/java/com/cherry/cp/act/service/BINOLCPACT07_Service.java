/*	
 * @(#)BINOLCPACT07_Service.java     1.0 @2013-07-15		
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
 * 礼品领用报表Service
 * 
 * @author menghao
 * 
 * @version 1.0 2013/07/15
 */
public class BINOLCPACT07_Service extends BaseService {
	/**
	 * 取得礼品领用单据数量
	 * 
	 * @param map
	 * @return
	 */
	public int getGiftDrawCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT07.getGiftDrawCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得礼品领用单据List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGiftDrawList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT07.getGiftDrawList");
		return baseServiceImpl.getList(parameterMap);
	}

	/**
	 * 取得活动信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActivity(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT07.getActivity");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 获取礼品领用明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT07.getExportDetailCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得导出的礼品领用明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCPACT07.getExportDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
}
