/*
 * @(#)BINOLSSPRM25_Service.java     1.0 2010/10/27
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
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 盘点查询Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM25_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getTakingCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25.getTakingCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25.getTakingList");
		return baseServiceImpl.getList(map);
	}
	
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
    /**
	 * 取得促销品盘点一览List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List getTakingInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25.getTakingInfo");
		return baseServiceImpl.getList(map);
	}
    /**
	 * 取得促销品盘点库存List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> gettakingStock(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25_1.gettakingStock");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销品盘点库存数据总数
	 * @param map
	 * @return
	 */
	public int getTakingStockCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM25_1.getTakingStockCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
}
