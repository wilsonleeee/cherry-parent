/*
 * @(#)BINOLMBMBM28_Form.java     1.0 2013.09.23
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
package com.cherry.mb.mbm.form;

import java.util.List;

/**
 * 会员问题处理画面Form
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM28_Form {
	
	/** 问题票ID **/
	private String issueId;
	
	/** 问题票单号 **/
	private String issueNo;
	
	/** 问题处理ID **/
	private String issueActionId;
	
	/** 处理内容 **/
	private String actionBodyAdd;
	
	/** 解决结果 **/
	private String resolutionAdd;
	
	/** 变更前解决结果 **/
	private String oldResolutionAdd;
	
	/** 类型 **/
	private String issueTypeAdd;
	
	/** 子类型 **/
	private String issueSubTypeAdd;
	
	/** 摘要 **/
	private String issueSummaryAdd;
	
	/** 描述 **/
	private String descriptionAdd;
	
	/** 担当者 **/
	private String assigneeAdd;
	
	/** 报告人 **/
	private String speakerAdd;
	
	/** 到期日 **/
	private String dueDateAdd;
	
	/** 优先级 **/
	private String priorityAdd;
	
	/** 销售单号 **/
	private String billCodeAdd;
	
	/** 活动类型 **/
	private String campaignTypeAdd;
	
	/** 活动代码 **/
	private String campaignCodeAdd;
	
	/** 关联问题票号 **/
	private String reIssueNoAdd;
	
	/** 当前处理员工ID **/
	private String curAuthor;
	
	/** 返回问题票ID **/
	private String backIssueId;
	
	/** 返回问题票ID队列 **/
	private List<String> backIssueIdQuene;

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getActionBodyAdd() {
		return actionBodyAdd;
	}

	public void setActionBodyAdd(String actionBodyAdd) {
		this.actionBodyAdd = actionBodyAdd;
	}

	public String getIssueActionId() {
		return issueActionId;
	}

	public void setIssueActionId(String issueActionId) {
		this.issueActionId = issueActionId;
	}

	public String getResolutionAdd() {
		return resolutionAdd;
	}

	public void setResolutionAdd(String resolutionAdd) {
		this.resolutionAdd = resolutionAdd;
	}

	public String getOldResolutionAdd() {
		return oldResolutionAdd;
	}

	public void setOldResolutionAdd(String oldResolutionAdd) {
		this.oldResolutionAdd = oldResolutionAdd;
	}

	public String getIssueTypeAdd() {
		return issueTypeAdd;
	}

	public void setIssueTypeAdd(String issueTypeAdd) {
		this.issueTypeAdd = issueTypeAdd;
	}

	public String getIssueSubTypeAdd() {
		return issueSubTypeAdd;
	}

	public void setIssueSubTypeAdd(String issueSubTypeAdd) {
		this.issueSubTypeAdd = issueSubTypeAdd;
	}

	public String getIssueSummaryAdd() {
		return issueSummaryAdd;
	}

	public void setIssueSummaryAdd(String issueSummaryAdd) {
		this.issueSummaryAdd = issueSummaryAdd;
	}

	public String getDescriptionAdd() {
		return descriptionAdd;
	}

	public void setDescriptionAdd(String descriptionAdd) {
		this.descriptionAdd = descriptionAdd;
	}

	public String getAssigneeAdd() {
		return assigneeAdd;
	}

	public void setAssigneeAdd(String assigneeAdd) {
		this.assigneeAdd = assigneeAdd;
	}

	public String getSpeakerAdd() {
		return speakerAdd;
	}

	public void setSpeakerAdd(String speakerAdd) {
		this.speakerAdd = speakerAdd;
	}

	public String getDueDateAdd() {
		return dueDateAdd;
	}

	public void setDueDateAdd(String dueDateAdd) {
		this.dueDateAdd = dueDateAdd;
	}

	public String getPriorityAdd() {
		return priorityAdd;
	}

	public void setPriorityAdd(String priorityAdd) {
		this.priorityAdd = priorityAdd;
	}

	public String getBillCodeAdd() {
		return billCodeAdd;
	}

	public void setBillCodeAdd(String billCodeAdd) {
		this.billCodeAdd = billCodeAdd;
	}

	public String getCampaignTypeAdd() {
		return campaignTypeAdd;
	}

	public void setCampaignTypeAdd(String campaignTypeAdd) {
		this.campaignTypeAdd = campaignTypeAdd;
	}

	public String getCampaignCodeAdd() {
		return campaignCodeAdd;
	}

	public void setCampaignCodeAdd(String campaignCodeAdd) {
		this.campaignCodeAdd = campaignCodeAdd;
	}

	public String getReIssueNoAdd() {
		return reIssueNoAdd;
	}

	public void setReIssueNoAdd(String reIssueNoAdd) {
		this.reIssueNoAdd = reIssueNoAdd;
	}

	public String getCurAuthor() {
		return curAuthor;
	}

	public void setCurAuthor(String curAuthor) {
		this.curAuthor = curAuthor;
	}

	public String getBackIssueId() {
		return backIssueId;
	}

	public void setBackIssueId(String backIssueId) {
		this.backIssueId = backIssueId;
	}

	public List<String> getBackIssueIdQuene() {
		return backIssueIdQuene;
	}

	public void setBackIssueIdQuene(List<String> backIssueIdQuene) {
		this.backIssueIdQuene = backIssueIdQuene;
	}

}
