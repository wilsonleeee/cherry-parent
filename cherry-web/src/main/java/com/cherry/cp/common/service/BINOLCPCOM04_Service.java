/*	
 * @(#)BINOLCPCOM04_Service.java     1.0 2011/7/18		
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
package com.cherry.cp.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 规则测试共通 Service
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM04_Service extends BaseService {
	
	/**
	 * 取得会员活动组规则
	 * 
	 * @param
	 * @return String
	 */
	public String getGrpRuleContent(Map<String, Object> map){	
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCPCOM04.getGrpRuleContent");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得名称
	 * 
	 * @param
	 * @return String
	 */
	public String getSearchName(Map<String, Object> map){
		String sqlId = (String) map.get("SQLID");
		map.put(CherryConstants.IBATIS_SQL_ID, sqlId);
		return (String) baseServiceImpl.get(map);
	}
	
	/**
     * 取得规则内容
     * 
     * @param map
     * @return
     * 		规则内容
     */
    public List<Map<String, Object>> getRuleContentList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM03.getRuleContentList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
	 * 取得会员等级List
	 * 
     * @param map
     * 			查询参数
	 * @return List
	 * 			会员等级List
	 */
	public List<Map<String, Object>> getMemLevelList(Map<String, Object> map){	
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCPCOM04.getMemLevelList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
     * 取得匹配到的规则名称List
     * 
     * @param map
     * 			查询参数
     * @return List
     * 			规则名称List
     */
    public List<Map<String, Object>> getCampaignNameList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM04.getCampaignNameList");
        return baseServiceImpl.getList(map);
    }
}
