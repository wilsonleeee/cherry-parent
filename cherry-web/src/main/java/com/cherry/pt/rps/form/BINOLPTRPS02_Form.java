/*
 * @(#)BINOLPTRPS02_Form.java     1.0 2010/11/03
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 我的发货单
 * @author weisc
 *
 */
public class BINOLPTRPS02_Form extends BINOLCM13_Form{
	
	/** 促销产品发货单信息 */ 
	private String deliverInfo;
	
	/** 收发货单号 */
	private String deliverRecNo;
	
	/** 部门类型 */
	private String departType;
	
	/** 部门ID */
	private String organizationId;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 实体仓库ID */
	private String inventId;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/** 部门类型List */
	private List<Map<String, Object>> departTypeList;

	/** 部门List */
	private List<Map<String, Object>> organizationList;

	/** 实体仓库List */
	private List<Map<String, Object>> inventoryList;
	
	/** 发货单List */
	private List<Map<String, Object>> deliverList;
	
	/** 节日  */
	private String holidays;	

    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	public List<Map<String, Object>> getDepartTypeList() {
		return departTypeList;
	}

	public void setDepartTypeList(List<Map<String, Object>> departTypeList) {
		this.departTypeList = departTypeList;
	}

	public List<Map<String, Object>> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<Map<String, Object>> organizationList) {
		this.organizationList = organizationList;
	}

	public List<Map<String, Object>> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<Map<String, Object>> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}
	
	public String getDeliverInfo() {
		return deliverInfo;
	}

	public void setDeliverInfo(String deliverInfo) {
		this.deliverInfo = deliverInfo;
	}

	public String getDeliverRecNo() {
		return deliverRecNo;
	}

	public void setDeliverRecNo(String deliverRecNo) {
		this.deliverRecNo = deliverRecNo;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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

	public String getInventId() {
		return inventId;
	}

	public void setInventId(String inventId) {
		this.inventId = inventId;
	}
	
	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public void setDeliverList(List<Map<String, Object>> deliverList) {
		this.deliverList = deliverList;
	}

	public List<Map<String, Object>> getDeliverList() {
		return deliverList;
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
}