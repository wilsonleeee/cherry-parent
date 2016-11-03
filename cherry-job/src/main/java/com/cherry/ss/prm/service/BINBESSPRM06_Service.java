/*
 * @(#)BINBESSPRO04_Service.java     1.0 2012/04/20
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
 * 平产品库存SERVICE
 * 
 * 
 * @author  
 * @version  
 */
public class BINBESSPRM06_Service extends BaseService {
	
    /**
     * 取得仓库List
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM06.getDepotInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得产品库存信息List
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getStockList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM06.getStockList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询入出库表的库存数量和（按入出库区分统计）List
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getInOutStockList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM06.getInOutStockList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入入出库总表，返回总表ID
     * @param map
     * @return
     */
    public int insertPromotionStockInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM06.insertPromotionStockInOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入出库表明细
     * @param map
     * @return
     */
    public void insertPromotionStockDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM06.insertPromotionStockDetail");
        baseServiceImpl.save(parameterMap);
    }
}
