/*	
 * @(#)BINOLJNMAN06_Action.java     1.0 2012/10/30		
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
package com.cherry.jn.man.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.jn.common.interfaces.BINOLJNCOM03_IF;
import com.cherry.jn.man.form.BINOLJNMAN06_Form;
import com.cherry.jn.man.interfaces.BINOLJNMAN06_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 积分规则配置添加 Action
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN06_Action extends BaseAction implements
ModelDriven<BINOLJNMAN06_Form>{
	private static Logger logger = LoggerFactory
			.getLogger(BINOLJNMAN06_Action.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -3572916272538990786L;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNCOM03_IF binoljncom03IF;
	@Resource
	private BINOLJNMAN06_IF binoljnman06IF;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	@Resource
	private CodeTable codeTable;
	
	/** 参数FORM */
	private BINOLJNMAN06_Form form = new BINOLJNMAN06_Form();
	
	@Override
	public BINOLJNMAN06_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String init() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();
		map.put("brandInfoId", form.getBrandInfoId());
		// 品牌信息
		Map<String, Object> brandInfo = new HashMap<String, Object>();
		// 品牌ID
		brandInfo.put("brandInfoId", form.getBrandInfoId());
		// 品牌名称
		brandInfo.put("brandName", binOLCM05_BL.getBrandName(map));
		brandInfoList = new ArrayList<Map<String, Object>>();
		brandInfoList.add(brandInfo);
		form.setBrandInfoList(brandInfoList);
		clubList = binOLCM05_BL.getClubList(map);
		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
			// 会员俱乐部ID
			map.put("memberClubId", form.getMemberClubId());
		} else if (null != clubList && !clubList.isEmpty()) {
			// 会员俱乐部ID
			map.put("memberClubId", clubList.get(0).get("memberClubId"));
		}
		// 会员活动类型：积分
		map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
		// 积分规则类型
		map.put("pointRuleType", "1");
		// 一般规则列表
		List<Map<String, Object>> unusedList = new ArrayList<Map<String, Object>>();
		// 一般规则列表
		List<Map<String, Object>> generalRuleList = new ArrayList<Map<String, Object>>();
		// 取得规则列表
		List<Map<String, Object>> ruleList = binoljncom03IF.getCampRuleList(map);
		if (null != ruleList && !ruleList.isEmpty()) {
			unusedList.addAll(ruleList);
			generalRuleList.addAll(ruleList);
		}
		// 积分规则类型
		map.put("pointRuleType", "4");
		// 取得默认积分规则
		List<Map<String, Object>> deftRuleList = binoljncom03IF.getCampRuleList(map);
		if (null != deftRuleList && !deftRuleList.isEmpty()) {
			// 默认规则
			form.setDeftRuleInfo(deftRuleList.get(0));
			generalRuleList.add(deftRuleList.get(0));
		}
		form.setGeneralRuleList(generalRuleList);
		// 积分规则类型
		map.put("pointRuleType", "3");
		// 取得组合规则
		List<Map<String, Object>> combRuleList = binoljncom03IF.getCampRuleList(map);
		List<Map<String, Object>> newCombRuleList = new ArrayList<Map<String, Object>>();
		if (null != combRuleList && !combRuleList.isEmpty()) {
			for (Map<String, Object> allCombRule : combRuleList) {
				String ruleDetail = (String) allCombRule.get("ruleDetail");
				if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
					Map<String, Object> combMap = (Map<String, Object>) JSONUtil.deserialize(ruleDetail);
					if (null != combMap && !combMap.isEmpty()) {
						Map<String, Object> combRuleMap = new HashMap<String, Object>();
						combRuleMap.putAll(combMap);
						// 活动ID
						combRuleMap.put("campaignId", String.valueOf(allCombRule.get("campaignId")));
						combRuleMap.put("strategy", "1");
						// 组内规则
						List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) combMap.get("geneRules");
						if (null != geneRuleList && !geneRuleList.isEmpty()) {
							combRuleMap.put("geneRulesStr", JSONUtil.serialize(geneRuleList));
						}
						newCombRuleList.add(combRuleMap);
					}
				}
			}
			if (!newCombRuleList.isEmpty()) {
				unusedList.addAll(newCombRuleList);
			}
		}
		form.setUnusedList(unusedList);
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 新建组合规则画面
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String addComb() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();
		map.put("brandInfoId", form.getBrandInfoId());
		// 品牌信息
		Map<String, Object> brandInfo = new HashMap<String, Object>();
		// 品牌ID
		brandInfo.put("brandInfoId", form.getBrandInfoId());
		// 品牌名称
		brandInfo.put("brandName", binOLCM05_BL.getBrandName(map));
		brandInfoList = new ArrayList<Map<String, Object>>();
		brandInfoList.add(brandInfo);
		form.setBrandInfoList(brandInfoList);
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 编辑组合规则画面
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String editComb() throws Exception{
    	combPageParams("0");
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 组合规则详细画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String detailComb() throws Exception{
    	combPageParams("1");
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 获取组合规则页面参数
	 * </p>
	 * 
	 * 
	 * @param kbn 页面区分
	 * @throws Exception 
	 * 
	 */
    private void combPageParams(String kbn) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
    	// 规则ID
    	map.put("campaignId", form.getCampaignId());
    	// 取得规则信息
    	Map<String, Object> combRuleInfo = binoljncom03IF.getCampRuleInfo(map);
    	if (null != combRuleInfo && !combRuleInfo.isEmpty()) {
    		String brandInfoId = String.valueOf(combRuleInfo.get("brandInfoId"));
    		form.setBrandInfoId(brandInfoId);
    		// 品牌ID
    		map.put("brandInfoId", brandInfoId);
    		List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();
    		// 品牌信息
    		Map<String, Object> brandInfo = new HashMap<String, Object>();
    		// 品牌ID
    		brandInfo.put("brandInfoId", brandInfoId);
    		// 品牌名称
    		String brandName = binOLCM05_BL.getBrandName(map);
    		brandInfo.put("brandName", brandName);
    		brandInfoList = new ArrayList<Map<String, Object>>();
    		brandInfoList.add(brandInfo);
    		form.setBrandInfoList(brandInfoList);
    		
    		// 组织ID
    		map.put("organizationInfoId", combRuleInfo.get("organizationInfoId"));
    		// 会员活动类型：积分
        	map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
        	// 积分规则类型:普通规则
    		map.put("pointRuleType", "1");
    		// 一般规则列表
    		List<Map<String, Object>> generalRuleList = new ArrayList<Map<String, Object>>();
    		// 取得规则列表
    		List<Map<String, Object>> ruleList = binoljncom03IF.getCampRuleList(map);
    		if (null != ruleList && !ruleList.isEmpty()) {
    			generalRuleList.addAll(ruleList);
    		}
    		// 积分规则类型:默认规则
    		map.put("pointRuleType", "4");
    		// 取得默认积分规则
    		List<Map<String, Object>> deftRuleList = binoljncom03IF.getCampRuleList(map);
    		if (null != deftRuleList && !deftRuleList.isEmpty()) {
    			generalRuleList.add(deftRuleList.get(0));
    		}
    		// 规则内容
    		String ruleDetail = (String) combRuleInfo.get("ruleDetail");
			if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
				Map<String, Object> combMap = (Map<String, Object>) JSONUtil.deserialize(ruleDetail);
				if (null != combMap && !combMap.isEmpty()) {
					// 组内规则
					List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) combMap.get("geneRules");
					if (null != geneRuleList) {
						for (Map<String, Object> geneRule : geneRuleList) {
							String campId = (String) geneRule.get("campaignId");
							Map<String, Object> ruleInfo = binoljncom03IF.findRuleById(campId, generalRuleList);
							if (null != ruleInfo) {
								// 规则名称
								geneRule.put("campaignName", ruleInfo.get("campaignName"));
							}
						}
					}
					// 品牌名称
					combMap.put("brandName", brandName);
					// 更新时间
					combMap.put("campUpdateTime", combRuleInfo.get("campUpdateTime"));
					// 更新次数
					combMap.put("campModifyCount", combRuleInfo.get("campModifyCount"));
					form.setCombRuleInfo(combMap);
				}
			} 
    	}
    }
    
    /**
	 * <p>
	 * 编辑画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String editInit() throws Exception{
    	// 编辑画面
    	pageParams("0");
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 详细画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String detailInit() throws Exception{
    	// 详细画面
    	pageParams("1");
    	return SUCCESS;
    }
    
    /**
	 * <p>
	 * 获取页面参数
	 * </p>
	 * 
	 * 
	 * @param kbn 页面区分
	 * @throws Exception 
	 * 
	 */
    private void pageParams(String kbn) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
    	// 会员活动类型：积分
    	map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
    	// 配置ID
    	map.put("campaignGrpId", form.getCampaignGrpId());
    	// 取得规则配置信息
    	Map<String, Object> ruleConfInfo = binoljncom03IF.getRuleConfigInfo(map);
    	if (null != ruleConfInfo && !ruleConfInfo.isEmpty()) {
    		map.putAll(ruleConfInfo);
    		List<Map<String, Object>> brandInfoList = new ArrayList<Map<String, Object>>();
    		// 品牌信息
    		Map<String, Object> brandInfo = new HashMap<String, Object>();
    		// 品牌ID
    		brandInfo.put("brandInfoId", map.get("brandInfoId"));
    		// 品牌名称
    		String brandName = (String) map.get("brandName");
    		brandInfo.put("brandName", map.get("brandName"));
    		brandInfoList = new ArrayList<Map<String, Object>>();
    		brandInfoList.add(brandInfo);
    		form.setBrandInfoList(brandInfoList);
    		if (null != map.get("memberClubId")) {
	    		// 会员俱乐部名称
	    		String clubName = (String) binOLCM05_BL.getClubName(map);
	    		if ("1".equals(kbn)) {
	    			form.setBrandName(brandName);
	    			form.setMemberClubName(clubName);
	    		} else {
	    			Map<String, Object> clubMap = new HashMap<String, Object>();
					clubMap.put("memberClubId", map.get("memberClubId"));
					// 取得品牌名
					clubMap.put("clubName", binOLCM05_BL.getClubName(map));
					clubList = new ArrayList<Map<String, Object>>();
					clubList.add(clubMap);
	    		}
    		} else {
    			if ("1".equals(kbn)) {
        			form.setBrandName(brandName);
        		}
    		}
    		// 配置名称
    		form.setGroupName((String) map.get("groupName"));
    		// 描述信息
    		form.setDescriptionDtl((String) map.get("descriptionDtl"));
    		if ("0".equals(kbn)) {
	    		// 更新时间
	    		form.setGrpUpdateTime((String) map.get("grpUpdateTime"));
	    		// 更新回数
	    		form.setGrpModifyCount(String.valueOf(map.get("grpModifyCount")));
	    		// 有效区分
	    		form.setGroupValidFlag((String) map.get("groupValidFlag"));
    		}
    		// 执行策略
    		if (null != map.get("execType")) {
    			form.setExecType(String.valueOf(map.get("execType")));
    		}
    		// 积分上限
    		Map<String, Object> pointLimit = null;
    		// 宿主规则
    		List<Map<String, Object>> mainRuleList = null;
    		// 配置内容
    		String grpRuleDetail = (String) map.get("ruleDetail");
    		if (!CherryChecker.isNullOrEmpty(grpRuleDetail)) {
    			Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpRuleDetail);
    			if (null != detailInfo && !detailInfo.isEmpty()) {
    				pointLimit = (Map<String, Object>) detailInfo.get("limitInfo");
        			mainRuleList = (List<Map<String, Object>>) detailInfo.get("mainRules");
        			form.setPointLimitInfo(pointLimit);
        			// 配置参数集合
        			Map<String, Object> gpInfo = (Map<String, Object>) detailInfo.get("gpInfo");
        			// 支付方式区分
        			String payTypeKbn = "0";
					String[] pTypes = null;
        			if (null != gpInfo) {
        				if (gpInfo.containsKey("pTypes")) {
        					payTypeKbn = "1";
        					List<String> pTypeList = (List<String>) gpInfo.get("pTypes");
        					pTypes = pTypeList.toArray(new String[]{});
        				}
        				if (gpInfo.containsKey("zkPrt")) {
        					form.setZkPrt((String) gpInfo.get("zkPrt"));
        				}
        			}
        			// 选择的支付方式
					List<Map<String, Object>> payTypeList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> codeList = codeTable.getCodes("1175");
        			if (null != codeList) {
						boolean allCheck = true;
						for (Map<String, Object> codeMap : codeList) {
							Map<String, Object> payTypeMap = new HashMap<String, Object>();
							payTypeMap.putAll(codeMap);
							String key = (String) codeMap.get("CodeKey");
							// 选择的支付方式
							if (null != pTypes && ConvertUtil.isContain(pTypes, key)) {
								payTypeMap.put("ckFlag", "1");
							} else {
								allCheck = false;
							}
							payTypeList.add(payTypeMap);
						}
						if (allCheck) {
							// 全选标识
							form.setPayTypeCodeALL("1");
						}
						
					}
        			form.setPayTypeList(payTypeList);
        			form.setPayTypeKbn(payTypeKbn);
    			}
    		}
    		// 积分规则类型:组合规则
    		map.put("pointRuleType", "3");
    		// 所有规则列表
    		List<Map<String, Object>> allRuleList = new ArrayList<Map<String, Object>>();
    		// 取得规则列表
    		List<Map<String, Object>> allCombRuleList = binoljncom03IF.getCampRuleList(map);
    		// 取得规则列表
    		List<Map<String, Object>> combRuleList = new ArrayList<Map<String, Object>>();
    		if (null != allCombRuleList && !allCombRuleList.isEmpty()) {
    			for (Map<String, Object> allCombRule : allCombRuleList) {
    				String ruleDetail = (String) allCombRule.get("ruleDetail");
    				if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
    					Map<String, Object> combMap = (Map<String, Object>) JSONUtil.deserialize(ruleDetail);
    					if (null != combMap && !combMap.isEmpty()) {
    						Map<String, Object> combRuleMap = new HashMap<String, Object>();
    						combRuleMap.putAll(combMap);
    						// 活动ID
    						combRuleMap.put("campaignId", String.valueOf(allCombRule.get("campaignId")));
    						combRuleMap.put("strategy", "1");
    						// 组内规则
    						List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) combMap.get("geneRules");
    						if (null != geneRuleList && !geneRuleList.isEmpty()) {
    							combRuleMap.put("geneRulesStr", JSONUtil.serialize(geneRuleList));
    						}
    						combRuleList.add(combRuleMap);
    					}
    				}
    			}
    		}
    		if (!combRuleList.isEmpty()) {
    			allRuleList.addAll(combRuleList);
    		}
    		// 积分规则类型:普通规则
    		map.put("pointRuleType", "1");
    		// 一般规则列表
    		List<Map<String, Object>> generalRuleList = new ArrayList<Map<String, Object>>();
    		
    		// 取得规则列表
    		List<Map<String, Object>> ruleList = binoljncom03IF.getCampRuleList(map);
    		if (null != ruleList && !ruleList.isEmpty()) {
    			generalRuleList.addAll(ruleList);
    			allRuleList.addAll(ruleList);
    		}
    		// 积分规则类型:默认规则
    		map.put("pointRuleType", "4");
    		// 取得默认积分规则
    		List<Map<String, Object>> deftRuleList = binoljncom03IF.getCampRuleList(map);
    		if (null != deftRuleList && !deftRuleList.isEmpty()) {
    			// 默认规则
    			form.setDeftRuleInfo(deftRuleList.get(0));
    			generalRuleList.add(deftRuleList.get(0));
    		}
    		form.setGeneralRuleList(generalRuleList);
    		// 积分规则类型 : 附属规则
    		map.put("pointRuleType", "2");
    		// 取得附属规则
    		List<Map<String, Object>> allExtraRuleList = binoljncom03IF.getCampRuleList(map);
    		if (!generalRuleList.isEmpty()) {
    			if (null != mainRuleList) {
					for (Map<String, Object> generalRule : generalRuleList) {
						// 普通规则ID
						String generalCampId = String.valueOf(generalRule.get("campaignId"));
						Map<String, Object> mainRuleMap = null;
						for (int i = 0; i < mainRuleList.size(); i++) {
							Map<String, Object> mainRule = mainRuleList.get(i);
        					// 主规则ID
        					String mainCampId = (String) mainRule.get("campaignId");
        					if (generalCampId.equals(mainCampId)) {
        						mainRuleMap = mainRule;
        						mainRuleList.remove(i);
        						break;
        					}
						}
						// 将附属规则信息添加到主规则中
						if (null != mainRuleMap) {
							List<Map<String, Object>> extraRuleList = new ArrayList<Map<String, Object>>();
							List<Map<String, Object>> extraList = (List<Map<String, Object>>) mainRuleMap.get("extraRules");
							if (null != extraList) {
								for (Map<String, Object> extraMap : extraList) {
									// 规则ID
					    			String campRuleId = (String) extraMap.get("campaignId");
					    			// 通过规则ID获取附属规则信息
					    			Map<String, Object> extraInfo = binoljncom03IF.findRuleById(campRuleId, allExtraRuleList);
					    			if (null != extraInfo) {
					    				extraRuleList.add(extraInfo);
					    			}
								}
							}
							if (!extraRuleList.isEmpty()) {
								generalRule.put("extraRuleList", extraRuleList);
							}
						}
					}
    			}
    		}
    		// 优先级
    		String priority = (String) ruleConfInfo.get("priorityRuleDetail");
    		if (!CherryChecker.isNullOrEmpty(priority)) {
    			List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priority);
    			if (null != priorityList) {
    				// 已启用规则列表
    				List<Map<String, Object>> usedRuleList = new ArrayList<Map<String, Object>>();
    				for (Map<String, Object> priorityMap : priorityList) {
    					// 默认区分
    					String defaultFlag = (String) priorityMap.get("defaultFlag");
    					if ("1".equals(defaultFlag)) {
    						continue;
    					}
    					// 组合规则执行策略
    					String combType = (String) priorityMap.get("combType");
    					// 规则ID
    					String campaignId = (String) priorityMap.get("campaignId");
    					// 匹配成功时动作
    					String afterMatch = (String) priorityMap.get("afterMatch");
    					// 通过规则ID获取规则信息
		    			Map<String, Object> ruleInfo = null;
    					// 一般规则
    					if (CherryChecker.isNullOrEmpty(combType)) {
    						// 通过规则ID获取一般规则信息
			    			ruleInfo = binoljncom03IF.findRuleById(campaignId, generalRuleList);
    					} else {
    						// 通过规则ID获取组合规则信息
			    			ruleInfo = binoljncom03IF.findRuleById(campaignId, combRuleList);
    					}
    					if (null != ruleInfo) {
    						ruleInfo.put("afterMatch", afterMatch);
    						usedRuleList.add(ruleInfo);
    					}
    				}
    				if (!usedRuleList.isEmpty()) {
    					form.setUsedRuleList(usedRuleList);
    					allRuleList.removeAll(usedRuleList);
    				}
    			}
    		}
    		form.setUnusedList(allRuleList);
    	}
    }
    
    /**
   	 * 验证组合规则信息
   	 * 
   	 * @param map
   	 * 			组合规则内容
   	 * @return boolean
   	 * 			验证结果
        * @throws Exception 
   	 * 
   	 */
   	private boolean validateComb(Map<String, Object> map) throws Exception {
   		// 验证结果
   		boolean isCorrect = true;
		// 组合名称
		String combRuleName = (String) map.get("campaignName");
		// 组合名称必须入力验证
		if(CherryChecker.isNullOrEmpty(combRuleName)) {
			this.addFieldError("campaignName", getText("ECM00009", new String[]{getText("PCP00037")}));
			isCorrect = false;
		} else if (combRuleName.length() > 50) {
			// 组合名称不能超过10位验证
			this.addFieldError("campaignName", getText("ECM00020", new String[] {
					getText("PCP00037"), "50" }));
			isCorrect = false;
		}
		List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) map.get("geneRules");
		if (null == geneRuleList || geneRuleList.isEmpty() || geneRuleList.size() == 1) {
			// 必须选择两个或两个以上规则
			this.addFieldError("addJon", getText("ECP00026"));
			isCorrect = false;
		}
		return isCorrect;
   	}
   	
   	
   	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public String save() throws Exception{
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
    	// 支付方式区分(选取部分支付方式)
    	if ("1".equals(form.getPayTypeKbn())) {
    		// 未选择一种支付方式
    		if (CherryChecker.isNullOrEmpty(form.getPayTypeCodes(), true))  {
    			// 至少选择一种支付方式
    			this.addFieldError(
    					"payTypeCodeALL", getText("EJN00001"));
    			return CherryConstants.GLOBAL_ACCTION_RESULT;
    		}
    		String[] pTypes = form.getPayTypeCodes().split(",");
    		map.put("pTypes", pTypes);
    	}
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		try {
			// 保存规则配置
			binoljnman06IF.tran_saveConfig(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	private boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		// 配置名称
		String groupName = form.getGroupName();
		//  配置名称必须入力验证
		if (CherryChecker.isNullOrEmpty(groupName, true)) {
			this.addFieldError("groupName",
					getText("ECM00009", new String[] { getText("PJN00001") }));
			isCorrect = false;
		} else if (groupName.length() > 50) {
			// 配置名称不能超过50位验证
			this.addFieldError(
					"groupName",
					getText("ECM00020", new String[] { getText("PJN00001"),
							"50" }));
			isCorrect = false;
		}
		// 配置描述
		String descriptionDtl = form.getDescriptionDtl();
		if (!CherryChecker.isNullOrEmpty(descriptionDtl, true) 
				&& descriptionDtl.length() > 300) {
			// 配置描述不能超过300位验证
			this.addFieldError(
					"descriptionDtl",
					getText("ECM00020", new String[] { getText("PJN00002"),
							"300" }));
			isCorrect = false;
		}
		// 积分上限
		String limit = form.getLimit();
		if (!CherryChecker.isNullOrEmpty(limit, true)) {
			Map<String, Object> pointLimit = (Map<String, Object>) JSONUtil.deserialize(limit);
			// 每单上限
			if ("1".equals(pointLimit.get("oneLimit"))) {
				// 每单上限区分
				String onePoint = (String) pointLimit.get("onePoint");
				// 每单积分上限,剩余不计积分
				if ("2".equals(onePoint)) {
					// 积分上限值
					String maxPoint = (String) pointLimit.get("maxPoint");
					// 积分上限值必须入力验证
					if(CherryChecker.isNullOrEmpty(maxPoint, true)){
						this.addFieldError("maxPoint", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })); 
						isCorrect = false;
						// 积分上限值浮点数验证
					} else if(!CherryChecker.isFloatValid(maxPoint, 10, 2)){
						this.addFieldError("maxPoint", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })); 
						isCorrect = false;
					}
					// 每单积分上限,剩余计算积分
				} else {
					// 积分上限值
					String maxGivePoint = (String) pointLimit.get("maxGivePoint");
					// 积分倍数
					String mulGive = (String) pointLimit.get("mulGive");
					// 积分上限值必须入力验证
					if(CherryChecker.isNullOrEmpty(maxGivePoint, true)){
						this.addFieldError("maxGivePoint", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })); 
						isCorrect = false;
						// 积分上限值浮点数验证
					} else if(!CherryChecker.isFloatValid(maxGivePoint, 10, 2)){
						this.addFieldError("maxGivePoint", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })); 
						isCorrect = false;
					}
					// 积分倍数必须入力验证
					if(CherryChecker.isNullOrEmpty(mulGive, true)){
						this.addFieldError("mulGive", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })); 
						isCorrect = false;
						// 积分倍数浮点数验证
					} else if(!CherryChecker.isFloatValid(mulGive, 10, 2)){
						this.addFieldError("mulGive", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })); 
						isCorrect = false;
					}
				}
			}
			// 每天上限
			if ("1".equals(pointLimit.get("allLimit"))) {
				// 每单上限区分
				String allPoint = (String) pointLimit.get("allPoint");
				// 每单积分上限,剩余不计积分
				if ("2".equals(allPoint)) {
					// 积分上限值
					String allLimitPoint = (String) pointLimit.get("allLimitPoint");
					// 积分上限值必须入力验证
					if(CherryChecker.isNullOrEmpty(allLimitPoint, true)){
						this.addFieldError("allLimitPoint", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })); 
						isCorrect = false;
						// 积分上限值浮点数验证
					} else if(!CherryChecker.isFloatValid(allLimitPoint, 10, 2)){
						this.addFieldError("allLimitPoint", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })); 
						isCorrect = false;
					}
					// 每单积分上限,剩余计算积分
				} else {
					// 积分上限值
					String maxAllGivePoint = (String) pointLimit.get("maxAllGivePoint");
					// 积分倍数
					String mulAllGive = (String) pointLimit.get("mulAllGive");
					// 积分上限值必须入力验证
					if(CherryChecker.isNullOrEmpty(maxAllGivePoint, true)){
						this.addFieldError("maxAllGivePoint", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00030") })); 
						isCorrect = false;
						// 积分上限值浮点数验证
					} else if(!CherryChecker.isFloatValid(maxAllGivePoint, 10, 2)){
						this.addFieldError("maxAllGivePoint", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00030"), "10", "2" })); 
						isCorrect = false;
					}
					// 积分倍数必须入力验证
					if(CherryChecker.isNullOrEmpty(mulAllGive, true)){
						this.addFieldError("mulAllGive", getText("ECM00009", new String[] { PropertiesUtil.getText("PMB00026") })); 
						isCorrect = false;
						// 积分倍数浮点数验证
					} else if(!CherryChecker.isFloatValid(mulAllGive, 10, 2)){
						this.addFieldError("mulAllGive", getText("ECM00024", new String[] { PropertiesUtil.getText("PMB00026"), "10", "2" })); 
						isCorrect = false;
					}
				}
			}
		}
		return isCorrect;
	}
    
	/**
	 * <p>
	 * 保存组合规则
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public String saveComb() throws Exception{
    	// 提交的组合规则内容
    	String combInfo = form.getCombInfo();
    	Map<String, Object> map = null;
    	if (!CherryChecker.isNullOrEmpty(combInfo)) {
    		map = (Map<String, Object>) JSONUtil.deserialize(combInfo);
    		Map<String, Object> combMap = new HashMap<String, Object>();
    		combMap.putAll(map);
    		combMap.remove("brandInfoId");
    		combMap.remove("campUpdateTime");
    		combMap.remove("campModifyCount");
    		combMap.remove("campaignId");
    		combInfo = JSONUtil.serialize(combMap);
    	}
    	if (null == map) {
    		map = new HashMap<String, Object>();
    	}
    	// 验证提交的参数
		if (!validateComb(map)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		map.put("ruleDetail", combInfo);
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 创建者
		map.put("campaignSetBy", userInfo.getBIN_UserID());
		// 创建者
		map.put("campaignLeader", userInfo.getBIN_UserID());
		try {
			// 保存组合规则
			binoljnman06IF.tran_saveComb(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /**
	 * 保存规则匹配顺序
	 * 
	 * @param 
	 * 
	 * @throws Exception 
	 */
	public void savePriority() throws Exception{
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		String message = getText("ICM00002");
		try {
			Map<String, Object> configInfo = binoljncom03IF.getRuleConfigInfo(map);
			if (null != configInfo && !configInfo.isEmpty()) {
				// 优先级内容
				map.put("priority", configInfo.get("priorityRuleDetail"));
				// 配置内容
				map.put("grpRuleDetail", configInfo.get("ruleDetail"));
				// 更新时间
				map.put("grpUpdateTime", configInfo.get("grpUpdateTime"));
				// 更新次数
				map.put("grpModifyCount", configInfo.get("grpModifyCount"));
				// 执行策略
				map.put("execType", configInfo.get("execType"));
				// 品牌ID
				map.put("brandInfoId", configInfo.get("brandInfoId"));
				// 组织ID
				map.put("organizationInfoId", configInfo.get("organizationInfoId"));
				// 会员活动类型
				map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
				// 有效区分
				map.put("groupValidFlag", configInfo.get("groupValidFlag"));
			}
			// 保存规则匹配顺序
			binoljnman06IF.tran_savePriority(map);
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			message = getText("ECM00005");
		}
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, message);
	}
	/**
	 * <p>
	 * 规则关系查看画面
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String relatView() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
    	// 规则ID
		String ruleId = form.getCampaignId();
    	map.put("campaignId", ruleId);
    	// 取得规则信息
    	Map<String, Object> ruleInfo = binoljncom03IF.getCampRuleInfo(map);
    	if (null != ruleInfo && !ruleInfo.isEmpty()) {
    		// 品牌ID
    		String brandInfoId = String.valueOf(ruleInfo.get("brandInfoId"));
    		map.put("brandInfoId", brandInfoId);
    		// 品牌名称
    		String brandName = binOLCM05_BL.getBrandName(map);
    		// 规则关系信息
    		Map<String, Object> relatMap = new HashMap<String, Object>();
    		relatMap.put("brandName", brandName);
    		// 规则名称
    		relatMap.put("campaignName", ruleInfo.get("campaignName"));
    		// 规则类型
    		String prType = (String) ruleInfo.get("prType");
    		relatMap.put("prType", prType);
    		// 会员活动类型：积分
    		map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
    		// 取得规则配置列表
    		List<Map<String, Object>> ruleConfList = binoljnman06IF.getRuleConfList(map);
    		if (null != ruleConfList && !ruleConfList.isEmpty()) {
    			// 非附属规则
    			if (!"2".equals(prType)) {
    				// 包含的附属规则列表
    				List<Map<String, Object>> extraRuleList = new ArrayList<Map<String, Object>>();
    				// 积分规则类型 : 附属规则
    	    		map.put("pointRuleType", "2");
    	    		// 取得附属规则
    	    		List<Map<String, Object>> allExtraRuleList = binoljncom03IF.getCampRuleList(map);
    				for (Map<String, Object> ruleConfMap : ruleConfList) {
    					// 配置内容
    		    		String grpRuleDetail = (String) ruleConfMap.get("grpRuleDetail");
    		    		if (!CherryChecker.isNullOrEmpty(grpRuleDetail)) {
    		    			// 配置名称
    		    			String groupName = (String) ruleConfMap.get("groupName");
    		    			Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpRuleDetail);
    		    			if (null != detailInfo && !detailInfo.isEmpty()) {
    		    				// 宿主规则列表
    		    				List<Map<String, Object>> mainRuleList = (List<Map<String, Object>>) detailInfo.get("mainRules");
    		    				Map<String, Object> mainRuleMap = null;
    		    				if (null != mainRuleList) {
	    							for (int i = 0; i < mainRuleList.size(); i++) {
	    								Map<String, Object> mainRule = mainRuleList.get(i);
	    	        					// 主规则ID
	    	        					String mainCampId = (String) mainRule.get("campaignId");
	    	        					if (ruleId.equals(mainCampId)) {
	    	        						mainRuleMap = mainRule;
	    	        						break;
	    	        					}
	    							}
    		    				}
    							// 将附属规则信息添加到主规则中
    							if (null != mainRuleMap) {
    								List<Map<String, Object>> extraList = (List<Map<String, Object>>) mainRuleMap.get("extraRules");
    								if (null != extraList) {
    									for (Map<String, Object> extraMap : extraList) {
    										// 规则ID
    						    			String campRuleId = (String) extraMap.get("campaignId");
    						    			// 通过规则ID获取附属规则信息
    						    			Map<String, Object> extraInfo = binoljncom03IF.findRuleById(campRuleId, allExtraRuleList);
    						    			if (null != extraInfo) {
    						    				Map<String, Object> newExtraInfo = new HashMap<String, Object>();
    						    				newExtraInfo.putAll(extraInfo);
    						    				newExtraInfo.put("groupName", groupName);
    						    				extraRuleList.add(newExtraInfo);
    						    			}
    									}
    								}
    							}
    		    			}
    		    		}
    				}
    				if (!extraRuleList.isEmpty()) {
    					// 包含的附属规则列表
    					relatMap.put("extraRuleList", extraRuleList);
    				}
    				// 附属规则
    			} else {
    				// 一般规则列表
    	    		List<Map<String, Object>> generalRuleList = new ArrayList<Map<String, Object>>();
    				// 积分规则类型 : 附属规则
    	    		map.put("pointRuleType", "1");
    	    		// 取得规则列表
    	    		List<Map<String, Object>> ruleList = binoljncom03IF.getCampRuleList(map);
    	    		if (null != ruleList && !ruleList.isEmpty()) {
    	    			generalRuleList.addAll(ruleList);
    	    		}
    	    		// 积分规则类型:默认规则
    	    		map.put("pointRuleType", "4");
    	    		// 取得默认积分规则
    	    		List<Map<String, Object>> deftRuleList = binoljncom03IF.getCampRuleList(map);
    	    		if (null != deftRuleList && !deftRuleList.isEmpty()) {
    	    			generalRuleList.add(deftRuleList.get(0));
    	    		}
    				// 宿主规则列表
    				List<Map<String, Object>> mainRuleList = new ArrayList<Map<String, Object>>();
    				for (Map<String, Object> ruleConfMap : ruleConfList) {
    					// 配置内容
    		    		String grpRuleDetail = (String) ruleConfMap.get("grpRuleDetail");
    		    		if (!CherryChecker.isNullOrEmpty(grpRuleDetail)) {
    		    			// 配置名称
    		    			String groupName = (String) ruleConfMap.get("groupName");
    		    			Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpRuleDetail);
    		    			if (null != detailInfo && !detailInfo.isEmpty()) {
    		    				// 宿主规则列表
    		    				List<Map<String, Object>> mainRules = (List<Map<String, Object>>) detailInfo.get("mainRules");
    		    				if (null != mainRules) {
	    		    				// 附属规则列表
	    		    				for (Map<String, Object> mainRule : mainRules) {
	    		    					List<Map<String, Object>> extraList = (List<Map<String, Object>>) mainRule.get("extraRules");
	    								if (null != extraList) {
	    									boolean flag = false;
	    									// 主规则ID
	        	        					String mainCampId = (String) mainRule.get("campaignId");
	    									for (Map<String, Object> extraMap : extraList) {
	    										// 规则ID
	    						    			String campRuleId = (String) extraMap.get("campaignId");
	    						    			if (ruleId.equals(campRuleId)) {
	    						    				flag = true;
	    	    	        						break;
	    	    	        					}
	    									}
	    									if (flag) {
	    										// 通过规则ID获取宿主规则信息
	    						    			Map<String, Object> mainInfo = binoljncom03IF.findRuleById(mainCampId, generalRuleList);
	    						    			if (null != mainInfo) {
	    						    				Map<String, Object> newMainInfo = new HashMap<String, Object>();
	    						    				newMainInfo.putAll(mainInfo);
	    						    				newMainInfo.put("groupName", groupName);
	    						    				mainRuleList.add(newMainInfo);
	    						    			}
	    									}
	    								}
	    		    				}
    		    				}
    		    			}
    		    		}
    				}
    				if (!mainRuleList.isEmpty()) {
    					// 宿主规则列表
    					relatMap.put("mainRuleList", mainRuleList);
    				}
    			}
    		}
    		// 非附属规则
			if (!"2".equals(prType)) {
				// 积分规则类型
				map.put("pointRuleType", "3");
				// 取得组合规则
				List<Map<String, Object>> allCombRuleList = binoljncom03IF.getCampRuleList(map);
				if (null != allCombRuleList && !allCombRuleList.isEmpty()) {
					// 所属组合列表
					List<Map<String, Object>> combRuleList = new ArrayList<Map<String, Object>>();
					for (Map<String, Object> allCombRule : allCombRuleList) {
						String ruleDetail = (String) allCombRule.get("ruleDetail");
						if (!CherryChecker.isNullOrEmpty(ruleDetail)) {
							Map<String, Object> combMap = (Map<String, Object>) JSONUtil.deserialize(ruleDetail);
							if (null != combMap && !combMap.isEmpty()) {
								// 组内规则
								List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) combMap.get("geneRules");
								if (null != geneRuleList) {
									boolean flag = false;
									for (Map<String, Object> geneRule : geneRuleList) {
										String campId = (String) geneRule.get("campaignId");
										if (ruleId.equals(campId)) {
											flag = true;
	    	        						break;
	    	        					}
									}
									if (flag) {
										Map<String, Object> combInfo = new HashMap<String, Object>();
										// 规则名称
										combInfo.put("campaignName", allCombRule.get("campaignName"));
										combRuleList.add(combInfo);
									}
								}
							}
						}
					}
					if (!combRuleList.isEmpty()) {
    					// 所属组合规则列表
    					relatMap.put("combRuleList", combRuleList);
    				}
				}
			}
			form.setRelatInfo(relatMap);
    	}
    	return SUCCESS;
    }
}
