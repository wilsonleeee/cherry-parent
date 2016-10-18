/*
 * @(#)BINOLPTJCS42_Service.java     1.0 2015/01/19
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
package com.cherry.pt.jcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品信息二维码维护Service
 * 
 * @author niushunjie
 * @version 1.0 2015.01.19
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS42_Service extends BaseService{
    /**
     * 取得产品信息二维码总数
     * 
     * @param map 查询条件
     * @return 返回产品信息二维码总数
     */
    public int getProductQRCodeCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.getProductQRCodeCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得产品信息二维码List
     * 
     * @param map 查询条件
     * @return 产品信息二维码List
     */
    public List<Map<String, Object>> getProductQRCodeList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.getProductQRCodeList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得所有产品List
     * 
     * @param map 查询条件
     * @return 产品信息二维码List
     */
    public List<Map<String, Object>> getAllProductList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.getAllProductList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得所有经销商List
     * 
     * @param map 查询条件
     * @return 产品信息二维码List
     */
    public List<Map<String, Object>> getAllResellerList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.getAllResellerList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得产品信息二维码List(Excel)
     * 
     * @param map 查询条件
     * @return 返回产品信息二维码List
     */
    @SuppressWarnings("unchecked")
    public List getProductQRCodeListExcel(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.getProductQRCodeListExcel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 删除【产品信息二维码临时表】
     * @param map
     * @return
     */
    public int deleteProductQRCodeTemp(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.deleteProductQRCodeTemp");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 插入【产品信息二维码临时表】
     * @param list
     */
    public void insertProductQRCodeTemp(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLPTJCS42.insertProductQRCodeTemp");
    }
    
    /**
     * 更新产品信息二维码表
     * @param map
     * @return 
     */
    public int mergeProductQRCode(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS42.mergeProductQRCode");
        return baseServiceImpl.update(parameterMap);
    }
}