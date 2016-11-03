/*	
 * @(#)BINBEMBVIS03_Service.java     1.0 2012/12/18		
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
package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员回访任务下发batch处理Service
 * 
 * @author WangCT
 * @version 1.0 2012/12/18
 */
public class BINBEMBVIS03_Service extends BaseService {
	
	/**
	 * 从会员回访任务接口表中删除已导入到老后台的回访任务
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delWitCancelVisitTask(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS03.delWitCancelVisitTask");
		return ifServiceImpl.remove(paramMap);
	}
	
	/**
	 * 取得已完成的会员回访任务List
	 * 
	 * @param map 查询条件
	 * @return 已完成的会员回访任务List
	 */
	public List<Map<String, Object>> getCompletedVisitTaskList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS03.getCompletedVisitTaskList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得进行中的未下发的会员回访任务List
	 * 
	 * @param map 查询条件
	 * @return 进行中的未下发的会员回访任务List
	 */
	public List<Map<String, Object>> getVisitTaskList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS03.getVisitTaskList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得已取消的未下发的会员回访任务List
	 * 
	 * @param map 查询条件
	 * @return 已取消的未下发的会员回访任务List
	 */
	public List<Map<String, Object>> getCancelVisitTaskList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS03.getCancelVisitTaskList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 从会员回访任务接口表中删除已完成的回访任务
	 * 
	 * @param list 删除条件
	 */
	public void delWitCompletedVisitTask(List<Map<String,Object>> list) {
		ifServiceImpl.deleteAll(list, "BINBEMBVIS03.delWitCompletedVisitTask");
	}
	
	/**
	 * 从会员回访任务表中把已完成的回访任务更新成接口表已删除
	 * 
	 * @param list 更新条件
	 */
	public void updateCompletedVisitTask(List<Map<String,Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBVIS03.updateCompletedVisitTask");
	}
	
	/**
	 * 把进行中的未下发的回访任务下发到会员回访任务接口表中
	 * 
	 * @param list 插入内容
	 */
	public void insertWitVisitTask(List<Map<String,Object>> list) {
		ifServiceImpl.saveAll(list, "BINBEMBVIS03.insertWitVisitTask");
	}
	
	/**
	 * 把已取消的未下发的回访任务下发到会员回访任务接口表中
	 * 
	 * @param list 更新条件
	 */
	public void updateWitVisitTask(List<Map<String,Object>> list) {
		ifServiceImpl.updateAll(list, "BINBEMBVIS03.updateWitVisitTask");
	}
	
	/**
	 * 从会员回访任务表中把状态为进行中的未下发的回访任务更新成已下发
	 * 
	 * @param list 更新条件
	 */
	public void updateVisitTask(List<Map<String,Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBVIS03.updateVisitTask");
	}
	
	/**
	 * 从会员回访任务表中把状态为取消的未下发的回访任务更新成已下发
	 * 
	 * @param list 更新条件
	 */
	public void updateCanlelVisitTask(List<Map<String,Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMBVIS03.updateCanlelVisitTask");
	}

}
