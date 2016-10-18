/*	
 * @(#)GrpPointInit.java     1.0 2011/11/02		
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM02_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.TemplateInit;
import com.cherry.jn.common.service.BINOLJNCOM03_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员入会和升降级模板初始化处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class GrpPointInit extends TemplateInit{

	@Resource
	private BINOLJNCOM03_Service binoljncom03_Service;
	
	@Resource
    private BINOLCPCOM02_IF binolcpcom02IF;
	
	/**
	 * 组合规则初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000030_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		String priorityJSON = binolcpcom02IF.getPriorityMap(paramMap);
		// 取得已保存的优先级信息
		List<Map<String, Object>> PriorityList = null;
		if(null != priorityJSON){
			PriorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityJSON);
		}
		// 取得之前保存下来的组合规则信息
		List<Map<String, Object>> ruleList = (List<Map<String, Object>>) tempMap.get("combinationRule");
		// 非新建规则组的情况
		if(null != ruleList && !ruleList.isEmpty()){
			paramMap.put("pointRuleType", "1");
			// 取得所有的基本规则
			List<Map<String, Object>> ruleAllList = binoljncom03_Service.getCampaignRuleList(paramMap);
			// 处理后来加入的基本积分规则
			for(Map<String, Object> ruleMap : ruleList){
				for(Map<String, Object> ruleAllMap : ruleAllList){
					if(String.valueOf(ruleMap.get("campaignId")).equals(String.valueOf(ruleAllMap.get("campaignId")))){
						ruleAllMap.put("extraRule", ruleMap.get("extraRule"));
						break;
					}
				}
			}
			// 根据规则id取得规则名
			for(Map<String, Object> ruleInfoMap : ruleAllList){
				// 基本积分规则名
				String campaignName = binoljncom03_Service.getRuleName(ruleInfoMap);
				Map<String, Object> dateMap = binolcpcom02_Service.getDateMap(ruleInfoMap);
				ruleInfoMap.putAll(dateMap);
				ruleInfoMap.put("campaignName", campaignName);
				if(null != ruleInfoMap.get("extraRule")){
					// 取得附加规则list
					List<Map<String, Object>> extraRuleList = (List<Map<String, Object>>) ruleInfoMap.get("extraRule");
					// 循环list，取得规则名
					for(Map<String, Object> extraRuleMap : extraRuleList){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("campaignId", extraRuleMap.get("campaignId"));
						String ruleName = binoljncom03_Service.getRuleName(map);
						extraRuleMap.put("campaignName", ruleName);
					}
				}
//				}else{
//					paramMap.put("pointRuleType", "2");
//					// 取得所有的基本规则
//					List<Map<String, Object>> extraRuleAllList = binoljncom03_Service.getCampaignRuleList(paramMap);
//					ruleInfoMap.put("extraRule", extraRuleAllList);
//				}
			}
			// 按优先级顺序显示
			if (null != PriorityList && !PriorityList.isEmpty()) {
				List<Map<String, Object>> priorityRuleList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> priority : PriorityList) {
					String campaignId = String.valueOf(priority.get("campaignId"));
					for (int i = 0; i < ruleAllList.size(); i++) {
						Map<String, Object> ruleMap = ruleAllList.get(i);
						String campaignId1 = String.valueOf(ruleMap.get("campaignId"));
						if (campaignId.equals(campaignId1)) {
							priorityRuleList.add(ruleMap);
							ruleAllList.remove(i);
							i--;
						}
					}
				}
				if (!ruleAllList.isEmpty()) {
					priorityRuleList.addAll(ruleAllList);
				}
				ruleAllList = priorityRuleList;
			}
			// 更新当前的组合规则list
			tempMap.put("combinationRule", ruleAllList);
		}else{
			paramMap.put("pointRuleType", "1");
			// 新建规则组时加载所有的积分规则
			ruleList = binoljncom03_Service.getCampaignRuleList(paramMap);
//			for(Map<String, Object> ruleMap : ruleList){
//				paramMap.put("pointRuleType", "2");
//				// 取得所有的基本规则
//				List<Map<String, Object>> extraRuleAllList = binoljncom03_Service.getCampaignRuleList(paramMap);
//				Map<String, Object> ruleInfoMap = new HashMap<String, Object>();
//				ruleMap.put("extraRule", extraRuleAllList);
//			}
			tempMap.put("combinationRule", ruleList);
		}
	}
	
	/**
	 * 策略组大模块初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000042_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 该积分规则组中若没有积分规则，则不显示策略组
		//List<Map<String, Object>> ruleList = binoljncom03_Service.getCampaignRuleList(paramMap);
		tempMap.put("showFlg", "0");
//		if(null == ruleList || ruleList.isEmpty()){
//			tempMap.put("showFlag", "0");
//		}
	}
	
	/**
	 * 策略组初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000031_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 取得已保存的策略组信息
		List<Map<String, Object>> ruleList = null;
		if(!CherryChecker.isNullOrEmpty(tempMap.get("strategyRuleList"))){ 
			ruleList = (List<Map<String, Object>>) tempMap.get("strategyRuleList");
		}
		if (null != ruleList) {
			// 循环策略组中的积分规则，取得规则名
			for(int i = 0; i < ruleList.size(); i++){
				Map<String, Object> ruleMap = ruleList.get(i);
				String campaignName = binoljncom03_Service.getRuleName(ruleMap);
				if(null == campaignName){
					ruleList.remove(i);
					i--;
				}else{
					ruleMap.put("campaignName", campaignName);
				}
			}
		}
	}
	
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
	public void BASE000032_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {// 取得当前会员类型的规则配置优先级信息
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
					Map<String, Object> dateMap = binolcpcom02_Service.getDateMap(ruleMap);
					ruleMap.putAll(dateMap);
					if(null == campaignName){
						// 删除不存在的规则
						ruleList.remove(ruleMap);
						i--;
					}else{
						ruleMap.put("campaignName", campaignName);
					}
				}
				if(null == ruleMap || ruleMap.isEmpty() || null == ruleMap.get("campaignId")){
					// 删除不存在的规则
					ruleList.remove(ruleMap);
					i--;
				}
			}
			paramMap.put("pointRuleType", "1");
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
		List<Map<String, Object>> straList = new ArrayList<Map<String, Object>>();
		if(null != paramMap.get("extraInfo")){
			Map<String, Object> extraMap = (Map<String, Object>) JSONUtil.deserialize((String) paramMap.get("extraInfo"));
			straList = (List<Map<String, Object>>) extraMap.get("straList");
		}
		if(null != straList){
			for(Map<String, Object> straMap : straList){
				Map<String, Object> ruleMap = new HashMap<String, Object>();
				if(!CherryChecker.isNullOrEmpty(straMap.get("strategyGroup"))){
					ruleMap.put("strategyRuleName", straMap.get("strategyGroup"));
					ruleList.add(ruleMap);
				}
			}
		}
		if (null != ruleList && !ruleList.isEmpty()) {
			paramMap.put("pointRuleType", "2");
			// 取得所有的积分规则
			List<Map<String, Object>> affilRuleList = binoljncom03_Service.getCampaignRuleList(paramMap);
			if (null != affilRuleList && !affilRuleList.isEmpty()) {
				for (Map<String, Object> ruleMap: ruleList) {
					// 已配置的规则ID
					List<String> ruleKeys = (List) ruleMap.get("keys");
					if (null != ruleKeys && ruleKeys.size() > 1) {
						List<Map<String, Object>> affilList = new ArrayList<Map<String, Object>>();
						for (int i = 1; i < ruleKeys.size(); i++) {
							String ruleKey = ruleKeys.get(i);
							if (null != ruleKey) {
								for (Map<String, Object> affilRuleMap : affilRuleList) {
									// 附属规则
									if (ruleKey.equals(String.valueOf(affilRuleMap.get("campaignId")))) {
										Map<String, Object> affilMap = new HashMap<String, Object>();
										affilMap.put("campaignName", affilRuleMap.get("campaignName"));
										affilList.add(affilMap);
										break;
									}
								}
							}
						}
						ruleMap.put("affilList", affilList);
					}
				}
			}
		}
		tempMap.put("priorityRuleList", ruleList);
		tempMap.put("priorityRuleDotConList", ruleDotConList);
		// 默认积分规则
		List<Map<String, Object>> defaultRuleList = new ArrayList<Map<String, Object>>();
		if (null != ruleList) {
			for (int i = ruleList.size() - 1; i >= 0; i--) {
				Map<String, Object> ruleMap = ruleList.get(i);
				if ("1".equals(ruleMap.get("defaultFlag"))) {
					defaultRuleList.add(ruleMap);
					ruleList.remove(i);
					break;
				}
			}
		}
		tempMap.put("defaultRuleList", defaultRuleList);
		
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
	public void BASE000052_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {// 取得当前会员类型的规则配置优先级信息
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
					Map<String, Object> dateMap = binolcpcom02_Service.getDateMap(ruleMap);
					ruleMap.putAll(dateMap);
					if(null == campaignName){
						// 删除不存在的规则
						ruleList.remove(ruleMap);
						i--;
					}else{
						ruleMap.put("campaignName", campaignName);
					}
				}
				if(null == ruleMap || ruleMap.isEmpty() || null == ruleMap.get("campaignId")){
					// 删除不存在的规则
					ruleList.remove(ruleMap);
					i--;
				}
			}
			paramMap.put("pointRuleType", "1");
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
		List<Map<String, Object>> straList = new ArrayList<Map<String, Object>>();
		if(null != paramMap.get("extraInfo")){
			Map<String, Object> extraMap = (Map<String, Object>) JSONUtil.deserialize((String) paramMap.get("extraInfo"));
			straList = (List<Map<String, Object>>) extraMap.get("straList");
		}
		if(null != straList){
			for(Map<String, Object> straMap : straList){
				Map<String, Object> ruleMap = new HashMap<String, Object>();
				if(!CherryChecker.isNullOrEmpty(straMap.get("strategyGroup"))){
					ruleMap.put("strategyRuleName", straMap.get("strategyGroup"));
					ruleList.add(ruleMap);
				}
			}
		}
		if (null != ruleList && !ruleList.isEmpty()) {
			paramMap.put("pointRuleType", "2");
			// 取得所有的积分规则
			List<Map<String, Object>> affilRuleList = binoljncom03_Service.getCampaignRuleList(paramMap);
			if (null != affilRuleList && !affilRuleList.isEmpty()) {
				for (Map<String, Object> ruleMap: ruleList) {
					// 已配置的规则ID
					List<String> ruleKeys = (List) ruleMap.get("keys");
					if (null != ruleKeys && ruleKeys.size() > 1) {
						List<Map<String, Object>> affilList = new ArrayList<Map<String, Object>>();
						for (int i = 1; i < ruleKeys.size(); i++) {
							String ruleKey = ruleKeys.get(i);
							if (null != ruleKey) {
								for (Map<String, Object> affilRuleMap : affilRuleList) {
									// 附属规则
									if (ruleKey.equals(String.valueOf(affilRuleMap.get("campaignId")))) {
										Map<String, Object> affilMap = new HashMap<String, Object>();
										affilMap.put("campaignName", affilRuleMap.get("campaignName"));
										affilList.add(affilMap);
										break;
									}
								}
							}
						}
						ruleMap.put("affilList", affilList);
					}
				}
			}
		}
		tempMap.put("priorityRuleList", ruleList);
		tempMap.put("priorityRuleDotConList", ruleDotConList);
		// 默认积分规则
		List<Map<String, Object>> defaultRuleList = new ArrayList<Map<String, Object>>();
		if (null != ruleList) {
			for (int i = ruleList.size() - 1; i >= 0; i--) {
				Map<String, Object> ruleMap = ruleList.get(i);
				if ("1".equals(ruleMap.get("defaultFlag"))) {
					defaultRuleList.add(ruleMap);
					ruleList.remove(i);
					break;
				}
			}
		}
		tempMap.put("defaultRuleList", defaultRuleList);
	}
}
