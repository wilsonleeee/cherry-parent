/*
 * @(#)BINOLSTCM15_Service.java     1.0 2012/09/10
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品发货接收方库存弹出table共通Service
 * 
 * @author niushunjie
 * @version 1.0 2012.09.10
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM15_Service extends BaseService{
    /**
     * 取得产品发货接收方库存总数
     * 
     * @param map
     * @return
     */
    public int getStockCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM15.getStockCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取产品发货接收方库存List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getStockList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM15.getStockList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询库存权限List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getStockPrivilegeList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM15.getStockPrivilegeList");
        return baseServiceImpl.getList(map);
    }
}
