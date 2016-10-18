/*
 * @(#)BINOLMBMBM26_Form.java     1.0 2013.09.23
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员问题画面Form
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM26_Form extends DataTable_BaseForm {
	
	/** 会员ID **/
	private String memberInfoId;
	
	/** 问题票号 **/
	private String issueNoQ;
	
	/** 问题摘要 **/
	private String issueSummaryQ;
	
	/** 问题类型 **/
	private String issueTypeQ;
	
	/** 问题子类型 **/
	private String issueSubTypeQ;
	
	/** 解决结果 **/
	private String resolutionQ;
	
	/** 问题状态 **/
	private String issueStatusQ;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getIssueNoQ() {
		return issueNoQ;
	}

	public void setIssueNoQ(String issueNoQ) {
		this.issueNoQ = issueNoQ;
	}

	public String getIssueSummaryQ() {
		return issueSummaryQ;
	}

	public void setIssueSummaryQ(String issueSummaryQ) {
		this.issueSummaryQ = issueSummaryQ;
	}

	public String getIssueTypeQ() {
		return issueTypeQ;
	}

	public void setIssueTypeQ(String issueTypeQ) {
		this.issueTypeQ = issueTypeQ;
	}

	public String getIssueSubTypeQ() {
		return issueSubTypeQ;
	}

	public void setIssueSubTypeQ(String issueSubTypeQ) {
		this.issueSubTypeQ = issueSubTypeQ;
	}

	public String getResolutionQ() {
		return resolutionQ;
	}

	public void setResolutionQ(String resolutionQ) {
		this.resolutionQ = resolutionQ;
	}

	public String getIssueStatusQ() {
		return issueStatusQ;
	}

	public void setIssueStatusQ(String issueStatusQ) {
		this.issueStatusQ = issueStatusQ;
	}

}
