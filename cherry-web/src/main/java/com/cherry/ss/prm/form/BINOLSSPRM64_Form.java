/*
 * @(#)BINOLSSPRM64_Form.java     1.0 2013/01/25
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
 * 入库一览Form
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM64_Form extends BINOLCM13_Form{
	
	/**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /**入库单list*/
    private List<Map<String,Object>> prmInDepotList;
    
	/**促销品厂商ID*/
    private String prmVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
	/** 单号  */
	private String billNoIF;
	
	/** 审核区分  */
	private String verifiedFlag;
	
	/** 总数量，总金额 */
	private Map<String,Object> sumInfo;
	
    /** 入库状态*/
    private String tradeStatus;
    
    /** 导入批次*/
    private String importBatch;
	
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


	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getBillNoIF() {
		return billNoIF;
	}

	public void setBillNoIF(String billNoIF) {
		this.billNoIF = billNoIF;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}
	
    public List<Map<String, Object>> getPrmInDepotList() {
		return prmInDepotList;
	}

	public void setPrmInDepotList(List<Map<String, Object>> prmInDepotList) {
		this.prmInDepotList = prmInDepotList;
	}

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

	public String getImportBatch() {
		return importBatch;
	}

	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}
}