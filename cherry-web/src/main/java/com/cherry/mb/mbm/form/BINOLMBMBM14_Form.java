/*
 * @(#)BINOLMBMBM14_Form.java     1.0 2013/04/16
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
 * 会员资料修改履历详细画面Form
 * 
 * @author WangCT
 * @version 1.0 2013/04/16
 */
public class BINOLMBMBM14_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;

	/** 操作柜台 **/
	private String modifyCounter;
	
	/** 操作员工 **/
	private String modifyEmployee;
	
	/** 操作类型 **/
	private String modifyType;
	
	/** 操作时间上限 **/
	private String modifyTimeStart;
	
	/** 操作时间下限 **/
	private String modifyTimeEnd;
	
	/** 会员资料修改履历ID **/
	private String memInfoRecordId;
	
	/** 批次号 **/
	private String batchNo;
	
	/** 备注 **/
	private String remark;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getModifyCounter() {
		return modifyCounter;
	}

	public void setModifyCounter(String modifyCounter) {
		this.modifyCounter = modifyCounter;
	}

	public String getModifyEmployee() {
		return modifyEmployee;
	}

	public void setModifyEmployee(String modifyEmployee) {
		this.modifyEmployee = modifyEmployee;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getModifyTimeStart() {
		return modifyTimeStart;
	}

	public void setModifyTimeStart(String modifyTimeStart) {
		this.modifyTimeStart = modifyTimeStart;
	}

	public String getModifyTimeEnd() {
		return modifyTimeEnd;
	}

	public void setModifyTimeEnd(String modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}

	public String getMemInfoRecordId() {
		return memInfoRecordId;
	}

	public void setMemInfoRecordId(String memInfoRecordId) {
		this.memInfoRecordId = memInfoRecordId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
