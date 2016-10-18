/*
 * @(#)BINOLSTSFH02_Service.java     1.0 2010/11/03
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
package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
/**
 * 
 * 订货单一览
 * 
 * @author weisc
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH02_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProductOrderCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getProductOrderCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	public List getProductOrderList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getProductOrderList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品订货单记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得订单导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductOrderExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH02.getProductOrderExportList");
		return baseServiceImpl.getList(parameterMap);
	}
}
