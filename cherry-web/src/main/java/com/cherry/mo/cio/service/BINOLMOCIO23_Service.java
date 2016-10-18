/*  
 * @(#)BINOLMOCIO01_Service.java     1.0 2011/06/09      
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
package com.cherry.mo.cio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 部门消息管理Service
 * 
 * @author nanjunbo
 * @version 1.0 2016.09.19
 */
public class BINOLMOCIO23_Service extends BaseService {

    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getDepartmentMessageCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO23.getDepartmentMessageCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得取得柜台消息总数List
     * 
     * @param map 查询条件
     * @return 返回机器总数
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDepartmentMessageList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO23.getDepartmentMessageList");
        return baseServiceImpl.getList(parameterMap);
    }
   
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getDepartmentMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO23.getDepartmentMessage");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }   
}
