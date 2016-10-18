/*  
 * @(#)BINBEMQMES97_Service.java     1.0 2012/12/06      
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
package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 查询共通 Service
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINBEMQMES97_Service extends BaseService{
    
    @Resource(name="binBEMQMES97_Service_Cache")
    private BINBEMQMES97_Service_Cache binBEMQMES97_Service_Cache;
    
    /**
     * 查询柜台部门信息
     * @param map
     * @return
     */
    public Map<String, Object> selCounterDepartmentInfo(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("CounterCode", map.get("CounterCode"));
        cacheMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
        cacheMap.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
        return binBEMQMES97_Service_Cache.selCounterDepartmentInfo_c(cacheMap);
    }
    
    /**
     * 查询员工信息
     * @param map
     * @return
     */
    public Map<String, Object> selEmployeeInfo(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("EmployeeCode", map.get("EmployeeCode"));
        cacheMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
        cacheMap.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
        return binBEMQMES97_Service_Cache.selEmployeeInfo_c(cacheMap);
    }
    
    /**
     * 查询产品信息
     * @param map
     * @return
     */
    public Map<String, Object> selProductInfo(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BarCode", map.get("BarCode"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
        cacheMap.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
        return binBEMQMES97_Service_Cache.selProductInfo_c(cacheMap);
    }
    
    /**
     * 查询barcode变更后的产品信息
     * @param map
     * @return
     */
    public Map<String, Object> selPrtBarCode(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BarCode", map.get("BarCode"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("TradeDateTime", map.get("TradeDateTime"));
        return binBEMQMES97_Service_Cache.selPrtBarCode_c(cacheMap);
    }
    
    /**
     * 查询产品信息  根据产品厂商ID
     * @param map
     * @return
     */
    public Map<String, Object> selProductInfoByPrtVenID(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_ProductVendorID", map.get("BIN_ProductVendorID"));
        return binBEMQMES97_Service_Cache.selProductInfoByPrtVenID_c(cacheMap);
    }
    
    /**
     * 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
     * @param map
     * @return
     */
    public List<Map<String, Object>> selProAgainByPrtVenID(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_ProductVendorID", map.get("BIN_ProductVendorID"));
        return binBEMQMES97_Service_Cache.selProAgainByPrtVenID_c(cacheMap);
    }
    
    /**
     * 查询barcode变更后的产品信息（按tradeDateTime与StartTime最接近的升序）
     * @param map
     * @return
     */
    public List<Map<String, Object>> selPrtBarCodeList(Map<String, Object> map) {
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BarCode", map.get("BarCode"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("TradeDateTime", map.get("TradeDateTime"));
        return binBEMQMES97_Service_Cache.selPrtBarCodeList_c(cacheMap);
    }
    
    /**
     * 查询促销产品信息
     * @param map
     * @return
     */
    public HashMap selPrmProductInfo(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_OrganizationID", map.get("BIN_OrganizationID"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("BarCode", map.get("BarCode"));
        return binBEMQMES97_Service_Cache.selPrmProductInfo_c(cacheMap);
    }
    
    /**
     * 查询barcode变更后的促销产品信息
     * @param map
     * @return
     */
    public HashMap selPrmProductPrtBarCodeInfo(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BarCode", map.get("BarCode"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("TradeDateTime", map.get("TradeDateTime"));
        return binBEMQMES97_Service_Cache.selPrmProductPrtBarCodeInfo_c(cacheMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID
     * @param map
     * @return
     */
    public HashMap selPrmProductInfoByPrmVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_OrganizationID", map.get("BIN_OrganizationID"));
        cacheMap.put("BIN_PromotionProductVendorID", map.get("BIN_PromotionProductVendorID"));
        return binBEMQMES97_Service_Cache.selPrmProductInfoByPrmVenID_c(cacheMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
     * @param map
     * @return
     */
    public List selPrmAgainByPrmVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_PromotionProductVendorID", map.get("BIN_PromotionProductVendorID"));
        return binBEMQMES97_Service_Cache.selPrmAgainByPrmVenID_c(cacheMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID，不区分有效状态
     * @param map
     * @return
     */
    public List selPrmByPrmVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BIN_PromotionProductVendorID", map.get("BIN_PromotionProductVendorID"));
        return binBEMQMES97_Service_Cache.selPrmByPrmVenID_c(cacheMap);
    }
    
    /**
     * 查询barcode变更后的促销产品信息（按tradeDateTime与StartTime最接近的升序）
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrmPrtBarCodeList(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("BarCode", map.get("BarCode"));
        cacheMap.put("UnitCode", map.get("UnitCode"));
        cacheMap.put("TradeDateTime", map.get("TradeDateTime"));
        return binBEMQMES97_Service_Cache.selPrmPrtBarCodeList_c(cacheMap);
    }
    
    public Map<String,Object> selMemberInfo(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selMemberInfo");
        return (Map) baseServiceImpl.get(parameterMap);
    }
    
    public Map<String,Object> selQuestionID(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selQuestionID");
        return (Map) baseServiceImpl.get(parameterMap);
    }
}