/*	
 * @(#)BINOLSSPRM61_Service.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销品移库一览Service
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM61_Service extends BaseService {
    /**
     * 取得总数
     * 
     * @param map
     * @return
     */
    public int getShiftCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM61.getShiftCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getShiftList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM61.getShiftList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM61.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
}