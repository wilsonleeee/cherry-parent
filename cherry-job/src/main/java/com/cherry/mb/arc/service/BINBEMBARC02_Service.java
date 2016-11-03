/*	
 * @(#)BINBEMBARC02_Service.java     1.0 2013/04/11
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
 * 会员积分初始导入处理 Service
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC02_Service extends BaseService{
	
	/**
	 * 取得品牌数据库中会员积分信息总数
	 * 
	 * @param map
	 * @return
	 */
	public int getWitMemPointCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getWitMemPointCount");
		return witBaseServiceImpl.getSum(map);
	}
	
	/**
	 * 从品牌数据库中查询会员积分信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 会员积分信息
	 * 
	 */
	public List<Map<String, Object>> getWitMemPointList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.getWitMemPointList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌数据库中最近几天有变化的会员积分信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 会员积分信息
	 * 
	 */
	public List<Map<String, Object>> getWitMemLastList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.getWitMemLastList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 品牌数据库中最近几天有变化的会员积分总数
	 * 
	 * @param map
	 * @return
	 */
	public int getWitMemLastCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getWitMemLastCount");
		return witBaseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得积分明细(品牌业务表)
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 积分明细(品牌业务表)
	 * 
	 */
	public List<Map<String, Object>> getWitChangeLogList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.getWitChangeLogList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 取得积分明细(品牌维护表)
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 积分明细(品牌维护表)
	 * 
	 */
	public List<Map<String, Object>> getWitPointmaintList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.getWitPointmaintList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 标记品牌会员积分信息表中已导入的数据
	 * 
	 * @param map
	 * @return int
	 */
	public int updateWitMempoints(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.updateWitMempoints");
		return witBaseServiceImpl.update(map);
	}
	
	/**
	 * 标记品牌会员积分变化主表中已导入的数据
	 * 
	 * @param map
	 * @return int
	 */
	public int updateWitPointChange(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.updateWitPointChange");
		return witBaseServiceImpl.update(map);
	}
	
	/**
	 * 导入失败的记录去除标记(积分信息表)
	 * 
	 * @param map
	 * @return int
	 */
	public void delFailWitMempoints(List<Map<String, Object>> list) {
		this.updateAllWit(list, "BINBEMBARC02.updateFailWitMempoints");
	}
	
	/**
	 * 导入失败的记录去除标记 (积分变化主表)
	 * 
	 * @param map
	 * @return int
	 */
	public void delFailWitPointChange(List<Map<String, Object>> list) {
		this.updateAllWit(list, "BINBEMBARC02.updateFailWitPointChange");
	}
	
	/**
	 * 取得品牌数据库中会员积分变化主表信息总数
	 * 
	 * @param map
	 * @return
	 */
	public int getWitPointChangeCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getWitPointChangeCount");
		return witBaseServiceImpl.getSum(map);
	}
	
	/**
	 * 从品牌数据库中查询会员积分变化主表信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 会员积分变化主表信息
	 * 
	 */
	public List<Map<String, Object>> getWitPointChangeList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBARC02.getWitPointChangeList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 根据会员ID取得会员积分信息总数 
	 * 
	 * @param map
	 * @return
	 */
	public int getMemPointCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcMemPointCount");
		Integer count = baseServiceImpl.getSum(map);
		if (null == count) {
			return 0;
		}
		return count;
	}
	
	/**
	 * 删除已存在的主表记录
	 * @param list
	 */
	public void clearMemPointChange(List<Map<String, Object>> list){
		baseServiceImpl.deleteAll(list, "BINBEMBARC02.clearArcMemPointChange");
	}
	
	/**
	 * 删除已存在的明细记录
	 * @param list
	 */
	public void clearChangeDetail(List<Map<String, Object>> list){
		baseServiceImpl.deleteAll(list, "BINBEMBARC02.clearArcChangeDetail");
	}
	
	/**
	 * 插入会员积分信息表
	 * @param map
	 */
	public void addMemPointList (List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBARC02.insertMemPoint");
	}
	
	/**
	 * 插入会员积分变化主表
	 * @param map
	 */
	public void addPointChangeList(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBARC02.insertArcPointChange");
	}
	
	/**
	 * 插入会员积分变化明细表
	 * @param map
	 */
	public void addChangeDetailList(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBARC02.insertArcChangeDetail");
	}
	
	/**
	 * 取得积分变化主表信息 
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPointChangeInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcChangeInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得积分变化主表ID列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChangeIdList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcChangeIdList");
		return (List<Map<String, Object>> ) baseServiceImpl.getList(map);
	}
	
	/**
	 * 通过卡号取得会员ID
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemCardInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcMemCardInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得柜台信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcEmployeeInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员积分信息表中最近几天有变化的会员积分信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemLastList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC02.getArcMemLastList");
		return (List<Map<String, Object>> ) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 更新会员积分信息(批量处理)
	 * 
	 * @param list 需要更新的会员列表
	 * 
	 */	
	public void updateMemPointList(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBARC02.updateArcMemPointList");
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
	
	/**
	 * 用批处理更新一组数据(品牌数据库)
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void updateAllWit(final List<Map<String, Object>> list, final String sqlId) {
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
				witBaseServiceImpl.updateAll(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
	}
}
