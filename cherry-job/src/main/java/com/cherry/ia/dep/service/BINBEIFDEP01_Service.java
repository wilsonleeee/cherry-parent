/*
 * @(#)BINBEIFDEP01_Service.java     1.0 2010/11/12
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

package com.cherry.ia.dep.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 部门列表导入Service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFDEP01_Service extends BaseService {
	
	/**
	 * 
	 * 从接口数据库中查询部门数据
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getDepartList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门ID
	 * 
	 */
	public Object getDepartId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getDepartId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 更新部门信息表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.updateDepart");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询部门上级节点
	 * 
	 * @param map 查询条件
	 * @return 部门上级节点
	 * 
	 */
	public Object getSeniorDepPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getSeniorDepPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得品牌的顶层节点
	 * 
	 * @param map 查询条件
	 * @return 品牌的顶层节点
	 * 
	 */
	public Object getFirstPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getFirstPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得品牌下的未知节点
	 * 
	 * @param map 查询条件
	 * @return 品牌下的未知节点
	 * 
	 */
	public Object getUnknownPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getUnknownPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 部门新节点
	 * 
	 */
	public Object getNewDepNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getNewDepNodeId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入部门信息
	 * 
	 * @param map 插入信息
	 * @return 部门ID
	 * 
	 */
	public int insertDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.insertDepart");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 备份部门信息表
	 * 
	 * @param map 查询条件
	 * @return 无
	 * 
	 */
	public void backupDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.backupDepart");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 删除世代番号超过上限的数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void clearBackupData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.clearBackupData");
		baseServiceImpl.remove(paramMap);
	}

	/**
	 * 
	 * 更新世代番号
	 * 
	 * @param map 查询条件
	 * @return 无
	 * 
	 */
	public void updateBackupCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询需要伦理删除的部门数据
	 * 
	 * @param map 删除条件
	 * @return 需要伦理删除的部门List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelDepartList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getDelDepartList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 伦理删除无效的部门数据
	 * 
	 * @param map 需要伦理删除的部门
	 * @return 无
	 * 
	 */
	public void delInvalidDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.delInvalidDepart");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 添加仓库
	 * 
	 * @param map 添加内容
	 * 
	 */
	public int addDepotInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.addDepotInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 
	 * 更新柜台仓库名称
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateDepotInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFDEP01.updateDepotInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 添加部门仓库关系
	 * 
	 * @param map 添加内容
	 * 
	 */
	public void addInventoryInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.addInventoryInfo");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 
	 * 取得品牌下的未知节点信息
	 * 
	 * @param map 查询条件
	 * @return 未知节点信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSubNodeCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getSubNodeCount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 删除未知节点
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void delUnknownNode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.delUnknownNode");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 判断仓库编码是否已经存在
	 * 
	 * @param map 查询条件
	 * 
	 */
	public int getDepotCountByCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFDEP01.getDepotCountByCode");
		return baseServiceImpl.getSum(parameterMap);
	}

}
