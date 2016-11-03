/*
 * @(#)BINBESSRPS01_Service.java     1.0 2012/11/08
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
package com.cherry.ss.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 销售月度统计Service
 * 
 * @author WangCT
 * @version 1.0 2012/11/08
 */
public class BINBESSRPS01_Service extends BaseService {
	
	/**
	 * 查询销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleCountInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.getSaleCountInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加销售月度统计信息
	 * 
	 * @param list 添加内容
	 */
	public void addSaleCountHistory(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBESSRPS01.addSaleCountHistory");
	}
	
	/**
	 * 更新销售月度统计信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSaleCountHistory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.updateSaleCountHistory");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除销售月度统计信息
	 * 
	 * @param map 删除条件
	 * @return 删除件数 
	 */
	public int deleteSaleCountHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.deleteSaleCountHistory");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 把销售统计区分更新为待处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSaleCountFlagWait(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.updateSaleCountFlagWait");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 把销售统计区分更新为处理完成状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSaleCountFlagEnd(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.updateSaleCountFlagEnd");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询未进行销售月度统计的最小时间
	 * 
	 * @param map 查询条件
	 * @return 未进行销售月度统计的最小时间
	 */
	public String getMinSaleDate(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.getMinSaleDate");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询指定日期所在的财务年月
	 * 
	 * @param map 查询条件
	 * @return 指定日期所在的财务年月
	 */
	public Map<String, Object> getFiscalMonth(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.getFiscalMonth");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询指定财务月的最小最大自然日
	 * 
	 * @param map 查询条件
	 * @return 指定财务月的最小最大自然日
	 */
	public Map<String, Object> getMinMaxDateValue(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.getMinMaxDateValue");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加销售月度统计信息
	 * 
	 * @param map 添加内容
	 */
	public void addSaleCountHistoryAll(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.addSaleCountHistoryAll");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 查询未进行销售统计且为销售记录修改的记录数
	 * 
	 * @param map 查询条件
	 * @return 未进行销售统计且为销售记录修改的记录数
	 */
	public int getModifiedSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBESSRPS01.getModifiedSaleCount");
		return baseServiceImpl.getSum(parameterMap);
	}

}
