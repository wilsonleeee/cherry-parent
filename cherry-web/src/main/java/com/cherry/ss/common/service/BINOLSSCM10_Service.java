/*
 * @(#)BINOLSSCM10_Service.java     1.0 2013/09/02
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
package com.cherry.ss.common.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销品入库单弹出table共通Service
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
@SuppressWarnings("unchecked")
public class BINOLSSCM10_Service extends BaseService{
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return
     */
    public int getInDepotCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSCM10.getInDepotCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得入库单List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInDepotList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSCM10.getInDepotList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得入库单详细List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInDepotDetailList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSCM10.getInDepotDetailList");
        return baseServiceImpl.getList(map);
    }
}