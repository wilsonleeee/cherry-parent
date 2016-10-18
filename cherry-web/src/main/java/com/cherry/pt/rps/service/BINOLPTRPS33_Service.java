/*
 * @(#)BINOLPTRPS33_Service.java     1.0 2014/9/24
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 电商订单一览Service
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS33_Service extends BaseService{

    /**
     * 获取电商订单记录总数
     * @param map
     * @return
     */
    public int getESOrderMainCount(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getESOrderMainCount");
        return baseServiceImpl.getSum(map);
    }
    
    /**
     * 获取电商订单记录LIST
     * @param map
     * @return
     */
    public List<Map<String,Object>> getESOrderMainList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getESOrderMainList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得销售总金额与总数量及销售记录单数量
     * 
     * */
    public List<Map<String,Object>> getSumInfo(Map<String,Object> map){
        return baseServiceImpl.getList(map,"BINOLPTRPS33.getSumInfo");
    }
    
    /**
     * 获取电商订单记录明细总数
     * @param map
     * @return
     */
    public int getExportDetailCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getExportDetailCount");
        return baseServiceImpl.getSum(map);
    }
    
    /**
     * 取得Excel导出的电商订单记录明细List
     * @param map
     * @return
     */
    public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getExportDetailList");
        return baseServiceImpl.getList(map);
    }
    
//    /**
//     * 取得支付方式
//     * @param map
//     * @return
//     */
//    public List<Map<String, Object>> getPayTypeList(Map<String, Object> map) {
//        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getPayTypeList");
//        return baseServiceImpl.getList(map);
//    }
}