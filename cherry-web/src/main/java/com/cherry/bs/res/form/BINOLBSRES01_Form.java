package com.cherry.bs.res.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSRES01_Form extends DataTable_BaseForm {

	private String parentResellerCode;
	private String provinceId;	
	private String cityId;
	private String type;
	private String organizationInfoId;
	private String brandInfoId;
	private String resellerInfoId;
	private String resellerCode;
	private String resellerName;
	private String validFlag;
	private String regionId;
	private String resellerCodeIf;
	private String levelCode;
	private String priceFlag;
	private String status;

	/**导出CSV编码*/
	private String charset;
	
	/**导出格式 */
	private String exportFormat;
	private String[] resellerInfoIdArr;
	public String getParentResellerCode() {
		return parentResellerCode;
	}

	public void setParentResellerCode(String parentResellerCode) {
		this.parentResellerCode = parentResellerCode;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getResellerCodeIf() {
		return resellerCodeIf;
	}

	public void setResellerCodeIf(String resellerCodeIf) {
		this.resellerCodeIf = resellerCodeIf;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getResellerInfoId() {
		return resellerInfoId;
	}

	public void setResellerInfoId(String resellerInfoId) {
		this.resellerInfoId = resellerInfoId;
	}

	public String getResellerCode() {
		return resellerCode;
	}

	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}

	public String getResellerName() {
		return resellerName;
	}

	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String[] getResellerInfoIdArr() {
		return resellerInfoIdArr;
	}

	public void setResellerInfoIdArr(String[] resellerInfoIdArr) {
		this.resellerInfoIdArr = resellerInfoIdArr;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}


}
