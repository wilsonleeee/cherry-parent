/*	
 * @(#)BINBAT152_Service.java     1.0 2016/07/09		
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

package com.cherry.ss.pro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 补录产品入出库成本(标准接口)Service
 * 
 * @author chenkuan
 * 
 * @version 2016-07-09
 * 
 */
public class BINBAT152_Service extends BaseService {
	
	
	/**
	 * 
	 * 查询产品入出库批次表的数据（即明细中有成本价为空，且是出库类型的数据）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProBatchInOutList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProBatchInOutList");
		return baseServiceImpl.getList(paramMap);
	}
	
	
	/**
	 * 根据产品入出库批次表ID 查询产品入出库批次记录明细表数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProBatchInOutDetailList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProBatchInOutDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	
	/**
	 * 查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID查询）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProNewBatchStockList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProNewBatchStockList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 插入产品入出库批次记录明细表
	 * 
	 * @param map
	 * @return
	 */
	public void insertProBatchInOutDetail(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.insertProBatchInOutDetail");
		 baseServiceImpl.save(paramMap);
	}
	
	/**
	 *根据明细ID删除产品入出库批次记录明细表
	 * 
	 * @param map
	 * @return
	 */
	public void deleteProBatchInOutDetail(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.deleteProBatchInOutDetail");
		 baseServiceImpl.update(paramMap);
	}
	
	
	/**
	 * 根据产品入出库批次ID删除对应的产品入出库批次记录明细
	 * 
	 * @param map
	 * @return
	 */
	public void deleteProBatchInOutDetailByMainId(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.deleteProBatchInOutDetailByMainId");
		 baseServiceImpl.update(paramMap);
	}
	
	/**
	 *修改产品批次库存表库存数
	 * 
	 * @param map
	 * @return
	 */
	public int updateProNewBatchStock(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.updateProNewBatchStock");
		return baseServiceImpl.update(paramMap);
	}
	
	
	/**
	 *修改关联退货后剩余数量
	 * 
	 * @param map
	 * @return
	 */
	public int updateRelSrResidualQuantity(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.updateRelSrResidualQuantity");
		return baseServiceImpl.update(paramMap);
	}
	
	
	/**
	 * 根据批次库存id得到对应的批次库存明细中成本价为空的数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCostPriceIsNullAmount (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getCostPriceIsNullAmount");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	
	/**
	 * 根据批次库存id得到对应的总成本价
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object>  getTotalCostPrice (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getTotalCostPrice");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	
	/**
	 * 根据入出库明细id修改对应的成本价
	 * 
	 * @param map
	 * @return
	 */
	public void updateCostPriceByDetails(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.updateCostPriceByDetails");
		 baseServiceImpl.update(paramMap);
	}
	
	
	/**
	 * 根据批次库存id修改对应的总成本价
	 * 
	 * @param map
	 * @return
	 */
	public void updateTotalCostPrice (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.updateTotalCostPrice");
		 baseServiceImpl.update(paramMap);
	}
	
	
	/**
     * 取得产品库存表指定仓库产品的首末次信息
     * @param map
     * @return
     */
    public Map<String,Object> getProductNewBatchStock(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductNewBatchStock");
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
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductBatchInOutDetailByRelevanceNoAndPrt");
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
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.insertProductNewBatchStock");
    	return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 根据接收退库(AR)的单据号找到退库(RR)的入出库批次明细数据。
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductBatchInOutDetailByARRelevanceNo(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductBatchInOutDetailByARRelevanceNo");
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
    	parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductBatchInOutDetailByBgRelevanceNo");
    	return baseServiceImpl.getList(parameterMap);
    }

	/******* 单据入出库各产品的总成本写入原始交易单据明细  *********/

	/**
	 * 根据发货单单号，取得发货单相关成本信息。
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoListByDeliverNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductInfoListByDeliverNum");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.updateProductDeliverDetail");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getCostPriceByRR");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.updateProductReturnDetail");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductInfoListByStockTakingNum");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.updateProductStockTakingDetail");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductInfoListByAllocationOutIDNum");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.updateProductAllocationOutDetail");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductInfoListByAllocationInIDNum");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.updateProductAllocationInDetail");
		return baseServiceImpl.update(parameterMap);

	}
}
