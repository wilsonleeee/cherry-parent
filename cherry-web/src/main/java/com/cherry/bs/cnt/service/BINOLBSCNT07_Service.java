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
     * 取得柜台积分计划List
     *
     * @param map
     * @return
     */
    public Map<String, Object> getPointPlanByOrganizationId (Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.getPointPlanByOrganizationId");
        return (Map) baseServiceImpl.get(paramMap);
    }

    /**
     * 取得所有柜台积分计划
     */
    public List<Map<String,Object>> getCounterPointPlanAllList(Map<String,Object> map){

        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.getCounterPointPlanAllList");
        return baseServiceImpl.getList(map);

    }

    /**
     *
     * 更新柜台积分计划设置表
     *
     * @param map
     * @return
     *
     */
    public int updateCounterPointPlan(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.updateCounterPointPlan");
        return baseServiceImpl.update(paramMap);
    }

    /**
     *
     * 插入柜台积分计划设置表
     *
     * @param map
     * @return
     *
     */
    public void insertCounterPointPlan(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.insertCounterPointPlan");
        baseServiceImpl.save(paramMap);
    }

    /**
     *
     * 更新柜台积分计划设置履历表
     *
     * @param map
     * @return
     *
     */
    public int updateCounterPointPlanHistory(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.updateCounterPointPlanHistory");
        return baseServiceImpl.update(paramMap);
    }
    /**
     *
     * 更新柜台积分计划设置履历表执行标志
     *
     * @param map
     * @return
     *
     */
    public int updateExecuteFlagOfCounterPointPlanHistory(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.updateExecuteFlagOfCounterPointPlanHistory");
        return baseServiceImpl.update(paramMap);
    }

    /**
     *
     * 插入柜台积分计划设置履历表
     *
     * @param map
     * @return
     *
     */
    public void insertCounterPointPlanHistory(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.insertCounterPointPlanHistory");
        baseServiceImpl.save(paramMap);
    }

    /**
     *
     * 插入柜台积分额度明细表
     *
     * @param map
     * @return
     *
     */
    public void insertCounterLimitInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT07.insertCounterLimitInfo");
        baseServiceImpl.save(paramMap);
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
