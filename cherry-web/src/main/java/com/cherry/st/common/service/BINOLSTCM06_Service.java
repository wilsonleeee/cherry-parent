/*
 * @(#)BINOLSSPRM06_Service.java     1.0 2011/09/20
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

/**
 * 
 * 产品盘点业务共通Service
 * 
 * @author niushunjie
 * @version 1.0 2011.09.20
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM06_Service extends BaseService {
    /**
     * 插入盘点单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductStockTaking(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM06.insertProductStockTaking");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入盘点单明细表
     * @param map
     * @return
     */
    public void insertProductTakingDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM06.insertProductTakingDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 修改盘点单主表数据。
     * @param map
     * @return
     */
    public int updateProductStockTakingMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM06.updateProductStockTakingMain");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定盘点单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductStockTakingMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM06.getProductStockTakingMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定盘点单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductStockTakingDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM06.getProductStockTakingDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
}
