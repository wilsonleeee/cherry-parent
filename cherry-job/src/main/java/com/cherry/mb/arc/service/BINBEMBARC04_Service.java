/*	
 * @(#)BINBEMBARC04_Service.java     1.0 2013/11/07		
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
import com.cherry.cm.service.BaseService;

/**
 * 老后台会员资料导入新后台处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/11/07
 */
public class BINBEMBARC04_Service extends BaseService {
	
	/**
	 * 把会员接口表中状态为处理中的数据全部更新成未处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateGetStatusNull(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.updateGetStatusNull");
		return witBaseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询需要处理数据的日期List
	 * 
	 * @param map 查询条件
	 * @return 需要处理数据的日期List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInsertdateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.getInsertdateList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 按日期把会员接口表中未处理的数据全部更新成处理中状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateGetStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.updateGetStatus");
		return witBaseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询未换过卡的会员数据List
	 * 
	 * @param map 查询条件
	 * @return 未换过卡的会员数据List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.getMemberInfoList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询换过卡的会员数据List
	 * 
	 * @param map 查询条件
	 * @return 换过卡的会员数据List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberChangeInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.getMemberChangeInfoList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 把换过卡的会员数据按换卡顺序写入到临时表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemTemp(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.updateMemTemp");
		return witBaseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得临时表中的会员数据List
	 * 
	 * @param map 查询条件
	 * @return 临时表中的会员数据List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemTempList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.getMemTempList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 批量删除临时表的数据
	 * 
	 * @param list 删除条件
	 */
	public void delMemTemp(List<Map<String, Object>> list) {
		witBaseServiceImpl.deleteAll(list, "BINBEMBARC04.delMemTemp");
	}
	
	/**
	 * 批量更新处理过的会员数据的状态
	 * 
	 * @param list 更新条件
	 */
	public void updateGetStatusEnd(List<Map<String, Object>> list) {
		witBaseServiceImpl.updateAll(list, "BINBEMBARC04.updateGetStatusEnd");
	}
	
	/**
	 * 删除临时表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int dropMemTemp(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC04.dropMemTemp");
		return witBaseServiceImpl.update(paramMap);
	}

}
