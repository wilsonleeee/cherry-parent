/*	
 * @(#)ActTemplateInitCP.java     1.0 2013/12/26		
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil.CampComparator;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.ss.common.PromotionConstants;
import com.googlecode.jsonplugin.JSONException;



/**
 * 优惠券初始化处理
 * 
 * @author LuoHong
 * @version 1.0 2013.12.26
 */
public class ActTemplateInitCP extends ActTemplateInit {
	/**
	 * 活动奖励模板初始化
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000061_init(Map<String, Object> paramMap,
			Map<String, Object> tempMap) throws Exception {
		// 活动列表List
		List<Map<String, Object>> campList = ActUtil.getCampList(paramMap);
		CampaignDTO dto = (CampaignDTO) paramMap.get(CampConstants.CAMP_INFO);
		String deliveryFlag = getConfigValue(paramMap,"1311");
		// 虚拟促销品生成方式
		String virtPrmFlag = null;
		if (null != campList) {
			for (Map<String, Object> camp : campList) {
				String rewardType = ConvertUtil.getString(camp
						.get(CampConstants.REWARD_TYPE));
				// 活动档次设置虚拟促销品ID
				setPrmVendorId(camp);
				if (CampConstants.REWARD_TYPE_1.equals(rewardType)) {// 奖励产品
					setRewardInfo(camp, dto.getGroupFlag(),deliveryFlag);
				}else if (CampConstants.REWARD_TYPE_2.equals(rewardType)) {// 奖励抵用券
					if("3".equals(camp.get("virtPrmFlag"))){
						setRewardInfo(camp, "0",deliveryFlag);
					}
				}
				if (null == virtPrmFlag) {
					String vpf = ConvertUtil.getString(camp
							.get(CampConstants.VIRT_PRM_FLAG));
					if (!"".equals(vpf)) {
						virtPrmFlag = vpf;
					}
				}
				String searchCode = ConvertUtil.getString(camp
						.get(CampConstants.SEARCH_CODE));
				if (!"".equals(searchCode)) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 品牌Id
					map.put(CherryConstants.BRANDINFOID,
							paramMap.get(CherryConstants.BRANDINFOID));
					// 组织Id
					map.put(CherryConstants.ORGANIZATIONINFOID,
							paramMap.get(CherryConstants.ORGANIZATIONINFOID));
					// 会员信息搜索条件
					map.put(CampConstants.SEARCH_CODE, searchCode);
					// 活动对象类型
					map.put(CampConstants.CAMP_MEB_TYPE,
							camp.get(CampConstants.CAMP_MEB_TYPE));
					// 查询对象总数量
					int total = binolcpcom03IF.searchMemCount(map);
					camp.put("total", total);
				}
				// 活动Code
				if(dto.getCampaignCode()!=null){
					camp.put("campaignCode", dto.getCampaignCode());
				}				
				// 领用结束时间
				camp.put("obtainToDate", dto.getObtainToDate());
			}
		}
		if (null == virtPrmFlag) {
			virtPrmFlag = getConfigValue(paramMap, "1068");
		}
		tempMap.put(CampConstants.CAMP_LIST, campList);
		tempMap.put("needBuyFlag", dto.getNeedBuyFlag());
		tempMap.put("deliveryFlag", deliveryFlag);
		// 虚拟促销品生成方式
		tempMap.put(CampConstants.VIRT_PRM_FLAG, virtPrmFlag);
		// 是否启用组合奖励
		tempMap.put(CampConstants.GROUP_FLAG, dto.getGroupFlag());
	}

	/**
	 * 活动奖励模板初始化
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
		CampaignDTO campDTO = (CampaignDTO) paramMap
				.get(CampConstants.CAMP_INFO);
		String deliveryFlag = getConfigValue(paramMap,"1311");
		if (null != campList) {
			for (Map<String, Object> campMap : campList) {
				// ******************活动首页信息****************** //
				// 子活动类型
				campMap.put(CampConstants.SUBCAMP_TYPE,
						campDTO.getSubCampType());
				// 验证方式
				campMap.put(CampConstants.SUBCAMPAIGN_VALID,
						campDTO.getSubCampaignValid());
				// 采集活动获知方式
				campMap.put(CampConstants.ISCOLLECTINFO,
						campDTO.getIsCollectInfo());
				// 验证码规则
				campMap.put(CampConstants.LOCALVALIDRULE,
						campDTO.getLocalValidRule());
				campMap.put("needBuyFlag",campDTO.getNeedBuyFlag());
				// ******************活动阶段****************** //
				String timeList = ConvertUtil.getString(campMap
						.get(CampConstants.TIME_LIST));
				List<Map<String, Object>> list = ConvertUtil
						.json2List(timeList);
				campMap.put(CampConstants.TIME_LIST, list);
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
				campMap.putAll(getMebSearchMap(paramMap, campMap));
				// ******************活动奖励****************** //
				campMap.put("campaignType", campDTO.getCampaignType());
				// 子活动奖励类型
				String rewardType = ConvertUtil.getString(campMap
						.get(CampConstants.REWARD_TYPE));
				if (CampConstants.REWARD_TYPE_1.equals(rewardType)) {// 奖励礼品
					setRewardInfo(campMap, campDTO.getGroupFlag(),deliveryFlag);
				} else if (CampConstants.REWARD_TYPE_2.equals(rewardType)) {// 奖励抵用券
					if("3".equals(campMap.get("virtPrmFlag"))){
						campMap.put("rewardInfo", campMap.get("rewardPrice"));
					}
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
		// CONPON类型
		String couponType = ConvertUtil.getString(tempMap.get("couponType"));
		// CONPON数量
		int couponCount = ConvertUtil.getInt(tempMap.get("couponCount"));		
		// CONPON批次号
		String couponBatchNo = ConvertUtil.getString(tempMap.get("batchCode"));
		campaignRule.setPriceControl(priceControl);
		// CONPON类型
		campaignRule.setCouponType(couponType);
		// CONPON数量
		campaignRule.setCouponCount(couponCount);
		// CONPON批次号
		campaignRule.setCouponBatchNo(couponBatchNo);
		CampaignDTO campDTO = (CampaignDTO) paramMap
				.get(CampConstants.CAMP_INFO);
		List<CampRuleResultDTO> ruleResultList = new ArrayList<CampRuleResultDTO>();
		// 活动奖励
		String rewardInfo = ConvertUtil.getString(tempMap
				.get(CampConstants.REWARD_INFO));
		// 奖励类型
		String rewardType = ConvertUtil.getString(tempMap
				.get(CampConstants.REWARD_TYPE));
		
		// 用户选择虚拟促销品virtPrmFlag=3
		String virtPrmFlag = ConvertUtil.getString(tempMap.get("virtPrmFlag"));
		CampRuleResultDTO dto = null;
		// 指定礼品
		if (CampConstants.REWARD_TYPE_1.equals(rewardType)) {
			List<Map<String, Object>> list = getAllPrtList(rewardInfo,
					campDTO.getGroupFlag());
			// 取得礼品List
			getProDtoList(list, ruleResultList);
		}else if (CampConstants.REWARD_TYPE_2.equals(rewardType)) {// 抵用券
			// 抵用券金额
			float couponPrice = ConvertUtil.getFloat(rewardInfo);
//			if("3".equals(virtPrmFlag)){
//				List<Map<String, Object>> list = getAllPrtList(rewardInfo,"0");
//				// 取得礼品List
//				getProDtoList(list, ruleResultList);
//			}else{
				// 生成TZZK类型的虚拟促销品
				dto = getVirtualPro(paramMap, tempMap,
						PromotionConstants.PROMOTION_TZZK_TYPE_CODE, couponPrice);
//			}
			
		}
		// 拼装成PromotionRule表需要的条件
		convertRuleCondInfo(campaignRule,rewardType);
		// 拼装成PromotionRule表需要的结果
		convertRuleResultInfo(campaignRule,rewardInfo, dto,rewardType,campDTO.getGroupFlag());
		// 加入虚拟促销品
		if (null != dto) {
			dto.setRewardType(rewardType);
			ruleResultList.add(dto);
		}
		// 保存活动奖励段条件
		saveRuleResults(ruleResultList, paramMap, campaignRule);
	}
}