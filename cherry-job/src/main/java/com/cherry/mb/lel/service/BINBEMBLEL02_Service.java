/*	
 * @(#)BINBEMBLEL02_Service.java     1.0 2014/03/21		
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
package com.cherry.mb.lel.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 推算等级变化明细(雅漾)处理Service
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL02_Service extends BaseService{
	
	/**
	 * 取得BATCH执行记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			BATCH执行记录数
	 * 
	 */
	public int getBTExecCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getBTExecCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 更新执行标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateBTExecFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.updateBTExecFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(仅新增)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateBTExecFlagOnlyAdd(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.updateBTExecFlagOnlyAD");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(仅新增)生成明细用
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateFlagDetailOnlyAdd(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.updateFlagDetailOnlyAD");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得需要处理的会员信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要处理的会员信息
	 * 
	 */
	public List<Map<String, Object>> getMemList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getMemList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得等级列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 等级列表
	 * 
	 */
	public List<Map<String, Object>> getLevelList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得期间内的会员购买记录
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 等级列表
	 * 
	 */
	public List<Map<String, Object>> getBTBuyList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getBTBuyList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员历史等级
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 等级列表
	 * 
	 */
	public Map<String, Object> getHistoryLevelInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getHistoryLevelInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得等级列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 等级列表
	 * 
	 */
	public Object getTtlAmount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL02.getTtlAmount");
		Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != resultMap && !resultMap.isEmpty()
				&& null != resultMap.get("ttlAmount")) {
			return resultMap.get("ttlAmount");
		} else {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.getBTSaleCount");
			// 查询会员期初之前的销售数量
			int count = baseServiceImpl.getSum(map);
			if (count > 0) {
				return 0;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 查询区间内会员等级履历数量
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getLevelHistCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.getLevelHistCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 查询区间内会员等级变化年度统计数量
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getLevelChangeCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.getBTLevelChangeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 取得需要处理的会员件数(手动设定的会员)
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getMemExecCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL02.getMemExecCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 删除会员等级履历
	 * 
	 * @param list 删除条件
	 * 
	 */
	public void delLevelHistory(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMBLEL02.delLevelHistory");
	}
	
	/**
	 * 
	 * 删除会员等级变化履历
	 * 
	 * @param list 删除条件
	 * 
	 */
	public void delLevelChange(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMBLEL02.delLevelChange");
	}
	
	/**
	 * 插入会员等级调整履历表
	 * @param list 会员等级调整履历
	 */
	public void addLevelHistoryInfo(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBLEL02.addLevelHistoryInfo");
	}
	
	/**
	 * 插入会员等级变化年度统计表
	 * @param list 会员等级变化履历
	 */
	public void addLevelChangeReport(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBLEL02.addLevelChangeReport");
	}
	
	/**
	 * 
	 * 去除执行标识
	 * 
	 * @param list 更新条件
	 */
	public void clearExecFlag(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINBEMBLEL02.updateBTExecFlagNl");
	}
}
