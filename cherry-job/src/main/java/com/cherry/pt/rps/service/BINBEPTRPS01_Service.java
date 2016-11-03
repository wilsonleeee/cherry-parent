/*
 * @(#)BINBEPTRPS01_Service.java     1.0.0 2013/08/15
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 进销存统计查询Service 
 * 
 * 
 * 
 * @author zhangle
 * @version 1.0.0 2013.08.15
 */
public class BINBEPTRPS01_Service extends BaseService{
	
	/**
	 * 柜台数据list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 柜台主管数据list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBASDataList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getBASDataList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 部门数据list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartDataList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getDepartDataList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 工作流状态数据list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOFWDataList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getOFWDataList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 统计参数查询
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getInventoryOperationParameter");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入进销存操作统计数据
	 * @param list
	 */
	public void insertData(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.insertInventoryOperation");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 删除某个时间点之前的进销存操作统计数据
	 * @param map
	 * @return
	 */
	public int delData(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.delStatisticData");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得业务参数
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBussnissParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getBussnissParameter");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得部门参数
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEPTRPS01.getDepartParameter");
		return  baseServiceImpl.getList(map);
	}	
}
