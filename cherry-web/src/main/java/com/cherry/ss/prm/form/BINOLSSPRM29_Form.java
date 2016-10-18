/*		
 * @(#)BINOLSSPRM29_Form.java     1.0 2010/11/25		
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
package com.cherry.ss.prm.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 调拨记录查询form
 * 
 * @author lipc
 * @version 1.0 2010.11.25
 */
public class BINOLSSPRM29_Form extends BINOLCM13_Form{
	
	/** 调拨单号 */
	private String allocationNo;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;

	/**促销产品厂商ID*/
    private String prmVendorId;
    
    /**部门类型*/
    private String departType;
    
	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getAllocationNo() {
		return allocationNo;
	}

	public void setAllocationNo(String allocationNo) {
		this.allocationNo = allocationNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
