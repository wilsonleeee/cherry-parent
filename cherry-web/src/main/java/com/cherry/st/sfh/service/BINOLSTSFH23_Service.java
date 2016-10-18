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
public class BINOLSTSFH23_Service extends BaseService{

    /**
     * 获取符合查询条件的订单总数
     * @param map
     * @return
     */
	public int getOrderCount(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.getOrderCount");
		return baseServiceImpl.getSum(paramMap);
	}

    /**
     * 获取符合查询条件的订单主单数据
     * @param map
     * @return
     */
	public List<Map<String, Object>> getOrderInfoList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.getOrderInfoList");
		return baseServiceImpl.getList(paramMap);
	}

    /**
     * 获取符合查询条件的订单导出数据
     * @param map
     * @return
     */
	public List<Map<String, Object>> getOrderDetailInfoList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.getOrderDetailInfoList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 订单删除主表
	 * @param map
	 * @return
	 */
	public int deleteOrder(Map<String, Object> map) {
	    map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.deleteOrder");
        return baseServiceImpl.update(map);
	}
	/**
	 * 订单删除明细表
	 * @param map
	 * @return
	 */
	public int deleteOrderDetail(Map<String, Object> map) {
	    map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.deleteOrderDetail");
        return baseServiceImpl.update(map);
	}

	/**
	 * 根据订单号获取订单详细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderInfoByOrder(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.getOrderInfoByOrder");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 根据订单汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH23.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	
	
}
