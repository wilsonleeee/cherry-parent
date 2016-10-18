/*
 * @(#)BINOLPTRPS27_Service.java     1.0.0 2013/08/08
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
 * @version 1.0.0 2013.08.08
 */
public class BINOLPTRPS27_Service extends BaseService{
	/**
	 * 统计总数
	 * 
	 * @param map
	 * @return
	 */
	public int getInventoryOperationStatisticCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getInventoryOperationStatisticCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 统计list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInventoryOperationStatisticList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getInventoryOperationStatisticList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 部门参数查询
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getInventoryOperationParameter");
		return  baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除统计参数
	 * @param map
	 * @return
	 */
	public int delParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.delInventoryOperationParameter");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入新的参数
	 * @param map
	 * @return
	 */
	public int insertParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.insertInventoryOperationParameter");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 查询部门
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getDepartList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取调度任务
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSchedules(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getSchedules");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 添加调度任务
	 * @param map
	 * @return
	 */
	public int insertSchedules(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.insertSchedules");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新调度任务
	 * @param map
	 * @return
	 */
	public int updateSchedules(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.updateSchedules");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询业务参数
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBussnissParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getBussnissParameter");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新业务参数
	 * @param map
	 * @return
	 */
	public int updateBussnissParameter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.updateBussnissParameter");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 获取最后一次统计的数据日期
	 * @param map
	 * @return
	 */
	public String getLastStatisticDate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS27.getLastStatisticDate");
		return  (String) baseServiceImpl.get(map);
	}
}
