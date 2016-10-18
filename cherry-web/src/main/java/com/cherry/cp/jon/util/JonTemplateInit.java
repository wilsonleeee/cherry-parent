/*	
 * @(#)JonTemplateInit.java     1.0 2011/11/02		
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
package com.cherry.cp.jon.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.act.util.ActUtil.CampComparator;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.TemplateInit;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员入会和升降级模板初始化处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class JonTemplateInit extends TemplateInit{
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	protected BINOLCPPOI01_BL binolcppoi01_BL;
	
	/**
	 * 等级和有效期初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000010_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 取得会员等级List
		List<Map<String, Object>> memberLevelList = binolcpcom02_Service.getMemberLevelList(paramMap);
		if (null != memberLevelList && !memberLevelList.isEmpty()) {
			Map<String, Object> memberLevel = memberLevelList.get(0);
			// 等级开始日默认值
			if (CherryChecker.isNullOrEmpty(tempMap.get("fromDate"))) {
				tempMap.put("fromDate", memberLevel.get("fromDate"));
			}
			// 等级结束日默认值
			if (CherryChecker.isNullOrEmpty(tempMap.get("toDate"))) {
				tempMap.put("toDate", memberLevel.get("toDate"));
			}
			// 等级名称
			if (CherryChecker.isNullOrEmpty(tempMap.get("levelName"))) {
				tempMap.put("levelName", memberLevel.get("levelName"));
			}
			// 等级级别
			if (CherryChecker.isNullOrEmpty(tempMap.get("grade"))) {
				tempMap.put("grade", memberLevel.get("grade"));
			}
			// 解析会员等级有效期
			if(null != memberLevel.get("periodvalidity")){
				Map<String, Object> periodvalidityMap = (Map<String, Object>) JSONUtil.deserialize((String) memberLevel.get("periodvalidity"));
				String index = (String) periodvalidityMap.get("normalYear");
				tempMap.put("memberDate", periodvalidityMap.get("memberDate" + index));
				tempMap.put("textName", "text" + index);
			}
		}
		String levelDate = paramMap.get("campaignFromDate") + "~" + paramMap.get("campaignToDate"); 
		tempMap.put("campaignType", paramMap.get("campaignType"));
		tempMap.put("levelDateList", levelDate);
		tempMap.put("memberLevelList", memberLevelList);
		// 取得有选中结果的活动范围树
		tempMap.put(CampConstants.PLACE_JSON,
				getPlaceJson(getComMap(paramMap), tempMap));
	}
	
	/**
	 * 取得活动地点数组JSON
	 * 
	 * @param list
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getPlaceJson(Map<String, Object> comMap, Map<String, Object> map) {
		String palceJson = ConvertUtil.getString(map.get(
				CampConstants.PLACE_JSON));
		String locationType = ConvertUtil.getString(map.get(
				CampConstants.LOCATION_TYPE));
		List<Map<String, Object>> palceList = null;
		try {
			palceList = (List<Map<String, Object>>) JSONUtil
					.deserialize(palceJson);
		} catch (JSONException e) {
		}
		return getLocationInfo(comMap, locationType,
				palceList);
	}
	
	/**
	 * 取得共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		comMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		comMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		comMap.put("userID", map.get(CherryConstants.USERID));
		return comMap;
	}
	
	/**
	 * 取得活动地点节点【有选中状态】JSON信息
	 * 
	 * @param comMap
	 * @param locationType
	 * @param checkedNodes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLocationInfo(Map<String, Object> comMap,
			String locationType, List<Map<String, Object>> checkedNodes) {
		// 活动树的所有节点
		List<Map<String, Object>> nodeList = null;
		// 全部柜台或导入柜台
		if (CampConstants.LOTION_TYPE_0.equals(locationType)
				|| CampConstants.LOTION_TYPE_5.equals(locationType)) {
			nodeList = checkedNodes;
		} else if (!"".equals(locationType)) {
			Map<String, Object> params = new HashMap<String, Object>(comMap);
			params.put(CampConstants.LOCATION_TYPE, locationType);
			// 加载柜台
			params.put(CampConstants.LOADING_CNT, 1);
			nodeList = binolcppoi01_BL.getActiveLocation(params);
		}
		if (null != nodeList) {
			// 设置节点选中状态
			if (null != checkedNodes) {
				for (int i = 0; i < checkedNodes.size(); i++) {
					Map<String, Object> checkedNode = checkedNodes.get(i);
					ActUtil.setNodes(nodeList, checkedNode);
				}
			}
			try {
				return JSONUtil.serialize(nodeList);
			} catch (JSONException e) {
				return "[]";
			}
		} else {
			return "[]";
		}
	}
	
	/**
	 * 等级和有效期初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000010_save(Map<String, Object> paramMap, Map<String, Object> tempMap, CampaignRuleDTO campaignRule) throws Exception {
		List<CampRuleConditionDTO> ruleConditonList = new ArrayList<CampRuleConditionDTO>();
		// 规则有效期开始日
		String fromDate = (String) tempMap.get("fromDate");
		// 规则有效期结束日
		String toDate = (String) tempMap.get("toDate");
		// 会员等级
		String memberLevelId = (String) tempMap.get("memberLevelId");
		CampRuleConditionDTO ruleCondetion1 = new CampRuleConditionDTO();
		// 时间
		ruleCondetion1.setPropertyName(CampUtil.BASEPROP_TIME);
		ruleCondetion1.setBasePropValue1(fromDate);
		ruleCondetion1.setBasePropValue2(toDate);
		CampRuleConditionDTO ruleCondetion2 = new CampRuleConditionDTO();
		// 等级
		ruleCondetion2.setPropertyName(CampUtil.BASEPROP_MEMBERLEVEL);
		ruleCondetion2.setBasePropValue1(memberLevelId);
		//ruleConditonList.add(ruleCondetion1);
		ruleConditonList.add(ruleCondetion2);
		// 保存活动条件
		saveRuleConditons(ruleConditonList, paramMap, campaignRule);
	}
	
	/**
	 * 入会条件的显示初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000016_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 组织ID
		String orgIdStr = String.valueOf(paramMap.get("organizationInfoId"));
		// 品牌ID
		String brandIdStr = String.valueOf(paramMap.get("brandInfoId"));
		// 等级计算方式
		String levelCalcKbn = binOLCM14_BL.getConfigValue("1101", orgIdStr, brandIdStr);
		if ("2".equals(levelCalcKbn)) {
			tempMap.put("levelCalcKbn", levelCalcKbn);
		}
	}
	
	/**
	 * 入会条件的显示初期处理(确认画面)
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000017_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 组织ID
		String orgIdStr = String.valueOf(paramMap.get("organizationInfoId"));
		// 品牌ID
		String brandIdStr = String.valueOf(paramMap.get("brandInfoId"));
		// 等级计算方式
		String levelCalcKbn = binOLCM14_BL.getConfigValue("1101", orgIdStr, brandIdStr);
		if ("2".equals(levelCalcKbn)) {
			tempMap.put("levelCalcKbn", levelCalcKbn);
		}
	}
	
	/**
	 * 升级框中的显示初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000006_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		paramMap.put("upordownFlag", 0);
		// 取得会员等级List
		List<Map<String, Object>> upMemberLevelList = binolcpcom02_Service.getUpMemberLevelList(paramMap);
		if(null == upMemberLevelList || upMemberLevelList.isEmpty()){
			tempMap.put("showFlag", "0");
		}else{
			tempMap.put("showFlag", "1");
			// 仅当新增并且不是回退的时候设置为选择状态,新增的时候没有活动ID
			if(null == paramMap.get("campaignId") && null == paramMap.get("checkFlag")) {
				tempMap.put("isChecked", "1");
			}
		}
	}
	
	/**
	 * 升级设置框中的等级初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000007_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		paramMap.put("upordownFlag", 0);
		// 取得会员等级List
		List<Map<String, Object>> upMemberLevelList = binolcpcom02_Service.getUpMemberLevelList(paramMap);
		tempMap.put("upMemberLevelList", upMemberLevelList);
	}
	
	/**
	 * 降级框中的显示初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BUS000010_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		paramMap.put("upordownFlag", 1);
		// 取得会员等级List
		List<Map<String, Object>> downMemberLevelList = binolcpcom02_Service.getUpMemberLevelList(paramMap);
		String memberDateStr = binolcpcom02_Service.getMemberDate(paramMap);
		if(null == downMemberLevelList || downMemberLevelList.isEmpty() || null == memberDateStr){
			tempMap.put("showFlag", "0");
		}else{
			if(null != memberDateStr){
				Map<String, Object> memberDateMap = (Map<String, Object>) JSONUtil.deserialize(memberDateStr);
				if("3".equals(memberDateMap.get("normalYear"))){
					tempMap.put("showFlag", "0");
				}else{
					tempMap.put("showFlag", "1");
				}
			}
		}
	}
	
	/**
	 * 失效框中的显示初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BUS000012_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		String memberDateStr = binolcpcom02_Service.getMemberDate(paramMap);
		if(null == memberDateStr){
			tempMap.put("showFlag", "0");
		}else{
			Map<String, Object> memberDateMap = (Map<String, Object>) JSONUtil.deserialize(memberDateStr);
			if("3".equals(memberDateMap.get("normalYear"))){
				tempMap.put("showFlag", "0");
			}else{
				tempMap.put("showFlag", "1");
			}
		}
	}
	
	/**
	 * 降级中的等级初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000011_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		paramMap.put("upordownFlag", 1);
		// 取得会员等级List
		List<Map<String, Object>> downMemberLevelList = binolcpcom02_Service.getUpMemberLevelList(paramMap);
		tempMap.put("downMemberLevelList", downMemberLevelList);
	}
	/**
	 * 单次购买初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000001_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 仅当新增的时候设置为选择状态
		if(null == paramMap.get("campaignId") && null == paramMap.get("checkFlag")){
			// 入会规则设定画面
			if ("P00000000001".equals(paramMap.get("pageId"))) {
				// 默认选中
				tempMap.put("isChecked", "1");
			}
			// 升降级规则设定画面
			else if ("P00000000004".equals(paramMap.get("pageId"))) {
				// 默认选中
				tempMap.put("isChecked", "1");
			}
		}
	}
	
	/**
	 * 累积购买初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000002_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		
	}
	
	/**
	 * 购买产品初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000001_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 取得已选择的产品信息
		List<Map<String, Object>> proList = (List<Map<String, Object>>) tempMap.get("productList");
		if(null != proList && !proList.isEmpty()){
			for(Map<String, Object> proMap : proList){
				// 从数据库中取得产品名称
				Map<String, Object> prtMap = binolcpcom02_Service.getProInfo(proMap);
				// 向产品list中插入产品名称
				if(null != prtMap){
					proMap.putAll(prtMap);
				}
			}
		}
	}
	
	/**
	 * 购买产品(确认画面)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000004_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 取得产品list
		List<Map<String, Object>> proList = (List<Map<String, Object>>) tempMap.get("productList");
		if(null != proList && !proList.isEmpty()){
			for(Map<String, Object> proMap : proList){
				// 从数据库中取得产品名称
				Map<String, Object> prtMap = binolcpcom02_Service.getProInfo(proMap);
				// 产品list中插入产品名称
				if(null != prtMap){
					proMap.putAll(prtMap);
				}
			}
		}
	}
	
	/**
	 * 等级和有效期(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000007_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 会员等级ID
		paramMap.put("memberLevelId", tempMap.get("memberLevelId"));
		// 取得会员等级List
		String memberLevelName = binolcpcom02_Service.getMemberLevelName(paramMap);
		// 会员等级名称
		tempMap.put("levelName", memberLevelName);
		// 取得会员等级List
		List<Map<String, Object>> memberLevelList = binolcpcom02_Service.getMemberLevelList(paramMap);
		if(null != memberLevelList && !memberLevelList.isEmpty()){
			Map<String, Object> memberLevel = memberLevelList.get(0);
			// 解析会员等级有效期
			if(null != memberLevel.get("periodvalidity")){
				Map<String, Object> periodvalidityMap = (Map<String, Object>) JSONUtil.deserialize((String) memberLevel.get("periodvalidity"));
				String index = (String) periodvalidityMap.get("normalYear");
				tempMap.put("memberDate", periodvalidityMap.get("memberDate" + index));
				tempMap.put("textName", "text" + index);
			}
		}
		if ("1".equals(tempMap.get("isCounter"))) {
			String placeJson = ConvertUtil.getString(tempMap
					.get(CampConstants.PLACE_JSON));
			if (!CherryChecker.isNullOrEmpty(placeJson)) {
				List<Map<String, Object>> placelist = ConvertUtil
						.json2List(placeJson);
				// 活动地点按照等级排序
				Collections.sort(placelist, new CampComparator(
						CampConstants.LEVEL));
				tempMap.put("placeList", placelist);
			}
		}
	}
	
	/**
	 * 升级(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BUS000009_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 会员等级ID
		paramMap.put("memberLevelId", tempMap.get("upMemberLevelId"));
		// 取得会员等级List
		String memberLevelName = binolcpcom02_Service.getMemberLevelName(paramMap);
		// 会员等级名称
		tempMap.put("levelName", memberLevelName);
	}
	
	/**
	 * 降级(内容确认)初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000012_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		// 会员等级ID
		paramMap.put("memberLevelId", tempMap.get("downMemberLevelId"));
		// 取得会员等级List
		String memberLevelName = binolcpcom02_Service.getMemberLevelName(paramMap);
		// 会员等级名称
		tempMap.put("levelName", memberLevelName);
	}
	
	/**
	 * 规则描述初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * 
	 */
	public void BASE000050_init(Map<String, Object> paramMap, Map<String, Object> tempMap) {
		if("1".equals(paramMap.get("detailFlag"))){
			// 取得规则描述List
			List<Map<String, Object>> depList = binolcpcom02_Service.getDepList(paramMap);
			// 会员等级名称
			tempMap.put("depList", depList);
		}
	}
	
	/**
	 * 累计购买初期处理
	 * 
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void BASE000003_init(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 会员等级ID
		String memberLevelId = (String) paramMap.get("memberLevelId");
		if (!CherryChecker.isNullOrEmpty(memberLevelId, true)) {
			int levelId = Integer.parseInt(memberLevelId);
			// 取得会员等级有效期信息
			Map<String, Object> levelValidInfo = binOLCM31_BL.getLevelValidInfo(levelId);
			tempMap.put("levelValidInfo", levelValidInfo);
		}
	}
}
