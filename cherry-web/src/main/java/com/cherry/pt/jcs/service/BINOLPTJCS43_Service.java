/*
 * @(#)BINOLPTJCS43_Service.java     1.0 2015/10/13
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
package com.cherry.pt.jcs.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品关联维护Service
 * 
 * @author Hujh
 * @version 1.0 2015.10.13
 */
public class BINOLPTJCS43_Service extends BaseService{

	/**
	 * 查询符合条件的产品数量
	 * @param map
	 * @return
	 */
	public int getPrtCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS43.getPrtCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 根据查询条件获取产品list
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS43.getPrtList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取最大组号
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMaxGroupId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS43.getMaxGroupId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 产品关联
	 * @param list
	 */
	public void conjunction(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPTJCS43.conjunction");
	}

	/**
	 * 根据BIN_GroupID获取产品关联明细
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDetailPrtList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS43.getDetailPrtList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据BIN_ProductVendorID查询所在组的所有产品
	 * @param tempMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtListByPrtVendorId(Map<String, Object> map) {
		
		return baseServiceImpl.getList(map, "BINOLPTJCS43.getPrtListByPrtVendorId");
	}

	/**
	 * 根据BIN_ProductVendorID删除一条数据
	 * @param tempMap
	 */
	public void delOnePrt(Map<String, Object> map) {
		
		baseServiceImpl.remove(map, "BINOLPTJCS43.delOnePrt");
	}

	/**
	 * 删除一组数据
	 * @param tempMap
	 */
	public void delOneGroup(Map<String, Object> map) {
		
		baseServiceImpl.remove(map, "BINOLPTJCS43.delOneGroup");
	}

	/**
	 * 删除多组
	 * @param map
	 */
	public void delGroups(Map<String, Object> map) {
		
		baseServiceImpl.remove(map, "BINOLPTJCS43.delGroups");
	}

}