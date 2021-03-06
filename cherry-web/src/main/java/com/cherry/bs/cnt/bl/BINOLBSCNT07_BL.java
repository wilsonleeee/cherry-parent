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
import com.cherry.cm.core.CherryConstants;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


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
        List<Map<String, Object>> counterPointPlanList = binolbscnt07Service.getCounterPointPlanList(map);
        return counterPointPlanList;
    }

    /**
     * 取得所有柜台积分计划
     */
    public List<Map<String,Object>> getCounterPointPlanAllList(Map<String,Object> map){
        // 取得柜台积分计划所有List
        return binolbscnt07Service.getCounterPointPlanAllList(map);
    }

    /**
     * 柜台启用积分计划
     * @param map
     * @throws Exception
     */
    public void tran_enablePointPlan(Map<String, Object> map){
        //查询柜台对应的的积分计划
        Map<String, Object> pointPlanInfo = binolbscnt07Service.getPointPlanByOrganizationId(map);
        map.put("modifiedType","1");
        map.put("endDate", CherryConstants.longLongAfter);
        map.put("actualEndTime",CherryConstants.longLongAfter);

        if (pointPlanInfo == null){
            map.put("currentPointLimit","0");
            binolbscnt07Service.insertCounterPointPlan(map);
        }else{
            binolbscnt07Service.updateCounterPointPlan(map);
        }
        //废弃该柜台未开启的计划
        map.put("executeFlag","2");
        binolbscnt07Service.updateExecuteFlagOfCounterPointPlanHistory(map);
        //记录柜台积分计划变更履历
        map.put("executeFlag",null);
        binolbscnt07Service.insertCounterPointPlanHistory(map);

    }

    /**
     * 柜台停用积分计划
     * @param map
     * @throws Exception
     */
    public void tran_disablePointPlan(Map<String, Object> map){
        //更新对应柜台积分计划的结束时间
        binolbscnt07Service.updateCounterPointPlan(map);
        //记录柜台积分计划变更履历
        map.put("modifiedType","0");
        //柜台已启用，执行标志直接设为1
        map.put("executeFlag","1");
        map.put("actualEndTime",map.get("endDate"));
        binolbscnt07Service.insertCounterPointPlanHistory(map);
        //更新该柜台生效过的、实际结束时间大于停用设定时间的记录的实际结束时间为停用设定时间
        binolbscnt07Service.updateCounterPointPlanHistory(map);
    }

    /**
     * 柜台积分额度变更
     * @param map
     * @throws Exception
     */
    public void tran_pointChange(Map<String, Object> map){
        //更新对应柜台的积分额度
        map.put("changeType","pointChange");
        binolbscnt07Service.updateCounterPointPlan(map);
        //记录柜台积分额度明细
        map.put("tradeType","5");
        map.put("amount",0);
        binolbscnt07Service.insertCounterLimitInfo(map);
    }



    /**
     * 导出柜台积分计划信息Excel
     *
     * @param map
     * @return 导出柜台积分计划信息List
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        List<Map<String, Object>> dataList = binolbscnt07Service.getCounterPointPlanListExcel(map);
        String[][] array = {
                { "Cno", "CNT07.number", "15", "", "" },
                { "CounterCode", "CNT07.counterCode", "15", "", "" },
                { "CounterNameIF", "CNT07.counterName", "20", "", "" },
                { "Planstatus", "CNT07.pointPlan", "15", "", "" },
                { "Explain", "CNT07.explain", "15", "", "" },
                { "StartDate", "CNT07.startDate", "15", "", "" },
                { "EndDate", "CNT07.endDate", "15", "", "" },
                { "CurrentPointLimit", "CNT07.currentPointLimit", "15", "", "" },
                { "EmployeeName", "CNT07.modifier", "15", "", "" },
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

    /**
     * 取得系统时间
     *
     * @param
     * @return String
     */
    public String getSYSDate(){
        return binolbscnt07Service.getDateYMD();
    }
}
