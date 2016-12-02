/*	
 * @(#)BINOLCPCOM02_Form.java     1.0 2011/7/18		
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
package com.cherry.cp.common.form;

import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;

/**
 * 会员活动共通 Form
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM02_Form {
	
	/** 工作流实例ID */
	private long wfId;
	
	/** 执行动作ID */
	private int actionId;
	
	/** 存草稿动作ID */
	private int saveActionId;
	
	/** 初期显示执行动作ID */
	private int initActionId;
	
	/** 下一步动作 */
	private int nextAction;
	
	/** 上一步动作 */
	private int backAction;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	private String brandCode;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 活动信息 */
	private String camTemps;
	
	/** 动作区分 */
	private String actionKbn;
	
	/** 活动基本信息 */
	private CampaignDTO campInfo;
	
	/** 会员子活动 */
	private CampaignRuleDTO campaignRule;
	
	/** 会员活动保存信息 */
	private String campSaveInfo;
	
	/** 会员活动类型 */
	private String campaignType;
	
	/** 会员等级ID */
	private String memberLevelId;
	
	/** 操作区分 */
	private int optKbn;
	
	/** 操作区分 */
	private int copyFlag;
	
	/** 配置操作区分 */
	private String configKbn;
	
	/** 配置编辑区分 */
	private String confEditKbn;
	
	/** 传递的参数 */
	private String campParamInfo;
	
	/** 优先级高低标志 */
	private String prioritySet;
	
	/** 活动组更新时间  */
	private String grpUpdateTime;
	
	/** 活动组更新次数 */
	private String grpModifyCount;
	
	/** 活动ID*/
	private String campaignId;
	
	/** 子活动ID*/
	private String subCampId;
	
	/** 页面传值参数*/
	private String extraInfo;
	
	/** 扩展参数*/
	private String extArgs;
	
	/** 类型区分*/
	private String typeFlag;
	
	/** 规则配置列表 */
	private List<Map<String, Object>> ruleConfigList;
	
	/** 子活动菜单 */
	private List<Map<String, Object>> subMenuList;

	private String searchCode;
	private String campObjGroupType;
	private String campObjGroupValue;
	public long getWfId() {
		return wfId;
	}

	public void setWfId(long wfId) {
		this.wfId = wfId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	
	public int getSaveActionId() {
		return saveActionId;
	}

	public void setSaveActionId(int saveActionId) {
		this.saveActionId = saveActionId;
	}

	public int getInitActionId() {
		return initActionId;
	}

	public void setInitActionId(int initActionId) {
		this.initActionId = initActionId;
	}

	public int getNextAction() {
		return nextAction;
	}

	public void setNextAction(int nextAction) {
		this.nextAction = nextAction;
	}

	public int getBackAction() {
		return backAction;
	}

	public void setBackAction(int backAction) {
		this.backAction = backAction;
	}

	public String getCamTemps() {
		return camTemps;
	}

	public void setCamTemps(String camTemps) {
		this.camTemps = camTemps;
	}

	public String getActionKbn() {
		return actionKbn;
	}

	public void setActionKbn(String actionKbn) {
		this.actionKbn = actionKbn;
	}

	public CampaignDTO getCampInfo() {
		return campInfo;
	}

	public void setCampInfo(CampaignDTO campInfo) {
		this.campInfo = campInfo;
	}

	public CampaignRuleDTO getCampaignRule() {
		return campaignRule;
	}

	public void setCampaignRule(CampaignRuleDTO campaignRule) {
		this.campaignRule = campaignRule;
	}

	public String getCampSaveInfo() {
		return campSaveInfo;
	}

	public void setCampSaveInfo(String campSaveInfo) {
		this.campSaveInfo = campSaveInfo;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(String memberLevelId) {
		this.memberLevelId = memberLevelId;
	}

	public int getOptKbn() {
		return optKbn;
	}

	public void setOptKbn(int optKbn) {
		this.optKbn = optKbn;
	}

	public String getCampParamInfo() {
		return campParamInfo;
	}

	public void setCampParamInfo(String campParamInfo) {
		this.campParamInfo = campParamInfo;
	}

	public int getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(int copyFlag) {
		this.copyFlag = copyFlag;
	}

	public String getConfigKbn() {
		return configKbn;
	}

	public void setConfigKbn(String configKbn) {
		this.configKbn = configKbn;
	}

	public String getPrioritySet() {
		return prioritySet;
	}

	public void setPrioritySet(String prioritySet) {
		this.prioritySet = prioritySet;
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

	public String getConfEditKbn() {
		return confEditKbn;
	}

	public void setConfEditKbn(String confEditKbn) {
		this.confEditKbn = confEditKbn;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getExtArgs() {
		return extArgs;
	}

	public void setExtArgs(String extArgs) {
		this.extArgs = extArgs;
	}

	public List<Map<String, Object>> getRuleConfigList() {
		return ruleConfigList;
	}

	public void setRuleConfigList(List<Map<String, Object>> ruleConfigList) {
		this.ruleConfigList = ruleConfigList;
	}

	public String getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(String typeFlag) {
		this.typeFlag = typeFlag;
	}

	public List<Map<String, Object>> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<Map<String, Object>> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public String getSubCampId() {
		return subCampId;
	}

	public void setSubCampId(String subCampId) {
		this.subCampId = subCampId;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getCampObjGroupType() {
		return campObjGroupType;
	}

	public void setCampObjGroupType(String campObjGroupType) {
		this.campObjGroupType = campObjGroupType;
	}

	public String getCampObjGroupValue() {
		return campObjGroupValue;
	}

	public void setCampObjGroupValue(String campObjGroupValue) {
		this.campObjGroupValue = campObjGroupValue;
	}
}
