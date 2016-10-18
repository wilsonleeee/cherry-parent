/*
 * @(#)BINOLMBPTM02_Form.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 查询积分信息Form
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM02_Form extends DataTable_BaseForm {
	
	/** 单据日期查询开始时间 */
	private String changeDateStart;
	
	/** 单据日期查询结束时间 */
	private String changeDateEnd;
	
	/** 单据号 */
	private String billCode;
	
	/** 关联退货单号 */
	private String relevantSRCode;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 会员姓名 */
	private String memName;
	
	/** 会员手机 */
	private String mobilePhone;
	
	/** 部门ID */
	private String organizationId;
	
	/** 柜台号 */
	private String departCode;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 规则ID */
	private String subCampaignId;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 积分范围上限 */
	private String memPointStart;
	
	/** 积分范围下限 */
	private String memPointEnd;
	
	/** 字符编码 **/
	private String charset;
	
	/** 会员俱乐部ID */
	private String memberClubId;

	public String getChangeDateStart() {
		return changeDateStart;
	}

	public void setChangeDateStart(String changeDateStart) {
		this.changeDateStart = changeDateStart;
	}

	public String getChangeDateEnd() {
		return changeDateEnd;
	}

	public void setChangeDateEnd(String changeDateEnd) {
		this.changeDateEnd = changeDateEnd;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getRelevantSRCode() {
		return relevantSRCode;
	}

	public void setRelevantSRCode(String relevantSRCode) {
		this.relevantSRCode = relevantSRCode;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getSubCampaignId() {
		return subCampaignId;
	}

	public void setSubCampaignId(String subCampaignId) {
		this.subCampaignId = subCampaignId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getMemPointStart() {
		return memPointStart;
	}

	public void setMemPointStart(String memPointStart) {
		this.memPointStart = memPointStart;
	}

	public String getMemPointEnd() {
		return memPointEnd;
	}

	public void setMemPointEnd(String memPointEnd) {
		this.memPointEnd = memPointEnd;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
