/*
 * @(#)BINOLSTIOS04_Service.java     1.0 2011/9/28
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
package com.cherry.st.ios.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 商品盘点Service
 * 
 * @author niushunjie
 * @version 1.0 2011.9.28
 */
public class BINOLSTIOS04_Service extends BaseService{
    /**
     * 根据批次号取得批次ID
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS04.getProductBatchID");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取类别属性List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtCatPropertyList(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS04.getPrtCatPropertyList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取第几级分类
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtCatPropValueList(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS04.getPrtCatPropValueList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得产品列表（批次盘点）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductByBatchList(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS04.getProductByBatchList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得产品库存列表（非批次盘点）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductStockList(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS04.getProductStockList");
        return baseServiceImpl.getList(map);
    }
}
