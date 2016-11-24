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
import java.util.Date;
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
     * 柜台启用积分计划
     * @param map
     * @throws Exception
     */
    public void tran_enablePointPlan(Map<String, Object> map){
        //查询柜台对应的的积分计划
        Map<String, Object> pointPlanInfo = binolbscnt07Service.getPointPlanByOrganizationId(map);
        map.put("modifiedType",'1');
        map.put("endDate","2100-01-01");
        map.put("actualEndTime","2100-01-01");

        if (pointPlanInfo == null && pointPlanInfo.isEmpty()){
            binolbscnt07Service.insertCounterPointPlan(map);
        }else{
            binolbscnt07Service.updateCounterPointPlan(map);
        }
        //记录柜台积分计划变更履历
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
        map.put("modifiedType",'0');
        map.put("actualEndTime",map.get("endDate"));
        binolbscnt07Service.insertCounterPointPlanHistory(map);
        //更新柜台积分计划设置履历的实际结束时间
        binolbscnt07Service.updateCounterPointPlanHistory(map);
    }

    /**
     * 柜台积分额度变更
     * @param map
     * @throws Exception
     */
    public void tran_pointChange(Map<String, Object> map){
        //更新对应柜台的积分额度
        binolbscnt07Service.updateCounterPointPlan(map);
        //记录柜台积分额度明细
        map.put("tradeType","4");
        map.put("amount",0);
        map.put("tradeTime",new Date());
        binolbscnt07Service.insertCounterLimitInfo(map);
    }


}
