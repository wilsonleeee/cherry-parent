/*		
 * @(#)BINOLSSPRM13_Form.java     1.0 2010/10/27		
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
package com.cherry.ss.prm.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSSPRM13_Form extends DataTable_BaseForm {	
	
	/** 部门类型*/
	private String departType;
	
	/** 活动地点类型*/
	private String locationType;
	
	/** 活动时间地点 JSON */
	private String timeLocationJSON;
	
	/** 活动时间 JSON */
	private String timeJSON;
	
	/** 活动组id*/
	private String prmActGrp;
	
	/** 活动组id*/
	private String prmActGrps[];
	
	/** 促销活动类型 */
	private String activityType;

	/** 促销活动组预约开始时间 */
	private String reserveBeginDate;

	/** 促销活动组预约结束时间 */
	private String reserveEndDate;

	/** 促销活动组领用开始时间 */
	private String activityBeginDate;

	/** 促销活动组领用结束时间 */
	private String activityEndDate;
	
	/** 促销活动组类型 */
	private String prmGrpType;

	/** 促销活动组预约开始时间 */
	private String reserveBeDate;

	/** 促销活动组预约结束时间 */
	private String reserveEDate;

	/** 促销活动组领用开始时间 */
	private String activityBeDate;

	/** 促销活动组领用结束时间 */
	private String activityEDate;
	
	/** 兑换活动是否需要预约标志*/
	private String needReserve;

	/** 促销活动名 */
	private String prmActiveName;
	
	/** 促销规则描述 */
	private String descriptionDtl;

	/** 促销规则起始时间 */
	private String[] startTime;

	/** 促销规则结束时间 */
	private String[] endTime;

	/** 促销柜台 */
	private String[] prmCounters;
	
	/** 促销规则描述*/
	private String prmRuleDescribe;
	
	/** 促销活动适用次数 */
	private String prmActiveTimes;
	
	/** 促销规则条件 */
	private String prmConditionJson;

	/** 活动奖励INFO */
	private String resultInfo;
	
	/** 促销奖励金额 */
	private String prmRewardAmount;
	
	/** 假日 */
	private String holidays;
	
	/** 日历起始日期*/
	private String calStartDate;
	
	/** 区域市ID*/
	private String cityID;
	
	/** 渠道ID*/
	private String channelID;
	
	private String factionID;
	
	private String templateFlag;

	private String systemCode;

	private String linkMainCode;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getLinkMainCode() {
		return linkMainCode;
	}

	public void setLinkMainCode(String linkMainCode) {
		this.linkMainCode = linkMainCode;
	}

	public String getFactionID() {
		return factionID;
	}

	public void setFactionID(String factionID) {
		this.factionID = factionID;
	}

	/** 促销活动组名 */
	private String groupName;
	
	/** 终端是否可以更改 */
	private String mainModify;
	
	/** 适用次数 */
	private String maxReceiveQty;
	
	/** 规则HTML*/
	private String ruleHTML;
	
	/** 规则drl*/
	private String ruleDrl;
	
	/** 规则ID*/
	private String ruleId;
	
	/** 促销活动组信息List  */
	private List<Map<String, Object>> prmActiveGrpList;
	
	/** 产品信息List */
	private List<Map<String, Object>> productInfoList;
	
	/** 促销产品信息List  */
	private List<Map<String, Object>> promotionInfoList;
	
	/** 品牌信息List */
	private List brandInfoList;
	
	/** 选择品牌 */
	private String brandInfoId; 
	
	/** Map 设定*/
	private Map map;
	
	private String initFlag;
	
	private String jsTreeDataJsonStr;
	
	/** 查询条件--促销地点 */
	private String searchPrmLocation;
	
	

	public String[] getPrmActGrps() {
		return prmActGrps;
	}

	public void setPrmActGrps(String[] prmActGrps) {
		this.prmActGrps = prmActGrps;
	}

	public String getPrmActiveName() {
		return prmActiveName;
	}

	public void setPrmActiveName(String prmActiveName) {
		this.prmActiveName = prmActiveName;
	}

	public String[] getStartTime() {
		return startTime;
	}

	public void setStartTime(String[] startTime) {
		this.startTime = startTime;
	}

	public String[] getEndTime() {
		return endTime;
	}

	public void setEndTime(String[] endTime) {
		this.endTime = endTime;
	}

	public String[] getPrmCounters() {
		return prmCounters;
	}

	public void setPrmCounters(String[] prmCounters) {
		this.prmCounters = prmCounters;
	}

	public String getPrmRuleDescribe() {
		return prmRuleDescribe;
	}

	public void setPrmRuleDescribe(String prmRuleDescribe) {
		this.prmRuleDescribe = prmRuleDescribe;
	}

	public String getPrmActiveTimes() {
		return prmActiveTimes;
	}

	public void setPrmActiveTimes(String prmActiveTimes) {
		this.prmActiveTimes = prmActiveTimes;
	}

	public String getPrmConditionJson() {
		return prmConditionJson;
	}

	public void setPrmConditionJson(String prmConditionJson) {
		this.prmConditionJson = prmConditionJson;
	}

	public String getPrmRewardAmount() {
		return prmRewardAmount;
	}

	public void setPrmRewardAmount(String prmRewardAmount) {
		this.prmRewardAmount = prmRewardAmount;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getTimeLocationJSON() {
		return timeLocationJSON;
	}

	public void setTimeLocationJSON(String timeLocationJSON) {
		this.timeLocationJSON = timeLocationJSON;
	}

	public String getCalStartDate() {
		return calStartDate;
	}

	public void setCalStartDate(String calStartDate) {
		this.calStartDate = calStartDate;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Map<String, Object>> getPrmActiveGrpList() {
		return prmActiveGrpList;
	}

	public void setPrmActiveGrpList(List<Map<String, Object>> prmActiveGrpList) {
		this.prmActiveGrpList = prmActiveGrpList;
	}

	public String getMainModify() {
		return mainModify;
	}

	public void setMainModify(String mainModify) {
		this.mainModify = mainModify;
	}

	public String getMaxReceiveQty() {
		return maxReceiveQty;
	}

	public void setMaxReceiveQty(String maxReceiveQty) {
		this.maxReceiveQty = maxReceiveQty;
	}

	public String getDescriptionDtl() {
		return descriptionDtl;
	}

	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}

	public String getPrmActGrp() {
		return prmActGrp;
	}

	public void setPrmActGrp(String prmActGrp) {
		this.prmActGrp = prmActGrp;
	}

	public String getRuleHTML() {
		return ruleHTML;
	}

	public void setRuleHTML(String ruleHTML) {
		this.ruleHTML = ruleHTML;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getInitFlag() {
		return initFlag;
	}

	public void setInitFlag(String initFlag) {
		this.initFlag = initFlag;
	}

	public String getSearchPrmLocation() {
		return searchPrmLocation;
	}

	public void setSearchPrmLocation(String searchPrmLocation) {
		this.searchPrmLocation = searchPrmLocation;
	}

	public List<Map<String, Object>> getProductInfoList() {
		return productInfoList;
	}

	public void setProductInfoList(List<Map<String, Object>> productInfoList) {
		this.productInfoList = productInfoList;
	}

	public List<Map<String, Object>> getPromotionInfoList() {
		return promotionInfoList;
	}

	public void setPromotionInfoList(List<Map<String, Object>> promotionInfoList) {
		this.promotionInfoList = promotionInfoList;
	}

	public String getJsTreeDataJsonStr() {
		return jsTreeDataJsonStr;
	}

	public void setJsTreeDataJsonStr(String jsTreeDataJsonStr) {
		this.jsTreeDataJsonStr = jsTreeDataJsonStr;
	}

	public String getRuleDrl() {
		return ruleDrl;
	}

	public void setRuleDrl(String ruleDrl) {
		this.ruleDrl = ruleDrl;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getReserveBeginDate() {
		return reserveBeginDate;
	}

	public void setReserveBeginDate(String reserveBeginDate) {
		this.reserveBeginDate = reserveBeginDate;
	}

	public String getReserveEndDate() {
		return reserveEndDate;
	}

	public void setReserveEndDate(String reserveEndDate) {
		this.reserveEndDate = reserveEndDate;
	}

	public String getActivityBeginDate() {
		return activityBeginDate;
	}

	public void setActivityBeginDate(String activityBeginDate) {
		this.activityBeginDate = activityBeginDate;
	}

	public String getActivityEndDate() {
		return activityEndDate;
	}

	public void setActivityEndDate(String activityEndDate) {
		this.activityEndDate = activityEndDate;
	}

	public String getPrmGrpType() {
		return prmGrpType;
	}

	public void setPrmGrpType(String prmGrpType) {
		this.prmGrpType = prmGrpType;
	}

	public String getReserveBeDate() {
		return reserveBeDate;
	}

	public void setReserveBeDate(String reserveBeDate) {
		this.reserveBeDate = reserveBeDate;
	}

	public String getReserveEDate() {
		return reserveEDate;
	}

	public void setReserveEDate(String reserveEDate) {
		this.reserveEDate = reserveEDate;
	}

	public String getActivityBeDate() {
		return activityBeDate;
	}

	public void setActivityBeDate(String activityBeDate) {
		this.activityBeDate = activityBeDate;
	}

	public String getActivityEDate() {
		return activityEDate;
	}

	public void setActivityEDate(String activityEDate) {
		this.activityEDate = activityEDate;
	}

	public String getNeedReserve() {
		return needReserve;
	}

	public void setNeedReserve(String needReserve) {
		this.needReserve = needReserve;
	}	
	
	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getTimeJSON() {
		return timeJSON;
	}

	public void setTimeJSON(String timeJSON) {
		this.timeJSON = timeJSON;
	}

	public String getTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(String templateFlag) {
		this.templateFlag = templateFlag;
	}

}
