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
public class BINOLSTCM13_Service extends BaseService {
    /**
     * 插入退库申请单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProReturnRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.insertProReturnRequest");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入退库申请单明细表
     * @param map
     * @return
     */
    public void insertProReturnReqDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSTCM13.insertProReturnReqDetail");
    }
    
    /**
     * 修改退库申请单主表数据。
     * @param map
     * @return
     */
    public int updateProReturnRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.updateProReturnRequest");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定退库申请单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProReturnRequestMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.getProReturnRequestMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定退库申请库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProReturnReqDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.getProReturnReqDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 判断退库申请单号存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> selProReturnRequest(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.selProReturnRequest");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询退库申请单据(RJ)根据关联单号
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtReturnReqListByRelevanceNo(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.selPrtReturnReqListByRelevanceNo");
    	return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得转换后的系统时间
     * @return
     */
    public String getConvertSysDate(){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.getSYSDate");
        String sysDate = ConvertUtil.getString(baseServiceImpl.get(parameterMap));
        return sysDate;
    }
    
    /**
     * 删除【产品退库申请单据明细表】
     * @param map
     * @return
     */
    public int deleteProReturnReqDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM13.deleteProReturnReqDetail");
        return baseServiceImpl.update(parameterMap);
    }
}
