/*	
 * @(#)BINBAT121_Service.java     1.0 @2015-9-16
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
package com.cherry.webserviceout.alicloud.jstTrade.service;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 天猫退款转MQ（销售）
 *
 *
 * @author fxb
 *
 * @version  2016-11-04
 */
public class BINBAT125_Service extends BaseService {
	/**
     * 获取需转换的退款单
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRefundOrders(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.getRefundOrders");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过主表ID从订单明细表获取数据
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.getESOrderDetail");
        return baseServiceImpl.getList(paramMap);
    }

    /**
     * 根据天猫退款单的关联主单号查询对应原始订单
     * @param map
     * @return
     */
    public Map<String, Object> getOriginalOrder(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.getOriginalOrder");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }

    /**
     * 查询同一原始订单明细的更早退款单
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEarlierRefundOrder(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.getEarlierRefundOrder");
        return baseServiceImpl.getList(paramMap);
    }

    /**
     * 获取产品信息
     * @param map
     * @return
     */
    public Map<String, Object> getPrtInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.getPrtInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }

    public void updateESOrderState(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.updateESOrderState");
        baseServiceImpl.update(paramMap);
    }

    public void updateESOrderModifyCount(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT125.updateESOrderModifyCount");
        baseServiceImpl.update(paramMap);
    }
}
