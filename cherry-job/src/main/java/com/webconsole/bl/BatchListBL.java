/*
 * @(#)BatchListBL.java     1.0 2011/07/18
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
package com.webconsole.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.webconsole.service.BatchListService;

/**
 * 
 * batch一览查询BL
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/18
 */
public class BatchListBL {
	
	/** batch一览查询Service */
	@Resource
	private BatchListService batchListService;
	
	/**
	 * 取得batch一览总数
	 * 
	 * @param map 查询条件
	 * @return batch一览总数
	 */
	public int getBatchCount(Map<String, Object> map) {
		
		// 取得batch一览总数
		return batchListService.getBatchCount(map);
	}
	
	/**
	 * 取得batch一览
	 * 
	 * @param map 查询条件
	 * @return batch一览
	 */
	public List<Map<String, Object>> getBatchList(Map<String, Object> map) {
		
		// 取得batch一览
		return batchListService.getBatchList(map);
	}
	
	/**
	 * 取得品牌信息List
	 * 
	 * @param map 查询条件
	 * @return batch一览
	 */
	public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map) {
		
		// 取得品牌信息List
		return batchListService.getBrandInfoList(map);
	}
	
	/**
	 * 取得老后台数据源信息
	 * 
	 * @param map 查询条件
	 * @return 老后台数据源信息
	 */
	public Map<String, Object> getConfBrandInfo(Map<String, Object> map) {
		
		// 取得老后台数据源信息
		return batchListService.getConfBrandInfo(map);
	}

	/**
	 * 取得工作流文件信息
	 * 
	 * @param map 查询条件
	 * @return 工作流文件内容
	 */
	public Map<String, Object> getWorkFlowContent(Map<String, Object> map) {
		
		// 取得工作流文件信息
		return batchListService.getWorkFlowContent(map);
	}
	
	/**
	 * 取得batch品牌业务日期
	 * 
	 * @param map 查询条件
	 * @return batch
	 */
	public String getBussinessDate(Map<String, Object> map) {
		
		// 取得batch品牌业务日期
		return batchListService.getBussinessDate(map);
	}
}
