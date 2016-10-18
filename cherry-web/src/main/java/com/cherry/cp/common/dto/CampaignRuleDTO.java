/*	
 * @(#)CampaignRuleDTO.java     1.0 2011/04/26	
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.dto.BaseDTO;

/**
 * 会员子活动DTO
 * 
 * @author hub
 * @version 1.0 2010.04.26
 */
public class CampaignRuleDTO extends BaseDTO{
	
	/** 会员活动ID */
	private Integer campaignId;
	
	/** 会员子活动ID */
	private Integer campaignRuleId;
	
	/** 子活动名称 */
	private String subCampaignName;
	
	/** 会员子活动类型 */
	private String subCampRuleType;
	
	/** 活动验证 */
	private String subCampaignValid;
	
	/** 本地校验规则 */
	private String localValidRule;
	
	/** 是否采集活动获知方式 */
	private String isCollectInfo;

	/** 子活动类型 */
	private String subCampaignType ;
	
	/** 会员子活动连番 */
	private String subCampDetailNo;
	
	/** 子活动代号 */
	private String subCampaignCode;
	
	/** 活动规则文件名 */
	private String ruleFileName;
	
	/** 子活动规则 */
	private String campaignRule;
	
	/** 适用次数 */
	private String times;
	
	/** 数量上限 */
	private String topLimit;
	
	/** 规则描述 */
	private String description;
	
	/** 规则体详细 */
	private String ruleDetail;
	
	/** 规则体详细List */
	private List<Map<String, Object>> ruleDetailList;
	
	/** 规则文件内容 */
	private String ruleFileContent;
	
	/** 规则过滤器 */
	private String ruleFilter;
	
	/** 子活动状态 */
	private String state;
	
	/** 更新前更新日时 */
	private String ruleUpdateTime;
	
	/** 更新前更新次数 */
	private int ruleModifyCount;
	
	/** 会员活动规则条件明细 List */
	private List<CampRuleConditionDTO> campRuleCondList;
	
	/** 会员活动规则结果明细 List */
	private List<CampRuleResultDTO> campRuleResultList;
	
	/** 保存区分：0:新增  1:更新 */
	private String upKbn;
	
	/** 是否记录子活动ID */
	private String needHideId;
	
	private String exPointDeadDate;
	
	/** COUPON数量 */
	private int couponCount;
	
	/** COUPON类型 */
	private String couponType;
	
	/** COUPON批量号码 */
	private String couponBatchNo;
	
	private float priceControl;
	
	private int saleBatchNo;
	
	public float getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(float deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public int getDeliveryPoints() {
		return deliveryPoints;
	}

	public void setDeliveryPoints(int deliveryPoints) {
		this.deliveryPoints = deliveryPoints;
	}

	private float deliveryPrice;
	
	private int deliveryPoints;
	
	private String extendInfo;
	
	public String getNeedHideId() {
		return needHideId;
	}

	public void setNeedHideId(String needHideId) {
		this.needHideId = needHideId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getCampaignRuleId() {
		return campaignRuleId;
	}

	public void setCampaignRuleId(Integer campaignRuleId) {
		this.campaignRuleId = campaignRuleId;
	}
	
	public String getSubCampaignName() {
		return subCampaignName;
	}

	public void setSubCampaignName(String subCampaignName) {
		this.subCampaignName = subCampaignName;
	}

	public String getSubCampRuleType() {
		return subCampRuleType;
	}

	public void setSubCampRuleType(String subCampRuleType) {
		this.subCampRuleType = subCampRuleType;
	}
	
	public String getSubCampaignType() {
		return subCampaignType;
	}

	public void setSubCampaignType(String subCampaignType) {
		this.subCampaignType = subCampaignType;
	}
	
	public String getSubCampDetailNo() {
		return subCampDetailNo;
	}

	public void setSubCampDetailNo(String subCampDetailNo) {
		this.subCampDetailNo = subCampDetailNo;
	}

	public String getSubCampaignCode() {
		return subCampaignCode;
	}

	public void setSubCampaignCode(String subCampaignCode) {
		this.subCampaignCode = subCampaignCode;
	}

	public String getRuleFileName() {
		return ruleFileName;
	}

	public void setRuleFileName(String ruleFileName) {
		this.ruleFileName = ruleFileName;
	}

	public String getCampaignRule() {
		return campaignRule;
	}

	public void setCampaignRule(String campaignRule) {
		this.campaignRule = campaignRule;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getTopLimit() {
		return topLimit;
	}

	public void setTopLimit(String topLimit) {
		this.topLimit = topLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
	}
	
	public List<Map<String, Object>> getRuleDetailList() {
		if (null == ruleDetailList) {
			ruleDetailList = new ArrayList<Map<String, Object>>();
		}
		return ruleDetailList;
	}

	public void setRuleDetailList(List<Map<String, Object>> ruleDetailList) {
		this.ruleDetailList = ruleDetailList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRuleUpdateTime() {
		return ruleUpdateTime;
	}

	public void setRuleUpdateTime(String ruleUpdateTime) {
		this.ruleUpdateTime = ruleUpdateTime;
	}

	public int getRuleModifyCount() {
		return ruleModifyCount;
	}

	public void setRuleModifyCount(int ruleModifyCount) {
		this.ruleModifyCount = ruleModifyCount;
	}

	public String getUpKbn() {
		return upKbn;
	}

	public void setUpKbn(String upKbn) {
		this.upKbn = upKbn;
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

	public List<CampRuleConditionDTO> getCampRuleCondList() {
		if (null == campRuleCondList) {
			campRuleCondList = new ArrayList<CampRuleConditionDTO>();
		}
		return campRuleCondList;
	}

	public void setCampRuleCondList(List<CampRuleConditionDTO> campRuleCondList) {
		this.campRuleCondList = campRuleCondList;
	}

	public List<CampRuleResultDTO> getCampRuleResultList() {
		if (null == campRuleResultList) {
			campRuleResultList = new ArrayList<CampRuleResultDTO>();
		}
		return campRuleResultList;
	}

	public void setCampRuleResultList(List<CampRuleResultDTO> campRuleResultList) {
		this.campRuleResultList = campRuleResultList;
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

	public String getExPointDeadDate() {
		return exPointDeadDate;
	}

	public void setExPointDeadDate(String exPointDeadDate) {
		this.exPointDeadDate = exPointDeadDate;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponBatchNo() {
		return couponBatchNo;
	}

	public void setCouponBatchNo(String couponBatchNo) {
		this.couponBatchNo = couponBatchNo;
	}

	public float getPriceControl() {
		return priceControl;
	}

	public void setPriceControl(float priceControl) {
		this.priceControl = priceControl;
	}

	public int getSaleBatchNo() {
		return saleBatchNo;
	}

	public void setSaleBatchNo(int saleBatchNo) {
		this.saleBatchNo = saleBatchNo;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}
	
}
