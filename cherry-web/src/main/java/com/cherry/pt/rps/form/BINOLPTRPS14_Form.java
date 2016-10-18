/*		
 * @(#)BINOLPTRPS14_Form.java     1.0 2011/03/18		
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
package com.cherry.pt.rps.form;

/**
 * 销售记录详细查询Form
 * @author zgl
 * @version 1.0 2011.03.18
 *
 */
public class BINOLPTRPS14_Form {

	private String saleRecordId;
	
	private String billCode;
	
	/**系统配置项是否显示唯一码*/
	private String sysConfigShowUniqueCode;
	
	/**访问来源（1：会员销售一览画面）*/
	private String fromFlag;

	public String getSaleRecordId() {
		return saleRecordId;
	}

	public void setSaleRecordId(String saleRecordId) {
		this.saleRecordId = saleRecordId;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

    public String getSysConfigShowUniqueCode() {
        return sysConfigShowUniqueCode;
    }

    public void setSysConfigShowUniqueCode(String sysConfigShowUniqueCode) {
        this.sysConfigShowUniqueCode = sysConfigShowUniqueCode;
    }

	public String getFromFlag() {
		return fromFlag;
	}

	public void setFromFlag(String fromFlag) {
		this.fromFlag = fromFlag;
	}
}