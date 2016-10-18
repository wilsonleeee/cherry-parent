/*
 * @(#)BINOLMOWAT04_Service.java     1.0 2011/5/26
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
 * 异常盘点次数监控Service
 * 
 * @author niushunjie
 * @version 1.0 2011.5.26
 */
public class BINOLMOWAT04_Service extends BaseService{
    /**
     * 取得异常盘点次数柜台总数
     * 
     * @param map 查询条件
     * @return 返回异常盘点次数柜台总数
     */
    public int getCounterInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getCounterInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得异常盘点次数柜台List
     * 
     * @param map 查询条件
     * @return 返回异常盘点次数柜台List
     */
    @SuppressWarnings("unchecked")
    public List getCounterInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getCounterInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得异常盘点次数柜台List(Excel)
     * 
     * @param map 查询条件
     * @return 返回异常盘点次数柜台List
     */
    @SuppressWarnings("unchecked")
    public List getCounterInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getCounterInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得异常盘差总数
     * 
     * @param map 查询条件
     * @return 返回异常盘差总数
     */
    public int getAbnormalGainQuantityCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getAbnormalGainQuantityCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得异常盘差List
     * 
     * @param map 查询条件
     * @return 返回异常盘差List
     */
    @SuppressWarnings("unchecked")
    public List getAbnormalGainQuantityList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getAbnormalGainQuantityList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得异常盘差List(Excel)
     * 
     * @param map 查询条件
     * @return 返回异常盘差List
     */
    @SuppressWarnings("unchecked")
    public List getAbnormalGainQuantityListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT04.getAbnormalGainQuantityListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
