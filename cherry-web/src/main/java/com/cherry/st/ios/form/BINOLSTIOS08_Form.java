/*
 * @(#)BINOLSTIOS09_Form.java     1.0 2013/07/04
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
package com.cherry.st.ios.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 入库单（Excel导入）Form
 * 
 * @author zhangle
 * @version 1.0 2013.07.04
 */
public class BINOLSTIOS08_Form extends DataTable_BaseForm{
	/**入库单（Excel导入）主表ID*/
	private String productInDepotExcelId;
	/**单据号*/
	private String billNo;
	/**入库部门编码*/
	private String departCode;
	/**入库部门名称*/
	private String departName;
	/**导入日期开始*/
	private String importStartTime;
	/**导入日期结束*/
	private String importEndTime;
	/**入库日期开始*/
	private String inDepotStartTime;
	/**入库日期结束*/
	private String inDepotEndTime;
	/**导入结果**/
	private String importResult;
	/**导入原因*/
	private String comments;
	/**导入批次号*/
	private String importBatchId;
	/**入库状态*/
	private String tradeStatus;


	public String getProductInDepotExcelId() {
		return productInDepotExcelId;
	}

	public void setProductInDepotExcelId(String productInDepotExcelId) {
		this.productInDepotExcelId = productInDepotExcelId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getImportStartTime() {
		return importStartTime;
	}

	public void setImportStartTime(String importStartTime) {
		this.importStartTime = importStartTime;
	}

	public String getImportEndTime() {
		return importEndTime;
	}

	public void setImportEndTime(String importEndTime) {
		this.importEndTime = importEndTime;
	}

	public String getInDepotStartTime() {
		return inDepotStartTime;
	}

	public void setInDepotStartTime(String inDepotStartTime) {
		this.inDepotStartTime = inDepotStartTime;
	}

	public String getInDepotEndTime() {
		return inDepotEndTime;
	}

	public void setInDepotEndTime(String inDepotEndTime) {
		this.inDepotEndTime = inDepotEndTime;
	}

	public String getImportResult() {
		return importResult;
	}

	public void setImportResult(String importResult) {
		this.importResult = importResult;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getImportBatchId() {
		return importBatchId;
	}

	public void setImportBatchId(String importBatchId) {
		this.importBatchId = importBatchId;
	}
	
	
}