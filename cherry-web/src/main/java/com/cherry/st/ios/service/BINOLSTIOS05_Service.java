/*
 * @(#)BINOLSTIOS05_Service.java     1.0 2011/8/31
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
 * 自由盘点Service
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public class BINOLSTIOS05_Service extends BaseService{
    /**
     * 根据批次号取得批次ID
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS05.getProductBatchID");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得实体仓库的所属部门
     * @param map
     * @return
     */
    public List<Map<String,Object>> getOrganIdByDepotInfoID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS05.getOrganIdByDepotInfoID");
        return baseServiceImpl.getList(map);
    }
}
