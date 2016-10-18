/*		
 * @(#)BINOLPTRPS05_Form.java     1.0 2010/11/25		
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 产品调出单查询form
 * 
 * @author weisc
 * @version 1.0 2010.11.25
 */
public class BINOLPTRPS05_Form extends BINOLCM13_Form{
	
	/** 调拨单号 */
	private String allocationNo;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;
	
	/** 部门ID */
	private String organizationId;

    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	/** 调拨类型(1：调出 2：调入) */
	private String allocationType;
	
	/** 字符编码 **/
	private String charset;
	
	/**选中单据ID*/
	private String[] checkedBillIdArr;
	
	public String getAllocationNo() {
		return allocationNo;
	}

	public void setAllocationNo(String allocationNo) {
		this.allocationNo = allocationNo;
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}
	
	public String getAllocationType() {
		return allocationType;
	}

	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }
}
