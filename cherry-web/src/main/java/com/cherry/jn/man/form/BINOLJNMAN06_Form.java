/*
 * @(#)BINOLJNMAN06_Form.java     1.0 2012/10/30
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
package com.cherry.jn.man.form;

import java.util.List;
import java.util.Map;

/**
 * 积分规则配置添加 Form
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN06_Form {
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 会员俱乐部名称 */
	private String memberClubName;
	
	/** 未启用规则列表 */
	private List<Map<String, Object>> unusedList;
	
	/** 默认规则 */
	private Map<String, Object> deftRuleInfo;
	
	/** 一般规则列表 */
	private List<Map<String, Object>> generalRuleList;
	
	/** 已启用规则列表 */
	private List<Map<String, Object>> usedRuleList;
	
	/** 组合规则列表 */
	private List<Map<String, Object>> combRuleList;
	
	/** 组合规则信息 */
	private String combRules;
	
	/** 配置名称 */
	private String groupName;
	
	/** 配置描述 */
	private String descriptionDtl;
	
	/** 已启用规则 */
	private String usedRules;
	
	/** 默认规则 */
	private String deftRule;
	
	/** 未启用规则 */
	private String unusedRules;
	
	/** 组合规则  */
	private String combInfo;
	
	/**配置ID*/
	private String campaignGrpId;
	
	/**优先级策略*/
	private String execType;
	
	/**更新时间*/
	private String grpUpdateTime;
	
	/**更新次数*/
	private String grpModifyCount;
	
	/** 有效区分  */
	private String groupValidFlag;
	
	/** 规则ID */
	private String campaignId;
	
	/** 组合规则信息 */
	private Map combRuleInfo;
	
	/**匹配顺序*/
	private String prioritySel;
	
	/** 积分上限设置 */
	private String limit;
	
	/** 积分上限信息 */
	private Map<String, Object> pointLimitInfo;
	
	/** 规则关系信息 */
	private Map relatInfo;
	
	/** 支付方式区分 */
	private String payTypeKbn;
	
	/** 全选标识 */
	private String payTypeCodeALL;
	
	/** 支付方式类型 */
	private String payTypeCodes;
	
	/** 折扣产品 */
	private String zkPrt;
	
	/** 选择的支付方式 */
	private List<Map<String, Object>> payTypeList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<Map<String, Object>> getUnusedList() {
		return unusedList;
	}

	public void setUnusedList(List<Map<String, Object>> unusedList) {
		this.unusedList = unusedList;
	}

	public Map<String, Object> getDeftRuleInfo() {
		return deftRuleInfo;
	}

	public void setDeftRuleInfo(Map<String, Object> deftRuleInfo) {
		this.deftRuleInfo = deftRuleInfo;
	}

	public List<Map<String, Object>> getGeneralRuleList() {
		return generalRuleList;
	}

	public void setGeneralRuleList(List<Map<String, Object>> generalRuleList) {
		this.generalRuleList = generalRuleList;
	}

	public String getCombRules() {
		return combRules;
	}

	public void setCombRules(String combRules) {
		this.combRules = combRules;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescriptionDtl() {
		return descriptionDtl;
	}

	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}

	public String getUsedRules() {
		return usedRules;
	}

	public void setUsedRules(String usedRules) {
		this.usedRules = usedRules;
	}

	public String getDeftRule() {
		return deftRule;
	}

	public void setDeftRule(String deftRule) {
		this.deftRule = deftRule;
	}

	public String getCombInfo() {
		return combInfo;
	}

	public void setCombInfo(String combInfo) {
		this.combInfo = combInfo;
	}

	public String getCampaignGrpId() {
		return campaignGrpId;
	}

	public void setCampaignGrpId(String campaignGrpId) {
		this.campaignGrpId = campaignGrpId;
	}

	public String getExecType() {
		return execType;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public String getUnusedRules() {
		return unusedRules;
	}

	public void setUnusedRules(String unusedRules) {
		this.unusedRules = unusedRules;
	}

	public List<Map<String, Object>> getUsedRuleList() {
		return usedRuleList;
	}

	public void setUsedRuleList(List<Map<String, Object>> usedRuleList) {
		this.usedRuleList = usedRuleList;
	}

	public List<Map<String, Object>> getCombRuleList() {
		return combRuleList;
	}

	public void setCombRuleList(List<Map<String, Object>> combRuleList) {
		this.combRuleList = combRuleList;
	}

	public String getGrpUpdateTime() {
		return grpUpdateTime;
	}

	public void setGrpUpdateTime(String grpUpdateTime) {
		this.grpUpdateTime = grpUpdateTime;
	}

	public String getGrpModifyCount() {
		return grpModifyCount;
	}

	public void setGrpModifyCount(String grpModifyCount) {
		this.grpModifyCount = grpModifyCount;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public Map getCombRuleInfo() {
		return combRuleInfo;
	}

	public void setCombRuleInfo(Map combRuleInfo) {
		this.combRuleInfo = combRuleInfo;
	}

	public String getGroupValidFlag() {
		return groupValidFlag;
	}

	public void setGroupValidFlag(String groupValidFlag) {
		this.groupValidFlag = groupValidFlag;
	}

	public String getPrioritySel() {
		return prioritySel;
	}

	public void setPrioritySel(String prioritySel) {
		this.prioritySel = prioritySel;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public Map<String, Object> getPointLimitInfo() {
		return pointLimitInfo;
	}

	public void setPointLimitInfo(Map<String, Object> pointLimitInfo) {
		this.pointLimitInfo = pointLimitInfo;
	}

	public Map getRelatInfo() {
		return relatInfo;
	}

	public void setRelatInfo(Map relatInfo) {
		this.relatInfo = relatInfo;
	}

	public String getPayTypeKbn() {
		return payTypeKbn;
	}

	public void setPayTypeKbn(String payTypeKbn) {
		this.payTypeKbn = payTypeKbn;
	}
	
	public String getPayTypeCodes() {
		return payTypeCodes;
	}

	public void setPayTypeCodes(String payTypeCodes) {
		this.payTypeCodes = payTypeCodes;
	}

	public List<Map<String, Object>> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<Map<String, Object>> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public String getPayTypeCodeALL() {
		return payTypeCodeALL;
	}

	public void setPayTypeCodeALL(String payTypeCodeALL) {
		this.payTypeCodeALL = payTypeCodeALL;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getMemberClubName() {
		return memberClubName;
	}

	public void setMemberClubName(String memberClubName) {
		this.memberClubName = memberClubName;
	}

	public String getZkPrt() {
		return zkPrt;
	}

	public void setZkPrt(String zkPrt) {
		this.zkPrt = zkPrt;
	}
}
