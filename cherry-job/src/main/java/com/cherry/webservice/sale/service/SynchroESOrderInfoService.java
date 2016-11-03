/*  
 * @(#)SynchroESOrderInfoService.java     1.0 2014/08/01      
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

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 电商订单业务Service
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public class SynchroESOrderInfoService extends BaseService {
	
    /**
     * 判断销售单据号是否存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleRecordInfo(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getSaleRecordInfo");
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
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getMQ_Log");
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
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getMQ_Log_MultiBillType");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.insertESOrderMain");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入电商订单明细表
     * @param map
     * @return
     */
    public void insertESOrderDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"SynchroESOrderInfo.insertESOrderDetail");
    }
    
    /**
     * 插入电商订单支付构成表
     * @param map
     * @return
     */
    public void insertESOrderPayList(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"SynchroESOrderInfo.insertESOrderPayList");
    }
    
    /**
     * 查找电商订单信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESOrderMainInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getESOrderMainInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.updateESOrderMain");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getDepartInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getEmployeeInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getMemberInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 查找会员信息（用手机号）
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMemberInfoByMobilePhone(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getMemberInfoByMobilePhone");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.deleteESOrderDetail");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 删除【电商订单支付构成表】
     * @param map
     * @return
     */
    public int deleteESOrderPayList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.deleteESOrderPayList");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.addMemberInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.addMemCardInfo");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getESOrderMainData");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getESOrderDetailData");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getESOrderPayListData");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getDSOrderInfoMainData");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getDSOrderInfoSaleDetail");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getDSOrderInfoPayDetail");
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
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.getCodeTabel_PayTypeList");
        return baseConfServiceImpl.getList(paramMap);
    }
    /**
     * 查询产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryProductCache")
    public List<Map<String, Object>> selProductInfo_c(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selProductInfo");
        return (List<Map<String, Object>>)baseServiceImpl.getList(parameterMap);
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrtBarCode");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selProductInfoByPrtVenID");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selProAgainByPrtVenID");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrtBarCodeList");
        return (List<Map<String, Object>>)baseServiceImpl.getList(parameterMap);
    }
    /**
     * 查询促销产品信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryPromotionCache")
    public List<Map<String,Object>> selPrmProductInfo_c(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmProductInfo");
        return (List<Map<String,Object>>)baseServiceImpl.getList(parameterMap);
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmProductPrtBarCodeInfo");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmProductInfoByPrmVenID");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmAgainByPrmVenID");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmByPrmVenID");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroESOrderInfo.selPrmPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(parameterMap);
    }
}