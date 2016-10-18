/*
 * @(#)BINOLMOWAT02_Service.java     1.0 2011/5/11
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
package com.cherry.mo.wat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 销售异常数据监控Service
 * 
 * @author niushunjie
 * @version 1.0 2011.5.11
 */
public class BINOLMOWAT02_Service extends BaseService{
    /**
     * 取得销售异常柜台总数
     * 
     * @param map 查询条件
     * @return 返回销售异常柜台总数
     */
    public int getCounterInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT02.getCounterInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得销售异常柜台List
     * 
     * @param map 查询条件
     * @return 返回销售异常柜台List
     */
    @SuppressWarnings("unchecked")
    public List getCounterInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT02.getCounterInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得销售异常柜台List(Excel)
     * 
     * @param map 查询条件
     * @return 返回销售异常柜台List
     */
    @SuppressWarnings("unchecked")
    public List getCounterInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT02.getCounterInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
