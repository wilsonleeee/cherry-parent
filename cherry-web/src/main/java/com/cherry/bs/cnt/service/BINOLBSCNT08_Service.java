/*	
 * @(#)BINOLBSCNT08_Service.java     1.0 2016/11/24
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
 * 	导入经销商额度变更Service
 * 
 * @author chenkuan
 */
public class BINOLBSCNT08_Service extends BaseService {

    /**
     * 根据柜台编码或柜台名称 取得柜台详细信息
     *
     * @param  map
     * @return
     */
    public Map<String, Object> getCounterInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT08.getCounterInfo");
        return (Map)baseServiceImpl.get(map);
    }

    /**
     * 取得柜台对应的积分计划
     *
     * @param map
     * @return
     */
    public Map<String, Object> getCounterPointPlan(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT08.getCounterPointPlan");
        return (Map)baseServiceImpl.get(map);
    }


    /**
     *
     * 更新柜台积分计划设置表(批量操作)
     *
     * @param pointPlanList
     * @return
     *
     */
    public int updateCounterPointPlan(List<Map<String, Object>> pointPlanList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pointPlanList",pointPlanList);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT08.updateCounterPointPlan");
        return baseServiceImpl.update(paramMap);
    }

    /**
     *
     * 插入柜台积分额度明细表(批量操作)
     *
     * @param pointPlanList
     * @return
     *
     */
    public void insertCounterLimitInfo(List<Map<String, Object>> pointPlanList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pointPlanList",pointPlanList);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT08.insertCounterLimitInfo");
        baseServiceImpl.save(paramMap);
    }

}
