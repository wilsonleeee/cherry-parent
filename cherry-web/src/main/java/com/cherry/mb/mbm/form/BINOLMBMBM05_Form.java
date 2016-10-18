/*
 * @(#)BINOLMBMBM05_Form.java     1.0 2012.10.10
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
 * 会员销售详细画面Form
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM05_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 销售时间最小限制 */
	private String saleTimeStart;
	
	/** 销售时间最大限制 */
	private String saleTimeEnd;
	
	/** 单据号 */
	private String billCode;
	
	/** 交易类型 */
	private String saleType;
	
	/** 销售ID */
	private String saleRecordId;
	
	/** 显示模式（0：单据模式，1：明细模式） */
	private String displayFlag;
	
	/**系统配置项是否显示唯一码*/
    private String sysConfigShowUniqueCode;
    
    /** 会员俱乐部ID */
	private String memberClubId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getSaleTimeStart() {
		return saleTimeStart;
	}

	public void setSaleTimeStart(String saleTimeStart) {
		this.saleTimeStart = saleTimeStart;
	}

	public String getSaleTimeEnd() {
		return saleTimeEnd;
	}

	public void setSaleTimeEnd(String saleTimeEnd) {
		this.saleTimeEnd = saleTimeEnd;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getSaleRecordId() {
		return saleRecordId;
	}

	public void setSaleRecordId(String saleRecordId) {
		this.saleRecordId = saleRecordId;
	}

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

    public String getSysConfigShowUniqueCode() {
        return sysConfigShowUniqueCode;
    }

    public void setSysConfigShowUniqueCode(String sysConfigShowUniqueCode) {
        this.sysConfigShowUniqueCode = sysConfigShowUniqueCode;
    }

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}