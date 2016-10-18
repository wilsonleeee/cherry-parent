/*
 * @(#)BINOLMOWAT09_Service.java     1.0 2014/12/17
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
 * BA考勤信息查询Service
 * 
 * @author niushunjie
 * @version 1.0 2014.12.17
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT09_Service extends BaseService{
    /**
     * 取得BA考勤信息总数
     * 
     * @param map 查询条件
     * @return 返回BA考勤信息总数
     */
    public int getAttendanceInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT09.getAttendanceInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得BA考勤信息List
     * 
     * @param map 查询条件
     * @return BA考勤信息List
     */
    public List<Map<String, Object>> getAttendanceInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT09.getAttendanceInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得BA考勤信息List(Excel)
     * 
     * @param map 查询条件
     * @return 返回BA考勤List
     */
    @SuppressWarnings("unchecked")
    public List getAttendanceInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT09.getAttendanceInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}