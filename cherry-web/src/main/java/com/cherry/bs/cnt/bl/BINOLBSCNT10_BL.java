/*	
 * @(#)BINOLBSCNT10_BL.java     1.0 2011/05/09
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


import com.cherry.bs.cnt.service.BINOLBSCNT10_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * 	积分计划柜台明细BL
 *
 * @author chenkuan
 */
public class BINOLBSCNT10_BL {


    @Resource(name = "binOLBSCNT10_Service")
    private BINOLBSCNT10_Service binolbscnt10Service;

    /**
     * 取得柜台积分计划明细总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanDetailCount(Map<String, Object> map) {

        return binolbscnt10Service.getCounterPointPlanDetailCount(map);
    }

    /**
     * 取得柜台积分计划明细List
     *
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCounterPointPlanDetailList(Map<String, Object> map) throws Exception {

        // 取得柜台积分计划List
        List<Map<String, Object>> employeeList = binolbscnt10Service.getCounterPointPlanDetailList(map);
        return employeeList;
    }

}
