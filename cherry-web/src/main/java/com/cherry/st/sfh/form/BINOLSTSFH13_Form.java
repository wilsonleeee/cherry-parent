package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH13_Form extends DataTable_BaseForm {
	
	/**发货单（Excel导入）主表ID*/
	private String prtDeliverExcelId;
	
	/**单据号*/
	private String billNo;
	
	/**关联单据号*/
	private String relevanceNo;
	
	/**收货部门ID*/
	private String organizationId;
	
	/**收货部门ID*/
	private String organizationIdReceive;
	
	/**导入开始日期*/
	private String importDateStart;
	
	/**导入结束日期*/
	private String importDateEnd;
	
	/**导入结果**/
	private String importResult;
	
	/**导入原因*/
	private String comments;
	
	/**导入批次号*/
	private String importBatchId;
	
	/**发货处理状态*/
	private String tradeStatus;
	
	public String getPrtDeliverExcelId() {
		return prtDeliverExcelId;
	}
	public void setPrtDeliverExcelId(String prtDeliverExcelId) {
		this.prtDeliverExcelId = prtDeliverExcelId;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getRelevanceNo() {
		return relevanceNo;
	}
	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
	}
	
	public String getImportDateStart() {
		return importDateStart;
	}
	public void setImportDateStart(String importDateStart) {
		this.importDateStart = importDateStart;
	}
	public String getImportDateEnd() {
		return importDateEnd;
	}
	public void setImportDateEnd(String importDateEnd) {
		this.importDateEnd = importDateEnd;
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
	public String getImportBatchId() {
		return importBatchId;
	}
	public void setImportBatchId(String importBatchId) {
		this.importBatchId = importBatchId;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationIdReceive() {
		return organizationIdReceive;
	}
	public void setOrganizationIdReceive(String organizationIdReceive) {
		this.organizationIdReceive = organizationIdReceive;
	}
	
}
