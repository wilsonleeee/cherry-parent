package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL12_Form extends DataTable_BaseForm {
	// 支付类型
	private String payType;
	
	// 单据号
	private String payBillCode;
	
	// 单据时间
	private String payBillTime;
	
	// 会员卡号
	private String payMemberCode;
	
	// 会员手机号
	private String payMemberName;
	
	// 支付金额
	private String payAmount;
	
	// 收款柜台号
	private String payCounterCode;
	
	// 返回结果列表
	private List<Map<String, Object>> payResultList;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayBillCode() {
		return payBillCode;
	}

	public void setPayBillCode(String payBillCode) {
		this.payBillCode = payBillCode;
	}

	public String getPayBillTime() {
		return payBillTime;
	}

	public void setPayBillTime(String payBillTime) {
		this.payBillTime = payBillTime;
	}

	public String getPayMemberCode() {
		return payMemberCode;
	}

	public void setPayMemberCode(String payMemberCode) {
		this.payMemberCode = payMemberCode;
	}

	public String getPayMemberName() {
		return payMemberName;
	}

	public void setPayMemberName(String payMemberName) {
		this.payMemberName = payMemberName;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayCounterCode() {
		return payCounterCode;
	}

	public void setPayCounterCode(String payCounterCode) {
		this.payCounterCode = payCounterCode;
	}

	public List<Map<String, Object>> getPayResultList() {
		return payResultList;
	}

	public void setPayResultList(List<Map<String, Object>> payResultList) {
		this.payResultList = payResultList;
	}
	
}
