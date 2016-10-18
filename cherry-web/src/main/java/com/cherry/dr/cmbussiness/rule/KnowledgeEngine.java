/*	
 * @(#)KnowledgeEngine.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.rule;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Rule;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.DroolsFileDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM03_Service;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 规则引擎
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class KnowledgeEngine implements InitializingBean {
	
	private static Logger logger = LoggerFactory
	.getLogger(KnowledgeEngine.class.getName());
	
	/** 规则执行session的集合 */
	private Map<String, Object> ksessionMap;
	
	@Resource
	private BINBEDRCOM03_Service binbedrcom03_Service;
	
	/** 路径 */
	private String[] drlPaths;
	
	public KnowledgeEngine() {
		this.ksessionMap = new HashMap<String, Object>();
	}

	public void setDrlPaths(String[] drlPaths) {
		this.drlPaths = drlPaths;
	}

	/**
	 * 刷新单个规则文件
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @throws Exception 
	 * 
	 */
	public synchronized void refreshRule(int campaignId) throws Exception {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 活动ID
		searchMap.put("campaignId", campaignId);
		// 取得单个活动的规则内容信息
		Map<String, Object> ruleInfo = binbedrcom03_Service.getRuleCampaignInfo(searchMap);
		if (null != ruleInfo && !ruleInfo.isEmpty()) {
			// 活动组ID
			Object campaignGrpIdObj = ruleInfo.get("campaignGrpId");
			if (null != campaignGrpIdObj) {
				int campaignGrpId = Integer.parseInt(campaignGrpIdObj.toString());
				// 组织代码
				String orgCode = (String) ruleInfo.get("orgCode");
				// 品牌代码
				String brandCode = (String) ruleInfo.get("brandCode");
				// 会员活动类型
				String campaignType = (String) ruleInfo.get("campaignType");
				// 会员活动类型加俱乐部ID
				String typeClub = campaignType + "_" + ruleInfo.get("memberClubId");
				// 取得规则库中该活动组的规则内容
				Map<String, Object> campGrpRule = getCampGrpRule(orgCode, brandCode, typeClub, campaignGrpId);
				if (null == campGrpRule || campGrpRule.isEmpty()) {
					searchMap.put("campaignGrpId", campaignGrpId);
					// 取得会员活动组List
					List<Map<String, Object>> campGrpList = binbedrcom03_Service.getCampaignGrpRuleList(searchMap);
					if (null != campGrpList && !campGrpList.isEmpty()) {
						Map<String, Object> campGrp = campGrpList.get(0);
						List<Map<String, Object>> campaignGrpRules = loadGrpRule(campGrp);
						// 添加一组规则到规则库中
						addGrpRuleToLib(orgCode, brandCode, campaignGrpRules);
					}
				} else {
					// 有效区分
					String validFlag = (String) ruleInfo.get("validFlag");
					// 规则优先级
					String priorityRuleDetail = (String) ruleInfo.get("priorityRuleDetail");
					// 取得已保存的优先级信息
					List<Map<String, Object>> priorityList = getPriorityList(priorityRuleDetail);
					if ("1".equals(validFlag)) {
						// 加载已配置的活动
						if (isContain(String.valueOf(campaignId), priorityList)) {
							// 验证规则库中是否存在该规则
							Map<String, Object> campRules = (Map<String, Object>) campGrpRule.get("campRules");
							// 活动规则对应的包名
							String ruleKey = "PKG_" + campaignId;
							// 活动规则
							Map<String, Object> campRule = (Map<String, Object>) campRules.get(ruleKey);
							if (null == campRule || campRule.isEmpty()) {
								KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
								// 加载活动规则
								Map<String, Object> campaignRule = loadCampRule(ruleInfo, kbuilder);
								// 添加活动规则到规则库中
								addRuleToLib(campGrpRule, typeClub, campaignId, campaignRule, kbuilder);
//								if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//									// 取得规则库中该活动组的规则内容
//									Map<String, Object> downCampGrpRule = getCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId);
//									// 添加活动规则到规则库中
//									addRuleToLib(downCampGrpRule, DroolsConstants.CAMPAIGN_TYPE9999, campaignId, campaignRule, kbuilder);
//								}
								campGrpRule.put("priorityList", priorityList);
							} else {
								// 修改回数(数据库中取得)
								int modifyCountDB = Integer.parseInt(ruleInfo.get("modifyCount").toString());
								// 修改回数
								int modifyCount = 0;
								Object modifyCountObj = campRule.get("modifyCount");
								if (null != modifyCountObj) {
									modifyCount = Integer.parseInt(modifyCountObj.toString());
								}
								// 规则有更新
								if (modifyCountDB > modifyCount) {
									KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
									// 加载活动规则
									Map<String, Object> campaignRule = loadCampRule(ruleInfo, kbuilder);
									Map<String, Object> grpRuleNew = copyGrpRule(campGrpRule);
									// 从规则组中删除指定规则
									removeRuleFromGrp(grpRuleNew, campaignId);
									// 添加活动规则到规则库中
									addRuleToLib(grpRuleNew, typeClub, campaignId, campaignRule, kbuilder);
									grpRuleNew.put("priorityList", priorityList);
									// 替换规则库中的规则
									changeCampGrpRule(orgCode, brandCode, typeClub, campaignGrpId, grpRuleNew);
									// 升降级类型
//									if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//										// 取得规则库中该活动组的规则内容
//										Map<String, Object> downCampGrpRule = getCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId);
//										Map<String, Object> downGrpRuleNew = copyGrpRule(downCampGrpRule);
//										// 从规则组中删除指定规则
//										removeRuleFromGrp(downGrpRuleNew, campaignId);
//										// 添加活动规则到规则库中
//										addRuleToLib(downGrpRuleNew, DroolsConstants.CAMPAIGN_TYPE9999, campaignId, campaignRule, kbuilder);
//										// 替换规则库中的规则
//										changeCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId, downGrpRuleNew);
//									}
								}
							}
						}
						// 规则无效时，从规则库中删除
					} else {
						campGrpRule.put("priorityList", priorityList);
						// 从规则组中删除指定规则
						removeRuleFromGrp(campGrpRule, campaignId);
						// 升降级类型
//						if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//							// 取得规则库中该活动组的规则内容
//							Map<String, Object> downCampGrpRule = getCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId);
//							if (null != downCampGrpRule && !downCampGrpRule.isEmpty()) {
//								// 从规则组中删除指定规则
//								removeRuleFromGrp(downCampGrpRule, campaignId);
//							}
//						}
					}
				}
			}
		}
	}
	
	/**
	 * 刷新一组规则
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @throws Exception 
	 * 
	 */
	public synchronized void refreshGrpRule(int campaignGrpId) throws Exception {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("campaignGrpId", campaignGrpId);
		// 取得会员活动组List
		List<Map<String, Object>> campGrpList = binbedrcom03_Service.getCampaignGrpRuleList(searchMap);
		if (null != campGrpList && !campGrpList.isEmpty()) {
			Map<String, Object> campGrp = campGrpList.get(0);
			// 组织代码
			String orgCode = (String) campGrp.get("orgCode");
			// 品牌代码
			String brandCode = (String) campGrp.get("brandCode");
			// 会员活动类型
			String campaignType = (String) campGrp.get("campaignType");
			// 会员活动类型加俱乐部ID
			String typeClub = campaignType + "_" + campGrp.get("memberClubId");
			// 有效区分
			String grpValidFlag = (String) campGrp.get("grpValidFlag");
			// 规则配置删除
			if ("0".equals(grpValidFlag)) {
				// 删除活动组的规则内容
				removeCampGrpRule(orgCode, brandCode, typeClub, campaignGrpId);
			} else {
				// 修改回数
				int modifyCountGrp = Integer.parseInt(campGrp.get("modifyCount").toString());
				// 取得规则库中该活动组的规则内容
				Map<String, Object> campGrpRule = getCampGrpRule(orgCode, brandCode, typeClub, campaignGrpId);
				if (null == campGrpRule || campGrpRule.isEmpty()) {
					List<Map<String, Object>> campaignGrpRules = loadGrpRule(campGrp);
					// 添加一组规则到规则库中
					addGrpRuleToLib(orgCode, brandCode, campaignGrpRules);
				} else {
					// 规则优先级
					String priorityRuleDetail = (String) campGrp.get("priorityRuleDetail");
					// 取得已保存的优先级信息
					List<Map<String, Object>> priorityList = getPriorityList(priorityRuleDetail);
					Map<String, Object> campGrpRuleNew = copyGrpRule(campGrpRule);
					// 修改回数
					int oldModifyCountGrp = 0;
					Object modifyCountGrpObj = campGrpRuleNew.get("modifyCount");
					if (null != modifyCountGrpObj) {
						oldModifyCountGrp = Integer.parseInt(modifyCountGrpObj.toString());
					} 
					campGrpRuleNew.put("priorityList", priorityList);
					campGrpRuleNew.put("modifyCount", modifyCountGrp);
					if (DroolsConstants.CAMPAIGN_TYPE3.equals(campaignType)) {
						boolean remFalg = true;
						// 规则配置内容
						String grpConfDetail = (String) campGrp.get("grpConfDetail");
						if (null != grpConfDetail && !grpConfDetail.isEmpty()) {
							Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpConfDetail);
							if (null != detailInfo && !detailInfo.isEmpty()) {
								// 配置参数集合
			        			Map<String, Object> gpInfo = (Map<String, Object>) detailInfo.get("gpInfo");
			        			if (null != gpInfo) {
			        				remFalg = false;
			        				// 配置参数集合
			        				campGrpRuleNew.put("GPINFO", gpInfo);
			        			}
							}
						}
						if (remFalg && campGrpRuleNew.containsKey("GPINFO")) {
							// 配置参数集合
	        				campGrpRuleNew.remove("GPINFO");
						}
					}
					// 取得规则库中该活动组的规则内容
	//				Map<String, Object> downCampGrpRuleNew = null;
	//				if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
	//					// 取得规则库中该活动组的规则内容
	//					Map<String, Object> downCampGrpRule = getCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId);
	//					downCampGrpRuleNew = copyGrpRule(downCampGrpRule);
	//					downCampGrpRuleNew.put("modifyCount", modifyCountGrp);
	//				}
					// 验证规则库中是否存在该规则
					Map<String, Object> campRules = (Map<String, Object>) campGrpRuleNew.get("campRules");
					// 取得规则内容List
					List<Map<String, Object>> ruleContentList = binbedrcom03_Service.getRuleContentList(campGrp);
					List<String> delCampList = new ArrayList<String>();
					if (null != priorityList && null != ruleContentList) {
						for (int i = 0; i < ruleContentList.size(); i++) {
							Map<String, Object> ruleInfo = ruleContentList.get(i);
							String campaignIdStr = ruleInfo.get("campaignId").toString();
							// 活动ID
							int campaignId = Integer.parseInt(campaignIdStr);
							if (isContain(campaignIdStr, priorityList)) {
								// 活动规则对应的包名
								String ruleKey = "PKG_" + campaignId;
								// 活动规则
								Map<String, Object> campRule = (Map<String, Object>) campRules.get(ruleKey);
								if (null == campRule || campRule.isEmpty()) {
									KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
									// 加载活动规则
									Map<String, Object> campaignRule = loadCampRule(ruleInfo, kbuilder);
									// 添加活动规则到规则库中
									addRuleToLib(campGrpRuleNew, typeClub, campaignId, campaignRule, kbuilder);
	//								if (null != downCampGrpRuleNew) {
	//									// 添加活动规则到规则库中
	//									addRuleToLib(downCampGrpRuleNew, DroolsConstants.CAMPAIGN_TYPE9999, campaignId, campaignRule, kbuilder);
	//								}
								} else {
									// 修改回数(数据库中取得)
									int modifyCountDB = Integer.parseInt(ruleInfo.get("modifyCount").toString());
									// 修改回数
									int modifyCount = 0;
									Object modifyCountObj = campRule.get("modifyCount");
									if (null != modifyCountObj) {
										modifyCount = Integer.parseInt(modifyCountObj.toString());
									}
									// 规则有更新
									if (modifyCountDB > modifyCount) {
										KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
										// 加载活动规则
										Map<String, Object> campaignRule = loadCampRule(ruleInfo, kbuilder);
										// 从规则组中删除指定规则
										removeRuleFromGrp(campGrpRuleNew, campaignId);
										// 添加活动规则到规则库中
										addRuleToLib(campGrpRuleNew, typeClub, campaignId, campaignRule, kbuilder);
	//									if (null != downCampGrpRuleNew) {
	//										// 从规则组中删除指定规则
	//										removeRuleFromGrp(downCampGrpRuleNew, campaignId);
	//										// 添加活动规则到规则库中
	//										addRuleToLib(downCampGrpRuleNew, DroolsConstants.CAMPAIGN_TYPE9999, campaignId, campaignRule, kbuilder);
	//									}
									}
								}
							} else {
								delCampList.add(campaignIdStr);
							}
						}
					}
					if (modifyCountGrp > oldModifyCountGrp) {
						// 规则包名
						String pkgGrpKey = "PKG_GRP_" + campaignGrpId;
						// 组规则
						String grpRuleContent = (String) campGrp.get("ruleFileContent");
						if (!CherryChecker.isNullOrEmpty(grpRuleContent)) {
							KnowledgeBase kbase = (KnowledgeBase) campGrpRuleNew.get("kbase");
							KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
							if (null != kbase) {
								InputStream in = null;
								try {
									// 加载配置规则
									in = new ByteArrayInputStream(grpRuleContent.getBytes("UTF-8"));
									kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
								} catch (Exception e) {
									throw new Exception("load group rule file exception!");
								} finally {
									if (null != in) {
										in.close();
									}
								}
								KnowledgeBuilderErrors errors = kbuilder.getErrors();
								if (errors.size() > 0) {
									for (KnowledgeBuilderError error : errors) {
										logger.error(error.getMessage(),error);
									}
								}
								if (null != kbase.getKnowledgePackage(pkgGrpKey)) {
									kbase.removeKnowledgePackage(pkgGrpKey);
								}
								kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
							}
						}
					}
					// 替换规则库中的规则
					changeCampGrpRule(orgCode, brandCode, typeClub, campaignGrpId, campGrpRuleNew);
					
					
	//				if (null != downCampGrpRuleNew && !delCampList.isEmpty()) {
	//					for (String campRuleId : delCampList) {
	//						// 从规则组中删除指定规则
	//						removeRuleFromGrp(downCampGrpRuleNew, Integer.parseInt(campRuleId));
	//					}
	//					// 替换规则库中的规则
	//					changeCampGrpRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE9999, campaignGrpId, downCampGrpRuleNew);
	//				}
				}
			}
		}
			
	}
	private List<Map<String, Object>> getPriorityList(String priorityRuleDetail) throws JSONException {
		if (!CherryChecker.isNullOrEmpty(priorityRuleDetail)) {
			// 取得已保存的优先级信息
			List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityRuleDetail);
			for (Map<String, Object> priority : priorityList) {
				// 已配置的规则ID
				List ruleKeys = (List) priority.get("keys");
				if (null != ruleKeys && !ruleKeys.isEmpty()) {
					Object firstKey = ruleKeys.get(0);
					if (firstKey instanceof String) {
						break;
					}
					List<String> ruleKeyList = new ArrayList<String>();
					for (Object key : ruleKeys) {
						ruleKeyList.add(String.valueOf(key));
					}
					priority.put("keys", ruleKeyList);
				}
			}
			return priorityList;
		}
		return null;
	}
	private Map<String, Object> copyGrpRule(Map<String, Object> campGrpRule) throws Exception {
		Map<String, Object> campGrpRuleNew = new HashMap<String, Object>();
		if (null != campGrpRule && !campGrpRule.isEmpty()) {
			// 优先级列表
			List<Map<String, Object>> priorityList = (List<Map<String, Object>>)campGrpRule.get("priorityList");
			if (null != priorityList) {
				campGrpRuleNew.put("priorityList", priorityList);
			}
			// 更新回数
			campGrpRuleNew.put("modifyCount", campGrpRule.get("modifyCount"));
			// 配置参数集合
			Map<String, Object> gpInfo = (Map<String, Object>) campGrpRule.get("GPINFO");
			if (null != gpInfo && !gpInfo.isEmpty()) {
				Map<String, Object> gpInfoNew = (Map<String, Object>) ConvertUtil.byteClone(gpInfo);
				campGrpRuleNew.put("GPINFO", gpInfoNew);
			}
			List<RuleFilterDTO> allFilterList = new ArrayList<RuleFilterDTO>();
			// 活动规则集合
			Map<String, Object> campRules = (Map<String, Object>) campGrpRule.get("campRules");
			if (null != campRules) {
				Map<String, Object> campRulesNew = new HashMap<String, Object>();
				for(Map.Entry<String,Object> cr: campRules.entrySet()){
					Map<String, Object> campRuleInfo = (Map<String, Object>) cr.getValue();
					Map<String, Object> campRuleInfoNew = new HashMap<String, Object>();
					// 修改回数
					campRuleInfoNew.put("modifyCount", campRuleInfo.get("modifyCount"));
					List<RuleFilterDTO> ruleFilters = (List<RuleFilterDTO>) campRuleInfo.get("ruleFilter");
					if (null != ruleFilters) {
						List<RuleFilterDTO> ruleFiltersNew = new ArrayList<RuleFilterDTO>();
						for (RuleFilterDTO ruleFilter : ruleFilters) {
							RuleFilterDTO ruleFilterNew = new RuleFilterDTO();
							// 复制新的条件DTO
							ConvertUtil.convertDTO(ruleFilterNew, ruleFilter, false);
							ruleFiltersNew.add(ruleFilterNew);
						}
						// 规则条件
						campRuleInfoNew.put("ruleFilter", ruleFiltersNew);
						allFilterList.addAll(ruleFiltersNew);
					}
					campRulesNew.put(cr.getKey(), campRuleInfoNew);
				}
				campGrpRuleNew.put("campRules", campRulesNew);
				campGrpRuleNew.put("allFilterList", allFilterList);
			}
			// 规则库
			KnowledgeBase kbase = (KnowledgeBase) campGrpRule.get("kbase");
			if (null != kbase) {
				KnowledgeBase kbaseNew = KnowledgeBaseFactory.newKnowledgeBase();
				kbaseNew.addKnowledgePackages(kbase.getKnowledgePackages());
				campGrpRuleNew.put("kbase", kbaseNew);
			}
		}
		return campGrpRuleNew;
	}
	
	private void removeRuleFromGrp(Map<String, Object> campGrpRule, int campaignId) {
		if (null != campGrpRule && !campGrpRule.isEmpty()) {
			Map<String, Object> campRules = (Map<String, Object>) campGrpRule.get("campRules");
			// 活动规则对应的包名
			String ruleKey = "PKG_" + campaignId;
			// 需要删除的活动规则
			Map<String, Object> campRule = (Map<String, Object>) campRules.get(ruleKey);
			if (null != campRule && !campRule.isEmpty()) {
				List<RuleFilterDTO> ruleFilter = (List<RuleFilterDTO>) campRule.get("ruleFilter");
				List<RuleFilterDTO> allFilterList = (List<RuleFilterDTO>) campGrpRule.get("allFilterList");
				if (null != allFilterList && !allFilterList.isEmpty()) {
					if (null != ruleFilter && !ruleFilter.isEmpty()) {
						allFilterList.removeAll(ruleFilter);
					}
				}
				// 规则库
				KnowledgeBase kbase = (KnowledgeBase) campGrpRule.get("kbase");
				if (null != kbase.getKnowledgePackage(ruleKey)) {
					kbase.removeKnowledgePackage(ruleKey);
				}
				campRules.remove(ruleKey);
			}
		}
	}
	
	/**
	 * 删除活动组的规则内容
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @param campaignGrpId
	 * 			活动组ID
	 * 
	 */
	private void removeCampGrpRule(String orgCode, String brandCode, String campaignType, int campaignGrpId) {
		// 取得组规则库
		Map<String, Object> grpRule = getGroupRule(orgCode, brandCode, campaignType);
		if (null != grpRule && !grpRule.isEmpty()) {
			String key = "PKG_GRP_" + campaignGrpId;
			grpRule.remove(key);
		}
	}
	
	/**
	 * 验证规则库中是否存在该活动组
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @param campaignGrpId
	 * 			活动组ID
	 * @return 验证结果  true: 存在    false: 不存在
	 * 
	 */
