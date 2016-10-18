/*  
 * @(#)BINBEMQMES97_Service_Cache.java     1.0 2012/12/06      
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

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 查询共通 Service（缓存）
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINBEMQMES97_Service_Cache extends BaseService{
    
    /**
     * 查询柜台部门信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryDepartCache")
    public Map<String, Object> selCounterDepartmentInfo_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selCounterDepartmentInfo");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询员工信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryEmpCache")
    public Map<String, Object> selEmployeeInfo_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selEmployeeInfo");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public Map<String, Object> selProductInfo_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selProductInfo");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询barcode变更后的产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public Map<String, Object> selPrtBarCode_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrtBarCode");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询产品信息  根据产品厂商ID（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public Map<String, Object> selProductInfoByPrtVenID_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selProductInfoByPrtVenID");
        return (Map<String, Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public List<Map<String, Object>> selProAgainByPrtVenID_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selProAgainByPrtVenID");
        return (List<Map<String, Object>>)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询barcode变更后的产品信息（按tradeDateTime与StartTime最接近的升序）（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public List<Map<String, Object>> selPrtBarCodeList_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrtBarCodeList");
        return (List<Map<String, Object>>)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询促销产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public HashMap<String,Object> selPrmProductInfo_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmProductInfo");
        return (HashMap<String,Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询barcode变更后的促销产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public HashMap<String,Object> selPrmProductPrtBarCodeInfo_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmProductPrtBarCodeInfo");
        return (HashMap<String,Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public HashMap<String,Object> selPrmProductInfoByPrmVenID_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmProductInfoByPrmVenID");
        return (HashMap<String,Object>)baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public List<Map<String,Object>> selPrmAgainByPrmVenID_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmAgainByPrmVenID");
        return (List<Map<String,Object>>)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询促销产品信息  根据促销产品厂商ID，不区分有效状态（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public List<Map<String,Object>> selPrmByPrmVenID_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmByPrmVenID");
        return (List<Map<String,Object>>)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询barcode变更后的促销产品信息（按tradeDateTime与StartTime最接近的升序）（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public List<Map<String,Object>> selPrmPrtBarCodeList_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES97.selPrmPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(parameterMap);
    }
}