package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOMAN08_Form extends DataTable_BaseForm{
	
	/**配置项ID**/
	 private Integer posConfigID;
	 
	 /**组织ID**/
	 private Integer organizationInfoId;
	 
	 /**品牌ID**/
	 private String brandInfoId;
	 
	 /**配置项代码**/
	 private String configCode;
	 
	 /**配置项说明**/
	 private String configNote;
	 
	 /**配置结果**/
	 private String configValue;
	 
	 /**配置类型**/
	 private String configType;
	 
	 /**是否有效区分**/
	 private String validFlag;

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public Integer getPosConfigID() {
		return posConfigID;
	}

	public void setPosConfigID(Integer posConfigID) {
		this.posConfigID = posConfigID;
	}

	public Integer getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(Integer organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getConfigNote() {
		return configNote;
	}

	public void setConfigNote(String configNote) {
		this.configNote = configNote;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	 
	 
}
