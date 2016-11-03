/*
 * @(#)BINBESSPRM02_Service.java     1.0 2010/12/20
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销产品月度库存重算SERVICE
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.12.20
 */
public class BINBESSPRM02_Service extends BaseService {
	
	/**
	 * 
	 * 查询离业务日期最近的一个截止日期
	 * 
	 * @param map 查询条件
	 * @return 离业务日期最近的一个截止日期
	 * 
	 */
	public Map<String, Object> getLastPrmStockCloseDay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getLastPrmStockCloseDay");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 判断指定截止日期是否存在月度库存统计信息
	 * 
	 * @param map 查询条件
	 * @return 件数
	 * 
	 */
	public int getPrmStockHistoryCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getPrmStockHistoryCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 
	 * 补录的促销产品入出库日期
	 * 
	 * @param map 查询条件
	 * @return 补录的促销产品入出库日期List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStockInOutDateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getStockInOutDateList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 补录的促销产品入出库数据
	 * 
	 * @param map 查询条件
	 * @return 补录的促销产品入出库数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInOutList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getInOutList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 查询要更新的截止日期LIST
	 * 
	 * @param map 查询条件
	 * @return 要更新的截止日期LIST
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEndDateList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getEndDateList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 更新促销产品月度库存表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateHistory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.updateHistory");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 
	 * 插入促销产品月度库存表
	 * 
	 * @param map 插入内容
	 * @return 无
	 * 
	 */
	public void insertHistory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.insertHistory");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 把截止计算区分从未反映到历史库存表中状态更新为待处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public int updateCloseFlagWait(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.updateCloseFlagWait");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.updateCloseFlagEnd");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询最近一个截止日期的月度库存信息
	 * 
	 * @param map 查询条件
	 * @return 最近一个截止日期的月度库存信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLastHistoryInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSPRM02.getLastHistoryInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
}
