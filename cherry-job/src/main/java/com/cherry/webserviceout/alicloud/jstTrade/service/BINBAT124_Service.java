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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;


/**
 *
 * 天猫销售订单转MQ（销售）
 * 
 *
 * @author hxhao
 *
 * @version  2016-10-27
 */
public class BINBAT124_Service extends BaseService {
	/**
     * 1.从电商订单主表抽取待转换的数据
     * @param map
     * @return
     */
    public List<Map<String, Object>> ESOrderMain(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT124.getESOrderMain");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 2-1.通过主表ID从订单明细表获取数据
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT124.getESOrderDetail");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT124.getPrtInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    public void updateESOrderState(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINJSTBAT124.updateESOrderState");
        baseServiceImpl.update(paramMap);
    }
}
