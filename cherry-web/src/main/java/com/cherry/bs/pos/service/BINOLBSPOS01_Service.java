/*
 * @(#)BINOLBSPOS01_Service.java     1.0 2010/10/27
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

package com.cherry.bs.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 岗位一览画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS01_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得某一岗位的直属下级岗位
	 * 
	 * @param map 检索条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getNextPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getNextPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得某一用户能访问的顶层岗位List
	 * 
	 * @param map 检索条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getFirstPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getFirstPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得某一用户能访问的顶层岗位级别
	 * 
	 * @param map 检索条件
	 * @return 岗位级别
	 */
	public Integer getFirstPositionLevel(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getFirstPositionLevel");
		return (Integer)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询岗位总数
	 * 
	 * @param map 检索条件
	 * @return 返回岗位总数
	 */
	public int getPositionCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getPositionCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得岗位信息List
	 * 
	 * @param map 检索条件
	 * @return 岗位信息List
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得上级岗位信息List
	 * 
	 * @param map 检索条件
	 * @return 上级岗位信息List
	 */
	public List<Map<String, Object>> getHigherPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getHigherPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得某一柜台主管的管辖柜台List
	 * 
	 * @param map 检索条件
	 * @return 柜台List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS01.getCounterList");
		return baseServiceImpl.getList(parameterMap);
	}

}
