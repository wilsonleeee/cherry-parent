/*
 * @(#)BINOLMOWAT03_Service.java     1.0 2011/6/24
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
 * 会员异常数据监控Service
 * 
 * @author niushunjie
 * @version 1.0 2011.6.24
 */
public class BINOLMOWAT03_Service extends BaseService{
    /**
     * 取得会员异常数据总数
     * 
     * @param map 查询条件
     * @return 会员异常数据总数
     */
    public int getMemberInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT03.getMemberInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得会员异常数据List
     * 
     * @param map 查询条件
     * @return 会员异常数据List
     */
    @SuppressWarnings("unchecked")
    public List getMemberInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT03.getMemberInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得会员异常数据List(Excel)
     * 
     * @param map 查询条件
     * @return 会员异常数据List
     */
    @SuppressWarnings("unchecked")
    public List getMemberInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT03.getMemberInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
