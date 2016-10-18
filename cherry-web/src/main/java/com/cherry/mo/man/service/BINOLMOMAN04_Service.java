/*  
 * @(#)BINOLMOMAN04_Service.java     1.0 2011/05/31      
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
package com.cherry.mo.man.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOMAN04_Service extends BaseService {

	/*
	 * 获取设置了升级状态的柜台的大区信息
	 * 
	 * @author zgl
	 * 
	 * @param map
	 * 
	 * @result list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getRegionList");
		return baseServiceImpl.getList(map);
	}

	/*
	 * 获取设置了升级状态的柜台所在区域的下级区域信息
	 * 
	 * @author zgl
	 * 
	 * @param map
	 * 
	 * @result list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubRegionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getSubRegionList");
		return baseServiceImpl.getList(map);
	}

	/*
	 * 获取设置了升级状态的柜台信息
	 * 
	 * @author zgl
	 * 
	 * @param map
	 * 
	 * @result list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getCounterList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 通过柜台ID更新柜台机器升级状态
	 * 
	 * @param map
	 * 
	 * */
	public void updateCounterUpdateStatusByCounterId(
			List<Map<String, Object>> list) {
		// map.put(CherryConstants.IBATIS_SQL_ID,
		// "BINOLMOMAN04.updateCounterUpdateStatusByCounterId");
		baseServiceImpl.updateAll(list,
				"BINOLMOMAN04.updateCounterUpdateStatusByCounterId");
	}

	/**
	 * 通过区域ID更新柜台机器升级状态
	 * 
	 * @param map
	 * 
	 * */
	public void updateCounterUpdateStatusByRegionId(
			List<Map<String, Object>> list) {
		// map.put(CherryConstants.IBATIS_SQL_ID,
		// "BINOLMOMAN04.updateCounterUpdateStatusByRegionId");
		baseServiceImpl.updateAll(list,
				"BINOLMOMAN04.updateCounterUpdateStatusByRegionId");
	}

	/**
	 * 更新机器升级状态时根据区域信息获得柜台信息
	 * 
	 * @param map
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterInformationWhenUpdateStatusByRegionId(
			Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN04.getCounterInformationWhenUpdateStatusByRegionId");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 更新机器升级状态时柜台ID信息获得柜台信息
	 * 
	 * @param map
	 *            查询条件
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterInformationWhenUpdateStatusByCounterId(
			Map<String, Object> map) {
		map
				.put(CherryConstants.IBATIS_SQL_ID,
						"BINOLMOMAN04.getCounterInformationWhenUpdateStatusByCounterId");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得未设置升级状态的柜台所在的大区LIST
	 * 
	 * @param map
	 *            查询条件
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRegionNoUpdateStatus(
			Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN04.getRegionNoUpdateStatus");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得未设置升级状态的柜台所在的区域的下级区域
	 * 
	 * @param map
	 *            查询条件
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubRegionNoUpdateStatus(
			Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN04.getSubRegionNoUpdateStatus");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取未设置升级状态的柜台信息
	 * 
	 * @param map
	 *            查询条件
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterNoUpdateStatus(
			Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN04.getCounterNoUpdateStatus");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 根据柜台信息删除柜台升级状态表中的相应的数据
	 * 
	 * @param list
	 * @return
	 * 
	 * */
	public void deleteUpdateStatusByCounter(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list,
				"BINOLMOMAN04.deleteUpdateStatusByCounter");
	}

	/**
	 * 删除指定区域下的柜台的在升级状态表中的相应的数据
	 * 
	 * @param list
	 * 
	 * 
	 * */
	public void deleteUpdateStatusByRegion(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list,
				"BINOLMOMAN04.deleteUpdateStatusByRegion");
	}

	/**
	 * 根据柜台信息向柜台升级信息表中插入数据
	 * 
	 * @param list
	 * 
	 * 
	 * */
	public void insertUpdateStatusByCounter(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list,
				"BINOLMOMAN04.insertUpdateStatusByCounter");
	}
	
	/**
	 * 取得下发所需的BrandCode,LastCode
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getBrandCodeLastCode(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getBrandCodeAndLastCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 取得下发所需的CounterCode
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCounterCode(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getCounterCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得机器类型List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMachineTypeList(Map<String, Object> map) {
	    map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN04.getMachineTypeList");
	    return baseServiceImpl.getList(map);
	}
}
