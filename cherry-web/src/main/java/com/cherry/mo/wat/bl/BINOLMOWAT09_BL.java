/*
 * @(#)BINOLMOWAT09_BL.java     1.0 2014/12/17
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
import com.cherry.mo.wat.interfaces.BINOLMOWAT09_IF;
import com.cherry.mo.wat.service.BINOLMOWAT09_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * BA考勤信息查询BL
 * 
 * @author niushunjie
 * @version 1.0 2014.12.17
 */
public class BINOLMOWAT09_BL  extends SsBaseBussinessLogic implements BINOLMOWAT09_IF{

    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLMOWAT09_Service")
    private BINOLMOWAT09_Service binOLMOWAT09_Service;
    
    
    /**
     * 取得BA考勤信息总数
     * 
     * @param map
     * @return BA考勤信息总数
     */
    @Override
    public int getAttendanceInfoCount(Map<String, Object> map) {
        return binOLMOWAT09_Service.getAttendanceInfoCount(map);
    }

    /**
     * 取得考勤信息List
     * 
     * @param map
     * @return 考勤信息List
     */
    @Override
    public List<Map<String, Object>>getAttendanceInfoList(Map<String, Object> map) {
        return binOLMOWAT09_Service.getAttendanceInfoList(map);
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
        List<Map<String, Object>> dataList = binOLMOWAT09_Service.getAttendanceInfoListExcel(map);
        String[][] array = {
                { "counterCode", "WAT09_counterCode", "20", "", "" },
                { "counterName", "WAT09_counterName", "20", "", "" },
                { "employeeCode", "WAT09_baCode", "20", "", "" },
                { "employeeName", "WAT09_baName", "20", "", "" },
                { "attendanceDate", "WAT09_attendanceDate", "15", "", "" },
                { "arriveTime", "WAT09_arriveTime", "20", "", "" },
                { "leaveTime", "WAT09_leaveTime", "20", "", "" },
        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLMOWAT09");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
}