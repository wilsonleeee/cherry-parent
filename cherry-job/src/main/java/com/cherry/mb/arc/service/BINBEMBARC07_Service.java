/*	
 * @(#)BINBEMBARC07_Service.java     1.0 2013/12/19
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
package com.cherry.mb.arc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 珀莱雅会员等级调整  Service
 * 
 * @author hub
 * @version 1.0 2013/12/19
 */
public class BINBEMBARC07_Service extends BaseService{
	
	/**
	 * 取得需要处理的会员总数
	 * 
	 * @param map
	 * @return
	 */
	public int getAdjustExecCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.getAdjustExecCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得需要处理的会员List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要处理的会员List
	 * 
	 */
	public List<Map<String, Object>> getAdjustExecList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBARC07.getAdjustExecList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要调整等级的会员List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要下发的清零明细List
	 * 
	 */
	public List<Map<String, Object>> getLevelAdjustList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBARC07.getLevelAdjustList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要调整等级的会员(增量查询)List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要调整等级的会员(增量查询)List
	 * 
	 */
	public List<Map<String, Object>> getLevelAdjustIncreList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBARC07.getLevelAdjustIncreList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入会员等级调整履历表
	 * @param map
	 */
	public void addLevelAdjustRecord(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBARC07.addLevelAdjustRecord");
	}
	
	/**
	 * 插入会员等级调整履历表(增量)
	 * @param map
	 */
	public void addLevelAdjustIncreRecord(List<Map<String, Object>> list){
		this.addAll(list, "BINBEMBARC07.addLevelAdjustRecord");
	}
	
	/**
	 * 
	 * 去除执行标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearExecFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.updateRecordExecFlagNl");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新执行标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExecFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.updateRecordExecFlagDo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 插入规则执行履历表
	 * 
	 * @param map
	 */
	public void addRuleExecRecord(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.addMSRuleExecRecord");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * 更新会员等级信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberLevelInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.updateMemberLevelInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新等级调整状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateAdjustStatus(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC07.updateAdjustStatus");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 用批处理更新一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void addAll(final List<Map<String, Object>> list, final String sqlId) {
		if (null != list && !list.isEmpty()) {
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			int size = list.size();
			while (true) {
				startNum = CherryBatchConstants.DATE_SIZE * currentNum;
				// 查询结束位置
				endNum = startNum + CherryBatchConstants.DATE_SIZE;
				if (endNum > size) {
					endNum = size;
				}
				// 数据抽出次数累加
				currentNum++;	
				List<Map<String, Object>> tempList = list.subList(startNum, endNum);
				baseServiceImpl.saveAll(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
	}
}
