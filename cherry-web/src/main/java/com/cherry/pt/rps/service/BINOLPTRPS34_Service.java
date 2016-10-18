/*
 * @(#)BINOLPTRPS34_Service.java     1.0 2014/9/24
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 电商订单详细Service
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
public class BINOLPTRPS34_Service extends BaseService{

    /**
     * 获取支付详细信息
     * @param map
     * @return list
     */
    public List<Map<String,Object>> getPayTypeDetail(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS34.getPayTypeDetail");
        return baseServiceImpl.getList(map);
    }

    /**
     * 获取电商订单详细
     * @param map
     * @return
     */
    public Map<String,Object> getESOrderMain(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS34.getESOrderMain");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
    
    /**
     * 获取产品销售记录单据中的产品详细
     * @param map
     * @return
     */
    public List<Map<String,Object>> geESOrderDetail(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS34.geESOrderDetail");
        return baseServiceImpl.getList(map);
    }
}