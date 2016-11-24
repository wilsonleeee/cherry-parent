/*	
 * @(#)BINOLBSCNT07_BL.java     1.0 2011/05/09
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
package com.cherry.bs.cnt.bl;


import com.cherry.bs.cnt.service.BINOLBSCNT07_Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;


/**
 *
 * 	积分计划柜台BL
 *
 * @author chenkuan
 */
public class BINOLBSCNT07_BL {


    @Resource(name = "binOLBSCNT07_Service")
    private BINOLBSCNT07_Service binolbscnt07Service;
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    /**
     * 取得柜台积分计划总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanCount(Map<String, Object> map) {

        return binolbscnt07Service.getCounterPointPlanCount(map);
    }

    /**
     * 取得柜台积分计划List
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCounterPointPlanList(Map<String, Object> map) throws Exception {

        // 取得柜台积分计划List
        List<Map<String, Object>> employeeList = binolbscnt07Service.getCounterPointPlanList(map);
        return employeeList;
    }

    /**
     * 导出柜台信息Excel
     *
     * @param map
     * @return 返回导出柜台信息List
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binolbscnt07Service.getCounterPointPlanListExcel(map);
        String[][] array = {
                { "Cno", "CNT07.number", "15", "", "" },
                { "counterCode", "CNT07.counterCode", "15", "", "" },
                { "CounterNameIF", "CNT07.counterName", "20", "", "" },
                { "Planstatus", "CNT07.pointPlan", "35", "", "" },
                { "Explain", "CNT07.explain", "15", "", "" },
                { "StartDate", "CNT07.startDate", "15", "", "" },
                { "EndDate", "CNT07.endDate", "15", "", "" },
                { "CurrentPointLimit", "CNT07.currentPointLimit", "15", "", "" },
                { "employeeName", "CNT07.modifier", "15", "", "" },
                { "Comment", "CNT07.comment", "15","",""}

        };
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        ep.setMap(map);
        ep.setArray(array);
        ep.setBaseName("BINOLBSCNT07");
        ep.setSheetLabel("sheetName");
        ep.setDataList(dataList);
        return binOLMOCOM01_BL.getExportExcel(ep);
    }
}
