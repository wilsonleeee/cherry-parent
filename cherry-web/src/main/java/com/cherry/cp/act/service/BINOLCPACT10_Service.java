/*	
 * @(#)BINOLCPACT10_BL.java     1.0 @2013-08-15		
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

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 兑换结果一览SERVICE
 * 
 * @author LuoHong
 * 
 */
public class BINOLCPACT10_Service extends BaseService {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 *兑换结果List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExchangeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT10.getExchangeList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 兑换结果数
	 * 
	 * @param map
	 * @return
	 */
	public int getExchangeCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT10.getExchangeCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 活动Excel导出数量
	 * 
	 * @param map
	 * @return
	 */
	public int getExcelCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT10.getExcelCount");
		return baseServiceImpl.getSum(map);
	}
}
