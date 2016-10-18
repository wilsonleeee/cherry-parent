/*
 * @(#)BINOLSSPRM03_Service.java     1.0 2011/09/08
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


@SuppressWarnings("unchecked")
public class BINOLSTCM03_Service extends BaseService {
	
	/**
	 * 取指定产品的销售价/会员价/成本价（采购价）/结算价
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtPrice(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.getPrtPrice");
		return baseServiceImpl.getList(paramMap);
	}
    /**
     * 插入发货单总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductDeliver(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.insertProductDeliver");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入发货单明细表
     * @param map
     * @return
     */
    public void insertProductDeliverDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.insertProductDeliverDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 修改发货单主表数据。
     * @param map
     * @return
     */
    public int updateProductDeliverMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.updateProductDeliverMain");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定发货单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductDeliverMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.getProductDeliverMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定发货库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductDeliverDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.getProductDeliverDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 给定发货单主ID，删除发货单明细
     * @param map
     * @return
     */
    public int deleteProductDeliverDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.delProductDeliverDetailData");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 更新发货单的制单者
     * @param map
     * @return
     */
    public int updateProductInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.updateProductInOut");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 查询产品发货单据号 
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtDeliverList(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM03.selPrtDeliverList");
        return baseServiceImpl.getList(parameterMap);
    }
}
