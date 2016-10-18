/*
 * @(#)BINOLSTCM16_Service.java     1.0 2012/11/27
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
 * 产品调拨操作共通Service
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM16_Service extends BaseService{
    /**
     * 插入产品调拨申请主表，返回主表ID
     * @param map
     * @return
     */
    public int insertProductAllocation(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.insertProductAllocation");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入产品调拨申请明细表
     * @param map
     * @return
     */
    public void insertProductAllocationDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM16.insertProductAllocationDetail");
    }
    
    /**
     * 修改产品调拨申请主表数据。
     * @param map
     * @return
     */
    public int updateProductAllocation(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.updateProductAllocation");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定产品调拨申请单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductAllocationMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定产品调拨申请单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductAllocationDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入产品调出主表，返回主表ID
     * @param map
     * @return
     */
    public int insertProductAllocationOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.insertProductAllocationOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入产品调出明细表
     * @param map
     * @return
     */
    public void insertProductAllocationOutDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM16.insertProductAllocationOutDetail");
    }
    
    /**
     * 修改产品调出表
     * @param map
     * @return
     */
    public int updateProductAllocationOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.updateProductAllocationOut");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定产品调出表主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductAllocationOutMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationOutMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定产品调出表主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductAllocationOutDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationOutDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 删除【产品盘点申请单据明细表】
     * @param map
     * @return
     */
    public int deleteProductAllocationOutDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.deleteProductAllocationOutDetail");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入产品调入主表，返回主表ID
     * @param map
     * @return
     */
    public int insertProductAllocationIn(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.insertProductAllocationIn");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入产品调入明细表
     * @param map
     * @return
     */
    public void insertProductAllocationInDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM16.insertProductAllocationInDetail");
    }
    
    /**
     * 给定产品调入表主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductAllocationInMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationInMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定产品调入表主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductAllocationInDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.getProductAllocationInDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 判断调出单号存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selProductAllocationOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM16.selProductAllocationOut");
        return baseServiceImpl.getList(parameterMap);
    }
}