/*
 * @(#)BINOLPLSCF01_Service.java     1.0 2010/10/27
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

package com.cherry.pl.scf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 基本配置管理Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF01_Service extends BaseService {
	
	/**
	 * 取得基本配置信息List
	 * 
	 * @param map 查询条件
	 * @return 基本配置信息List
	 */
	public List<Map<String, Object>> getSystemConfigList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.getSystemConfigList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 给某品牌添加默认的基本配置信息
	 * 
	 * @param map 添加内容
	 */
	@Deprecated
	public void addSystemConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.addSystemConfig");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 更新基本配置信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSystemConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.updateSystemConfig");
		return baseServiceImpl.update(parameterMap);
	}

    /**
     * 取得Admin基本配置信息List
     * 
     * @param map 查询条件
     * @return 基本配置信息List
     */
    public List<Map<String, Object>> getAdminSystemConfigList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.getAdminSystemConfigList");
        return baseServiceImpl.getList(parameterMap);
    }	
	
	/**
     * 取得基本配置信息List
     * 
     * @param map 查询条件
     * @return 基本配置信息List
     */
    public List<Map<String, Object>> getDefaultSystemConfigList() {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.getDefaultSystemConfigList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据配置项编码取得默认配置信息LIST
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDefaultSystemConfigByCode(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.getDefaultSystemConfigList");
        return baseServiceImpl.getList(parameterMap);
    }
	
	/**
     * 删除基本配置信息
     * 
     * @param map 删除条件
     */
    public int delSystemConfig(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.delSystemConfig");
        return baseServiceImpl.remove(parameterMap);
    }
	
    /**
     * 插入基本配置信息
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public void insertSystemConfig(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF01.insertSystemConfig");
        baseServiceImpl.save(parameterMap);
    }
    /**
     * 修改/插入利润分摊的基本配置信息到调度表
     * 
     * @param map 更新条件
     */
	public void tran_insertUpdSchedules(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM03.insertUpdSchedules");
        baseServiceImpl.save(parameterMap);
	}
    
}
