/*
 * @(#)BatchListService.java     1.0 2011/07/18
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
package com.webconsole.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * batch一览查询Service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/18
 */
public class BatchListService extends BaseService {
	
	/**
	 * 取得batch一览总数
	 * 
	 * @param map 查询条件
	 * @return batch一览总数
	 */
	public int getBatchCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getBatchCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得batch一览
	 * 
	 * @param map 查询条件
	 * @return batch一览
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBatchList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getBatchList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌信息List
	 * 
	 * @param map 查询条件
	 * @return batch一览
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getBrandInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得老后台数据源信息
	 * 
	 * @param map 查询条件
	 * @return 老后台数据源信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getConfBrandInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getConfBrandInfo111");
		return (Map)baseConfServiceImpl.get(map);
	}
	
	/**
	 * 取得未完成的工作流信息条数
	 * 
	 * @param map 查询条件
	 * @return 老后台数据源信息
	 */
	@SuppressWarnings("unchecked")
	public int getWorkFlowCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getWorkFlowCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得未完成的工作流信息
	 * 
	 * @param map 查询条件
	 * @return 老后台数据源信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWorkFlowInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getWorkFlowInfo");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得未完成的工作流文件内容信息
	 * 
	 * @param map 查询条件
	 * @return 老后台数据源信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getWorkFlowContent(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getWorkFlowContent");
		return (Map<String, Object>) baseConfServiceImpl.get(map);
	}
	
	/**
	 * 取得batch一览
	 * 
	 * @param map 查询条件
	 * @return batch一览
	 */
	@SuppressWarnings("unchecked")
	public  String getBussinessDate(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "batchList.getBussinessDate");
		return (String) baseServiceImpl.get(map);
	}
}
