/*	
 * @(#)BINOLBSCNT07_Service.java     1.0 2011/05/09
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
 * 	积分计划柜台Service
 * 
 * @author chenkuan
 */
public class BINOLBSCNT07_Service extends BaseService {

    /**
     * 取得柜台积分计划总数
     *
     * @param map
     * @return
     */
    public int getCounterPointPlanCount(Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.getCounterPointPlanCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得柜台积分计划List
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterPointPlanList (Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.getCounterPointPlanList");
        return baseServiceImpl.getList(map);
    }

    /**
     * 取得柜台信息List(Excel)
     *
     * @param map 查询条件
     *
     */
    @SuppressWarnings("unchecked")
    public List getCounterPointPlanListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.getCounterPointPlanListExcel");
        return baseServiceImpl.getList(parameterMap);
    }

}
