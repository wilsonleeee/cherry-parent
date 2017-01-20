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

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * 查询产品入出库批次表的接收退库和调入数据（即明细中有成本价为空，且是入库类型的数据）（AR+BG）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProBatchInOutListARBG (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProBatchInOutListARBG");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 *
	 * 查询产品入出库批次表的退货数据（即明细中有成本价为空，且是入库类型的数据）SR
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProBatchInOutListSR (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProBatchInOutListSR");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 *
	 * 查询产品入出库批次表的除接收退库，调入，退货以外的入库数据（即明细中有成本价为空，且是入库类型的数据）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProBatchInOutListByOther (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT152.getProBatchInOutList");
		return baseServiceImpl.getList(paramMap);
	}

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
	 * 批量插入产品入出库批次记录明细表
	 * 
	 * @param list
	 * @return
	 */
	public void insertProBatchInOutDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list,"BINBAT152.insertProBatchInOutDetail");
	}
	

	
	/**
	 * 删除原单明细时，根据自增长ID去删除，以提高效率
	 * @param list
	 * @return
	 */
	public void deleteProBatchInOutDetail(List<Map<String, Object>> list) {
		 baseServiceImpl.deleteAll(list,"BINBAT152.deleteProBatchInOutDetail");
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
	 * 修改入出库批次主表的总成本价（TotalCostPrice为空的记录）
	 * 
	 * @param
	 * @return
	 */
	public void updateTotalCostPrice () {
		Map<String,Object> paramMap = new HashMap<String, Object>();
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
	 * 根据产品厂商ID及入出库日期获取产品的各种价格
	 * @param map
	 * @return
     */
	public Map<String, Object> getProductPriceByID(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT152.getProductPriceByID");
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

}
