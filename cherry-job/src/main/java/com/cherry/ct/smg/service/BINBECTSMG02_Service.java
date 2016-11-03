/*	
 * @(#)BINBECTSMG02_Service.java     1.0 2013/02/28	
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
package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 沟通任务动态调度管理Service
 * 
 * @author WangCT
 * @version 1.0 2013/02/28	
 */
public class BINBECTSMG02_Service extends BaseService {
	
	/**
     * 查询已过期非运行状态的沟通调度信息List
     * 
     * @param map 查询条件
     * @return 已过期非运行状态的沟通调度信息List
     */
    public List<Map<String, Object>> getExpiredSchedulesList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.getExpiredSchedulesList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 删除已过期非运行状态的沟通调度信息
     * 
     * @param map 删除条件
     * @return 删除件数
     */
    public int delExpiredSchedules(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.delExpiredSchedules");
        return baseServiceImpl.remove(paramMap);
    }
    
    /**
     * 删除已过期非运行状态的沟通调度信息
     * 
     * @param list 删除条件
     */
    public void delExpiredSchedulesList(List<Map<String, Object>> list) {
        baseServiceImpl.deleteAll(list, "BINBECTSMG02.delExpiredSchedules");
    }
    
    /**
     * 查询未过期未加载的沟通调度信息List
     * 
     * @param map 查询条件
     * @return 未过期未加载的沟通调度信息List
     */
    public List<Map<String, Object>> getSchedulesList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.getSchedulesList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 把未过期未加载的沟通调度信息更新已加载
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public int updSchedules(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.updSchedules");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 把未过期未加载的沟通调度信息更新已加载
     * 
     * @param list 更新条件
     */
    public void updSchedulesList(List<Map<String, Object>> list) {
        baseServiceImpl.updateAll(list, "BINBECTSMG02.updSchedules");
    }
    
    /**
     * 初次加载沟通任务时把所有的沟通任务更新成未加载状态
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public int updSchedulesInit(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.updSchedulesInit");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 初次加载沟通任务时把所有正在运行的沟通任务更新成待运行状态
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public int updSchedulesStatusInit(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.updSchedulesStatusInit");
        return baseServiceImpl.update(paramMap);
    }

    /**
     * 初次加载沟通任务时把所有的正在运行状态的沟通计划更新成待运行状态
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public int updCommPlanInit(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.updCommPlanInit");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 查询调度信息List
     * 
     * @param map 查询条件
     * @return 调度信息List
     */
    public List<Map<String, Object>> getMemorySchedulesList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG02.getMemorySchedulesList");
        return baseServiceImpl.getList(paramMap);
    }
}
