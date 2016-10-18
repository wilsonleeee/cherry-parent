/*	
 * @(#)CampaignDTO.java     1.0 2011/04/26	
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
package com.cherry.cp.common.dto;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.dto.BaseDTO;

/**
 * 会员活动DTO
 * 
 * @author hub
 * @version 1.0 2010.04.26
 */
public class CampaignDTO extends BaseDTO{
	
	/** 操作区分 */
	private Integer operKbn;
	
	/** 会员活动ID */
	private Integer campaignId;
	
	/** 会员等级ID */
	private String memberLevelId;
	
	/** 规则体详细 */
	private String ruleDetail;
	
	/** 规则体详细 map */
	private Map<String, Object> ruleDetailMap;
	
	/** 规则文件内容 */
	private String ruleFileContent;
	
	/** 规则过滤器 */
	private String ruleFilter;
	
	/** 会员子活动List */
	private List<CampaignRuleDTO> campaignRuleList;
	
	/** 会员子活动 */
	private CampaignRuleDTO campaignRule;
	
	/** 会员活动代号 */
	private String campaignCode;
	
	/** 类型区分 */
	private String campaignTypeFlag;
	
	/** 会员活动类型 */
	private String campaignType;
	
	/** 会员活动组ID */
	private String campaignGrpId;
	
	/** 会员活动名称 */
	private String campaignName;
	
	/** 模板类型 */
	private String templateType;
	
	/** 积分规则类型 */
	private String pointRuleType;
	
	/** 会员等级组ID */
	private String memberLevelGroupId;
	
	/** 所属组织 */
	private String organizationInfoId;
	
	/** 所属品牌 */
	private String brandInfoId;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 会员俱乐部名称 */
	private String clubName;
	
	private String wechatFlag;
	
	/** 活动有效期开始日期 */
	private String campaignFromDate;
	
	/** 活动有效期结束日期 */
	private String campaignToDate;
	
	/** 活动预约开始日期 */
	private String campaignOrderFromDate;
	
	/** 活动预约结束日期 */
	private String campaignOrderToDate;
	
	/** 活动备货开始日期 */
	private String campaignStockFromDate;
	
	/** 活动备货结束日期 */
	private String campaignStockToDate;
	
	/** 活动领用开始日期 */
	private String obtainFromDate;
	
	/** 活动领用结束日期 */
	private String obtainToDate;
	
	/** 活动简略描述 */
	private String descriptionShort;
	
	/** 活动详细描述 */
	private String descriptionDtl;
	
	/** 适用次数 */
	private String times;
	
	/** 活动结果采用策略 */
	private String strategy;
	
	/** 数量上限 */
	private String maxCount;
	
	/** 每个会员的数量上限 */
	private String maxCountPer;
	
	/** 默认活动区分 */
	private String defaultFlag;
	
	/** 活动归属类型 */
	private String campaignBelongType;
	
	/** 经销商ID */
	private String resellerInfoId;
	
	/** 终端可否修改产品区分 */
	private String goodsChangeable;
	
	/** 终端可否修改产品数量区分 */
	private String amountChangeable;
	
	/** 终端可否修改产品价格区分 */
	private String priceChangeable;
	
	/** 是否需要扣减库存区分 */
	private String inventoryChangeable;
	
	/** 活动设定者 */
	private Integer campaignSetBy;
	
	/** 活动设定者名称 */
	private String campaignSetByName;
	
	/** 活动负责人 */
	private Integer campaignLeader;
	
	/** 活动状态 */
	private String state;
	
	/** 规则类型 */
	private String ruleType;
	
	/** 更新日时 */
	private String campUpdateTime;
	
	/** 更新次数 */
	private int campModifyCount;
	
	/** 工作流实例ID */
	private String workFlowId;
	
	/** 当前动作ID */
	private Integer actionId;
	
	/** 当前活动状态 */
	private String saveStatus;
	
	/** 验证码规则 */
	private String verifCodeRule;
	
	/** 保存区分：0:新增  1:更新 */
	private String upKbn;
	
	/** 下发标志区分：0:不下发  1:下发*/
	private String sendFlag;
	
	/** 是否需要预约*/
	private String reseFlag;
	
	/** 活动类型*/
	private String subCampType;
	
	/** 活动验证*/
	private String subCampaignValid;
	
	/** 本地校验规则*/
	private String localValidRule;
	
	/** 是否采集活动获知方式*/
	private String isCollectInfo;
	
	/** 是否需要购买*/
	private String needBuyFlag;
	
	/** 柜台礼品库存管理 */
	private String manageGift;
	
	/** 领用日期参考类型 */
	private String referType;
	
	private String exPointDeadDate;
	
	private String exPointDeductFlag;
	
	private String attrA;
	
