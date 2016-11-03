/*  
 * @(#)ProductInspectionService.java     1.0 2015/01/13      
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

package com.cherry.webservice.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 查货宝业务 Service
 * 
 * @author niushunjie
 * @version 1.0 2015.01.13
 */
public class ProductInspectionService extends BaseService {
    
    /**
     * 查找产品信息二维码表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductQRCode(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "ProductInspection.getProductQRCode");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入产品信息二维码扫描记录表
     * @param map
     * @return
     */
    public void insertProductQRCodeRecord(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "ProductInspection.insertProductQRCodeRecord");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 查找产品信息二维码扫描记录表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductQRCodeRecord(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "ProductInspection.getProductQRCodeRecord");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找产品信息二维码扫描记录表的工作日报
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductQRCodeRecordReport(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "ProductInspection.getProductQRCodeRecordReport");
        return baseServiceImpl.getList(paramMap);
    }
}