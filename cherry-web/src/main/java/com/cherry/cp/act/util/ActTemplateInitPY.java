/*	
 * @(#)ActTemplateInitPY.java     1.0 2013/01/24		
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
package com.cherry.cp.act.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil.CampComparator;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;


/**
 * 积分兑现初始化处理
 * 
 * @author lipc
 * @version 1.0 2013.01.24
 */
public class ActTemplateInitPY extends ActTemplateInit {
	/**
	 * 活动奖励模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000061_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		CampaignDTO dto = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		String deliveryFlag = getConfigValue(paramMap,"1311");
		// 虚拟促销品生成方式
		String virtualPrmFlag = null;
		if (null != campList) {
			for (Map<String, Object> camp : campList) {
				String rewardInfo = ConvertUtil.getString(camp
						.get(CampConstants.REWARD_INFO));
				// 活动档次设置虚拟促销品ID
				setPrmVendorId(camp);
				if(!"".equals(rewardInfo)){// 奖励产品
					List<Map<String, Object>> list = ConvertUtil.json2List(rewardInfo);
					list = getProList(list,deliveryFlag);
					camp.put(CampConstants.PRT_LIST, list);
				}
				if(null == virtualPrmFlag){
					String vpf = ConvertUtil.getString(camp
							.get(CampConstants.VIRT_PRM_FLAG));
					if(!"".equals(vpf)){
						virtualPrmFlag = vpf;
					}
				}
			}
		}
		if(null == virtualPrmFlag){
			virtualPrmFlag = getConfigValue(paramMap,"1068");
		}
		tempMap.put(CampConstants.CAMP_LIST, campList);
		tempMap.put("needBuyFlag", dto.getNeedBuyFlag());
		tempMap.put("deliveryFlag", deliveryFlag);
		// 虚拟促销品生成方式
		tempMap.put(CampConstants.VIRT_PRM_FLAG, virtualPrmFlag);
	}
	/**
	 * 活动确认模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000049_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws JSONException {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		CampaignDTO campDTO = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		String deliveryFlag = getConfigValue(paramMap,"1311");
		if (null != campList) {
			for (Map<String, Object> campMap : campList) {
				// ******************活动首页信息****************** //
				// 子活动类型
				campMap.put(CampConstants.SUBCAMP_TYPE, campDTO.getSubCampType());
				// 验证方式
				campMap.put(CampConstants.SUBCAMPAIGN_VALID, campDTO.getSubCampaignValid());
				//采集活动获知方式
				campMap.put(CampConstants.ISCOLLECTINFO, campDTO.getIsCollectInfo());
				// 验证码规则
				campMap.put(CampConstants.LOCALVALIDRULE, campDTO.getLocalValidRule());
				//主题活动类型
				campMap.put("campaignType", campDTO.getCampaignType());
				campMap.put("needBuyFlag",campDTO.getNeedBuyFlag());
				// ******************活动阶段****************** //
				//预约开始时间
				campMap.put(CampConstants.ORDER_FROMDATE, campDTO.getCampaignOrderFromDate());
				//预约结束时间
				campMap.put(CampConstants.ORDER_TODATE, campDTO.getCampaignOrderToDate());
				//备货开始时间
				campMap.put(CampConstants.STOCK_FROMDATE, campDTO.getCampaignStockFromDate());
				//备货结束时间
				campMap.put(CampConstants.STOCK_TODATE, campDTO.getCampaignStockToDate());
				//领用开始时间
				campMap.put(CampConstants.OBTAIN_FROMDATE, campDTO.getObtainFromDate());
				//领用结束时间
				campMap.put(CampConstants.OBTAIN_TODATE, campDTO.getObtainToDate());
				// ******************活动地点****************** //
				String placeJson = ConvertUtil.getString(campMap
						.get(CampConstants.PLACE_JSON));
				List<Map<String, Object>> placelist = ConvertUtil
						.json2List(placeJson);
				// 活动地点按照等级排序
				Collections.sort(placelist, new CampComparator(
						CampConstants.LEVEL));
				campMap.put(CampConstants.PLACE_JSON, placelist);
				// ******************活动对象****************** //
				campMap.putAll(getMebSearchMap(paramMap,campMap));
				// ******************活动奖励****************** //
				String rewardInfo = ConvertUtil.getString(campMap
						.get(CampConstants.REWARD_INFO));
				if("3".equals(campMap.get(CampConstants.VIRT_PRM_FLAG))){
					List<Map<String, Object>> prtInfoList = getProList(rewardInfo,deliveryFlag);
					campMap.put(CampConstants.PRT_LIST, prtInfoList);
				}
			}
			tempMap.put(CampConstants.CAMP_LIST, campList);
		}
	}
	/**
	 * 活动奖励模板保存
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception
	 * 
	 */
	public void BASE000061_save(Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		float priceControl = ConvertUtil.getFloat(tempMap.get("priceControl"));
		int saleBatchNo = ConvertUtil.getInt(tempMap.get("saleBatchNo"));
		campaignRule.setSaleBatchNo(saleBatchNo);
		campaignRule.setPriceControl(priceControl);
		List<CampRuleResultDTO> ruleResultList = new ArrayList<CampRuleResultDTO>();
		// 活动奖励
		String rewardInfo = ConvertUtil.getString(tempMap
						.get(CampConstants.REWARD_INFO));
		// 虚拟促销品生成方式
		String virtPrmFlag =  ConvertUtil.getString(tempMap
				.get(CampConstants.VIRT_PRM_FLAG));
		// 虚拟促销品生成方式：用户选择
		if(CampConstants.VIRT_PRM_FLAG_3.equals(virtPrmFlag)){
			List<Map<String, Object>> list = ConvertUtil
					.json2List(rewardInfo);
			getProDtoList(list, ruleResultList);
		}else {
			// 生成DHMY类型的虚拟促销品
			CampRuleResultDTO dto = getVirtualPro(paramMap, tempMap,PromotionConstants.PROMOTION_DHMY_TYPE_CODE,1);
			// 加入虚拟促销品
			if(null != dto){
				ruleResultList.add(dto);
			}
		}
		// 保存活动奖励段条件
		saveRuleResults(ruleResultList, paramMap, campaignRule);
	}
}
