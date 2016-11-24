/*	
 * @(#)BINOLBSCNT08_BL.java     1.0 2011/05/09
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
import com.cherry.bs.cnt.service.BINOLBSCNT08_Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * 	导入经销商额度变更BL
 *
 * @author chenkuan
 */
public class BINOLBSCNT08_BL {


    @Resource(name = "binOLBSCNT08_Service")
    private BINOLBSCNT08_Service binolbscnt08Service;
    /**
     * 取得柜台积分计划总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanCount(Map<String, Object> map) {

        return binolbscnt08Service.getCounterPointPlanCount(map);
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
        List<Map<String, Object>> employeeList = binolbscnt08Service.getCounterPointPlanList(map);
        return employeeList;
    }
}
