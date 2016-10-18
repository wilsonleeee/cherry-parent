/*
 * @(#)BINOLSTSFH22_Service.java     1.0 2012/11/13
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

/**
 * 
 * 订货（浓妆淡抹）Service
 * 
 * @author zw
 * @version 1.0 2016.9.19
 */
public class BINOLSTSFH22_Service extends BaseService{

    /**
     * 取得发货方部门地址
     * @param organizationId
     * @return
     */
	public String getDefaultAddress(Map<String, Object> param) {
	 	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(param);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.getDefaultAddress");
        return  (String) baseServiceImpl.get(parameterMap);
	}
    /**
     * 取得建议发货数据
     * @param param
     * @return
     */
	public List<Map<String, Object>> getSuggestProductByAjax(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.getSuggestProductByAjax");
		return baseServiceImpl.getList(paramMap);
	}
    /**
     * 点击款已付时，修改订单状态
     * @param param
     * @return
     */
	public int updateOrderStatus(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.updateOrderStatus");
	    return baseServiceImpl.update(paramMap);
		
	}
	
    /**
     * 取得订单号ID
     * @param map
     * @return
     */
	public String getOrderId(Map<String, Object> map) {
	 	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.getOrderId");
        return  (String) baseServiceImpl.get(parameterMap);
	}
    /**
     * 根据单号删除订单主表信息
     * @param map
     * @return
     */
	public void deleteOrderInfoMain(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.deleteOrderInfoMain");
		baseServiceImpl.remove(paramMap);
	}
    /**
     * 根据单号删除订单明细表信息
     * @param map
     * @return
     */
	public void deleteOrderInfoDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.deleteOrderInfoDetail");
		baseServiceImpl.remove(paramMap);
		
	}
	
    /**
     * 更具单号获取单据信息
     * @param param
     * @return
     */
	public Map<String, Object> getOrderInfo(Map<String, Object> map) {
		   Map<String, Object> parameterMap = new HashMap<String, Object>();
	       parameterMap.putAll(map);
	       parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.getOrderInfo");
	       return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	
    /**
     * 取得订货部门的NodeId
     * @param organizationId
     * @return
     */
	public String getNodeId(Map<String, Object> map) {
	 	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH22.getNodeId");
        return  (String) baseServiceImpl.get(parameterMap);
	}
	
	
	
}
