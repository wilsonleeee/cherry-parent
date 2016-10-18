/*	
 * @(#)BINOLRPQUERY_Service     1.0 2010/11/08		
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

package com.cherry.rp.query.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 查询BI报表共通Service
 * @author WangCT
 *
 */
public class BINOLRPQUERY_Service extends BaseService {
	
	/**
	 * 取得BI报表信息
	 * @param map
	 * @return 返回BI报表信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBIReportInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLRPQUERY.getBIReportInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得BI报表定义信息
	 * @param map
	 * @return 返回BI报表定义信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBIRptDefList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLRPQUERY.getBIRptDefList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得BI报表查询条件
	 * @param map
	 * @return 返回BI报表查询条件
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBIRptQryDefList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLRPQUERY.getBIRptQryDefList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得BI报表钻透定义信息
	 * @param map
	 * @return 返回BI报表钻透定义信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBIDrlThrough(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLRPQUERY.getBIDrlThrough");
		return (Map)baseServiceImpl.get(map);
	}

}
