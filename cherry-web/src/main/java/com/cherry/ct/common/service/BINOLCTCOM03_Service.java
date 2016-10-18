/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 沟通计划预览与确认Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM03_Service extends BaseService{
	/**
	 * 插入沟通计划信息
	 * 
	 * @param map
	 * @return 
	 */
	public void insertCommPlanInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertCommPlanInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新沟通计划信息
	 * 
	 * @param map
	 * @return 
	 */
	public void updateCommPlanInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.updateCommPlanInfo");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 插入沟通信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertCommInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertCommInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入调度信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertGtSchedules(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertGtSchedules");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新沟通调度有效状态
	 * 
	 * @param map
	 * @return 
	 */
	public void updateSchedulesFlag(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.updateSchedulesFlag");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 删除沟通信息
	 * 
	 * @param map
	 * @return
	 */
	public void stopCommInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.stopCommInfo");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 删除不在运行的调度信息
	 * 
	 * @param map
	 * @return
	 */
	public void deleteSchedulesInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.deleteSchedulesInfo");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入沟通对象信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertCommObjectInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertCommObjectInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入沟通设置详细信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertCommSetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertCommSetInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 删除沟通设置详细信息
	 * 
	 * @param map
	 * @return
	 */
	public void deleteCommSetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.deleteCommSetInfo");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入活动与沟通管理信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertCommAsActivity(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTCOM03.insertCommAsActivity");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 根据沟通计划编号取得沟通计划详细信息
	 * @param map
	 * @return 沟通计划信息map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlanInfoByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM03.getPlanInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 统计沟通对象搜索记录数量
	 * @param map
	 * @return 沟通对象记录集数量
	 */
	public int getObjRecordCountByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM03.getObjRecordCountByCode");
		return baseServiceImpl.getSum(paramMap);
	}
}
