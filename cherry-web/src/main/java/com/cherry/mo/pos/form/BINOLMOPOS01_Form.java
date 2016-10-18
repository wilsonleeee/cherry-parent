package com.cherry.mo.pos.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOPOS01_Form extends DataTable_BaseForm{
	// 支付代号
	private String storePayCode;
	// 支付名称
	private String storePayValue;
	// json字符串
	private String codeKeyList;
	// 启用/禁用
	private String isEnable;
	// 业务处理标志
	private String payType;
	// 默认支付方式
	private String defaultPay;
	// 操作json支付串
	private String editJson;
	
	public String getStorePayCode() {
		return storePayCode;
	}

	public void setStorePayCode(String storePayCode) {
		this.storePayCode = storePayCode;
	}

	public String getStorePayValue() {
		return storePayValue;
	}

	public void setStorePayValue(String storePayValue) {
		this.storePayValue = storePayValue;
	}

	public String getCodeKeyList() {
		return codeKeyList;
	}

	public void setCodeKeyList(String codeKeyList) {
		this.codeKeyList = codeKeyList;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDefaultPay() {
		return defaultPay;
	}

	public void setDefaultPay(String defaultPay) {
		this.defaultPay = defaultPay;
	}

	public String getEditJson() {
		return editJson;
	}

	public void setEditJson(String editJson) {
		this.editJson = editJson;
	}

}
