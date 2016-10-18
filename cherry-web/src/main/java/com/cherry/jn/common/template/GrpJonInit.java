/*	
 * @(#)GrpJonInit.java     1.0 2011/11/02		
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
package com.cherry.jn.common.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.TemplateInit;
import com.cherry.jn.common.service.BINOLJNCOM03_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员入会和升降级模板初始化处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class GrpJonInit extends TemplateInit{
	@Resource
	private BINOLJNCOM03_Service binoljncom03_Service;
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	/**
	 * 设置优先级初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000032_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 取得当前会员类型的规则配置优先级信息
		String priorityJSON = binolcpcom02IF.getPriorityMap(paramMap);
		// 取得已保存的优先级信息
		List<Map<String, Object>> ruleList = new ArrayList<Map<String, Object>>();
		if(null != priorityJSON){
			ruleList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityJSON);
		}
		// 未进行配置的规则
		List<Map<String, Object>> ruleDotConList = new ArrayList<Map<String, Object>>();
		// 根据规则id，取得规则名
		if(null != ruleList && !ruleList.isEmpty()){
			for(int i = 0;i < ruleList.size();i++){
				Map<String, Object> ruleMap = ruleList.get(i);
				if(null != ruleMap.get("campaignId")){
					String campaignName = binoljncom03_Service.getRuleName(ruleMap);
					if(null == campaignName){
						// 删除不存在的规则
						ruleList.remove(ruleMap);
						i--;
					}else{
						Map<String, Object> dateMap = binolcpcom02_Service.getDateMap(ruleMap);
						ruleMap.putAll(dateMap);
						ruleMap.put("campaignName", campaignName);
					}
				}
				if(null == ruleMap || ruleMap.isEmpty() || null == ruleMap.get("campaignId")){
					// 删除不存在的规则
					ruleList.remove(ruleMap);
					i--;
				}
			}
			// 取得所有的积分规则
			List<Map<String, Object>> ruleAllList = binoljncom03_Service.getCampaignRuleList(paramMap);
			// 插入后来新增的积分规则
			for(Map<String, Object> ruleAllMap : ruleAllList){
				int i = 0;
				// 循环当前已保存的积分规则list
				for(Map<String, Object> ruleMap : ruleList){
					String nowId = String.valueOf(ruleAllMap.get("campaignId"));
					String comId = "0";
					if(null != ruleMap.get("campaignId")){
						comId = (String) ruleMap.get("campaignId");
					}
					if(nowId.equals(comId)){
						break;
					}
					i++;
				}
				// 若循环正常结束，则该Map中为新增的积分规则
				if(i == ruleList.size()){
					ruleDotConList.add(ruleAllMap);
				}
			}
			
		}else{
			// 新建规则组时，取得当前所有积分规则
			ruleDotConList = binoljncom03_Service.getCampaignRuleList(paramMap);
		}
		tempMap.put("priorityRuleList", ruleList);
		tempMap.put("priorityRuleDotConList", ruleDotConList);
	}
	
	/**
	 * 查看优先级画面
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000052_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 取得当前会员类型的规则配置优先级信息
		String priorityJSON = binolcpcom02IF.getPriorityMap(paramMap);
		// 取得已保存的优先级信息
		List<Map<String, Object>> ruleList = new ArrayList<Map<String, Object>>();
		if(null != priorityJSON){
			ruleList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityJSON);
		}
		// 根据规则id，取得规则名
		if(null != ruleList && !ruleList.isEmpty()){
			for(int i = 0;i < ruleList.size();i++){
				Map<String, Object> ruleMap = ruleList.get(i);
				if(null != ruleMap.get("campaignId")){
					String campaignName = binoljncom03_Service.getRuleName(ruleMap);
					if(null == campaignName){
						// 删除不存在的规则
						ruleList.remove(ruleMap);
						i--;
					}else{
						Map<String, Object> dateMap = binolcpcom02_Service.getDateMap(ruleMap);
						ruleMap.putAll(dateMap);
						ruleMap.put("campaignName", campaignName);
					}
				}
			}
		}
		// 未进行配置的规则
		List<Map<String, Object>> ruleDotConList = new ArrayList<Map<String, Object>>();
		// 取得所有的积分规则
		List<Map<String, Object>> ruleAllList = binoljncom03_Service.getCampaignRuleList(paramMap);
		// 插入后来新增的积分规则
		for(Map<String, Object> ruleAllMap : ruleAllList){
			int i = 0;
			// 循环当前已保存的积分规则list
			for(Map<String, Object> ruleMap : ruleList){
				String nowId = String.valueOf(ruleAllMap.get("campaignId"));
				String comId = "0";
				if(null != ruleMap.get("campaignId")){
					comId = (String) ruleMap.get("campaignId");
				}
				if(nowId.equals(comId)){
					break;
				}
				i++;
			}
			// 若循环正常结束，则该Map中为新增的积分规则
			if(i == ruleList.size()){
				ruleDotConList.add(ruleAllMap);
			}
		}
		tempMap.put("priorityRuleList", ruleList);
		tempMap.put("priorityRuleDotConList", ruleDotConList);
	}
}
