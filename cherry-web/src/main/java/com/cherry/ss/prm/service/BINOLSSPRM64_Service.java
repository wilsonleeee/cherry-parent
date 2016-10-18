/*
 * @(#)BINOLSSPRM64_Service.java     1.0 2013/01/25
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 入库一览Service
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM64_Service extends BaseService {
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return 
     */
	public int getPrmInDepotCount(Map<String,Object> map){
	    Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM64.getPrmInDepotCount");
		return (Integer) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得入库单list
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrmInDepotList(Map<String,Object> map){
	    Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM64.getPrmInDepotList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
	    Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM64.getSumInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
}