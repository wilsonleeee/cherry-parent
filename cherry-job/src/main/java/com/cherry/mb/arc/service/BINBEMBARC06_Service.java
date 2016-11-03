/*	
 * @(#)BINBEMBARC06_Service.java     1.0 2013/12/19
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 汇美舍会员积分清零明细下发处理 Service
 * 
 * @author hub
 * @version 1.0 2013/12/19
 */
public class BINBEMBARC06_Service extends BaseService{
	
	/**
	 * 取得需要下发的清零明细总数
	 * 
	 * @param map
	 * @return
	 */
	public int getClearSendCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC06.getClearSendCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 去除执行标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearExecFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC06.updateExecFlagNl");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC06.updateExecFlagDo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得需要下发的清零明细List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要下发的清零明细List
	 * 
	 */
	public List<Map<String, Object>> getClearSendList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBARC06.getClearSendList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要更新清零记录的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要更新清零记录的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getUpClearList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBARC06.getUpClearList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 删除清零记录
	 * 
	 * @param 需要删除的会员列表
	 *
	 * 
	 */
	public void delPointsClearRecord(List<Map<String, Object>> list) {
		this.deleteAll(list, "BINBEMBARC06.delPointsClearRecord");
	}
	
	/**
	 * 
	 * 更新下发状态
	 * 
	 * @param 需要更新的会员列表
	 *
	 * 
	 */
	public void updateSendStatus(List<Map<String, Object>> list) {
		this.updateAll(list, "BINBEMBARC06.updateSendStatus");
	}
	
	/**
	 * 
	 * 更新下发状态(成功)
	 * 
	 * @param 需要更新的会员列表
	 *
	 * 
	 */
	public void updateStatusSuccess(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBARC06.updateStatusSuccess");
	}
	
	/**
	 * 
	 * 更新下发状态(失败)
	 * 
	 * @param 需要更新的会员列表
	 *
	 * 
	 */
	public void updateStatusFail(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBARC06.updateStatusFail");
	}
	
	/**
	 * 取得同一会员同一清零时间的记录
	 * 
	 * @param map
	 * @return
	 */
	public int getIFDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC06.getIFDetailCount");
		return ifServiceImpl.getSum(map);
	}
	
	/**
	 * 插入积分清零通知清单接口表 
	 * @param map
	 */
	public void addIFClearDetail(List<Map<String, Object>> list){
		ifServiceImpl.saveAll(list, "BINBEMBARC06.insertIFClearDetail");
	}
	
	/**
	 * 
	 * 更新积分清零通知清单接口表 
	 * 
	 * @param list 需要更新的清零记录列表
	 * 
	 */	
	public void updateIFClearDetail(List<Map<String, Object>> list) {
		ifServiceImpl.updateAll(list, "BINBEMBARC06.updateIFClearDetail");
	}
	
	/**
	 * 用批处理删除一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void deleteAll(final List<Map<String, Object>> list, final String sqlId) {
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
				baseServiceImpl.deleteAll(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
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
	public void updateAll(final List<Map<String, Object>> list, final String sqlId) {
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
				baseServiceImpl.updateAll(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
	}
}
