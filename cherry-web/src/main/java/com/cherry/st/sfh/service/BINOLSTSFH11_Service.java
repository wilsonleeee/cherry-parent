/*	
 * @(#)BINOLSTSFH11_Service.java     1.0 2012/11/15		
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
package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品收货一览service
 * 
 * @author niushunjie
 * @version 1.0 2012.11.15
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH11_Service extends BaseService {
    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    public int getDeliverCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH11.getDeliverCount");
        return baseServiceImpl.getSum(parameterMap);
    }

    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH11.getDeliverList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得某一产品的总数量和总金额
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        return (Map<String,Object>)baseServiceImpl.get(parameterMap, "BINOLSTSFH11.getSumInfo");
    }
}