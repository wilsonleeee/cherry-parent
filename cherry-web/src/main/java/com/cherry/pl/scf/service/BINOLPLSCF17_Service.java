/*
 * @(#)BINOLPLSCF17_Service.java     1.0 2013/08/27
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 业务处理器管理画面Service
 * 
 * @author hub
 * @version 1.0 2013/08/27
 */
public class BINOLPLSCF17_Service extends BaseService{
	
	/**
     * 取得业务处理器列表
     * 
     * @param map
     * @return List
     * 		业务处理器列表
     */
    public List<Map<String, Object>> getHandlerList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF17.getHandlerList");
        return baseConfServiceImpl.getList(map);
    }
	
	/**
     * 取得业务处理器件数 
     * 
     * @param map
     * @return int
     * 		业务处理器件数
     */
    public int getHandlerCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF17.getHandlerCount");
        return baseConfServiceImpl.getSum(map);
	}
    
    /**
     * 取得业务处理器详细信息
     * 
     * @param map
     * @return List
     * 		业务处理器详细信息
     */
    public Map<String, Object> getHandlerDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF17.getHandlerDetail");
        return (Map<String, Object>) baseConfServiceImpl.get(map);
    }
    
    /**
	 * 批量插入业务处理器信息
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void addHandlers(List<Map<String, Object>> addList) {
		baseConfServiceImpl.saveAll(addList, "BINOLPLSCF17.insertHandlerInfo");
	}
	
	/**
	 * 批量更新业务处理器信息
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void upHandlers(List<Map<String, Object>> updateList) {
		baseConfServiceImpl.updateAll(updateList, "BINOLPLSCF17.updateValidHandler");
	}
	
	/**
     * 取得品牌信息
     * 
     * @param map
     * @return List
     * 		品牌信息
     */
    public Map<String, Object> getBrandDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF17.getBrandDetail");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
}
