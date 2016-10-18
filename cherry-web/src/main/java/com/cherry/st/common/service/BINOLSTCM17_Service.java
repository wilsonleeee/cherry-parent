/*  
 * @(#)BINOLSTCM17_Service.java     1.0 2012/12/06      
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
package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 出库业务相关操作Service
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINOLSTCM17_Service extends BaseService{
    /**
     * 插入出库单主表
     * 
     * @param map
     */
    public int insertProductOutDepot(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM17.insertProductOutDepot");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入出库单明细表
     * 
     * @param map
     */
    public void insertProductOutDepotDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"BINOLSTCM17.insertProductOutDepotDetail");
    }
    
    /**
     * 给定出库单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductOutDepotMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM17.getProductOutDepotMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定出库库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductOutDepotDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM17.getProductOutDepotDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
}