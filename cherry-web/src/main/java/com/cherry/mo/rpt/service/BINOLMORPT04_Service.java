/*  
 * @(#)BINOLMORPT02_Service.java     1.0 2011.10.24 
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
 * 答卷管理Service
 * 
 * @author WangCT
 * @version 1.0 2011.10.24
 */
public class BINOLMORPT04_Service extends BaseService {
	
	/**
     * 根据答卷ID获取答卷信息
     * 
     * @param map 查询条件
     * @return 答卷信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCheckAnswer(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMORPT04.getCheckAnswer");
        return (Map)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 根据问卷ID和答卷ID获取问题和答案
     * 
     * @param map 查询条件
     * @return 问题和答案信息List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCheckQuestionList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMORPT04.getCheckQuestionList");
        return baseServiceImpl.getList(parameterMap);
    }

}
