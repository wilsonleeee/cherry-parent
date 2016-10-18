/*
 * @(#)BINOLMOWAT01_Service.java     1.0 2011/4/27
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
 * 终端实时监控Service
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT01_Service extends BaseService{
    /**
     * 取得机器总数
     * 
     * @param map 查询条件
     * @return 返回机器总数
     */
    public int getMachineInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT01.getMachineInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得机器List
     * 
     * @param map 查询条件
     * @return 机器List
     */
    public List<Map<String, Object>> getMachineInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT01.getMachineInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得机器List(Excel)
     * 
     * @param map 查询条件
     * @return 返回机器List
     */
    public List getMachineInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT01.getMachineInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT01.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
}
