/*
 * @(#)BINBESSPRM05_Service.java     1.0 2011/08/09
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销产品同步Service
 * 
 * 
 * @author hub
 * @version 1.0 2011.08.09
 */
@SuppressWarnings("unchecked")
public class BINBESSPRM05_Service extends BaseService {
    /**
     * 取得柜台List
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getCounterInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 按柜台查询促销产品
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getPromotionProductList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getPromotionProductList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 增量更新新后台产品
     * 
     * @param map
     * @return
     */
    public int updatePromotionProduct(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.updatePromotionStockByIncrement");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入库存操作流水表，返回一个流水ID
     * @param map
     * @return
     */
    public int insertPromotionInventoryLog(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.insertPromotionInventoryLog");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入【促销产品盘点业务单据表】
     * @param map
     * @return
     */
    public int insertPromotionStockTaking(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.insertPromotionStockTaking");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入【促销产品盘点业务单据明细表】
     * @param list
     */
    public void insertPromotionTakingDetail(List<Map<String, Object>> list){
        baseServiceImpl.saveAll(list,"BINBESSPRM05.insertPromotionTakingDetail");
    }

    /**
     * 根据自增长ID取得盘点单据及其详细信息
     * @param promotionInventoryLogID
     * @return
     */
    public List<Map<String, Object>> getStockTakingInfoByID(int promotionStockTakingID){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("promotionStockTakingId", promotionStockTakingID);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getStockTakingInfoByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(parameterMap);
        return ret;
    }
    
    /**
     * 插入入出库总表，返回总表ID
     * @param map
     * @return
     */
    public int insertPromotionStockInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.insertPromotionStockInOut");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.insertPromotionStockDetail");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getOldStockList");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getOldCounterInfoList");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getNewCounterDepotInfoList");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getNewCounterLogicInfoList");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getBrandInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入产品库存
     * @param map
     * @return
     */
    public void insertPromotionStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.insertPromotionStock");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM05.getNewProductList");
        return baseServiceImpl.getList(parameterMap);
    }
}
