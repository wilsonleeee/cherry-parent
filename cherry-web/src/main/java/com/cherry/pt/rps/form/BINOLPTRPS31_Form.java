package com.cherry.pt.rps.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 代理商提成报表Form
 * @author menghao
 *
 */
public class BINOLPTRPS31_Form extends DataTable_BaseForm {
	
	/**品牌ID*/
	private String brandInfoId;
	
	/**当前代理商对应的BaInfoId【通过CODE来对应】*/
	private String baInfoId;
	
	/** 二级代理商CODE */
	private String resellerCode;
	
	/**一级代理商CODE*/
	private String parentResellerCode;
	
	/** 代理商类型*/
	private String resellerType;
	
	/**代理商名称*/
	private String baName;
	
	/** 开始日期 */
	private String startDate;
	
	/** 结束日期 */
	private String endDate;
	
	/**会员卡号*/
	private String memberCode;
	
	/**会员手机*/
	private String mobilePhone;
	
	/**产品厂商ID*/
	private String prtVendorId;
	
	/**导出格式区分*/
	private String exportFormat;
	
	/** 代理商所属省份*/
	private String provinceId;
	
	/** 代理商所属城市 */
	private String cityId;

	public String getResellerCode() {
		return resellerCode;
	}

	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}

	public String getParentResellerCode() {
		return parentResellerCode;
	}

	public void setParentResellerCode(String parentResellerCode) {
		this.parentResellerCode = parentResellerCode;
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

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBaName() {
		return baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getBaInfoId() {
		return baInfoId;
	}

	public void setBaInfoId(String baInfoId) {
		this.baInfoId = baInfoId;
	}

	public String getResellerType() {
		return resellerType;
	}

	public void setResellerType(String resellerType) {
		this.resellerType = resellerType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
}
