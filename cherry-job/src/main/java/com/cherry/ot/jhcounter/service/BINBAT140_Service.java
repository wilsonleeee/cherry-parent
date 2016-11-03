/*	
 * @(#)BINBAT140_Service.java     1.0 @2016-3-18
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
package com.cherry.ot.jhcounter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


/**
 *
 * SAP接口(WSDL)：柜台导入SERVICE
 *
 * @author zhouwei
 *
 * @version  2016-3-18
 */
@SuppressWarnings("unchecked")
public class BINBAT140_Service extends BaseService {
	
	
	
	/**
	 * 更新备份表备份次数
	 * 
	 * @param int
	 * 
	 * 
	 * @return 无
	 * 
	 */
	public void updateBackupCount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT140.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}
	
	
	
	/**
	 * 清理柜台备份表
	 * 
	 * @param int
	 * 
	 * 
	 * @return 无
	 * 
	 */
	public void clearBackupData(int count) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("count", count);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.clearBackupData");
		baseServiceImpl.remove(paramMap);

	}
	
	
	/**
	 * 
	 * 备份标准柜台接口信息表 
	 * 
	 * @param map 需要备份数据的查询条件
	 * @return 无
	 * 
	 */

	public void backupCounters(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT140.backupCounters");
		baseServiceImpl.save(map);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getChannelId");
		return baseServiceImpl.get(paramMap);
	}
	
	
	
	/**
	 * 
	 * 取得cityCode的上一级/上二级区域信息
	 * 
	 * @param counterMap 查询条件
	 * @return 区域信息
	 * 
	 */
	public Map<String, Object> getParentRegionByCity(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getParentRegionByCity");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	

	/**
	 * 
	 * 查询组织结构中的柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 部门ID
	 * 
	 */
	public Map<String, Object> getOrganizationId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getOrganizationId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getUnknownPath");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getFirstPath");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getNewDepNodeId");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.insertDepart");
		return baseServiceImpl.saveBackId(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getCounterNodeId");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.insertCouOrg");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 在组织结构表中查询父节点NodId
	 * 
	 * @param counterMap 判断条件
	 * @return Object
	 * 
	 */
	public Map<String, Object> getparentNodeID(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getparentNodeID");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
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
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getDepotCountByCode");
		return baseServiceImpl.getSum(parameterMap);
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
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.addDepotInfo");
		return baseServiceImpl.saveBackId(parameterMap);
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
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.addInventoryInfo");
		baseServiceImpl.save(parameterMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT140.updateDepotInfo");
		return baseServiceImpl.update(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getCouHeadDepPath");
		return (String)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.updateCouOrg");
		return baseServiceImpl.update(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getCounterId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.insertCounterInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.insertCounterEvent");
		baseServiceImpl.save(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.updateCounterInfo");
		return baseServiceImpl.update(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getCounterEventId");
		return baseServiceImpl.getList(paramMap);
	}

	

	
	/**
	 * 
	 * 取得品牌下的未知节点信息
	 * 
	 * @param map 查询条件
	 * @return 未知节点信息
	 * 
	 */
	public Map<String, Object> getSubNodeCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getSubNodeCount");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询要伦理删除的柜台数据
	 * 
	 * @param map 查询条件
	 * @return 需要删除的柜台List
	 * 
	 */
	public List<Map<String, Object>> getDelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.getDelList");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.delInvalidCounters");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.delInvalidDepart");
		baseServiceImpl.update(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT140.delUnknownNode");
		baseServiceImpl.remove(paramMap);
	}
	
	
	
	
}
