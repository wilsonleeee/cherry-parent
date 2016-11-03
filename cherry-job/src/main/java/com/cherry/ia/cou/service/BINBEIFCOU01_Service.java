/*
 * @(#)BINBEIFCOU01_Service.java     1.0 2010/11/12
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

package com.cherry.ia.cou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 柜台列表导入Service
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.11.12
 */
public class BINBEIFCOU01_Service extends BaseService {

	/**
	 * 
	 * 从接口数据库中查询柜台数据
	 * 
	 * @param map 查询条件
	 * @return 接口表中取得的柜台List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCountersList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCountersList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 柜台ID
	 * 
	 */
	public Map<String, Object> getCounterId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCounterId");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询组织结构中的柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 部门ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrganizationId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getOrganizationId");
		return (Map)baseServiceImpl.get(paramMap);
	}

	/**
	 * 
	 * 更新柜台信息表
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.updateCounterInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.updateCouOrg");
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
	public int insertCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertCounterInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertCounterEvent");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getFirstPath");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCounterNodeId");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertCouOrg");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 
	 * 备份柜台信息表
	 * 
	 * @param map 需要备份数据的查询条件
	 * @return 无
	 * 
	 */
	public void backupCounters(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.backupCounters");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 删除世代番号超过上限的数据
	 * 
	 * @param map 查询条件
	 * @return 无
	 * 
	 */
	public void clearBackupData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.clearBackupData");
		baseServiceImpl.remove(paramMap);

	}

	/**
	 * 
	 * 更新世代番号
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */
	public void updateBackupCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}

	/**
	 * 
	 * 查询要伦理删除的柜台数据
	 * 
	 * @param map 查询条件
	 * @return 需要删除的柜台List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getDelList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 伦理删除无效的柜台数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void delInvalidCounters(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.delInvalidCounters");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 伦理删除无效的部门数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void delInvalidDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.delInvalidDepart");
		baseServiceImpl.update(paramMap);
	}	

	/**
	 * 
	 * 取得cityCode的上一级/上二级区域信息
	 * 
	 * @param counterMap 查询条件
	 * @return 区域信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParentRegionByCity(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getParentRegionByCity");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询区域信息
	 * 
	 * @param counterMap 查询条件
	 * @return 区域信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRegionInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getRegionInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询省区域信息
	 * 
	 * @param counterMap 查询条件
	 * @return 省区域信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProvinceInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getProvinceInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询城市区域信息
	 * 
	 * @param counterMap 查询条件
	 * @return 城市区域信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCityInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCityInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	/**
	 * 
	 * 查询区域节点插入位置
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */	
	public Object getNewRegNode(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getNewRegNode");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入区域节点
	 * 
	 * @param counterMap 插入内容
	 * @return 区域ID
	 * 
	 */
	public int insertRegNode(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertRegNode");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 插入省区域节点
	 * 
	 * @param counterMap 插入内容
	 * @return 省区域ID
	 * 
	 */
	public int insertPrvnRegNode(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertPrvnRegNode");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 
	 * 插入城市节点
	 * 
	 * @param counterMap 插入内容
	 * @return 城市区域ID
	 * 
	 */
	public int insertCityNode(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertCityNode");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 更新城市上级节点
	 * 
	 * @param counterMap 更新内容
	 * @return 更新件数
	 * 
	 */
	public int updateCityNode(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.updateCityNode");
		return baseServiceImpl.update(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getChannelId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入渠道
	 * 
	 * @param counterMap 插入内容
	 * @return 渠道ID
	 * 
	 */
	public int insertChannelId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertChannelId");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 查询区域表中的品牌节点
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */
	public Object getBrandRegionPath(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getBrandRegionPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得柜台主管所在部门节点
	 * 
	 * @param map 查询条件
	 * @return 部门节点
	 */
	public String getCouHeadDepPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCouHeadDepPath");
		return (String)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getResellerInfoId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入经销商
	 * 
	 * @param counterMap 插入内容
	 * @return 经销商ID
	 * 
	 */
	public int insertResellerInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.insertResellerInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 判断柜台事件信息是否已经存在
	 * 
	 * @param counterMap 判断条件
	 * @return 柜台事件ID
	 * 
	 */
	public List<String> getCounterEventId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU01.getCounterEventId");
		return baseServiceImpl.getList(paramMap);
	}

}
