/*  
 * @(#)BINOLMORPT01_Service.java     1.0 2011.10.21  
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
package com.cherry.mo.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 考核答卷管理Service
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT01_Service extends BaseService {
	
	/**
     * 取得考核答卷信息总数
     * 
     * @param map 查询条件
     * @return 考核答卷信息总数
     */
    public int getCheckAnswerCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMORPT01.getCheckAnswerCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得考核答卷信息List
     * 
     * @param map 查询条件
     * @return 考核答卷信息List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCheckAnswerList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMORPT01.getCheckAnswerList");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 取得考核答卷信息List(Excel)
     * 
     * @param map 查询条件
     * @return 返回考核答卷信息List
     */
    @SuppressWarnings("unchecked")
    public List getCheckAnswerListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMORPT01.getCheckAnswerListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
