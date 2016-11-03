/*
 * @(#)BINBESSPRO01_Service.java     1.0 2011/3/11
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
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品月度库存统计SERVICE
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2011.3.11
 */
public class BINBESSPRO01_Service extends BaseService {

	/**
	 * 
	 * 查询截止日期
	 * 
	 * @param map 查询条件
	 * @return 截止日期List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCutOfDate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getCutOfDate");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询当月截止日期记录是否存在
	 * 
	 * @param map 查询条件
	 * @return 当月截止日期记录数
	 * 
	 */
	public Integer getProStockCloseDayCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getProStockCloseDayCount");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入产品月度库存截止日期表
	 * 
	 * @param map 插入内容
	 * @return 无
	 * 
	 */
	public void insertProStockCloseDay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.insertProStockCloseDay");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 查询入出库数据明细
	 * 
	 * @param map 查询条件
	 * @return 入出库数据明细List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNewstockHistory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getNewstockHistory");
		return baseServiceImpl.getList(paramMap);
	}

//	/**
//	 * 
//	 * 插入产品月度库存表
//	 * 
//	 * @param map 插入内容
//	 * @return 无
//	 * 
//	 */
//	public void insertStockHistory(Map<String, Object> map) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.putAll(map);
//		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.insertStockHistory");
//		baseServiceImpl.save(paramMap);
//	}

	/**
	 * 
	 * 把截止计算区分更新为待处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public int updateCloseFlagWait(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.updateCloseFlagWait");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 把截止计算区分从待处理状态更新为已经反映到历史库存表中状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public int updateCloseFlagEnd(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.updateCloseFlagEnd");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 
	 * 插入新纪录到截止日期表
	 * 
	 * @param map 插入内容
	 * @return 无
	 * 
	 */
	public void insertStockCloseDay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.insertStockCloseDay");
		baseServiceImpl.save(paramMap);
	}

//	/**
//	 * 
//	 * 查询月度库存表是否有相同记录
//	 * 
//	 * @param map 查询条件
//	 * @return 产品月度库存ID
//	 * 
//	 */
//	public Object getStockHistoryID(Map<String, Object> map) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.putAll(map);
//		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getStockHistoryID");
//		return baseServiceImpl.get(paramMap);
//	}
//
//	/**
//	 * 
//	 * 删除相同记录
//	 * 
//	 * @param stockHistoryID 产品月度库存ID
//	 * @return 无
//	 * 
//	 */	
//	public void deleteSameStockHistory(int stockHistoryID) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("stockHistoryID", stockHistoryID);
//		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.deleteSameStockHistory");
//		baseServiceImpl.remove(paramMap);
//	}

	/**
	 * 
	 * 查询截止日期表是否已有记录
	 * 
	 * @param map 查询条件
	 * @return 截止日期ID
	 * 
	 */
	
	public Object getStockCloseDate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getStockCloseDate");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询需要重算的截止日期List
	 * 
	 * @param map 查询条件
	 * @return 需要重算的截止日期List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRecalDateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getRecalDateList");
		return baseServiceImpl.getList(paramMap);
	}
	
//	/**
//	 * 
//	 * 查询已经存在的月度库存表记录List
//	 * 
//	 * @param map 查询条件
//	 * @return 已经存在的月度库存表记录List
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Map<String, Object>> getStockHistoryList(Map<String, Object> map) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.putAll(map);
//		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.getStockHistoryList");
//		return baseServiceImpl.getList(paramMap);
//	}
	
//	/**
//	 * 
//	 * 批量删除已经存在的月度库存表记录
//	 * 
//	 * @param list 促销产品月度库存ID集合
//	 * @return 无
//	 * 
//	 */	
//	public void deleteSameStockHistory(List<Map<String, Object>> list) {
//		baseServiceImpl.deleteAll(list, "BINBESSPRO01.deleteSameStockHistory");
//	}
	
	/**
	 * 
	 * 删除库存为0的月度库存记录
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void deleteZeroStockHistory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.deleteZeroStockHistory");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 
	 * 删除指定截止日期的所有月度库存记录
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void deleteStockHistoryByColseDate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRO01.deleteStockHistoryByColseDate");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 
	 * 批量插入产品月度库存表
	 * 
	 * @param list 插入内容
	 * @return 无
	 * 
	 */
	public void insertStockHistoryBatch(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBESSPRO01.insertStockHistory");
	}

}
