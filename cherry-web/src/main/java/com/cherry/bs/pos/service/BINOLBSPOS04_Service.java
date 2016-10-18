/*
 * @(#)BINOLBSPOS04_Service.java     1.0 2010/10/27
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 添加岗位画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS04_Service extends BaseService {
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewNodeId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.getNewNodeId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加岗位
	 * 
	 * @param map 添加内容
	 */
	public void addPosition(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.addPosition");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 取得岗位类别信息
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.getPositionCategoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得某一部门的所有上级部门岗位List
	 * 
	 * @param map 查询条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getPositionByOrg(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.getPositionByOrg");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 判断组织中是否存在岗位
	 * 
	 * @param map 查询条件
	 * @return 岗位数
	 */
	public int getPositionIdByOrgInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.getPositionIdByOrgInfo");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得组织中的最上级部门ID
	 * 
	 * @param map 查询条件
	 * @return 最上级部门IDList
	 */
	public List<String> getOrgIdByOrgInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS04.getOrgIdByOrgInfo");
		return baseServiceImpl.getList(parameterMap);
	}

}
