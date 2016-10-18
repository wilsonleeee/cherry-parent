/*	
 * @(#)TemplateInit_IF.java     1.0 2011/7/18		
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
package com.cherry.cp.common.interfaces;

import java.util.Map;

import com.cherry.cp.common.dto.CampaignRuleDTO;

/**
 * 会员活动模板初期处理共通 IF
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public interface TemplateInit_IF {
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param mdName
	 *            指定的方法名
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void invokeMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception;
	
	/**
	 * 运行保存方法
	 * 
	 * @param mdName
	 *            指定的方法名
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param campaignRule
	 *            会员子活动DTO
	 * @throws Exception 
	 * 
	 */
	public void invokeSaveMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception;
}
