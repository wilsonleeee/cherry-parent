/*
 * @(#)BINOLMOWAT05_BL.java     1.0 2011/8/1
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
package com.cherry.mo.wat.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mo.wat.interfaces.BINOLMOWAT05_IF;
import com.cherry.mo.wat.service.BINOLMOWAT05_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 考勤信息查询BL
 * 
 * @author niushunjie
 * @version 1.0 2011.8.1
 */
public class BINOLMOWAT05_BL  extends SsBaseBussinessLogic implements BINOLMOWAT05_IF{

    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource
    private BINOLMOWAT05_Service binOLMOWAT05_Service;
    
    
    /**
     * 取得考勤信息总数
     * 
     * @param map
     * @return 考勤信息总数
     */
    @Override
    public int getAttendanceInfoCount(Map<String, Object> map) {
        return binOLMOWAT05_Service.getAttendanceInfoCount(map);
    }

    /**
     * 取得考勤信息List
     * 
     * @param map
     * @return 考勤信息List
     */
    @Override
    public List<Map<String, Object>>getAttendanceInfoList(
            Map<String, Object> map) {
        return binOLMOWAT05_Service.getAttendanceInfoList(map);
    }
    
    /**
     * 取得考勤统计查询总数
     * 
     * @param map 查询条件
     * @return 返回考勤统计信息总数
     */
    @Override
    public int getAttendanceCountNum(Map<String, Object> map) {
    	return binOLMOWAT05_Service.getAttendanceCountNum(map);
    }
    
    /**
     *  取得考勤统计信息List
     * 
     * @param map 查询条件
     * @return 考勤统计信息List
     */
    @Override
    public List<Map<String, Object>> getAttendanceCountList(Map<String, Object> map) {
    	return binOLMOWAT05_Service.getAttendanceCountList(map);
    }
    
    /**
     * 取得员工名称及岗位
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getEmployeeInfoById(Map<String, Object> map) {
    	return binOLMOWAT05_Service.getEmployeeInfoById(map);
    }

    /**
     * 取得岗位类别信息List
     * 
     * @param map
     * @return 岗位类别信息List
     */
    @Override
    public List<Map<String, Object>> getPositionCategoryList(
            Map<String, Object> map) {
        return binOLMOWAT05_Service.getPositionCategoryList(map);
    }

    /**
     * 导出考勤信息Excel
     * 
     * @param map
     * @return 返回导出考勤信息List
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binOLMOWAT05_Service.getAttendanceInfoListExcel(map);
        String[][] array = {
        		{ "employeeCode", "WAT05_employeeCode", "", "", "" },
                { "employeeName", "WAT05_employeeName", "", "", "" },
                { "region", "WAT05_region", "15", "", "" },
                { "province", "WAT05_province", "15", "", "" },
                { "city", "WAT05_city", "15", "", "" },
                { "departName", "WAT05_departName", "25", "", "" },
                { "categoryName", "WAT05_categoryName", "15", "", "" },
                { "arriveTime", "WAT05_arriveTime", "20", "", "" },
                { "leaveTime", "WAT05_leaveTime", "20", "", "" },
                { "stayMinutes", "WAT05_stayMinutesSum", "20", "", "" },
                { "udiskSN", "WAT05_udiskSN", "25", "", "" }
                
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT05");
        ep.setSheetLabel("sheetName1");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
    
    /**
     * 考勤统计信息导出
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public byte[] exportCountExcel(Map<String, Object> map) throws Exception {
    	List<Map<String, Object>> dataList = binOLMOWAT05_Service.getAttendanceCountListExcel(map);
        String[][] array = {
        		{ "employeeCode", "WAT05_employeeCode", "25", "", "" },
                { "employeeName", "WAT05_employeeName", "25", "", "" },
                { "region", "WAT05_region", "15", "", "" },
                { "province", "WAT05_province", "15", "", "" },
                { "city", "WAT05_city", "15", "", "" },
                { "categoryName", "WAT05_categoryName", "15", "", "" },
                { "udiskSN", "WAT05_udiskSN", "25", "", "" },
                { "arrCntCount", "WAT05_arrCntCount", "15", "right", "" },
                { "arrCntSum", "WAT05_arrCntSum", "15", "right", "" },
                { "stayMinutesSum", "WAT05_stayMinutesSum", "15", "right", "" }
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT05");
        ep.setSheetLabel("sheetName2");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
}