	private String attrB;
	
	private String attrC;
	
	private String valA;
	
	private String valB;
	
	private String obtainRule;
	
	private String gotCounter;
	
	private String groupFlag;
	
	private String isShared;
	
	public String getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(String memberLevelId) {
		this.memberLevelId = memberLevelId;
	}

	public Integer getOperKbn() {
		return operKbn;
	}

	public void setOperKbn(Integer operKbn) {
		this.operKbn = operKbn;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignCode() {
		return campaignCode;
	}
	
	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
	}
	
	public Map<String, Object> getRuleDetailMap() {
		return ruleDetailMap;
	}

	public void setRuleDetailMap(Map<String, Object> ruleDetailMap) {
		this.ruleDetailMap = ruleDetailMap;
	}

	public String getRuleFileContent() {
		return ruleFileContent;
	}
	
	public void setRuleFileContent(String ruleFileContent) {
		this.ruleFileContent = ruleFileContent;
	}

	public String getRuleFilter() {
		return ruleFilter;
	}

	public void setRuleFilter(String ruleFilter) {
		this.ruleFilter = ruleFilter;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCampaignGrpId() {
		return campaignGrpId;
	}

	public void setCampaignGrpId(String campaignGrpId) {
		this.campaignGrpId = campaignGrpId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getMemberLevelGroupId() {
		return memberLevelGroupId;
	}

	public void setMemberLevelGroupId(String memberLevelGroupId) {
		this.memberLevelGroupId = memberLevelGroupId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
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
	
	public String getCampaignTypeFlag() {
		return campaignTypeFlag;
	}

	public void setCampaignTypeFlag(String campaignTypeFlag) {
		this.campaignTypeFlag = campaignTypeFlag;
	}

	public String getCampaignOrderFromDate() {
		return campaignOrderFromDate;
	}

	public void setCampaignOrderFromDate(String campaignOrderFromDate) {
		this.campaignOrderFromDate = campaignOrderFromDate;
	}

	public String getCampaignOrderToDate() {
		return campaignOrderToDate;
	}

	public void setCampaignOrderToDate(String campaignOrderToDate) {
		this.campaignOrderToDate = campaignOrderToDate;
	}
	
	public String getObtainFromDate() {
		return obtainFromDate;
	}

	public void setObtainFromDate(String obtainFromDate) {
		this.obtainFromDate = obtainFromDate;
	}

	public String getObtainToDate() {
		return obtainToDate;
	}

	public void setObtainToDate(String obtainToDate) {
		this.obtainToDate = obtainToDate;
	}

	public String getCampaignFromDate() {
		return campaignFromDate;
	}

	public void setCampaignFromDate(String campaignFromDate) {
		this.campaignFromDate = campaignFromDate;
	}

	public String getCampaignToDate() {
		return campaignToDate;
	}

	public void setCampaignToDate(String campaignToDate) {
		this.campaignToDate = campaignToDate;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getDescriptionDtl() {
		return descriptionDtl;
	}

	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}

	public String getMaxCountPer() {
		return maxCountPer;
	}

	public void setMaxCountPer(String maxCountPer) {
		this.maxCountPer = maxCountPer;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getCampaignBelongType() {
		return campaignBelongType;
	}

	public void setCampaignBelongType(String campaignBelongType) {
		this.campaignBelongType = campaignBelongType;
	}

	public String getResellerInfoId() {
		return resellerInfoId;
	}

	public void setResellerInfoId(String resellerInfoId) {
		this.resellerInfoId = resellerInfoId;
	}

	public String getGoodsChangeable() {
		return goodsChangeable;
	}

	public void setGoodsChangeable(String goodsChangeable) {
		this.goodsChangeable = goodsChangeable;
	}

	public String getAmountChangeable() {
		return amountChangeable;
	}

	public void setAmountChangeable(String amountChangeable) {
		this.amountChangeable = amountChangeable;
	}

	public String getPriceChangeable() {
		return priceChangeable;
	}

	public void setPriceChangeable(String priceChangeable) {
		this.priceChangeable = priceChangeable;
	}

	public String getInventoryChangeable() {
		return inventoryChangeable;
	}

	public void setInventoryChangeable(String inventoryChangeable) {
		this.inventoryChangeable = inventoryChangeable;
	}

	public Integer getCampaignSetBy() {
		return campaignSetBy;
	}

	public void setCampaignSetBy(Integer campaignSetBy) {
		this.campaignSetBy = campaignSetBy;
	}
	
	public String getCampaignSetByName() {
		return campaignSetByName;
	}

	public void setCampaignSetByName(String campaignSetByName) {
		this.campaignSetByName = campaignSetByName;
	}

	public Integer getCampaignLeader() {
		return campaignLeader;
	}

	public void setCampaignLeader(Integer campaignLeader) {
		this.campaignLeader = campaignLeader;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCampUpdateTime() {
		return campUpdateTime;
	}

	public void setCampUpdateTime(String campUpdateTime) {
		this.campUpdateTime = campUpdateTime;
	}

	public int getCampModifyCount() {
		return campModifyCount;
	}

	public void setCampModifyCount(int campModifyCount) {
		this.campModifyCount = campModifyCount;
	}

	public List<CampaignRuleDTO> getCampaignRuleList() {
		return campaignRuleList;
	}

	public void setCampaignRuleList(List<CampaignRuleDTO> campaignRuleList) {
		this.campaignRuleList = campaignRuleList;
	}

	public CampaignRuleDTO getCampaignRule() {
		return campaignRule;
	}

	public void setCampaignRule(CampaignRuleDTO campaignRule) {
		this.campaignRule = campaignRule;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(String workFlowId) {
		this.workFlowId = workFlowId;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}

	public String getUpKbn() {
		return upKbn;
	}

	public void setUpKbn(String upKbn) {
		this.upKbn = upKbn;
	}

	public String getPointRuleType() {
		return pointRuleType;
	}

	public void setPointRuleType(String pointRuleType) {
		this.pointRuleType = pointRuleType;
	}
	
	public String getVerifCodeRule() {
		return verifCodeRule;
	}

	public void setVerifCodeRule(String verifCodeRule) {
		this.verifCodeRule = verifCodeRule;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getReseFlag() {
		return reseFlag;
	}

	public void setReseFlag(String reseFlag) {
		this.reseFlag = reseFlag;
	}

	public String getSubCampType() {
		return subCampType;
	}

	public void setSubCampType(String subCampType) {
		this.subCampType = subCampType;
	}

	public String getSubCampaignValid() {
		return subCampaignValid;
	}

	public void setSubCampaignValid(String subCampaignValid) {
		this.subCampaignValid = subCampaignValid;
	}

	public String getLocalValidRule() {
		return localValidRule;
	}

	public void setLocalValidRule(String localValidRule) {
		this.localValidRule = localValidRule;
	}

	public String getIsCollectInfo() {
		return isCollectInfo;
	}

	public void setIsCollectInfo(String isCollectInfo) {
		this.isCollectInfo = isCollectInfo;
	}

	public String getNeedBuyFlag() {
		return needBuyFlag;
	}

	public void setNeedBuyFlag(String needBuyFlag) {
		this.needBuyFlag = needBuyFlag;
	}

	public String getCampaignStockFromDate() {
		return campaignStockFromDate;
	}

	public void setCampaignStockFromDate(String campaignStockFromDate) {
		this.campaignStockFromDate = campaignStockFromDate;
	}

	public String getCampaignStockToDate() {
		return campaignStockToDate;
	}

	public void setCampaignStockToDate(String campaignStockToDate) {
		this.campaignStockToDate = campaignStockToDate;
	}

	public String getReferType() {
		return referType;
	}

	public void setReferType(String referType) {
		this.referType = referType;
	}

	public String getAttrA() {
		return attrA;
	}

	public void setAttrA(String attrA) {
		this.attrA = attrA;
	}

	public String getAttrB() {
		return attrB;
	}

	public void setAttrB(String attrB) {
		this.attrB = attrB;
	}

	public String getAttrC() {
		return attrC;
	}

	public void setAttrC(String attrC) {
		this.attrC = attrC;
	}

	public String getValA() {
		return valA;
	}

	public void setValA(String valA) {
		this.valA = valA;
	}

	public String getValB() {
		return valB;
	}

	public void setValB(String valB) {
		this.valB = valB;
	}

	public String getObtainRule() {
		return obtainRule;
	}

	public void setObtainRule(String obtainRule) {
		this.obtainRule = obtainRule;
	}

	public String getGotCounter() {
		return gotCounter;
	}

	public void setGotCounter(String gotCounter) {
		this.gotCounter = gotCounter;
	}

	public String getExPointDeadDate() {
		return exPointDeadDate;
	}

	public void setExPointDeadDate(String exPointDeadDate) {
		this.exPointDeadDate = exPointDeadDate;
	}

	public String getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}

	public String getExPointDeductFlag() {
		return exPointDeductFlag;
	}

	public void setExPointDeductFlag(String exPointDeductFlag) {
		this.exPointDeductFlag = exPointDeductFlag;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getManageGift() {
		return manageGift;
	}

	public void setManageGift(String manageGift) {
		this.manageGift = manageGift;
	}

	public String getWechatFlag() {
		return wechatFlag;
	}

	public void setWechatFlag(String wechatFlag) {
		this.wechatFlag = wechatFlag;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}
}
