/*
 * @(#)BINOLSSPRM26_Service.java     1.0 2010/11/05
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 盘点单明细Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.05
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM26_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得盘点单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getTakingInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品盘点ID
		paramMap.put("stockTakingId", map.get("stockTakingId"));
		// 盈亏
		paramMap.put("profitKbn", map.get("profitKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM26.getTakingInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得盘点单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品盘点ID
		paramMap.put("stockTakingId", map.get("stockTakingId"));
		// 排序字段
		paramMap.put("detailOrderBy", map.get("detailOrderBy"));
		// 盈亏区分 
		paramMap.put("profitKbn", map.get("profitKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM26.getTakingDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM26.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
}
