/*	
 * @(#)BINOLJNMAN05_Service.java     1.0 2012/10/30		
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
package com.cherry.jn.man.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 积分规则配置一览Service
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN05_Service extends BaseService{
	
	/**
     * 取得规则配置List
     * 
     * @param map
     * @return
     * 		规则配置List
     */
    public List<Map<String, Object>> getCampGrpList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN05.getCampGrpList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得规则配置件数 
     * 
     * @param map
     * @return
     * 		规则配置件数
     */
    public int getCampGrpCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN05.getCampGrpCount");
        return baseServiceImpl.getSum(paramMap);
	}
    
    /**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @return int
	 */
	public int updateValidConfig(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN05.updateValidConfig");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新规则的配置ID
	 * 
	 * @param map
	 * @return int
	 */
	public int updateRuleGrpId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN05.updateRuleGrpId");
		return baseServiceImpl.update(map);
		 
	}
}
