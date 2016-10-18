/*	
 * @(#)BINOLJNMAN15_BL.java     1.0 2013/08/28		
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
package com.cherry.jn.man.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.jn.man.interfaces.BINOLJNMAN15_IF;
import com.cherry.jn.man.service.BINOLJNMAN15_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 积分清零 BL
 * 
 * @author hub
 * @version 1.0 2013.08.28
 */
public class BINOLJNMAN15_BL implements BINOLJNMAN15_IF{
	
	@Resource
	private BINOLJNMAN15_Service binoljnman15_Service;
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	/**
     * 取得会员子活动条数
     * 
     * @param map
     * @return
     * 		会员子活动条数
     */
	@Override
    public int getCampaignRuleCount(Map<String, Object> map) {
		return binoljnman15_Service.getCampaignRuleCount(map);
	}
    
    /**
     * 取得会员活动List
     * 
     * @param map
     * @return
     * 		会员子活动List
     */
	@Override
    public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		return binoljnman15_Service.getCampaignList(map);
	}
	
	/**
	 * 停用或者启用配置
	 * 
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void tran_editValid(Map<String, Object> map) throws Exception{
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLJNMAN15");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLJNMAN15");
		// 作成者
		map.put(CherryConstants.CREATEDBY, "BINOLJNMAN15");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, "BINOLJNMAN15");
		// 停用或者启用配置
		int result = binoljnman15_Service.updateRuleValid(map);
		// 更新报错处理
		if(0 == result){
			throw new Exception("update rule exception!");
		}
		// 取得优先级配置信息
		Map<String, Object> configInfo = binoljnman15_Service.getConfInfo(map);
		if (null == configInfo || configInfo.isEmpty()) {
			throw new Exception("can not find config!");
		}
		// 优先级
		String priorityDetail = (String) configInfo.get("priorityRuleDetail");
		// 若存在配置信息，循环list查询当前ID是否进行过配置
		List<Map<String, Object>> priorityList = null;
        if (!CherryChecker.isNullOrEmpty(priorityDetail)) {
	        // 若存在配置信息，循环list查询当前ID是否进行过配置
			priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityDetail);
        }
        if (null == priorityList) {
        	priorityList = new ArrayList<Map<String, Object>>();
        }
        // 规则ID
        String campaignId = (String) map.get("campaignId");
        boolean upFlag = false;
		// 启用规则
		if ("1".equals(map.get("validFlag"))) {
			// 不存在时加入优先级队列
			if (!isContain(campaignId, priorityList)) {
				Map<String, Object> priorityMap = new HashMap<String, Object>();
				priorityMap.put("campaignId", campaignId);
				List<String> keys = new ArrayList<String>();
				keys.add(campaignId);
				priorityMap.put("keys", keys);
				priorityList.add(priorityMap);
				upFlag = true;
			}
		} else {
			// 存在时从优先级队列中删除
			if (isContain(campaignId, priorityList)) {
				for(int i = 0;i < priorityList.size();i++){
					Map<String, Object> priorityMap = priorityList.get(i);
					if(campaignId.equals(priorityMap.get("campaignId"))){
						upFlag = true;
						priorityList.remove(i);
						break;
					}
				}
			}
		}
		if (upFlag) {
			// 规则配置优先级信息
			map.put("priorityMes", JSONUtil.serialize(priorityList));
			// 规则配置ID
			map.put("campaignGrpId", configInfo.get("campaignGrpId"));
			// 更新优先级配置
			result = binoljnman15_Service.updateConfig(map);
			// 更新报错处理
			if(0 == result){
				throw new Exception("update config exception!");
			}
			// 刷新单个规则文件
			knowledgeEngine.refreshRule(Integer.parseInt(campaignId));
		}
	}
	
	/**
	 * 验证优先级组是否包含指定活动
	 * 
	 * @param campaignId
	 * 			活动ID
	 * @param priorityList
	 * 			优先级List
	 * @return 验证结果 true : 包含, false: 不包含
	 * 
	 */
	private boolean isContain(String campaignId, List<Map<String, Object>> priorityList) {
		if (null != priorityList) {
			for (Map<String, Object> priorityInfo : priorityList) {
				// 活动ID
				String campId = String.valueOf(priorityInfo.get("campaignId"));
				if (campaignId.equals(campId)) {
					return true;
				}
			}
		}
		return false;
	}
}
