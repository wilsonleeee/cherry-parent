/*
 * @(#)BINOLSSPRM01_Service.java     1.0 2011/09/02
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
import com.cherry.cm.util.ConvertUtil;


@SuppressWarnings("unchecked")
public class BINOLSTCM01_Service extends BaseService {

    /**
     * 插入入出库总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductStockInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductStockInOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入出库表明细
     * @param map
     * @return
     */
    public void insertProductStockDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductStockDetail");
        baseServiceImpl.save(parameterMap);
    }
    

    /**
     * 插入入出库批次总表，返回总表ID
     * @param map
     * @return
     */
    public int insertProductBatchInOut(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductBatchInOut");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入入出库表批次明细
     * @param map
     * @return
     */
    public void insertProductBatchInOutDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        
        String costPrice = ConvertUtil.getString(parameterMap.get("CostPrice")); // 成本价
        String tradeType = ConvertUtil.getString(parameterMap.get("TradeType")); // 业务类型
        if(!ConvertUtil.isBlank(costPrice) && CherryConstants.BUSINESS_TYPE_NS.equals(tradeType)){
        	parameterMap.put("RelSrResidualQuantity", parameterMap.get("Quantity")); // 关联退货后剩余数量
        }
        
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductBatchInOutDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 取得产品库存表指定仓库产品的首末次信息
     * @param map
     * @return
     */
    public Map<String,Object> getProductNewBatchStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductNewBatchStock");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 根据原始单据号及仓库产品信息,取得产品入出库明细记录。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductNewBatchStockList(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductNewBatchStockList");
    	return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得产品库存表指定仓库产品的集合（满足出库要求则有数据）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductNewBatchStockListForOutStock(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductNewBatchStockListForOutStock");
    	return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 更新产品批次库存表(修改批次库存数量)
     * 
     * @param map
     * @return
     */
    public int updProductNewBatchStock(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updProductNewBatchStock");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 更新产品入出库批次库存表(修改入出库批次库存记录的剩余可退货数量)
     * 
     * @param map
     * @return
     */
    public int updProductBatchInOutDetail(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updProductBatchInOutDetail");
    	return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 更新产品入出库批次主表成本价
     * 
     * @param map
     * @return
     */
    public int updProductBatchInOut(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updProductBatchInOut");
    	return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 检查出库要求的批次库存是否满足
     * @param map
     * @return
     */
    public Map<String,Object> checkProductNewBatchStock(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.checkProductNewBatchStock");
    	return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 根据原始单据号及仓库产品信息,取得产品入出库明细记录。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchInOutDetailByRelevanceNoAndPrt(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductBatchInOutDetailByRelevanceNoAndPrt");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据原始单据号及仓库产品信息取得产品入出库明细记录。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchInOutDetailByBgRelevanceNo(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductBatchInOutDetailByBgRelevanceNo");
    	return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据接收退库(AR)的单据号找到退库(RR)的入出库批次明细数据。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchInOutDetailByARRelevanceNo(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductBatchInOutDetailByARRelevanceNo");
    	return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 插入【产品批次库存表(新建)】并返回批次库存表ID
     * @param map
     * @return
     */
    public int insertProductNewBatchStock(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductNewBatchStock");
    	return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 增量更新产品库存
     * 
     * @param map
     * @return
     */
    public int updateProductStock(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductStockByIncrement");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入产品库存
     * @param map
     * @return
     */
    public void insertProductStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductStock");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 修改入出库单据主表数据。
     * @param map
     * @return
     */
    public int updateProductInOutMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductInOutMain");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param map
     * @return
     */
    public Map<String,Object> getProductInOutMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInOutMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定入出库单主ID，取得明细信息。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductInOutDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInOutDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 修改产品批次库存
     * @param map
     * @return
     */
    public int updateProductBatchStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductBatchStockByIncrement");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入产品批次库存
     * @param map
     * @return
     */
    public void insertProductBatchStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.insertProductBatchStock");
        baseServiceImpl.save(parameterMap);
    }

    /**
     * 根据发货单单号，取得发货单相关成本信息。
     * @param map
     * @return
     */
	public List<Map<String, Object>> getProductInfoListByDeliverNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInfoListByDeliverNum");
    	return baseServiceImpl.getList(parameterMap);
	}

    /**
     * 修改发货单明细表中的总成本
     * @param map
     * @return
     */
	public int updateProductDeliverDetail(Map<String, Object> map) {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductDeliverDetail");
         return baseServiceImpl.update(parameterMap);
		
	}



    /**
     * 根据盘点单单号，取得发货单相关成本信息。
     * @param map
     * @return
     */
	public List<Map<String, Object>> getProductInfoListByStockTakingNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInfoListByStockTakingNum");
    	return baseServiceImpl.getList(parameterMap);
	}

    /**
     * 修改盘点单明细表中的总成本
     * @param map
     * @return
     */
	public int updateProductStockTakingDetail(Map<String, Object> map) {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductStockTakingDetail");
         return baseServiceImpl.update(parameterMap);
		
	}


	
	
	/**
     * 根据退库单单号，取得退库单相关成本信息。
     * @param map
     * @return
     */
	public List<Map<String, Object>> getCostPriceByRR(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getCostPriceByRR");
    	return baseServiceImpl.getList(parameterMap);
	}

    /**
     * 修改退库单明细表中的总成本
     * @param map
     * @return
     */
	public int updateProductReturnDetail(Map<String, Object> map) {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductReturnDetail");
         return baseServiceImpl.update(parameterMap);
		
	}

    /**
     * 根据调出单单号，取得调出单相关成本信息。
     * @param map
     * @return
     */
	public List<Map<String, Object>> getProductInfoListByAllocationOutIDNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInfoListByAllocationOutIDNum");
    	return baseServiceImpl.getList(parameterMap);
	}

    /**
     * 修改调出单明细表中的总成本
     * @param map
     * @return
     */
	public int updateProductAllocationOutDetail(Map<String, Object> map) {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductAllocationOutDetail");
         return baseServiceImpl.update(parameterMap);
		
	}

    /**
     * 根据调入单单号，取得调入单相关成本信息。
     * @param map
     * @return
     */
	public List<Map<String, Object>> getProductInfoListByAllocationInIDNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.getProductInfoListByAllocationInIDNum");
    	return baseServiceImpl.getList(parameterMap);
	}

    /**
     * 修改调入单明细表中的总成本
     * @param map
     * @return
     */
	public int updateProductAllocationInDetail(Map<String, Object> map) {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM01.updateProductAllocationInDetail");
         return baseServiceImpl.update(parameterMap);
		
	}

}
