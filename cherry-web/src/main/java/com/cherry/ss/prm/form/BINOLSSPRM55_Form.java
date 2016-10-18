/*		
 * @(#)BINOLSSPRM55_Form.java     1.0 2010/11/25		
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;
import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 调拨记录查询form
 * 
 * @author dingyc
 * @version 1.0 2010.11.25
 */
public class BINOLSSPRM55_Form extends BINOLCM13_Form{
	
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
	
	/** 部门ID */
	private String organizationId;

	/** 调拨部门List */
	private List<Map<String, Object>> orgList;

	/** 调拨记录List */
	private List<Map<String, Object>> allocationList;
	
	/** 假日信息 */
	private String holidays;

    /**促销产品厂商ID*/
    private String prmVendorId;
    
    /**处理状态 */
    private String tradeStatus;

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getOrgList() {
		return this.orgList;
	}

	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}

	public List<Map<String, Object>> getAllocationList() {
		return this.allocationList;
	}

	public void setAllocationList(List<Map<String, Object>> allocationList) {
		this.allocationList = allocationList;
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

    public String getPrmVendorId() {
        return prmVendorId;
    }

    public void setPrmVendorId(String prmVendorId) {
        this.prmVendorId = prmVendorId;
    }
    public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
}
