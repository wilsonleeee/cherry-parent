/*
 * @(#)BINOLSSPRM18_Form.java     1.0 2010/11/03
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

/**
 * 
 * 我的发货单
 * @author dingyc
 *
 */
public class BINOLSSPRM18_Form extends BINOLCM13_Form{
	
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
	
	/** 入库区分 */
	private String stockInFlag;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 部门类型List */
	private List<Map<String, Object>> departTypeList;

	/** 部门List */
	private List<Map<String, Object>> organizationList;

	/** 实体仓库List */
	private List<Map<String, Object>> inventoryList;
	
	/** 发货单List */
	private List deliverList;
	
	/** 节日  */
	private String holidays;	

    /**促销产品厂商ID*/
    private String prmVendorId;
	
	/**部门Id*/
	private String inOrganizationId;
	
    /**部门联动条 查询 发货部门/收货部门 标志*/
    private String departInOutFlag;
	
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

	public List getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List deliverList) {
		this.deliverList = deliverList;
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

	public String getStockInFlag() {
		return stockInFlag;
	}

	public void setStockInFlag(String stockInFlag) {
		this.stockInFlag = stockInFlag;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

    public String getPrmVendorId() {
        return prmVendorId;
    }

    public void setPrmVendorId(String prmVendorId) {
        this.prmVendorId = prmVendorId;
    }
	
	public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

    public String getDepartInOutFlag() {
        return departInOutFlag;
    }

    public void setDepartInOutFlag(String departInOutFlag) {
        this.departInOutFlag = departInOutFlag;
    }

	
}
