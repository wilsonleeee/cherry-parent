/*
 * @(#)MemRuleExecRecord.java     1.0 2012/08/14      
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
package com.cherry.cm.mongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 会员规则执行履历记录
 * 
 * @author WangCT
 */
@Document(collection="MGO_RuleExecRecord")
@CompoundIndexes({
	@CompoundIndex(name="member_recordKbn", def="{'orgCode':1,'brandCode':1,'memberInfoId':1,'recordKbn':1,'billCode':1,'billType':1}"),
	@CompoundIndex(name="member_titcketDate", def="{'orgCode':1,'brandCode':1,'memberInfoId':1,'ticketDate':1,'recordKbn':1}")
})
public class MemRuleExecRecord {
	
	@Id
	private String id;
	/** 组织代码 **/
	private String orgCode;
	/** 品牌代码 **/
	private String brandCode;
	/** 组织ID **/
	private int organizationInfoId;
	/** 品牌ID **/
	private int brandInfoId;
	/** 会员ID **/
	private int memberInfoId;
	/** 会员卡号 **/
	private String memCode;
	/** 单据号 **/
	private String billCode;
	/** 业务类型 **/
	private String billType;
	/** 单据产生日期 **/
	private String ticketDate;
	/** 部门ID **/
	private String organizationId;
	/** 柜台号 **/
	private String countercode;
	/** 员工ID **/
	private String employeeId;
	/** Ba卡号 **/
	private String baCode;
	/** 数据来源 **/
	private String channel;
	/** 履历区分 **/
	private int recordKbn;
	/** 更新前的值 **/
	private String oldValue;
	/** 更新后的值 **/
	private String newValue;
	/** 计算日期 **/
	private String calcDate;
	/** 理由 **/
	private int reason;
	/** 规则ID **/
	private String ruleIds;
	/** 匹配的活动 **/
	private String subCampaignCodes;
	/** 变化类型 **/
	private String changeType;
	/** 重算次数 **/
	private int reCalcCount;
	/** 有效区分 **/
	private String validFlag;
	/** 创建时间 **/
	private String createTime;
	/** 创建者 **/
	private String createdBy;
	/** 创建程序名 **/
	private String createPGM;
	/** 更新时间 **/
	private String updateTime;
	/** 更新者 **/
	private String updatedBy;
	/** 更新程序名 **/
	private String updatePGM;
	/** 更新次数 **/
	private int modifyCount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public int getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public int getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public int getMemberInfoId() {
		return memberInfoId;
	}
	public void setMemberInfoId(int memberInfoId) {
		this.memberInfoId = memberInfoId;
	}
	public String getMemCode() {
		return memCode;
	}
	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getTicketDate() {
		return ticketDate;
	}
	public void setTicketDate(String ticketDate) {
		this.ticketDate = ticketDate;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getCountercode() {
		return countercode;
	}
	public void setCountercode(String countercode) {
		this.countercode = countercode;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getBaCode() {
		return baCode;
	}
	public void setBaCode(String baCode) {
		this.baCode = baCode;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public int getRecordKbn() {
		return recordKbn;
	}
	public void setRecordKbn(int recordKbn) {
		this.recordKbn = recordKbn;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getCalcDate() {
		return calcDate;
	}
	public void setCalcDate(String calcDate) {
		this.calcDate = calcDate;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public String getRuleIds() {
		return ruleIds;
	}
	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}
	public String getSubCampaignCodes() {
		return subCampaignCodes;
	}
	public void setSubCampaignCodes(String subCampaignCodes) {
		this.subCampaignCodes = subCampaignCodes;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public int getReCalcCount() {
		return reCalcCount;
	}
	public void setReCalcCount(int reCalcCount) {
		this.reCalcCount = reCalcCount;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatePGM() {
		return createPGM;
	}
	public void setCreatePGM(String createPGM) {
		this.createPGM = createPGM;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatePGM() {
		return updatePGM;
	}
	public void setUpdatePGM(String updatePGM) {
		this.updatePGM = updatePGM;
	}
	public int getModifyCount() {
		return modifyCount;
	}
	public void setModifyCount(int modifyCount) {
		this.modifyCount = modifyCount;
	}
}
