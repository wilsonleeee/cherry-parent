/*
 * @(#)BINOLMBMBM07_BL.java     1.0 2012.12.07
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
package com.cherry.mb.mbm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM07_Service;
import com.cherry.ss.prm.bl.BINOLSSPRM37_BL;

/**
 * 会员活动记录画面BL
 * 
 * @author WangCT
 * @version 1.0 2012.12.07
 */
public class BINOLMBMBM07_BL {
	
	/** 会员活动记录画面Service */
	@Resource
	private BINOLMBMBM07_Service binOLMBMBM07_Service;
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 促销活动编辑_BL **/
	@Resource
	private BINOLSSPRM37_BL binOLSSPRM37_BL;
	
	/**
	 * 查询会员参与活动履历总数
	 * 
	 * @param map 检索条件
	 * @return 参与活动履历总数
	 */
	public int getCampaignHistoryCount(Map<String, Object> map) {
		
		// 查询会员参与活动履历总数
		return binOLMBMBM07_Service.getCampaignHistoryCount(map);
	}
	
	/**
	 * 查询会员参与活动履历List
	 * 
	 * @param map 检索条件
	 * @return 参与活动履历List
	 */
	public List<Map<String, Object>> getCampaignHistoryList(Map<String, Object> map) {
		
		// 查询会员参与活动履历List
		return binOLMBMBM07_Service.getCampaignHistoryList(map);
	}
	
	/**
	 * 查询会员已预约活动List
	 * 
	 * @param map 检索条件
	 * @return 会员已预约活动List
	 */
	public List<Map<String, Object>> getCampaignOrderList(Map<String, Object> map) {
		
		// 查询会员已预约活动List
		return binOLMBMBM07_Service.getCampaignOrderList(map);
	}
	
	/**
	 * 查询会员已预约活动明细
	 * 
	 * @param map 检索条件
	 * @return 会员已预约活动明细
	 */
	public Map<String, Object> getCampaignOrderDetail(Map<String, Object> map) {
		
		// 查询会员已预约活动明细
		Map<String, Object> campaignOrderDetail = binOLMBMBM07_Service.getCampaignOrderDetail(map);
		if(campaignOrderDetail != null) {
			// 取得礼品信息List
			List<Map<String, Object>> prtInfoList = binOLMBMBM07_Service.getPrtInfoList(map);
			if(prtInfoList != null && !prtInfoList.isEmpty()) {
				campaignOrderDetail.put("prtInfoList", prtInfoList);
			}
		}
		return campaignOrderDetail;
	}
	
	/**
	 * 查询会员可参加活动List
	 * 
	 * @param map 检索条件
	 * @return 可参加活动List
	 */
	public List<Map<String, Object>> getCurCampaignList(Map<String, Object> map) {
		
		List<Map<String, Object>> campaignList = new ArrayList<Map<String,Object>>();
		
//		// 查询会员正参加着的活动List
//		List<Map<String, Object>> campaignOrderList = binOLMBMBM07_Service.getCampaignOrderList(map);
//		if(campaignOrderList != null && !campaignOrderList.isEmpty()) {
//			campaignList.addAll(campaignOrderList);
//			map.put("campaignOrderList", campaignOrderList);
//		}
		// 查询所有进行中的会员活动List
		List<Map<String, Object>> curCampaignList = binOLMBMBM07_Service.getCurCampaignList(map);
		if(curCampaignList != null && !curCampaignList.isEmpty()) {
			List<String> subCampaignIdList = new ArrayList<String>();
			for(int i = 0; i < curCampaignList.size(); i++) {
				Map<String, Object> curCampaignMap = curCampaignList.get(i);
				int actLocationType = (Integer)curCampaignMap.get("actLocationType");
				String subCampaignId = curCampaignMap.get("subCampaignId").toString();
				if(subCampaignIdList.contains(subCampaignId)) {
					continue;
				} else {
					subCampaignIdList.add(subCampaignId);
				}
				if(actLocationType == 0) {// 活动条件为全部会员的场合
					campaignList.add(curCampaignMap);
				} else if(actLocationType == 2 || actLocationType == 3) {// 活动条件为搜索结果或者EXCEL导入的场合
					String customerCode = (String)curCampaignMap.get("customerCode");
					if(customerCode != null && !"".equals(customerCode)) {
						campaignList.add(curCampaignMap);
					}
				} else if(actLocationType == 1) {// 活动条件为搜索条件的场合
					String conditionInfo = (String)curCampaignMap.get("conditionInfo");
					if(conditionInfo != null && !"".equals(conditionInfo)) {
						Map<String, Object> reqContentMap = ConvertUtil.json2Map(conditionInfo);
						reqContentMap.put("memberInfoId", map.get("memberInfoId"));
						Map<String, Object> resultMap = binOLCM33_BL.searchMemList(reqContentMap);
						if(resultMap != null) {
							int count = Integer.parseInt(resultMap.get("total").toString());
							if(count != 0) {
								campaignList.add(curCampaignMap);
							}
						}
					}
				}
			}
		}
		
//		// 查询促销活动List
//		List<Map<String, Object>> promotionActList = binOLMBMBM07_Service.getPromotionActList(map);
//		if(promotionActList != null && !promotionActList.isEmpty()) {
//			campaignList.addAll(promotionActList);
//		}
		return campaignList;
	}
	
