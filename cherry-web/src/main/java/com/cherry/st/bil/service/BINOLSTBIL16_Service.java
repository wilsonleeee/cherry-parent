/*
 * @(#)BINOLSTBIL16_Service.java     1.0 2012/8/23
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
package com.cherry.st.bil.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 盘点申请单明细一览Service
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
public class BINOLSTBIL16_Service extends BaseService{
    /**
     * 删除【产品盘点申请单据明细表】
     * @param map
     * @return
     */
    public int deleteProStocktakeRequestDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL16.deleteProStocktakeRequestDetail");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL16.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
}
