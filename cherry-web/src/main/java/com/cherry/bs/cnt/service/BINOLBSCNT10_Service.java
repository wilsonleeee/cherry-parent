/*	
 * @(#)BINOLBSCNT07_Service.java     1.0 2016/11/28
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
package com.cherry.bs.cnt.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * 	积分计划柜台明细Service
 * 
 * @author chenkuan
 */
public class BINOLBSCNT10_Service extends BaseService {

    /**
     * 取得柜台积分计划明细总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanDetailCount(Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT10.getCounterPointPlanDetailCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得柜台积分计划明细List
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterPointPlanDetailList (Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT10.getCounterPointPlanDetailList");
        return baseServiceImpl.getList(map);
    }


}