	/**
	 * 查询活动详细信息
	 * 
	 * @param map 检索条件
	 * @return 活动详细信息
	 */
	public Map<String, Object> getCampaignDetail(Map<String, Object> map) {
		
		// 取得活动基础信息
		Map<String,Object> subInfoMap = binOLMBMBM07_Service.getSubBaseInfo(map);
		if(subInfoMap != null && !subInfoMap.isEmpty()) {
			// 取得活动结果信息
			List<Map<String,Object>> subResultInfoList = binOLMBMBM07_Service.getSubResultList(map);
			if(subResultInfoList != null && !subResultInfoList.isEmpty()) {
				subInfoMap.put("subResultInfoList",subResultInfoList);
			}
			
			// 取得活动条件信息
			List<Map<String,Object>> subConditionList = binOLMBMBM07_Service.getSubConditionList(map);
			// 活动地点List
			List<Map<String,Object>> campPlaceList = new ArrayList<Map<String,Object>>();
			// 活动阶段List
			List<Map<String,Object>> campTimeList = new ArrayList<Map<String,Object>>();
			// 活动对象
			Map<String, Object> campMebMap = new HashMap<String, Object>();
			if (null != subConditionList && !subConditionList.isEmpty()) {
				for(Map<String,Object> subCon : subConditionList) {
					// 活动条件属性名
					String propertyName= ConvertUtil.getString(subCon.get("propertyName"));
					if("baseProp_customer".equals(propertyName)) {// 活动对象
						campMebMap.put("campMebType", subCon.get("actLocationType"));
						campMebMap.put("searchCode", subCon.get("basePropValue1"));
						campMebMap.put("conInfo", subCon.get("conditionInfo"));
						
					} else if("baseProp_channal".equals(propertyName)
								|| "baseProp_city".equals(propertyName)
								|| "baseProp_counter".equals(propertyName)) {// 活动地点
						// 活动范围类型
						String actLocationType = ConvertUtil.getString(subCon.get("actLocationType"));
						// 活动地点内容
						String propValue = ConvertUtil.getString(subCon.get("basePropValue1"));
						if("2".equals(actLocationType)
								|| "4".equals(actLocationType)
								|| "5".equals(actLocationType)) {//区域指定柜台，渠道指定柜台，导入柜台
							map.put("counterCode", propValue);
							// 柜台名称
							String counterName = binOLMBMBM07_Service.getCounterName(map);
							subCon.put("counterName", counterName);
							// 柜台号
							subCon.put("counterCode", propValue);
							
						} else if("1".equals(actLocationType)) {//按区域
							// 根据城市ID查询名称
							map.put("cityId", propValue);
							// 城市名称
							String cityName = binOLMBMBM07_Service.getCityName(map);
							subCon.put("placeName", cityName);
						} else if("3".equals(actLocationType)) {//按渠道
							// 根据渠道ID查询名称
							map.put("channelId", propValue);
							// 渠道名称
							String channelName = binOLMBMBM07_Service.getChannelName(map);
							subCon.put("placeName", channelName);
						}
						subInfoMap.put("actLocationType", actLocationType);
						campPlaceList.add(subCon);
					} else if("baseProp_rese_time".equals(propertyName)
							|| "baseProp_obtain_time".equals(propertyName)
							|| "baseprop_stocking_time".equals(propertyName)) {// 活动阶段
						campTimeList.add(subCon);
					}
					if(campMebMap != null && !campMebMap.isEmpty()) {
						subInfoMap.put("campMebMap", campMebMap);
					}
					if(campPlaceList != null && !campPlaceList.isEmpty()) {
						subInfoMap.put("campPlaceList", campPlaceList);
					}
					if(campTimeList != null && !campTimeList.isEmpty()) {
						subInfoMap.put("campTimeList", campTimeList);
					}
				}
			}
		}
		return subInfoMap;
	}
	
