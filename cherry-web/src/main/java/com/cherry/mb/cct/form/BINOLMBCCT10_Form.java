package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT10_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;
	
	/** 菜单ID */
	private String currentMenuID;
	
	/** 客户编号/会员卡号 */
	private String customerCode;
	
	/** 客户来电号码 */
	private String customerNumber;
	
	/** 问题记录时间查询起始时间 */
	private String createTimeStart;
	
	/** 问题记录时间查询截止时间 */
	private String createTimeEnd;
	
	/** 客户姓名 */
	private String customerName;
	
	/** 问题类型 */
	private String issueType;
	
	/** 问题处理情况 */
	private String resolution;
	
	/** 客服工号 */
	private String classNo;
	
	/** 是否会员 */
	private String isMember;
	
	/** 问题来源 */
	private String issueSource;
	
	/** 导出格式*/
	private String exportFormat;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCurrentMenuID() {
		return currentMenuID;
	}

	public void setCurrentMenuID(String currentMenuID) {
		this.currentMenuID = currentMenuID;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getIssueSource() {
		return issueSource;
	}

	public void setIssueSource(String issueSource) {
		this.issueSource = issueSource;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}
	
}
