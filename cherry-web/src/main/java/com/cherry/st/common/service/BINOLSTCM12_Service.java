/*
 * @(#)BINOLSTCM12_Service.java     1.0 2012/06/26
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
 * 产品发货单弹出table共通Service
 * @author LuoHong
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM12_Service extends BaseService{
    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    public int getDeliverCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM12.getDeliverCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM12.getDeliverList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得发货单详细List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDeliverDetailList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM12.getDeliverDetailList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得部门信息List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDepartInfoList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTCM12.getDepartInfoList");
        return baseServiceImpl.getList(map);
    }
}