//	private boolean checkGrpExist(String orgCode, String brandCode, String campaignType, int campaignGrpId) {
//		// 取得组规则库
//		Map<String, Object> grpRule = getGroupRule(orgCode, brandCode, campaignType);
//		if (null != grpRule && !grpRule.isEmpty()) {
//			String key = "PKG_GRP_" + campaignGrpId;
//			if (grpRule.containsKey(key)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * 取得规则库中该活动组的规则内容
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @param campaignGrpId
	 * 			活动组ID
	 * @return 验证结果
	 * 			活动组的规则内容 
	 * 
	 */
	private Map<String, Object> getCampGrpRule(String orgCode, String brandCode, String campaignType, int campaignGrpId) {
		// 取得组规则库
		Map<String, Object> grpRule = getGroupRule(orgCode, brandCode, campaignType);
		if (null != grpRule && !grpRule.isEmpty()) {
			String key = "PKG_GRP_" + campaignGrpId;
			return (Map<String, Object>) grpRule.get(key);
		}
		return null;
	}
	
	/**
	 * 替换规则库中的规则
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @param campaignGrpId
	 * 			活动组ID
	 * @return 验证结果
	 * 			活动组的规则内容 
	 * 
	 */
	private void changeCampGrpRule(String orgCode, String brandCode, String campaignType, int campaignGrpId, Map<String, Object> campGrpRule) {
		// 取得组规则库
		Map<String, Object> grpRule = getGroupRule(orgCode, brandCode, campaignType);
		if (null != grpRule && !grpRule.isEmpty()) {
			String key = "PKG_GRP_" + campaignGrpId;
			grpRule.put(key, campGrpRule);
		}
	}
	
	/**
	 * 验证规则库中是否存在该规则
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @param campaignGrpId
	 * 			活动组ID
	 * @param campaignId
	 * 			活动组ID
	 * @return 验证结果  true: 存在    false: 不存在
	 * 
	 */
