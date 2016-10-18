/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/06
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
package com.cherry.ct.pln.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 沟通模板一览Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public class BINOLCTPLN01_Service extends BaseService{
	/**
     * 获取沟通计划List
     * 
     * @param map
     * @return
     * 		沟通计划List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPlanList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN01.getPlanList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取沟通计划数量
     * 
     * @param map
     * @return
     * 		沟通计划数量
     */
	public int getPlanCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN01.getPlanCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 停用沟通计划的活动关联信息
	 * @param map
	 * @return
	 */
	public int stopCommPlanActivityInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN01.stopCommPlanActivityInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 停用沟通计划
	 * @param map
	 * @return
	 */
	public int stopCommPlan (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN01.stopCommPlan");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 停用沟通设置
	 * @param map
	 * @return
	 */
	public int stopCommSet (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN01.stopCommSet");
		return baseServiceImpl.update(map);
	}
}
