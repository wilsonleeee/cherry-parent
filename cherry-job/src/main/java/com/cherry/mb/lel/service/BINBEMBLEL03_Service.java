/*	
 * @(#)BINBEMBLEL03_Service.java     1.0 2014/03/21		
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
 * 会员等级计算及报表导出 Service
 * 
 * @author HUB
 * @version 1.0 2014/03/21
 */
public class BINBEMBLEL03_Service extends BaseService{
	
	/**
	 * 
	 * 更新执行标识(导入初始等级)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateBTExecFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.updateLE03ExecFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(计算会员等级)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExecFlag01(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.updateLE03ExecFlag01");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(记录某个时间点会员等级)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExecFlag02(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.updateLE03ExecFlag02");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(会员等级重算)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExecFlag03(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.updateLE03ExecFlag03");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识(从老后台导入会员等级)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExecFlag04(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.updateLE03ExecFlag04");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 取得需要处理的会员件数(手动设定的会员)
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getMemExecCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.getMemExecCount01");
		return baseServiceImpl.getSum(map);
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
				"BINBEMBLEL03.getLE03MemList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入会员当前等级表
	 * @param list 会员当前等级表
	 */
	public void addMemCurLevelList(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBLEL03.addMemCurLevelInfo");
	}
	
	/**
	 * 
	 * 查询会员初始导入之前的销售数量
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getSaleCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.getLE03SaleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得已经导入的会员列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 已经导入的会员列表
	 * 
	 */
	public List<Map<String, Object>> getImptedMemList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getImptedMemList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要处理的会员信息(计算等级)
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要处理的会员信息(计算等级)
	 * 
	 */
	public List<Map<String, Object>> getCurLevelList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03CurLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得导入时间
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			导入时间
	 * 
	 */
	public String getMaxInitTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getMaxInitTime");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员等级计算错误日志表
	 * @param list 会员等级计算错误日志表
	 */
	public void addLevelCalcErrorList(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBLEL03.addLevelCalcErrorInfo");
	}
	
	/**
	 * 查询需要计算的销售记录
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要计算的销售记录
	 * 
	 */
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03SaleRecordList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得累计金额
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 累计金额
	 * 
	 */
	public double getTtlAmount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03TtlAmount");
		Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != resultMap && !resultMap.isEmpty()
				&& null != resultMap.get("ttlAmount")) {
			return Double.parseDouble(resultMap.get("ttlAmount").toString());
		}
		return 0;
	}
	
	/**
	 * 
	 * 更新会员当前等级表(批量处理)
	 * 
	 * @param list 需要更新的会员当前等级表
	 * 
	 */	
	public void updateMemCurLevelList(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBLEL03.updateMemCurLevelInfo");
	}
	
	/**
	 * 取得记录等级的会员信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要记录等级的会员信息
	 * 
	 */
	public List<Map<String, Object>> getLevelRecordList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03LevelRecordList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员某个时间点的等级
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 等级列表
	 * 
	 */
	public int getHistoryLevel(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLevelChangeInfo");
		Map<String, Object> histortyLevelInfo = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != histortyLevelInfo && !histortyLevelInfo.isEmpty()) {
			if (null != histortyLevelInfo.get("memLevel")) {
				return Integer.parseInt(histortyLevelInfo.get("memLevel").toString());
			}
		} else if (null != map.get("initialMemLevel")){
			return Integer.parseInt(map.get("initialMemLevel").toString());
		}
		return 0;
	}
	
	/**
	 * 
	 * 删除会员等级履历
	 * 
	 * @param list 删除条件
	 * 
	 */
	public void delLevelHistory(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMBLEL03.delLE03LevelHistory");
	}
	
	/**
	 * 
	 * 删除会员等级变化履历
	 * 
	 * @param list 删除条件
	 * 
	 */
	public void delLevelChange(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMBLEL03.delLE03LevelChange");
	}
	
	/**
	 * 取得铂金会员购买总数
	 * 
	 * @param map
	 * @return
	 */
	public int getVipPlusBuyCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBLEL03.getVipPlusBuyCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得铂金会员购买信息 
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 促销品信息List
	 * 
	 */
	public List<Map<String, Object>> getVipPlusBuyList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getVipPlusBuyList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要处理的会员信息(从老后台导入会员等级及有效期处理)
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要处理的会员信息
	 * 
	 */
	public List<Map<String, Object>> getImptLevelList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03ImptLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 去除执行标识
	 * 
	 * @param list 更新条件
	 */
	public void clearExecFlag04(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINBEMBLEL03.updateBTExecFlagNl04");
	}
	
	/**
	 * 取得会员卡号列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 需要处理的会员信息
	 * 
	 */
	public List<Map<String, Object>> getMemCardList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03MemCardList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 从老后台取得会员等级变化列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 会员等级变化列表
	 * 
	 */
	public List<Map<String, Object>> getWitLevelList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBLEL03.getLE03WitLevelList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 更新会员等级有效期
	 * 
	 * @param list 需要更新的会员
	 * 
	 */	
	public void updateLevelDateInfo(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBLEL03.updateLE03LevelDateInfo");
	}
	
	/**
	 * 
	 * 记录错误信息
	 * 
	 * @param list 需要更新的会员
	 * 
	 */	
	public void updateLevelErrInfo(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBLEL03.updateLE03LevelErrInfo");
	}
}