//	private boolean checkRuleExist(String orgCode, String brandCode, String campaignType, int campaignGrpId, int campaignId) {
//		// 取得组规则库
//		Map<String, Object> grpRule = getGroupRule(orgCode, brandCode, campaignType);
//		if (null != grpRule && !grpRule.isEmpty()) {
//			String key = "PKG_GRP_" + campaignGrpId;
//			// 组内的所有规则库
//			Map<String, Object> campRuleLib = (Map<String, Object>) grpRule.get(key);
//			if (null != campRuleLib && !campRuleLib.isEmpty()) {
//				Map<String, Object> campRules = (Map<String, Object>) campRuleLib.get("campRules");
//				if (null != campRules && !campRules.isEmpty()) {
//					String ruleKey = "PKG_" + campaignId;
//					if (campRules.containsKey(ruleKey)) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	/**
	 * 取得对应的规则执行session
	 * 
	 * @param droolsFileDTO
	 * 			规则文件 DTO
	 * @return StatelessKnowledgeSession
	 * 
	 */
	public StatelessKnowledgeSession getKsession(DroolsFileDTO droolsFileDTO) {
		// 文件名称
		String drlName = droolsFileDTO.getDroolsFileName();
		if (null != ksessionMap && ksessionMap.containsKey(drlName)) {
			return (StatelessKnowledgeSession) ksessionMap.get(drlName);
		}
		return null;
	}
	
	/**
	 * 读取规则文件
	 * 
	 * @param String
	 * 			规则文件名称
	 * @return KnowledgeBase
	 * 
	 * @throws Exception
	 * 
	 */
	public KnowledgeBase readKnowledgeBase(String drlPath, String drlName) throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newFileResource(PropertiesUtil.pps.getProperty(drlPath)
				+ System.getProperty("file.separator") + drlName + ".drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				logger.error(error.toString());
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
	
	/**
	 * 读取所有规则文件
	 * 
	 * @param String
	 * 			规则文件名称
	 * @return KnowledgeBase
	 * 
	 * @throws Exception
	 * 
	 */
	public void readKnowledgeBaseAll () throws Exception {
		if (null != drlPaths) {
			if (null == ksessionMap) {
				ksessionMap = new HashMap<String, Object>();
			}
			for (String drlPath : drlPaths) {
				File file = new File(PropertiesUtil.pps.getProperty(drlPath));
				File[] fileArr = file.listFiles();
				if (null != fileArr) {
					for (File f : fileArr) {
						if (f.isFile()) {
							if (f.getName().indexOf(".drl") == f.getName().length() - 4) {
								String name = f.getName().replace(".drl", "");
								KnowledgeBase kbase = readKnowledgeBase(drlPath, name);
								// 将规则执行session放入集合当中
								ksessionMap.put(name, kbase.newStatelessKnowledgeSession());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 加载活动规则
	 * 
	 * @param name
	 * 			组合名称
	 * @param ruleContentMap
	 * 			规则信息
	 * @param ruleMap
	 * 			记载加载过的规则集合
	 * @param kbuilder
	 * 			KnowledgeBuilder
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> loadCampRule(Map<String, Object> ruleContentMap, KnowledgeBuilder kbuilder) throws Exception {
		// 规则内容
		String ruleContent = (String) ruleContentMap.get("ruleFileContent");
		if (CherryChecker.isNullOrEmpty(ruleContent)) {
			return null;
		}
		// 规则条件
		String ruleFilter = (String) ruleContentMap.get("ruleFilter");
		// 修改回数
		int modifyCount = Integer.parseInt(ruleContentMap.get("modifyCount").toString());
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(ruleContent.getBytes("UTF-8"));
			kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
		} catch (Exception e) {
			throw new Exception("load rule file exception!");
		} finally {
			if (null != in) {
				in.close();
			}
		}
		Map<String, Object> ruleMap = new HashMap<String, Object>();
		if (!CherryChecker.isNullOrEmpty(ruleFilter)) {
			List<Map<String, Object>> filterMapList = (List<Map<String, Object>>) JSONUtil.deserialize(ruleFilter);
			// 活动的所有规则条件
			List<RuleFilterDTO> filterList = new ArrayList<RuleFilterDTO>();
			for (Map<String, Object> filter : filterMapList) {
				RuleFilterDTO filterDTO = CampRuleUtil.map2Bean(filter, RuleFilterDTO.class);
				filterList.add(filterDTO);
			}
			// 修改回数
			ruleMap.put("modifyCount", modifyCount);
			// 规则条件
			ruleMap.put("ruleFilter", filterList);
		}
		return ruleMap;
	}
	
	/**
	 * 加载规则组
	 * 
	 * @param name
	 * 			组合名称
	 * @param campGrp
	 * 			规则组信息
	 * @param ruleMap
	 * 			记载加载过的规则集合
	 * 
	 * @throws Exception
	 * 
	 */
	private List<Map<String, Object>> loadGrpRule(Map<String, Object> campGrp) throws Exception {
		// 规则优先级
		String priorityRuleDetail = (String) campGrp.get("priorityRuleDetail");
		if (CherryChecker.isNullOrEmpty(priorityRuleDetail)) {
			return null;
		}
		// 取得已保存的优先级信息
		List<Map<String, Object>> priorityList = getPriorityList(priorityRuleDetail);
		Map<String, Object> campRules = new HashMap<String, Object>();
		// 取得规则内容List
		List<Map<String, Object>> ruleContentList = binbedrcom03_Service.getRuleContentList(campGrp);
		if (null == ruleContentList || ruleContentList.isEmpty()) {
			return null;
		}
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		// 规则条件
		List<RuleFilterDTO> allFilterList = new ArrayList<RuleFilterDTO>();
		
		for (Map<String, Object> ruleContentMap : ruleContentList) {
			// 会员活动ID
			String campaignId = String.valueOf(ruleContentMap.get("campaignId"));
			// 加载已配置的活动
			if (isContain(campaignId, priorityList)) {
				// 规则包名
				String pkgKey = "PKG_" + campaignId;
				// 加载活动规则
				Map<String, Object> campaignRule = loadCampRule(ruleContentMap, kbuilder);
				if (null != campaignRule && !campaignRule.isEmpty()) {
					campRules.put(pkgKey, campaignRule);
					// 规则条件
					List<RuleFilterDTO> filterList = (List<RuleFilterDTO>) campaignRule.get("ruleFilter");
					if (null != filterList && !filterList.isEmpty()) {
						allFilterList.addAll(filterList);
					}
				}
			}
		}
		// 会员活动类型
		String campaignType = (String) campGrp.get("campaignType");
		// 组规则
		String grpRuleContent = (String) campGrp.get("ruleFileContent");
		// 会员活动组ID
		String campaignGrpId = String.valueOf(campGrp.get("campaignGrpId"));
		// 修改回数
		int modifyCount = Integer.parseInt(campGrp.get("modifyCount").toString());
		// 活动组规则
		List<Map<String, Object>> campaignGrpRules = new ArrayList<Map<String, Object>>();
		// 规则包名
		String pkgGrpKey = "PKG_GRP_" + campaignGrpId;
		// 升降级类型
//		if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//			// 从升降级规则中分离出降级和失效规则
//			KnowledgeBase downKbase = KnowledgeBaseFactory.newKnowledgeBase();
//			downKbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
//			// 删除升级规则
//			removeRuleByNameStart("MLUP_", downKbase);
//			// 提取出降级规则
//			//if (downKbase.getKnowledgePackages().size() > 0) {
//				Map<String, Object> groupRule = new HashMap<String, Object>();
//				Map<String, Object> grpMap = new HashMap<String, Object>();
//				// 活动组修改回数
//				grpMap.put("modifyCount", modifyCount);
//				// 组内的活动信息
//				grpMap.put("campRules", campRules);
//				// 降级规则库
//				grpMap.put("kbase", downKbase);
//				//if (!allFilterList.isEmpty()) {
//					// 规则条件
//					grpMap.put("allFilterList", allFilterList);
//				//}
//				// 活动类型:降级
//				groupRule.put("campaignType", DroolsConstants.CAMPAIGN_TYPE9999);
//				groupRule.put(pkgGrpKey, grpMap);
//				campaignGrpRules.add(groupRule);
//			//}
//		}
		if (!CherryChecker.isNullOrEmpty(grpRuleContent)) {
			InputStream in = null;
			try {
				// 加载配置规则
				in = new ByteArrayInputStream(grpRuleContent.getBytes("UTF-8"));
				kbuilder.add(ResourceFactory.newInputStreamResource(in), ResourceType.DRL);
			} catch (Exception e) {
				throw new Exception("load group rule file exception!");
			} finally {
				if (null != in) {
					in.close();
				}
			}
		}
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				logger.error(error.getMessage(),error);
			}
		}
		//throw new IllegalArgumentException("Could not parse knowledge.");
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		// 升降级类型
//		if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//			// 删除降级失效规则,只保留升级规则
//			removeRuleByNameStart("MLDOWN_", kbase);
////			if (0 == kbase.getKnowledgePackages().size()) {
////				return campaignGrpRules;
////			}
//		}
		Map<String, Object> groupRule = new HashMap<String, Object>();
		Map<String, Object> grpMap = new HashMap<String, Object>();
		// 活动组修改回数
		grpMap.put("modifyCount", modifyCount);
		// 组内的活动信息
		grpMap.put("campRules", campRules);
		// 降级规则库
		grpMap.put("kbase", kbase);
		//if (!allFilterList.isEmpty()) {
			// 规则条件
			grpMap.put("allFilterList", allFilterList);
		//}
		// 优先级列表
		grpMap.put("priorityList", priorityList);
		if (DroolsConstants.CAMPAIGN_TYPE3.equals(campaignType)) {
			// 规则配置内容
			String grpConfDetail = (String) campGrp.get("grpConfDetail");
			if (null != grpConfDetail && !grpConfDetail.isEmpty()) {
				Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpConfDetail);
				if (null != detailInfo && !detailInfo.isEmpty()) {
					// 配置参数集合
        			Map<String, Object> gpInfo = (Map<String, Object>) detailInfo.get("gpInfo");
        			if (null != gpInfo) {
        				// 配置参数集合
        				grpMap.put("GPINFO", gpInfo);
        			}
				}
			}
		}
		// 会员活动类型
		groupRule.put("campaignType", campaignType + "_" + campGrp.get("memberClubId"));
		groupRule.put(pkgGrpKey, grpMap);
		campaignGrpRules.add(groupRule);
		return campaignGrpRules;
	}
	
	/**
	 * 删除规则(通过匹配规则名开头)
	 * 
	 * @param nameStart
	 * 			规则名开头
	 * @param kbase
	 * 			规则库
	 * 
	 */
	private void removeRuleByNameStart(String nameStart, KnowledgeBase kbase) {
		List<KnowledgePackage> packageList = (List<KnowledgePackage>) kbase.getKnowledgePackages();
		if (null != packageList && !packageList.isEmpty()) {
			// 循环规则包
			for (KnowledgePackage rp : packageList) {
				// 包名
				String pkName = rp.getName();
				List<Rule> ruleList = (List<Rule>) rp.getRules();
				int removeNum = 0;
				// 循环规则
				for (Rule rule : ruleList) {
					// 规则名
					String ruleName = rule.getName();
					// 匹配规则名称开头
					if (ruleName.startsWith(nameStart)) {
						// 删除指定名称的规则
						kbase.removeRule(pkName, ruleName);
						removeNum++;
					}
				}
				// 如果该包下所有规则都删除的情况下删除整个包
				if (removeNum == ruleList.size()) {
					kbase.removeKnowledgePackage(pkName);
				}
			}
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
				} else {
					// 活动ID
					List<String> keys = (List<String>) priorityInfo.get("keys");
					if (null != keys) {
						for (String key : keys) {
							if (campaignId.equals(key)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 从数据库中加载规则文件
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * 
	 * @throws Exception
	 * 
	 */
	private void loadRulesDB(String orgCode, String brandCode) throws Exception {
		Map<String, Object> brandMap = new HashMap<String, Object>();
		// 组织Code
		brandMap.put("orgCode", orgCode);
		// 品牌code
		brandMap.put("brandCode", brandCode);
		// 取得品牌信息
		Map<String, Object> osbrandInfo = binbedrcom03_Service.getOSBrandInfo(brandMap);
		if (null == osbrandInfo) {
			throw new Exception("can not get the brand message!");
		}
		osbrandInfo.put("grpValidFlag", "1");
		// 取得会员活动组List
		List<Map<String, Object>> campGrpList = binbedrcom03_Service.getCampaignGrpRuleList(osbrandInfo);
		if (null != campGrpList) {
			Map<String, Object> brandRule = new HashMap<String, Object>();
			// 品牌规则库名称
			String brandRuleName = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
			// 将品牌规则添加到规则库中
			ksessionMap.put(brandRuleName, brandRule);
			for (Map<String, Object> campGrp : campGrpList) {
				List<Map<String, Object>> campaignGrpRules = loadGrpRule(campGrp);
				// 添加一组规则到规则库中
				addGrpRuleToLib(orgCode, brandCode, campaignGrpRules);
			}
		}
	}
	
	/**
	 * 添加一组规则到品牌规则库中
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignGrpList
	 * 			组规则列表
	 * 
	 */
	private void addGrpRuleToLib(String orgCode, String brandCode, List<Map<String, Object>> campaignGrpRules) {
		// 循环组规则列表
		if (null != campaignGrpRules && !campaignGrpRules.isEmpty()) {
			for (Map<String, Object> campaignGrp : campaignGrpRules) {
				// 会员活动类型
				String campaignType = (String) campaignGrp.get("campaignType");
				// 取得组规则库
				Map<String, Object> groupRule = getGroupRule(orgCode, brandCode, campaignType);
				campaignGrp.remove("campaignType");
				groupRule.putAll(campaignGrp);
			}
		}
	}
	
	/**
	 * 添加活动规则到规则库中
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignGrpList
	 * 			组规则列表
	 * 
	 */
	private void addRuleToLib(Map<String, Object> campGrpRule, String campaignType, int campaignId, 
			Map<String, Object> campaignRule, KnowledgeBuilder kbuilder) {
		if (null == campaignRule) {
			return;
		}
		Map<String, Object> campRules = (Map<String, Object>) campGrpRule.get("campRules");
		if (null != campRules) {
			//if (!DroolsConstants.CAMPAIGN_TYPE9999.equals(campaignType)) {
				String pkgKey = "PKG_" + campaignId;
				campRules.put(pkgKey, campaignRule);
				List<RuleFilterDTO> allFilterList = (List<RuleFilterDTO>) campGrpRule.get("allFilterList");
				if (null != allFilterList) {
					// 规则条件
					List<RuleFilterDTO> filterList = (List<RuleFilterDTO>) campaignRule.get("ruleFilter");
					if (null != filterList && !filterList.isEmpty()) {
						allFilterList.addAll(filterList);
					}
				}
			//}
			// 规则库
			KnowledgeBase kbase = (KnowledgeBase) campGrpRule.get("kbase");
			// 从升降级规则中分离出降级和失效规则
			KnowledgeBase kbaseNew = KnowledgeBaseFactory.newKnowledgeBase();
			kbaseNew.addKnowledgePackages(kbuilder.getKnowledgePackages());
			// 升降级类型
//			if (DroolsConstants.CAMPAIGN_TYPE2.equals(campaignType)) {
//				// 删除降级失效规则,只保留升级规则
//				removeRuleByNameStart("MLDOWN_", kbaseNew);
//			} 
//			else if (DroolsConstants.CAMPAIGN_TYPE9999.equals(campaignType)) {
//				// 删除升级规则
//				removeRuleByNameStart("MLUP_", kbaseNew);
//			}
			if (kbaseNew.getKnowledgePackages().size() > 0) {
				kbase.addKnowledgePackages(kbaseNew.getKnowledgePackages());
			}
		}
	}
	
	/**
	 * 取得组规则库
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @return Map
	 * 			组规则库
	 * 
	 */
	public Map<String, Object> getGroupRule(String orgCode, String brandCode, String campaignType) {
		// 取得品牌对应的规则库
		Map<String, Object> brandRule = getBrandRule(orgCode, brandCode);
		// 各品牌规则库的key
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode, campaignType);
		// 组规则库
		Map<String, Object> groupRule = (Map<String, Object>) brandRule.get(key);
		if (null == groupRule) {
			synchronized (this) {
				groupRule = new HashMap<String, Object>();
				// 创建新的组规则库
				brandRule.put(key, groupRule);
			}
		}
		return groupRule;
	}
	
	/**
	 * 取得品牌规则库
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @return Map
	 * 			品牌规则库
	 * 
	 */
	private Map<String, Object> getBrandRule(String orgCode, String brandCode) {
		// 各品牌规则库的key
		String key = CampRuleUtil.getCombKeyByLine(orgCode, brandCode);
		// 品牌规则库
		Map<String, Object> brandRule = (Map<String, Object>) ksessionMap.get(key);
		if (null == brandRule) {
			synchronized (this) {
				// 创建新的品牌规则库
				brandRule = new HashMap<String, Object>();
				ksessionMap.put(key, brandRule);
			}
		}
		return brandRule;
	}
	
	/**
	 * 刷新所有规则
	 * 	
	 * @param organizationInfoId
	 * 			组织ID
	 * @param brandInfoId
	 * 			品牌ID
	 * 
	 * @throws Exception
	 * 
	 */
	public void refreshAllRule(int organizationInfoId, int brandInfoId) throws Exception {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 组织ID
		searchMap.put("organizationInfoId", organizationInfoId);
		// 品牌ID
		searchMap.put("brandInfoId", brandInfoId);
		// 取得会员活动组List
		List<Map<String, Object>> campGrpList = binbedrcom03_Service.getCampaignGrpRuleList(searchMap);
		if (null != campGrpList) {
			for (Map<String, Object> campGrpMap : campGrpList) {
				// 会员活动组ID
				int campaignGrpId = Integer.parseInt(campGrpMap.get("campaignGrpId").toString());
				// 刷新一组规则
				refreshGrpRule(campaignGrpId);
			}
		}
	}
	
	/**
	 * 从数据库中读取所有规则文件
	 * 
	 * 
	 * @throws Exception
	 * 
	 */
	public void readKnowledgeBaseDB () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得配置数据库品牌List
		List<Map<String, Object>> confBrandList = binbedrcom03_Service.getConfBrandInfoList(map);
		
		// 保存当前进程中的datasource type,用于junit测试，因为在junit测试时在beforeclass设置的datasource type会被这个方法消除掉
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		
		// 品牌信息
		if (null != confBrandList) {
			for (Map<String, Object> brandInfo : confBrandList) {
				try {
					// 新后台品牌数据源
					String dataSource = (String) brandInfo.get("dataSourceName");
					if (CherryChecker.isNullOrEmpty(dataSource)) {
						continue;
					}
					CustomerContextHolder.setCustomerDataSourceType(dataSource);
					String orgCode = (String) brandInfo.get("orgCode");
					String brandCode = (String) brandInfo.get("brandCode");
					// 加载规则
					loadRulesDB(orgCode, brandCode);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					//throw e;
				} finally {
//					String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
					if (null != dataSourceType) {
						// 恢复保存的datasource type,使得junit测试能够顺利进行
						CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
						// 清除新后台品牌数据源
//						CustomerContextHolder.clearCustomerDataSourceType();
					} else {
						String cusDateSource = CustomerContextHolder.getCustomerDataSourceType();
						if (null != cusDateSource) {
							// 清除新后台品牌数据源
							CustomerContextHolder.clearCustomerDataSourceType();
						}
					}
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		String format = "yyyy-MM-dd";
//		System.setProperty("drools.dateformat", format);
		readKnowledgeBaseAll ();
		readKnowledgeBaseDB ();
	}
}
