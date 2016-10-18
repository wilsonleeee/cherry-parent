/*	
 * @(#)BINOLBSCNT06_Service.java     1.0 2011/05/09		
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
package com.cherry.bs.cnt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	柜台Excel导入处理Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT06_Service extends BaseService {
	
	/**
	 * 
	 * 查询柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 柜台ID
	 * 
	 */
	public Object getCounterId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getCounterId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询组织结构中的柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 部门ID
	 * 
	 */
	public Object getOrganizationId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getOrganizationId");
		return baseServiceImpl.get(paramMap);
	}

	/**
	 * 
	 * 更新柜台信息表
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int updateCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.updateCounterInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新在组织结构中的柜台
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.updateCouOrg");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 
	 * 插入柜台信息
	 * 
	 * @param counterMap 插入内容
	 * @return 柜台ID
	 * 
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int insertCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.insertCounterInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 
	 * 插入柜台开始事件信息
	 * 
	 * @param counterMap 插入内容
	 * @return 无
	 * 
	 */
	public void insertCounterEvent(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.insertCounterEvent");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 取得品牌的顶层节点
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */				
	public Object getFirstPath(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getFirstPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台在组织结构表中的插入位置
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */				
	public Object getCounterNodeId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getCounterNodeId");
		return baseServiceImpl.get(paramMap);
	}

	/**
	 * 
	 * 在组织结构表中插入柜台节点
	 * 
	 * @param counterMap 插入内容
	 * @return Object
	 * 
	 */					
	public Object insertCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.insertCouOrg");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 查询城市区域ID
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getRegionId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getRegionId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询商场ID
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getMallInfoId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getMallInfoId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询渠道ID
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getChannelId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getChannelId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询经销商ID
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getResellerInfoId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getResellerInfoId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询上级部门path
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getHigherPath(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT06.getHigherPath");
		return baseServiceImpl.get(paramMap);
	}

}