	/**
	 * 查询促销活动详细信息
	 * 
	 * @param map 检索条件
	 * @return 促销活动详细信息
	 */
	public Map<String, Object> getProCampaignDetail(Map<String, Object> map) {
		
		// 查询促销活动信息
		Map<String,Object> promotionActInfo = binOLMBMBM07_Service.getPromotionActInfo(map);
		if(promotionActInfo != null && !promotionActInfo.isEmpty()) {
			
			map.put("showType", "detail");
			map.put("activeID", map.get("subCampaignId"));
			// 取得促销活动结果详细
			List<Map<String,Object>> subResultInfoList = binOLSSPRM37_BL.getActiveResultDetail(map);
			if(subResultInfoList != null && !subResultInfoList.isEmpty()) {
				promotionActInfo.put("subResultInfoList",subResultInfoList);
			}
			
			// 查询促销活动条件
			List<Map<String,Object>> promotionActConList = binOLMBMBM07_Service.getPromotionActConList(map);
			// 活动地点List
			List<Map<String,Object>> campPlaceList = new ArrayList<Map<String,Object>>();
			// 活动阶段List
			List<Map<String,Object>> campTimeList = new ArrayList<Map<String,Object>>();
			if (null != promotionActConList && !promotionActConList.isEmpty()) {
				for(Map<String,Object> promotionActConMap : promotionActConList) {
					// 活动条件属性名
					String propertyName = ConvertUtil.getString(promotionActConMap.get("propertyName"));
					if("baseProp_city".equals(propertyName) 
							|| "baseProp_channal".equals(propertyName) 
							|| "baseProp_counter".equals(propertyName)) {// 活动地点
						// 活动范围类型
						String actLocationType = ConvertUtil.getString(promotionActConMap.get("actLocationType"));
						// 活动地点内容
						String propValue = ConvertUtil.getString(promotionActConMap.get("basePropValue1"));
						if("2".equals(actLocationType)
								|| "4".equals(actLocationType)
								|| "6".equals(actLocationType)) {//区域指定柜台，渠道指定柜台，导入柜台
							map.put("counterCode", propValue);
							// 柜台名称
							String counterName = binOLMBMBM07_Service.getCounterName(map);
							promotionActConMap.put("counterName", counterName);
							// 柜台号
							promotionActConMap.put("counterCode", propValue);
							
						} else if("1".equals(actLocationType)) {//按区域
							// 根据城市ID查询名称
							map.put("cityId", propValue);
							// 城市名称
							String cityName = binOLMBMBM07_Service.getCityName(map);
							promotionActConMap.put("placeName", cityName);
						} else if("3".equals(actLocationType)) {//按渠道
							// 根据渠道ID查询名称
							map.put("channelId", propValue);
							// 渠道名称
							String channelName = binOLMBMBM07_Service.getChannelName(map);
							promotionActConMap.put("placeName", channelName);
						}
						promotionActInfo.put("actLocationType", actLocationType);
						campPlaceList.add(promotionActConMap);
					} else if("baseProp_time".equals(propertyName)) {// 活动阶段
						campTimeList.add(promotionActConMap);
					}
					if(campPlaceList != null && !campPlaceList.isEmpty()) {
						promotionActInfo.put("campPlaceList", campPlaceList);
					}
					if(campTimeList != null && !campTimeList.isEmpty()) {
						promotionActInfo.put("campTimeList", campTimeList);
					}
				}
			}
		}
		return promotionActInfo;
	}
}
