package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT08_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;
	
	/** 菜单ID */
	private String currentMenuID;
	
	/** 客户姓名 */
	private String customerName;
	
	/** 所属行业 */
	private String industry;
	
	/** 会员手机号 */
	private String mobilePhone;
	
	/** 客户类型 */
	private String customerType;
	
	/** 生日月份 */
	private String birthMonth;
	
	/** 登记时间查询起始日期 */
	private String joinTimeStart;
	
	/** 登记时间查询截止日期 */
	private String joinTimeEnd;
	
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}
	
}
