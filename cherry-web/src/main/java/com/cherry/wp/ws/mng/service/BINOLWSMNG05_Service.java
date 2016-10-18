/*
 * @(#)BINOLWSMNG05_Service.java     1.0 2014/10/20
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
package com.cherry.wp.ws.mng.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 调出Service
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
public class BINOLWSMNG05_Service extends BaseService {
    
    /**
     * 取得调入申请单总数
     * 
     * @param map
     * @return
     */
    public int getProductAllocationCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLWSMNG05.getProductAllocationCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得调入申请单List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductAllocationList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,"BINOLWSMNG05.getProductAllocationList");
        return baseServiceImpl.getList(map);
    }
}