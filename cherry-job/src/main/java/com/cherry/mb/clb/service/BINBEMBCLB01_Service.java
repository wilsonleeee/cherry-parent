/*	
 * @(#)BINBEMBCLB01_Service.java     1.0 2014/11/06		
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
package com.cherry.mb.clb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员俱乐部下发(实时)处理Service
 * 
 * @author HUB
 * @version 1.0 2014/11/06
 */
public class BINBEMBCLB01_Service extends BaseService{
	
	/**
	 * 取得需要下发的会员俱乐部列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 会员俱乐部列表
	 * 
	 */
	public List<Map<String, Object>> getClubList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBCLB01.getClubList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得已下发的会员俱乐部柜台关系列表
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 已下发的会员俱乐部柜台关系列表
	 * 
	 */
	public List<Map<String, Object>> getClubCounterList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBCLB01.getClubCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得已失效的会员俱乐部柜台关系列表 
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 已失效的会员俱乐部柜台关系列表 
	 * 
	 */
	public List<Map<String, Object>> getInvaildClubCounterList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEMBCLB01.getInvaildClubCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入会员俱乐部与柜台对应关系表
	 * @param list
	 */
	public void addCounterClub(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMBCLB01.insertClubCounter");
	}
	
	/**
	 * 插入会员俱乐部与柜台对应关系表(品牌数据库) 
	 * @param list
	 */
	public void addWitCounterClub(List<Map<String, Object>> list){
		witBaseServiceImpl.saveAll(list, "BINBEMBCLB01.insertWitCounterClub");
	}
	
	/**
	 * 更新会员俱乐部与柜台对应关系
	 * @param list
	 */
	public void updateCounterClub(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINBEMBCLB01.updateClubCounter");
	}
	
	/**
	 * 停用会员俱乐部与柜台对应关系
	 * @param list
	 */
	public void delCounterClub(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINBEMBCLB01.updateClubCounterDel");
	}
	
	/**
	 * 删除会员俱乐部与柜台对应关系(品牌数据库)
	 * @param list
	 */
	public void delWitmemclub(List<Map<String, Object>> list){
		witBaseServiceImpl.deleteAll(list, "BINBEMBCLB01.delWitmemclub");
	}
	
	/**
	 * 停用会员俱乐部与柜台对应关系 (品牌数据库)
	 * @param list
	 */
	public void invaildWitmemclub(List<Map<String, Object>> list){
		witBaseServiceImpl.updateAll(list, "BINBEMBCLB01.updateWitClubCounterDel");
	}
	
	/**
	 * 启用会员俱乐部与柜台对应关系 (品牌数据库)
	 * @param list
	 */
	public void updateWitClubCounterValid(List<Map<String, Object>> list){
		witBaseServiceImpl.updateAll(list, "BINBEMBCLB01.updateWitClubCounterValid");
	}
	
	/**
	 * 更新俱乐部信息 (品牌数据库)
	 * @param map
	 */
	public int updateWitClubInfo(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.updateWitClubInfo");
		return witBaseServiceImpl.update(map);
	}
	
	/**
	 * 插入俱乐部信息 (品牌数据库)
	 * @param map
	 */
	public void insertWitClubInfo(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.insertWitClubInfo");
		witBaseServiceImpl.save(map);
	}
	
	/**
	 * 更新会员俱乐部BATCH执行标识
	 * @param map
	 */
	public int updateClubBatchFlag(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.updateClubBatchFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 清除会员俱乐部BATCH执行标识
	 * @param map
	 */
	public int clearClubBatchFlag(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.updateClubBatchFlagClear");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员俱乐部下发标识
	 *
	 * @return
	 */
	public int updateClubSendFlag(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.updateClubSendFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得品牌数据源信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌数据源信息
	 */
	public Map<String, Object> getOldDataSourceInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBCLB01.getOldDataSourceName");
		return (Map<String, Object>) baseConfServiceImpl.get(map);
	}
}
