/*
 * @(#)BINOLSTCM13_Service.java     1.0 2012/07/24
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
 * 产品退库申请单操作共通Service
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
@SuppressWarnings("unchecked")
public class BINOLSTCM21_Service extends BaseService {
    /**
     * 插入退库申请单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertSaleReturnRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.insertSaleReturnRequest");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入退库申请单明细表
     * @param map
     * @return
     */
    public void insertSaleReturnReqDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM21.insertSaleReturnReqDetail");
    }
    
    /**
     * 插入退库申请单支付明细表
     * @param map
     * @return
     */
    public void insertSaleReturnReqPayDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM21.insertSaleReturnReqPayDetail");
    }
    /**
     * 修改退库申请单主表数据。
     * @param map
     * @return
     */
    public int updateSaleReturnRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.updateProReturnRequest");
        return baseServiceImpl.update(parameterMap);
    }
   
    
    /**
     * 给定退货申请单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getSaleReturnRequestMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.getSaleReturnRequestMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 取得转换后的系统时间
     * @return
     */
    public String getConvertSysDate(){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.getSYSDate");
        String sysDate = ConvertUtil.getString(baseServiceImpl.get(parameterMap));
        return sysDate;
    }
    
    /**
     * 给定退库申请库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleReturnReqDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.getSaleReturnReqDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 给定退库申请库单主ID，取得支付明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleReturnReqPayDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM21.getSaleReturnReqPayDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
}
