package com.cherry.yh.rep.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLYHREP01_Form extends DataTable_BaseForm {
	/**品牌ID*/
	private String brandInfoId;
	
	/**订单号*/
	private String billCode;
	
	/** 二级代理商CODE */
	private String resellerCode;
	
	/**一级代理商CODE*/
	private String parentResellerCode;
	
	/** 开始日期 */
	private String startDate;
	
	/** 结束日期 */
	private String endDate;
	
	/** 代理商所属省份*/
	private String provinceId;
	
	/** 代理商所属城市 */
	private String cityId;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

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
