/*
 * @(#)BINOLSTCM14_Service.java     1.0 2012/08/23
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
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 产品盘点申请单操作共通Service
 * 
 * @author niushunjie
 * @version 1.0 2012.08.23
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM14_Service extends BaseService {
    /**
     * 插入盘点申请单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProStocktakeRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.insertProStocktakeRequest");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入盘点申请单明细表
     * @param map
     * @return
     */
    public void insertProStocktakeRequestDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM14.insertProStocktakeRequestDetail");
    }
    
    /**
     * 修改盘点申请单主表数据。
     * @param map
     * @return
     */
    public int updateProStocktakeRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.updateProStocktakeRequest");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定盘点申请单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProStocktakeRequestMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.getProStocktakeRequestMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定盘点申请库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProStocktakeRequestDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.getProStocktakeRequestDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 判断盘点申请单号存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selProStocktakeRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.selProStocktakeRequest");
        return baseServiceImpl.getList(parameterMap);
    }
    /**
     * 取得盘点申请单明细数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> selProStocktakeRequestDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.selProStocktakeRequestDetail");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得转换后的系统时间
     * @return
     */
    public String getConvertSysDate(){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.getSYSDate");
        String sysDate = ConvertUtil.getString(baseServiceImpl.get(parameterMap));
        return sysDate;
    }
    
    /**
     * 删除【产品盘点申请单据明细表】
     * @param map
     * @return
     */
    public int deleteProStocktakeRequestDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM14.deleteProStocktakeRequestDetail");
        return baseServiceImpl.update(parameterMap);
    }
}
