/*
 * @(#)BINOLSTIOS10_Service.java     1.0 2013/08/16
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 退库申请Service
 * 
 * @author niushunjie
 * @version 1.0 2011.09.06
 */
@SuppressWarnings("unchecked")
public class BINOLSTIOS10_Service extends BaseService{
    public String getDepart(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS10.getDepart");
        return  (String) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 取得产品List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> searchProductList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS10.searchProductList");
        return baseServiceImpl.getList(map);
    }
}