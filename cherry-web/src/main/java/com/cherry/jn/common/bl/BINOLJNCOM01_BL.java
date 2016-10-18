/*	
 * @(#)BINOLJNCOM01_BL.java     1.0 2011/4/18		
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
package com.cherry.jn.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.common.interfaces.BINOLJNCOM01_IF;
import com.cherry.jn.common.interfaces.JnRuleCondition_IF;
import com.cherry.jn.common.interfaces.TemplateRule_IF;
import com.cherry.jn.common.service.BINOLJNCOM01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员入会共通 BL
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM01_BL implements BINOLJNCOM01_IF {

	@Resource
	private BINOLJNCOM01_Service binoljncom01_Service;
	
	@Resource
    private TemplateRule_IF templateRuleIF;
	
	@Resource
    private JnRuleCondition_IF jnRuleConditionIF;

	/**
	 * 取得页面对应的活动模板List
	 * 
	 * @param map
	 * @return 活动模板List
	 */
	@Override
	public List<Map<String, Object>> searchCamTempList(Map<String, Object> map) {
		// 取得页面对应的活动模板List
		List<Map<String, Object>> camTempList = binoljncom01_Service
				.getCamTempList(map);
		List<Map<String, Object>> newCamTempList = new ArrayList<Map<String, Object>>();
		if (null != camTempList && !camTempList.isEmpty()) {
			// 页面ID
			String pageId = (String) map.get("pageId");
			for (int i = 0; i < camTempList.size(); i++) {
				Map<String, Object> camTemp = camTempList.get(i);
				// 页面ID
				String camPageId = (String) camTemp.get("pageId");
				// 页面ID相等，表示是业务模板
				if (camPageId.equals(pageId)) {
					Map<String, Object> newCamTemp = new HashMap<String, Object>();
					newCamTemp.putAll(camTemp);
					newCamTempList.add(newCamTemp);
					camTempList.remove(i);
					i--;
					// 会员活动模板ID
					String camTempId = ConvertUtil.getString(newCamTemp
							.get("camTempId"));
					// 基础模板List
					List<Map<String, Object>> combTemps = new ArrayList<Map<String, Object>>();
					for (int j = 0; j < camTempList.size(); j++) {
						Map<String, Object> camTemp1 = camTempList.get(j);
						// 会员活动模板ID
						String camTempId1 = ConvertUtil.getString(camTemp1
								.get("camTempId"));
						// 会员活动模板ID相等，表示是基础模板
						if (camTempId.equals(camTempId1)) {
							Map<String, Object> combTemp = new HashMap<String, Object>();
							combTemp.putAll(camTemp1);
							combTemps.add(combTemp);
							camTempList.remove(j);
							j--;
						}
					}
					if (!combTemps.isEmpty()) {
						newCamTemp.put("combTemps", combTemps);
					}
				}
			}
		}
		return newCamTempList;
	}
	
	/**
	 * 取得页面显示的活动模板List
	 * 
	 * @param List
	 *            数据库取得的活动模板List
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @return 页面显示的活动模板List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> convertCamTempList(
			Map<String, Object> map,
			List<Map<String, Object>> camTemps) {
		// 取得页面对应的活动模板List
		List<Map<String, Object>> camTempList = searchCamTempList(map);
		if (null != camTempList) {
			for (Map<String, Object> camTempDB : camTempList) {
				// 集合标识符(数据库)
				String groupCodeDB = (String) camTempDB.get("groupCode");
				if (null != camTemps) {
					// 循环页面提交的活动模板List
					for (Map<String, Object> camTemp : camTemps) {
						// 集合标识符
						String groupCode = (String) camTemp.get("groupCode");
						if (groupCodeDB.equals(groupCode)) {
							if (camTempDB.containsKey("combTemps")) {
								// 基础模板List(数据库)
								List<Map<String, Object>> combTempsDB = (List<Map<String, Object>>) camTempDB.get("combTemps");
								// 基础模板List
								List<Map<String, Object>> combTemps = (List<Map<String, Object>>) camTemp.get("combTemps");
								if (null != combTempsDB) {
									for (Map<String, Object> combTempDB : combTempsDB) {
										// 基础模板集合标识符(数据库)
										String combGroupCodeDB = (String) combTempDB.get("groupCode");
										if (null != combTemps) {
											for (Map<String, Object> combTemp : combTemps) {
												// 基础模板集合标识符
												String combGroupCode = (String) combTemp.get("groupCode");
												if (combGroupCodeDB.equals(combGroupCode)) {
													combTemp.remove("tempCode");
													combTempDB.putAll(combTemp);
													combTemps.remove(combTemp);
													break;
												}
											}
										}
									}
								}
							} 
							if (camTemp.containsKey("combTemps")) {
								camTemp.remove("combTemps");
							}
							camTemp.remove("tempCode");
							camTempDB.putAll(camTemp);
							camTemps.remove(camTemp);
							break;
						}
					}
				}
			}
		}
		return camTempList;
	}
	
	/**
	 * 会员活动保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return String
	 * 			规则信息
	 * @throws Exception 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String saveCampaign(Map<String, Object> map) throws Exception {
		// 会员活动信息
		CampaignDTO campaignInfo = (CampaignDTO) map.get("campaignInfo");
		if (null != campaignInfo) {
			// 操作区分
			int operKbn = campaignInfo.getOperKbn();
			// 会员子活动DTO
			CampaignRuleDTO campaignRuleInfo = campaignInfo.getCampaignRule();
			// 会员子活动ID
			int campRuleId = 0;
			// 新建会员活动
			if (1 == operKbn) {
				// 会员活动ID
				int campaignId = campaignInfo.getCampaignId();
				if (0 == campaignId) {
					// 插入会员活动表并返回会员活动ID
					campaignId = binoljncom01_Service.insertCampaign(campaignInfo);
					// 会员活动ID
					campaignInfo.setCampaignId(campaignId);
					if (null != campaignRuleInfo) {
						// 会员活动ID
						campaignRuleInfo.setCampaignId(campaignId);
					}
				}
				if (null != campaignRuleInfo) {
					// 插入会员子活动表并返回子活动ID
					campRuleId = binoljncom01_Service.insertCampaignRule(campaignRuleInfo);
					// 会员子活动ID
					campaignRuleInfo.setCampaignRuleId(campRuleId);
				}
				// 更新会员活动
			} else if (2 == operKbn) {
				if (null != campaignRuleInfo) {
					binoljncom01_Service.updateCampaignRule(campaignRuleInfo);
					campRuleId = campaignRuleInfo.getCampaignRuleId();
					// 会员活动规则条件明细DTO
					CampRuleConditionDTO camRuleCondition = new CampRuleConditionDTO();
					// 会员子活动ID
					camRuleCondition.setCampaignRuleId(campRuleId);
					// 删除会员活动规则条件明细
					binoljncom01_Service.deleteCamRuleCondition(camRuleCondition);
				}
			}
			// 模板List
			List<Map<String, Object>> camTempList = null;
			String camTemps = (String) map.get("camTemps");
			if (null != camTemps && !"".equals(camTemps)) {
				camTempList = (List<Map<String, Object>>) JSONUtil.deserialize(camTemps);
			}
			// 规则条件明细List
			List<CampRuleConditionDTO> jnRuleConditionList = jnRuleConditionIF.createJnRuleConditionList(camTempList);
			if (null != jnRuleConditionList) {
				// 基础信息
				BaseDTO baseDTO = (BaseDTO) map.get("baseDTO");
				for (CampRuleConditionDTO camRuleConditionDTO : jnRuleConditionList) {
					ConvertUtil.convertDTO(camRuleConditionDTO, baseDTO, true);
					// 基础属性ID
					int campaignBasePropId = 0;
					// 取得基础属性ID
					String campBasePropId = binoljncom01_Service.getCampaignBasePropID(camRuleConditionDTO);
					if (null != campBasePropId && !"".equals(campBasePropId)) {
						campaignBasePropId = Integer.parseInt(campBasePropId);
					} else {
						// 插入会员活动基础属性表并返回会员活动基础属性ID
						campaignBasePropId = binoljncom01_Service.insertCampaignBaseProp(camRuleConditionDTO);
					}
					// 基础属性ID
					//camRuleConditionDTO.setCampaignBasePropId(campaignBasePropId);
					// 会员子活动ID
					camRuleConditionDTO.setCampaignRuleId(campRuleId);
					// 基础属性值区分
					camRuleConditionDTO.setPropFlag("1");
					binoljncom01_Service.insertCamRuleCondition(camRuleConditionDTO);
				}
			}
			Map<String, Object> ruleInfo = (Map<String, Object>) map.get("ruleInfo");
			// 关系List
			Map<String, Object> relationMap = null;
			String relationInfo = (String) map.get("relationInfo");
			if (null != relationInfo && !"".equals(relationInfo)) {
				relationMap = (Map<String, Object>) JSONUtil.deserialize(relationInfo);
			}
			if (null != ruleInfo && !ruleInfo.isEmpty() && null != camTempList
					&& null != relationMap) {
				// 会员等级ID
//				ruleInfo.put("memberLevelId", campaignRuleInfo.getMemberLevelId());
//				// 会员等级级别
//				ruleInfo.put("memberLevelGrade", campaignRuleInfo.getMemberLevelGrade());
				// 规则名称
				ruleInfo.put("ruleName", ConvertUtil.getString(campRuleId));
				String rule = templateRuleIF.convertTemplateToRule(camTempList, relationMap, ruleInfo);
				return rule;
			}
		}
		return null;
	}

	
	/**
	 * 取得会员等级有效期List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			会员等级有效期List
	 */
	@SuppressWarnings("unchecked")
	public List getMemberLevelList(Map<String, Object> map) {
		// 业务日期
		String busDate = binoljncom01_Service.getBussinessDate(map);
		map.put("busDate", busDate);
		return binoljncom01_Service.getMemberLevelList(map);
	}
	
	/**
	 * 取得会员活动等级List
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 会员等级List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> convertMemberLevelList(Map<String, Object> map) throws Exception {
		// 存放会员等级信息
		List<Map<String, Object>> memberLevelList = new ArrayList<Map<String,Object>>();
		Map<String, Object> baseMap = new HashMap<String,Object>();
		// 组织信息ID
		baseMap.put(CherryConstants.ORGANIZATIONINFOID, 
				map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		baseMap.put(CherryConstants.BRANDINFOID, 
				map.get(CherryConstants.BRANDINFOID));
		// 活动类型
		baseMap.put("campaignType", map.get("campaignType"));
		// 业务日期
		String busDate = binoljncom01_Service.getBussinessDate(map);
		baseMap.put("busDate", busDate);
		// 会员俱乐部ID
		baseMap.put("memberClubId", map.get("memberClubId"));
		// 取得等级名和等级ID
		memberLevelList = binoljncom01_Service.getCamTempList(baseMap);
		// 取得会员有效期标志(是否永久有效)
		for(Map<String, Object> memberLevelMap : memberLevelList){
			// 会员有效期信息
			String memberDateStr = (String) memberLevelMap.get("periodvalidity");
			if(null != memberDateStr){
				// 有效期信息转化为map格式
				Map<String, Object> memberDateMap = (Map<String, Object>) JSONUtil.deserialize(memberDateStr);
				// 向等级map中插入永久有效标志
				memberLevelMap.put("foreverFlag", memberDateMap.get("normalYear"));
			}
		}
		// 取得等级详细信息
		if(null != memberLevelList && !memberLevelList.isEmpty()){
			for(Map<String,Object> memberLevel : memberLevelList){
				Map<String, Object> camTempRuleMap = new HashMap<String,Object>();
				// 通过等级ID查询活动详细信息
				camTempRuleMap.put("memberLevelId", memberLevel.get("memberLevelId"));
				camTempRuleMap.putAll(baseMap);
				memberLevel.put("camTempRule", binoljncom01_Service.getCampaignRuleList(camTempRuleMap));
			}
		}
		return memberLevelList;
	}

	/**
	 * 取得会员子活动List
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void convertCampaignRuleList(Map<String, Object> map) throws Exception {
		// 会员子活动List
		List<Map<String, Object>> campaignRuleList = binoljncom01_Service.getCampaignRuleList(map);
		if (null != campaignRuleList && !campaignRuleList.isEmpty()) {
			Map<String, Object> campaignRule0 = (Map<String, Object>) campaignRuleList.get(0);
			// 会员活动ID
			map.put("campaignId", campaignRule0.get("campaignId"));
			// 会员等级List
			List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
			if (null != list) {
				for (Map<String, Object> memberLevel : list) {
					// 会员子活动List
					List<Map<String, Object>>  campRuleList = new ArrayList<Map<String, Object>>(); 
					// 会员等级ID
					int memberLevelId1 = (Integer) memberLevel.get("memberLevelId");
					for (int i = 0; i < campaignRuleList.size(); i++) {
						Map<String, Object> campaignRule = campaignRuleList.get(i);
						// 会员等级ID
						int memberLevelId2 = 0;
						if (null != campaignRule.get("memberLevelId")) {
							memberLevelId2 = (Integer) campaignRule.get("memberLevelId");
						}
						if (memberLevelId1 == memberLevelId2) {
							campRuleList.add(campaignRule);
							// 规则体详细 
							String ruleDetail = (String) campaignRule.get("ruleDetail");
							campaignRule.remove("ruleDetail");
							if (null != ruleDetail) {
								List<Map<String, Object>> camTemps = (List<Map<String, Object>>) JSONUtil.deserialize(ruleDetail);
								// 取得页面对应的活动模板List
								List<Map<String, Object>> camTempList = convertCamTempList(map, camTemps);
								campaignRule.put("camTempList", camTempList);
								campaignRuleList.remove(i);
								i--;
							}
						}
					}
					memberLevel.put("campRuleList", campRuleList);
				}
			}
		}
	}
	
	/**
     * 取得会员子活动信息
     * 
     * @param map
     * @return
     * 		会员子活动信息
     */
	@Override
	public Map<String, Object> getCampaignRuleMap(Map<String, Object> map) {
		// 取得会员子活动信息
		return binoljncom01_Service.getCampaignRuleMap(map);
	}
	
	/**
	 * 取得会员活动等级信息
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void convertMemberLevel(Map<String, Object> map) throws Exception {
		// 取得会员活动等级List
		List<Map<String, Object>> memberLevelList = binoljncom01_Service.getMemberLevelList(map);
		if (null != memberLevelList && !memberLevelList.isEmpty()) {
			List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys = {"groupId", "fromDate", "toDate"};
			keysList.add(keys);
			ConvertUtil.convertList2DeepList(memberLevelList, rootList, keysList, 0);
			// 将来生效的信息
			Map<String, Object> campaignMap = null;
			if (null != rootList && !rootList.isEmpty()) {
				campaignMap = new HashMap<String, Object>();
				campaignMap.putAll(rootList.get(0));
				// 组织信息ID
				campaignMap.put(CherryConstants.ORGANIZATIONINFOID, 
						map.get(CherryConstants.ORGANIZATIONINFOID));
				// 品牌信息ID
				campaignMap.put(CherryConstants.BRANDINFOID, 
						map.get(CherryConstants.BRANDINFOID));
				// 会员活动类型
				campaignMap.put("campaignType", map.get("campaignType"));
				// 页面ID
				campaignMap.put("pageId", map.get("pageId"));
				// 取得会员子活动List
				convertCampaignRuleList(campaignMap);
			}
			map.put("campaignMap", campaignMap);
		}
	}
	
	public int updateValid(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String campaignId = binoljncom01_Service.getCampaignId(map);
		paramMap.put("campaignId", campaignId);
		paramMap.putAll(map);
		int result = binoljncom01_Service.updateValid(paramMap);
		return result;
	}
}
