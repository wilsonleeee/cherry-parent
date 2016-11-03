/*
 * @(#)BINBESSPRO03_Service.java     1.0 2011/09/01
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
package com.cherry.ss.pro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品库存同步SERVICE
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.09.01
 */
public class BINBESSPRO03_Service extends BaseService {
    /**
     * 取得柜台List
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getCounterInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 按柜台查询产品
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProductList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getProductList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 增量更新新后台产品
     * 
     * @param map
     * @return
     */
    public int updateProduct(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.updateProductStockByIncrement");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入【产品盘点业务单据表】
     * @param map
     * @return
     */
    public int insertProductStockTaking(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.insertProductStockTaking");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入【产品盘点业务单据明细表】
     * @param list
     */
    public void insertProductTakingDetail(List<Map<String, Object>> list){
        baseServiceImpl.saveAll(list,"BINBESSPRO03.insertProductTakingDetail");
    }

    /**
     * 根据自增长ID取得盘点单据及其详细信息
     * @param productInventoryLogID
     * @return
     */
    public List<Map<String, Object>> getStockTakingInfoByID(int productStockTakingID){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("productStockTakingId", productStockTakingID);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getStockTakingInfoByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(parameterMap);
        return ret;
    }
    
    /**
     * 插入入出库总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductStockInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.insertProductStockInOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入出库表明细
     * @param map
     * @return
     */
    public void insertProductStockDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.insertProductStockDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 查询老后台库存表
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOldStockList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getOldStockList");
        return witBaseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询取得老后台柜台
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOldCounterInfoList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getOldCounterInfoList");
        return witBaseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得新后台柜台的实体仓库InfoList
     * @param map
     * @return
     */
    public List<Map<String, Object>> getNewCounterDepotInfoList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getNewCounterDepotInfoList");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 取得新后台柜台的逻辑仓库InfoList
     * @param map
     * @return
     */
    public List<Map<String, Object>> getNewCounterLogicInfoList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getNewCounterLogicInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得品牌InfoList
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getBrandInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入产品库存
     * @param map
     * @return
     */
    public void insertProductStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.insertProductStock");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 取得产品InfoList
     * @param map
     * @return
     */
    public List<Map<String, Object>> getNewProductList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRO03.getNewProductList");
        return baseServiceImpl.getList(parameterMap);
    }
}
