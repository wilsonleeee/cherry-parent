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

    /**
     * 查询柜台积分额度变更明细总数
     *
     * @param map
     * @return
     */
    public int getCounterPointLimitDetailCount(Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT10.getCounterPointLimitDetailCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 查询柜台积分额度变更明细List
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterPointLimitDetailList (Map<String, Object> map) {

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT10.getCounterPointLimitDetailList");
        return baseServiceImpl.getList(map);
    }
    /**
     * 取得柜台积分额度明细List(Excel)
     *
     * @param map 查询条件
     *
     */
    @SuppressWarnings("unchecked")
    public List getCounterPointLimitDetailListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT10.getCounterPointLimitDetailListExcel");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 取得销售信息
     * @param map
     * @return
     */
    public Map<String,Object> getSaleRecordInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map,"BINOLBSCNT10.getSaleRecordInfo");
    }

    /**
     * 取得销售明细信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleRecordDetailInfo(Map<String,Object> map){
        return baseServiceImpl.getList(map,"BINOLBSCNT10.getSaleRecordDetailInfo");
    }

}
