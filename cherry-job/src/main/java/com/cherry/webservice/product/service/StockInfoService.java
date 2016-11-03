/*  
 * @(#)MemberPointInfoService.java     1.0 2014/12/11      
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

package com.cherry.webservice.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 库存业务Service
 * 
 * @author niushunjie
 * @version 1.0 2014.12.11
 */
public class StockInfoService extends BaseService {
    /**
     * 取得部门信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDepartInfo(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "StockInfo.getDepartInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得库存List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getStockList(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "StockInfo.getStockList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得库存的总记录数
     * @param map
     * @return
     */
    public int getStockListTotalCNT(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "StockInfo.getStockListTotalCNT");
        return baseServiceImpl.getSum(paramMap);
    }
}