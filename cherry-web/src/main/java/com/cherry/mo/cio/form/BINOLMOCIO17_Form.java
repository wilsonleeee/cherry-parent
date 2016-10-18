package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO17_Form extends DataTable_BaseForm {
	/**柜台消息标题*/
	private String messageTitle;
	/**柜台消息内容*/
	private String messageBody;
	/**生效开始日期查询的开始日期*/
	private String startValidDateBegin;
	/**生效开始日期查询的结束日期*/
	private String startValidDateFinish;
	/**生效结束日期查询的开始日期*/
	private String endValidDateBegin;
	/**生效结束日期查询的结束日期*/
	private String endValidDateFinish;
	/**柜台号*/
	private String counterCode;
	/**柜台名称*/
	private String counterName;
	/**导入结果*/
	private String importResult;
	/**下发区分*/
	private String publishStatus;
	/**柜台消息导入主表ID*/
	private String counterMessageImportId;
	/**导入批次ID*/
	private String importBatchId;
	
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getStartValidDateBegin() {
		return startValidDateBegin;
	}
	public void setStartValidDateBegin(String startValidDateBegin) {
		this.startValidDateBegin = startValidDateBegin;
	}
	public String getStartValidDateFinish() {
		return startValidDateFinish;
	}
	public void setStartValidDateFinish(String startValidDateFinish) {
		this.startValidDateFinish = startValidDateFinish;
	}
	public String getEndValidDateBegin() {
		return endValidDateBegin;
	}
	public void setEndValidDateBegin(String endValidDateBegin) {
		this.endValidDateBegin = endValidDateBegin;
	}
	public String getEndValidDateFinish() {
		return endValidDateFinish;
	}
	public void setEndValidDateFinish(String endValidDateFinish) {
		this.endValidDateFinish = endValidDateFinish;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public String getImportResult() {
		return importResult;
	}
	public void setImportResult(String importResult) {
		this.importResult = importResult;
	}
	public String getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	public String getCounterMessageImportId() {
		return counterMessageImportId;
	}
	public void setCounterMessageImportId(String counterMessageImportId) {
		this.counterMessageImportId = counterMessageImportId;
	}
	public String getImportBatchId() {
		return importBatchId;
	}
	public void setImportBatchId(String importBatchId) {
		this.importBatchId = importBatchId;
	}
	
}
