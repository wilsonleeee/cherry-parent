/*
 * @(#)BINOLSSPRM66_Form.java     1.0 2013/09/17
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 入库Excel导入明细Form
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM67_Form extends DataTable_BaseForm {
	/** 导入批次 */
	private String importBatch;
	/** 入库单（Excel导入）主表ID */
	private String prmInDepotExcelId;
	/** 单据号 */
	private String billNo;
	/** 入库部门编码 */
	private String departCode;
	/** 入库部门名称 */
	private String departName;
	/** 导入日期开始 */
	private String importStartTime;
	/** 导入日期结束 */
	private String importEndTime;
	/** 入库日期开始 */
	private String inDepotStartTime;
	/** 入库日期结束 */
	private String inDepotEndTime;
	/** 导入结果 **/
	private String importResult;
	/** 导入原因 */
	private String comments;
	/** 入库状态 */
	private String tradeStatus;
	/** 导入批次ID */
	private String importBatchId;

	public String getImportBatch() {
		return importBatch;
	}

	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}

	public String getPrmInDepotExcelId() {
		return prmInDepotExcelId;
	}

	public void setPrmInDepotExcelId(String prmInDepotExcelId) {
		this.prmInDepotExcelId = prmInDepotExcelId;
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