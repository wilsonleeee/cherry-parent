/*
 * @(#)BINOLSSPRM68_Service.java     1.0 2015/09/21
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品关联Service
 * 
 * @author Hujh
 * @version 1.0 2015.09.21
 */
public class BINOLSSPRM71_Service extends BaseService {
	
	/**
	 * 查询关联的促销品数量
	 * @param searchMap
	 * @return
	 */
	public int getPrmCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM71.getPrmCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 查询相关联的促销品
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM71.getPrmList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询最大组号
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMaxGroupId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM71.getMaxGroupId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 促销品关联
	 * @param list
	 */
	public void conjunction(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLSSPRM71.conjunction");
	}

	/**
	 * 根据BIN_GroupID获取促销品关联明细
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDetailPrmList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM71.getDetailPrmList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 删除分组
	 * @param map
	 */
	public void delGroups(Map<String, Object> map) {
		baseServiceImpl.remove(map, "BINOLSSPRM71.delGroups");
	}

	/**
	 * 删除一条数据
	 * @param map
	 */
	public void delOnePrm(Map<String, Object> map) {
		baseServiceImpl.remove(map, "BINOLSSPRM71.delOnePrm");
	}

	/**
	 * 根据BIN_ProductVendorID查询所在组的所有数据
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmListByPrmVendorId(Map<String, Object> map) {
		
		return baseServiceImpl.getList(map, "BINOLSSPRM71.getPrmListByPrmVendorId");
	}

	/**
	 * 删除一组数据
	 * @param paramMap
	 */
	public void delOneGroup(Map<String, Object> map) {
		
		baseServiceImpl.remove(map, "BINOLSSPRM71.delOneGroup");
	}

}