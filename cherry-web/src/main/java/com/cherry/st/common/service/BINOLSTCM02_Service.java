/*
 * @(#)BINOLSSPRM02_Service.java     1.0 2011/09/08
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
package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


@SuppressWarnings("unchecked")
public class BINOLSTCM02_Service extends BaseService {
    /**
     * 插入订货单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductOrder(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM02.insertProductOrder");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入订货单明细表
     * @param map
     * @return
     */
    public void insertProductOrderDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM02.insertProductOrderDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 修改入订货单主表数据。
     * @param map
     * @return
     */
    public int updateProductOrderMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM02.updateProductOrderMain");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 批量更新订货单明细表数据
     * @param list
     */
    public void updateProductOrderDetail(List<Map<String, Object>> list) {
    	baseServiceImpl.updateAll(list,"BINOLSTCM02.updateProductOrderDetail");
    }
    
    /**
     * 给定订货单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductOrderMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM02.getProductOrderMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定订货库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductOrderDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM02.getProductOrderDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
}
