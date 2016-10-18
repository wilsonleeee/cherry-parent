package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT09_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;
	
	/** 菜单ID */
	private String currentMenuID;
	
	/** 客服工号 */
	private String classNo;
	
	/** 来电时间查询起始时间 */
	private String callTimeStart;
	
	/** 来电时间查询截止时间 */
	private String callTimeEnd;
	
	/** 是否会员 */
	private String isMember;
	
	/** 客户来电号码 */
	private String customerNumber;
	
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

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getCallTimeStart() {
		return callTimeStart;
	}

	public void setCallTimeStart(String callTimeStart) {
		this.callTimeStart = callTimeStart;
	}

	public String getCallTimeEnd() {
		return callTimeEnd;
	}

	public void setCallTimeEnd(String callTimeEnd) {
		this.callTimeEnd = callTimeEnd;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}
	
}
