/*	
 * @(#)BINOLJNMAN15_Service.java     1.0 2013/08/28		
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
 * 积分清零 Service
 * 
 * @author hub
 * @version 1.0 2013.08.28
 */
public class BINOLJNMAN15_Service extends BaseService{
	
	/**
     * 取得会员活动List
     * 
     * @param map
     * @return
     * 		会员子活动List
     */
    public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN15.getMAN15CampaignList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得会员子活动条数
     * 
     * @param map
     * @return
     * 		会员子活动条数
     */
    public int getCampaignRuleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN15.getMAN15CampaignRuleCount");
        return baseServiceImpl.getSum(paramMap);
	}
    
    /**
	 * 停用或启用规则
	 * 
	 * @param map
	 * @return int 更新件数
	 * 
	 */
	public int updateRuleValid(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN15.updateMAN15RuleValid");
		return baseServiceImpl.update(map);
	}
	
	/**
     * 取得优先级配置信息
     * 
     * @param map
     * @return
     * 		优先级配置信息
     */
    public Map<String, Object> getConfInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN15.getMAN15ConfInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
	 * 更新优先级配置
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfig(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN15.updateMAN15Config");
		return baseServiceImpl.update(map);
	}
}
