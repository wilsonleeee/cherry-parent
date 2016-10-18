/*	
 * @(#)BINOLJNCOM01_IF.java     1.0 2011/4/18		
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
package com.cherry.jn.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 会员入会共通 IF
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public interface BINOLJNCOM01_IF extends ICherryInterface {

	/**
	 * 取得页面对应的活动模板List
	 * 
	 * @param map
	 * @return 活动模板List
	 */
	public List<Map<String, Object>> searchCamTempList(Map<String, Object> map);

	/**
	 * 取得页面显示的活动模板List
	 * 
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @param List
	 *            页面提交的活动模板List
	 * @return 页面显示的活动模板List
	 */
	public List<Map<String, Object>> convertCamTempList(
			Map<String, Object> map,
			List<Map<String, Object>> camTemps);
	
	/**
	 * 会员活动保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return String
	 * 			规则信息
	 * @throws Exception 
	 */
	public String saveCampaign(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员活动等级List
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public List<Map<String, Object>> convertMemberLevelList(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员活动等级信息
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public void convertMemberLevel(Map<String, Object> map) throws Exception;
	
	/**
     * 取得会员子活动信息
     * 
     * @param map
     * @return
     * 		会员子活动信息
     */
    public Map<String, Object> getCampaignRuleMap(Map<String, Object> map);
	
}
