/*
 * @(#)RuleCalStateHandler.java     1.0 2012/08/02
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
package com.cherry.shindig.gadgets.container.handler.mb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.shindig.gadgets.service.mb.RuleCalStateService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 统计积分规则当月和当年累计计算笔数Handler
 * 
 * @author WangCT
 * @version 1.0 2012/08/02
 */
@Service(name = "ruleCalState")
public class RuleCalStateHandler {
	
	/** 查看积分信息Service **/
	@Resource
	private RuleCalStateService ruleCalStateService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getRuleCalState(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put("businessType", "2");
		map.put("operationType", "1");
		// 返回结果
		Map resultData = new HashMap();
		// 取得积分规则信息List
		List<Map<String, Object>> campaignNameList = ruleCalStateService.getCampaignNameList(map);
		if(campaignNameList != null && !campaignNameList.isEmpty()) {
			resultData.put("ruleCount", campaignNameList.size());
			String sysDate = ruleCalStateService.getDateYMD();
			// 统计积分规则本月累计计算笔数
			map.put("changeDate", DateUtil.getFirstDateYMD(sysDate));
			List<Map<String, Object>> ruleCalStateMonthList = ruleCalStateService.getRuleCalState(map);
			
			// 统计积分规则本年累计计算笔数
			map.put("changeDate", DateUtil.getCurrYearFirst(Integer.parseInt(sysDate.substring(0,4))));
			List<Map<String, Object>> ruleCalStateYearList = ruleCalStateService.getRuleCalState(map);
			
			// 合并年月统计信息
			List<Map<String, Object>> ruleCalStateList = new ArrayList<Map<String,Object>>();
			if(ruleCalStateYearList != null && !ruleCalStateYearList.isEmpty()) {
				if(ruleCalStateMonthList != null && !ruleCalStateMonthList.isEmpty()) {
					for(Map<String, Object> ruleCalStateYearMap : ruleCalStateYearList) {
						Integer subCampaignId = (Integer)ruleCalStateYearMap.get("subCampaignId");
						Integer mainRuleId = (Integer)ruleCalStateYearMap.get("mainRuleId");
						if(mainRuleId == null) {
							continue;
						}
						for(Map<String, Object> ruleCalStateMonthMap : ruleCalStateMonthList) {
							Integer _subCampaignId = (Integer)ruleCalStateMonthMap.get("subCampaignId");
							Integer _mainRuleId = (Integer)ruleCalStateMonthMap.get("mainRuleId");
							if(_mainRuleId != null && _mainRuleId.intValue() == mainRuleId.intValue()) {
								if(subCampaignId == null || subCampaignId.intValue() == _subCampaignId.intValue()) {
									ruleCalStateYearMap.put("monthTotalCount", ruleCalStateMonthMap.get("totalCount"));
									ruleCalStateMonthList.remove(ruleCalStateMonthMap);
									break;
								}
							}
						}
					}
				}
				ruleCalStateList.addAll(ruleCalStateYearList);
			}
			
			// 把统计信息合并到规则信息列表中，同时把附属规则关联到宿主规则下
			if((ruleCalStateList != null && !ruleCalStateList.isEmpty())) {
				List<Map<String, Object>> _campaignNameList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < campaignNameList.size(); i++) {
					Map<String, Object> campaignNameMap = campaignNameList.get(i);
					String pointRuleType = (String)campaignNameMap.get("pointRuleType");
					if("2".equals(pointRuleType)) {
						_campaignNameList.add(campaignNameMap);
						campaignNameList.remove(i);
						i--;
						continue;
					}
					int subCampaignId = (Integer)campaignNameMap.get("subCampaignId");
					for(Map<String, Object> ruleCalStateMap : ruleCalStateList) {
						Integer _mainRuleId = (Integer)ruleCalStateMap.get("mainRuleId");
						if(_mainRuleId != null && subCampaignId == _mainRuleId.intValue()) {
							Integer _subCampaignId = (Integer)ruleCalStateMap.get("subCampaignId");
							if(_subCampaignId == null) {
								campaignNameMap.put("yearTotalCount", ruleCalStateMap.get("totalCount"));
								campaignNameMap.put("monthTotalCount", ruleCalStateMap.get("monthTotalCount"));
								ruleCalStateList.remove(ruleCalStateMap);
								break;
							}
							
						}
					}
				}
				for(Map<String, Object> campaignNameMap : campaignNameList) {
					int subCampaignId = (Integer)campaignNameMap.get("subCampaignId");
					for(int i = 0; i < ruleCalStateList.size(); i++) {
						Map<String, Object> ruleCalStateMap = ruleCalStateList.get(i);
						Integer mainRuleId = (Integer)ruleCalStateMap.get("mainRuleId");
						if(mainRuleId != null && mainRuleId.intValue() == subCampaignId) {
							Integer _subCampaignId = (Integer)ruleCalStateMap.get("subCampaignId");
							if(_subCampaignId == null) {
								continue;
							}
							for(Map<String, Object> _campaignNameMap : _campaignNameList) {
								int subCampaignIdTemp = (Integer)_campaignNameMap.get("subCampaignId");
								if(_subCampaignId.intValue() == subCampaignIdTemp) {
									ruleCalStateMap.putAll(_campaignNameMap);
									break;
								}
							}
							if(ruleCalStateMap.get("campaignId") != null) {
								ruleCalStateMap.put("yearTotalCount", ruleCalStateMap.get("totalCount"));
								List<Map<String, Object>> subList = (List)campaignNameMap.get("subList");
								if(subList == null) {
									subList = new ArrayList<Map<String,Object>>();
									subList.add(ruleCalStateMap);
									campaignNameMap.put("subList", subList);
								} else {
									subList.add(ruleCalStateMap);
								}
							}
							ruleCalStateList.remove(i);
							i--;
						}
					}
				}
			}
		} else {
			resultData.put("ruleCount", 0);
		}
		resultData.put("ruleCalStateMes", campaignNameList);
		resultData.put("brandInfoId", brandInfoId.toString());
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}
