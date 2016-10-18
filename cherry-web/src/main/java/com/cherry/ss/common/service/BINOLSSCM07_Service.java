/*
 * @(#)BINOLSSCM07_Service.java     1.0 2012/09/27
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
package com.cherry.ss.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销品入出库操作共通Service
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSCM07_Service extends BaseService{
    /**
     * 插入入出库总表，返回总表ID
     * @param map
     * @return
     */
    public int insertPromotionStockInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.insertPromotionStockInOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入出库表明细
     * @param map
     * @return
     */
    public void insertPromotionStockDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.insertPromotionStockDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 增量更新产品库存
     * 
     * @param map
     * @return
     */
    public int updatePromotionStock(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.updatePromotionStockByIncrement");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入产品库存
     * @param map
     * @return
     */
    public void insertPromotionStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.insertPromotionStock");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 修改入出库单据主表数据。
     * @param map
     * @return
     */
    public int updatePromotionStockInOutMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.updatePromotionStockInOutMain");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getPromotionStockInOutMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.getPromotionStockInOutMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定入出库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPromotionStockDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM07.getPromotionStockDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
}