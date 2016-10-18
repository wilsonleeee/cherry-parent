/*
 * @(#)BINOLMBMBM04_Form.java     1.0 2012.10.10
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
 * 会员积分详细画面Form
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM04_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 积分变化日期最小限制 */
	private String changeDateStart;
	
	/** 积分变化日期最大限制 */
	private String changeDateEnd;
	
	/** 单据号 */
	private String billCode;
	
	/** 关联退货单号 */
	private String relevantSRCode;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 柜台号 */
	private String departCode;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 匹配的规则ID */
	private String subCampaignId;
	
	/** 积分ID */
	private String pointChangeId;
	
	/** 积分范围上限 */
	private String memPointStart;
	
	/** 积分范围下限 */
	private String memPointEnd;
	
	/** 会员俱乐部ID */
	private String memberClubId;
	
	/** 显示模式（0：单据模式，1：明细模式） */
	private String displayFlag;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

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

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getSubCampaignId() {
		return subCampaignId;
	}

	public void setSubCampaignId(String subCampaignId) {
		this.subCampaignId = subCampaignId;
	}

	public String getPointChangeId() {
		return pointChangeId;
	}

	public void setPointChangeId(String pointChangeId) {
		this.pointChangeId = pointChangeId;
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

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
