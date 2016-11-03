/*  
 * @(#)SaleInfoService.java     1.0 2014/08/01      
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

package com.cherry.webservice.sale.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 销售业务Service
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public class SaleInfoService extends BaseService {
	
    /**
     * 判断销售单据号是否存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleRecordInfo(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getSaleRecordInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取出老后台品牌dbo.MQ_Log数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMQ_Log(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getMQ_Log");
        return witBaseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取出老后台品牌dbo.MQ_Log数据（多业务类型）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMQ_Log_MultiBillType(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.getMQ_Log_MultiBillType");
        return witBaseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入电商订单主表，返回主表ID
     * @param map
     * @return
     */
    public int insertESOrderMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.insertESOrderMain");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入电商订单明细表
     * @param map
     * @return
     */
    public void insertESOrderDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"SaleInfo.insertESOrderDetail");
    }
    
    /**
     * 插入电商订单支付构成表
     * @param map
     * @return
     */
    public void insertESOrderPayList(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"SaleInfo.insertESOrderPayList");
    }
    
    /**
     * 查找电商订单信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderMainInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getESOrderMainInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 更新电商订单主表
     * @param map
     * @return
     */
    public int updateESOrderMain(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.updateESOrderMain");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 查找部门信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepartInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getDepartInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getEmployeeInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getMemberInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 删除【电商订单明细表】
     * @param map
     * @return
     */
    public int deleteESOrderDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.deleteESOrderDetail");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 根据电商订单ID取得电商订单明细中产品ID为NULL的数据
     * @param map
     * @return
     */
    public int getESOrderNonProIDDetailCount(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getESOrderNonProIDDetailCount");
        List<Map<String, Object>> list = baseServiceImpl.getList(paramMap);
        return null == list ? 0 : list.size();
    }
    
    /**
     * 删除【电商订单支付构成表】
     * @param map
     * @return
     */
    public int deleteESOrderPayList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.deleteESOrderPayList");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 插入会员信息表，返回主表ID
     * @param map
     * @return
     */
    public int addMemberInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleInfo.addMemberInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.addMemCardInfo");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 查找电商订单主表
     * @param map
     * @return
     */
    public Map<String, Object> getESOrderMainData(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getESOrderMainData");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得电商订单明细表信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderDetailData(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getESOrderDetailData");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得电商订单支付明细表信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderPayListData(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getESOrderPayListData");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找电商订单主表（用于获取电商订单业务接口）
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDSOrderInfoMainData(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getDSOrderInfoMainData");
        return  baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得电商订单明细表信息（用于获取电商订单业务接口）
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDSOrderInfoSaleDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getDSOrderInfoSaleDetail");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得电商订单支付明细表信息（用于获取电商订单业务接口）
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDSOrderInfoPayDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getDSOrderInfoPayDetail");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 支付方式的CodeTable
     * @param map
     * @return
     */
    public List<Map<String, Object>> getCodeTabel_PayTypeList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleInfo.getCodeTabel_PayTypeList");
        return baseConfServiceImpl.getList(paramMap);
    }
}