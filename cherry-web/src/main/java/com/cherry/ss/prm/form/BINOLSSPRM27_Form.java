/*
 * @(#)BINOLSSPRM27_Form.java     1.0 2010/11/03
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

import java.util.Map;

import net.sf.cglib.util.StringSwitcher;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 发货单查询Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.03
 */
public class BINOLSSPRM27_Form extends BINOLCM13_Form{
	
	/** 促销产品发货单信息 */
	private String deliverInfo;
	
	/** 收发货单号 */
	private String deliverRecNo;
	
	/** 关联单号 */
	private String relevanceNo;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/** 打印状态 */
	private String printStatus;
	
	/** 入库区分 */
	private String stockInFlag;
	
	/** 业务类型 */
	private String tradeType;
	
	/**促销产品厂商ID*/
	private String prmVendorId;
	
	/**部门类型*/
	private String departType;
	
	/**收货部门*/
	private String departNameReceive;
	
	/** 汇总信息 */
    private Map<String, Object> sumInfo;
    
	/**收货部门id*/
	private String inOrganizationId;
	
	/**发货部门id*/
	private String outOrganizationId;
	
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
	
	public String getRelevanceNo() {
		return relevanceNo;
	}

	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
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
	
	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}
	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
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
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	 public String getDepartNameReceive() {
		return departNameReceive;
	}

	public void setDepartNameReceive(String departNameReceive) {
		this.departNameReceive = departNameReceive;
	}
	
	public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

	public String getOutOrganizationId() {
		return outOrganizationId;
	}

	public void setOutOrganizationId(String outOrganizationId) {
		this.outOrganizationId = outOrganizationId;
	}
		
}
