package com.cherry.ct.pms.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCTPMS01_Form extends DataTable_BaseForm{

	private List<Map<String,Object>> paramList;
	private String smsChannel;
	private String supplierType;
	private String paramName;
	private String configGroup;
	private String paramCode;
	private String paramType;
	private String paramKey;
	private String paramValue;
	private String paramArr;
	public List<Map<String, Object>> getParamList() {
		return paramList;
	}

	public void setParamList(List<Map<String, Object>> paramList) {
		this.paramList = paramList;
	}

	public String getSmsChannel() {
		return smsChannel;
	}

	public void setSmsChannel(String smsChannel) {
		this.smsChannel = smsChannel;
	}

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamArr() {
		return paramArr;
	}

	public void setParamArr(String paramArr) {
		this.paramArr = paramArr;
	}

	

	
}
