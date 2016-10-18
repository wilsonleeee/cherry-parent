/*
 * @(#)BINOLSTSFH03_Service.java     1.0 2011/09/09
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
package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
/**
 * 
 * 订货单明细一览
 * 
 * @author niushunjie
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH03_Service extends BaseService{
    /**
     * 删除【产品订货单据表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductOrderLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.deleteProductOrderLogic");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 删除【产品订货单据明细表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductOrderDetailLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.deleteProductOrderDetailLogic");
        return baseServiceImpl.update(map);
    }
    /**
     * 删除【产品订货单据明细表】
     * @param map
     * @return
     */
    public int deleteProductOrderDetail(Map<String, Object> map){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.deleteProductOrderDetail");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 【产品订货单据明细表】订货核准数量置为0 
     * @param map
     * @return
     */
    public int setProductOrderDetailZero(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.setProductOrderDetailZero");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 根据订单ID获取订单明细的产品厂商ID
     * @param map
     * @return
     */
    public List<Map<String, Object>> getOrderDetailPrtVentorID(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.getOrderDetailPrtVentorID");
    	return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得近30天销量
     * @param map
     * @return
     */
    public List<Map<String, Object>> getSaleQuantity(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.getSaleQuantity");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得安全库存天数
     * @param map
     * @return
     */
    public List<Map<String, Object>> getLowestStockDays(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.getLowestStockDays");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得系数
     * @param map
     * @return
     */
    public List<Map<String, Object>> getAdtCoefficient(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.getAdtCoefficient");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据订货单的工作流ID取得关联的发货单主ID
     * @param map
     * @return
     */
    public String getDeliverIDByWorkFlowID(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH03.getDeliverIDByWorkFlowID");
        return ConvertUtil.getString(baseServiceImpl.get(parameterMap));
    }
    
}
