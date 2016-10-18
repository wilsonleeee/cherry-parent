/*
 * @(#)BINOLSTIOS01_Service.java     1.0 2011/09/06
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
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 入库Service
 * 
 * @author niushunjie
 * @version 1.0 2011.09.06
 */
@SuppressWarnings("unchecked")
public class BINOLSTIOS01_Service extends BaseService{
    /**
     * 取得产品批次ID
     * @param map
     * @return
     */
    public Map<String,Object> getProductBatchId(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS01.getProductBatchId");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 插入产品批次表，返回批次ID
     * @param map
     * @return
     */
    public int insertProductBatch(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS01.insertProductBatch");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
	public String getDepart(Map<String,Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS01.getDepart");
        return  (String) baseServiceImpl.get(parameterMap);
    }
}
