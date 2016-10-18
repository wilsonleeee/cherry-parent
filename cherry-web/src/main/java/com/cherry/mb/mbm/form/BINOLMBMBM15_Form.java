/*
 * @(#)BINOLMBMBM15_Form.java     1.0 2013/04/26
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

/**
 * 会员发卡柜台变更处理Form
 * 
 * @author WangCT
 * @version 1.0 2013/04/26
 */
public class BINOLMBMBM15_Form {
	
	/** 会员发卡柜台转移目标柜台 **/
	private String newOrgId;
	
	/** 会员发卡柜台转移目标柜台号 **/
	private String newCounterCode;
	
	/** 会员发卡柜台转移目标柜台测试区分 **/
	private String newCounterKind;
	
	/** 会员发卡柜台未转移前柜台 **/
	private String oldOrgId;
	
	/** 会员发卡柜台未转移前柜台号 **/
	private String oldCounterCode;
	
	/** 转柜批次号 **/
	private String batchCode;
	
	/** 转柜方式：1.正常转柜；2.转柜撤销 **/
	private String subType;

	public String getNewOrgId() {
		return newOrgId;
	}

	public void setNewOrgId(String newOrgId) {
		this.newOrgId = newOrgId;
	}

	public String getNewCounterCode() {
		return newCounterCode;
	}

	public void setNewCounterCode(String newCounterCode) {
		this.newCounterCode = newCounterCode;
	}

	public String getNewCounterKind() {
		return newCounterKind;
	}

	public void setNewCounterKind(String newCounterKind) {
		this.newCounterKind = newCounterKind;
	}

	public String getOldOrgId() {
		return oldOrgId;
	}

	public void setOldOrgId(String oldOrgId) {
		this.oldOrgId = oldOrgId;
	}

	public String getOldCounterCode() {
		return oldCounterCode;
	}

	public void setOldCounterCode(String oldCounterCode) {
		this.oldCounterCode = oldCounterCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

}
