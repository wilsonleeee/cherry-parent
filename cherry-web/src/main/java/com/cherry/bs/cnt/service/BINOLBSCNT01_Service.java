/*	
 * @(#)BINOLBSCNT01_Service.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	柜台查询画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT01_Service extends BaseService {
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCounterCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT01.getCounterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT01.getCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台区域信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRegionInfoList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT01.getRegionInfoList");
		return baseServiceImpl.getList(map);
	}
	 /**
     * 取得柜台信息List(Excel)
     * 
     * @param map 查询条件
     * 
     */
    @SuppressWarnings("unchecked")
    public List getCounterInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT01.getCounterInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得柜台主管名称List
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterBAS(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT01.getCounterBAS");
        return baseServiceImpl.getList(parameterMap);
    }
}
