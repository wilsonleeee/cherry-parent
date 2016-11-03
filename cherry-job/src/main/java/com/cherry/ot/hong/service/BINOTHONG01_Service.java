/*
 * @(#)BINOTHONG01_Service.java     1.0 2014/09/04
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
package com.cherry.ot.hong.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 宏巍电商订单获取 SERVICE
 * 
 * 
 * @author niushunjie
 * @version 1.0 2014.09.04
 */
public class BINOTHONG01_Service extends BaseService {
    /**
     * 查询电商接口信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESInterfaceInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getESInterfaceInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 更新电商接口信息表
     * @param map
     * @return
     */
    public int updateESInterfaceInfoLastTime(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.updateESInterfaceInfoLastTime");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 查找电商订单信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderMain(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getESOrderMain");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过电商订单号查询对应的销售单是否存在
     * @param map
     * @return
     */
    public Map<String, Object> getSaleRecordbByOrderCode(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getSaleRecordbByOrderCode");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 查找部门信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepartInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getDepartInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找员工信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEmployeeInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getEmployeeInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找会员信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMemberInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getMemberInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入会员信息表，返回主表ID
     * @param map
     * @return
     */
    public int addMemberInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.addMemberInfo");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入会员持卡信息表
     * @param map
     * @return
     */
    public void addMemCardInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.addMemCardInfo");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 查找产品信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProductInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getProductInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找促销品信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getPrmProductInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getPrmProductInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 添加地址信息
     * 
     * @param map 添加内容
     * @return 地址ID
     */
    public int addAddressInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.addAddressInfo");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 添加会员地址
     * 
     * @param map 添加内容
     */
    public void addMemberAddress(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.addMemberAddress");
        baseServiceImpl.save(paramMap);
    }
    /**
     *  查找电商产品对应关系表 
     * 
     * @param map
     */
    public List<Map<String,Object>> getUnitCodeByTradeTime(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.getUnitCodeByTradeTime");
        return baseServiceImpl.getList(paramMap);
    }
    /**
     * 添加电商产品对应关系
     * 
     * @param map 添加内容
     */
    public void addEsProductRelation(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.addEsProductRelation");
        baseServiceImpl.save(paramMap);
    }
    /**
     * 更新电商接口信息表
     * @param map
     * @return
     */
    public int updateProductRelation(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTHONG01.updateProductRelation");
        return baseServiceImpl.update(paramMap);
    }
    
	/**
	 * 取得失败的店铺订单集合
	 * 
	 * @param map
	 * @return
	 */
	public List getFaildOrderNoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.getFaildOrderNoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得失败的店铺订单集合
	 * 
	 * @param map
	 * @return
	 */
	public List getNoPrtOrderNoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTHONG01.getNoPrtOrderNoList");
		return baseServiceImpl.getList(map);
	}
}