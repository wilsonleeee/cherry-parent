/*	
 * @(#)BINOLSSCM09_Service.java     1.0 2013/01/25		
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
 * 入库共通Service
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSCM09_Service extends BaseService{
    /**
     * 插入入库单主表
     * 
     * @param map
     */
    public int insertPrmInDepot(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.insertPrmInDepot");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入库单明细表
     * 
     * @param map
     */
    public void insertPrmInDepotDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"BINOLSSCM09.insertPrmInDepotDetail");
    }
    
    /**
     * 修改入库单据主表数据
     * 
     * @param praMap
     */
     public int updatePrmInDepotMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.updatePrmInDepotMain");
        return baseServiceImpl.update(parameterMap);
    }
     
    /**
     * 
     *取得入库信息
     * 
     *@param praMap
     */
     @SuppressWarnings("unchecked")
    public Map<String, Object> getPrmInDepotMainData(Map<String,Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.getPrmInDepotMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
     
    /**
     * 
     *取得入库明细信息
     * 
     *@param praMap
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPrmInDepotDetailData(Map<String,Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.getPrmInDepotDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 判断入库单号存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrmInDepot(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.selPrmInDepot");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 给定入库单主ID，删除入库单明细
     * @param map
     * @return
     */
    public int delPrmInDepotDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM09.delPrmInDepotDetailData");
        return baseServiceImpl.remove(parameterMap);
    }
}