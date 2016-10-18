/*
 * @(#)BINOLMOWAT05_Service.java     1.0 2011/8/1
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
 * 考勤信息查询Service
 * 
 * @author niushunjie
 * @version 1.0 2011.8.1
 */
@SuppressWarnings("unchecked")
public class BINOLMOWAT05_Service extends BaseService{
    /**
     * 取得考勤信息总数
     * 
     * @param map 查询条件
     * @return 返回考勤信息总数
     */
    public int getAttendanceInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得考勤明细信息List
     * 
     * @param map 查询条件
     * @return 考勤信息List
     */
    public List<Map<String, Object>> getAttendanceInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得考勤明细总数
     * 
     * @param map 查询条件
     * @return 返回考勤统计信息总数
     */
    public int getAttendanceCountNum(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceCountNum");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     *  取得考勤统计信息List(主页)
     * 
     * @param map 查询条件
     * @return 考勤统计信息List
     */
    public List<Map<String, Object>> getAttendanceCountList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceCountList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     *  取得员工名称及岗位
     * 
     * @param map 查询条件
     * @return 考勤统计信息List
     */
    public Map<String, Object> getEmployeeInfoById(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getEmployeeInfoById");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 取得岗位类别信息List
     * 
     * @param map 查询条件
     * @return 岗位类别信息List
     */
    public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getPositionCategoryList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得考勤信息List(Excel)【此处在主页与明细页中都有使用】
     * 
     * @param map 查询条件
     * @return 返回异常盘差List
     */
    @SuppressWarnings("unchecked")
    public List getAttendanceInfoListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceInfoListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得考勤统计信息LIST(Excel)
     * @param map
     * @return
     */
    public List<Map<String, Object>> getAttendanceCountListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT05.getAttendanceCountListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
}
