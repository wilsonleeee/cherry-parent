/*
 * @(#)BINOLMBMBM22_Form.java     1.0 2013.08.13
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
 * 会员答卷画面Form
 * 
 * @author WangCT
 * @version 1.0 2013.08.13
 */
public class BINOLMBMBM22_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 问卷名称 */
	private String paperName;
	
	/** 调查时间上限 */
	private String checkDateStart;
	
	/** 调查时间下限 */
	private String checkDateEnd;
	
	/** 答卷ID */
	private String paperAnswerId;
	
	/** 问卷ID */
	private String paperId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public String getCheckDateStart() {
		return checkDateStart;
	}

	public void setCheckDateStart(String checkDateStart) {
		this.checkDateStart = checkDateStart;
	}

	public String getCheckDateEnd() {
		return checkDateEnd;
	}

	public void setCheckDateEnd(String checkDateEnd) {
		this.checkDateEnd = checkDateEnd;
	}

	public String getPaperAnswerId() {
		return paperAnswerId;
	}

	public void setPaperAnswerId(String paperAnswerId) {
		this.paperAnswerId = paperAnswerId;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

}
