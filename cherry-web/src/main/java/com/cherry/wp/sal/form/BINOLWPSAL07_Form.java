package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL07_Form extends DataTable_BaseForm {
	
	private String dgCounterCode;
	
	private String dgSaleDateStart;
	
	private String dgSaleDateEnd;
	
	private String dgBillCode;
	
	private String dgBaCode;
	
	private String memberSearchStr;
	
	private String billCode;
	
	private String dgCheckedBillCode;
	
	private String dgCheckedBaCode;
	
	private String dgCheckedMemberCode;
	
	private String dgCheckedMemberLevel;
	
	private String dgCheckedCustomerType;
	
	private String dgCheckedSaleType;
	
	private String dgCheckedBusinessDate;
	
	private String dgCheckedBusinessTime;
	
	private String dgCheckedTotalQuantity;
	
	private String dgCheckedTotalAmount;
	
	private String dgCheckedTotalOriginalAmount;
	
	private String dgCheckedRoundingAmount;
	
	private String dgCheckedCostPoint;
	
	private String dgCheckedCostPointAmount;
	
	private String billSrDetailStr;
	
	private String receivable;
	
	private String giveChange;
	
	private String cash;
	
	private String creditCard;
	
	private String bankCard;
	
	private String cashCard;
	
	private String aliPay;
	
	private String wechatPay;
	
	private String pointValue;
	
	private String exchangeCash;
	
	private String billDetailShowType;
	
	private String orgAliPay;
	
	private String orgWechatPay;
	
	private String comments;
	
	private String dgCardCode;
	
	private List<Map<String, Object>> billList;
	
	private List<Map<String, Object>> billDetailList;
	
	private List<Map<String, Object>> baList;
	
	// 服务类型
	private String serviceJsonList;
	
	// 支付方式集合
	private String paymentJsonList;
	
	//允许补登退货的开始时间
	private String returnbussinessDateStart;
	//允许补登退货的截止时间
	private String returnbussinessDateEnd;
	//补登退货时间
	private String returnbussinessDate;
	//会员当前总积分
	private String totalPoint;
	//会员销售所得积分
	private String pointGet;
	//会员退货时是否允许会员积分为负
	private String isPermitMemPointNegative;
	
	public String getDgCounterCode() {
		return dgCounterCode;
	}

	public void setDgCounterCode(String dgCounterCode) {
		this.dgCounterCode = dgCounterCode;
	}

	public String getDgSaleDateStart() {
		return dgSaleDateStart;
	}

	public void setDgSaleDateStart(String dgSaleDateStart) {
		this.dgSaleDateStart = dgSaleDateStart;
	}

	public String getDgSaleDateEnd() {
		return dgSaleDateEnd;
	}

	public void setDgSaleDateEnd(String dgSaleDateEnd) {
		this.dgSaleDateEnd = dgSaleDateEnd;
	}

	public String getDgBillCode() {
		return dgBillCode;
	}

	public void setDgBillCode(String dgBillCode) {
		this.dgBillCode = dgBillCode;
	}

	public String getDgBaCode() {
		return dgBaCode;
	}

	public void setDgBaCode(String dgBaCode) {
		this.dgBaCode = dgBaCode;
	}

	public String getMemberSearchStr() {
		return memberSearchStr;
	}

	public void setMemberSearchStr(String memberSearchStr) {
		this.memberSearchStr = memberSearchStr;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getDgCheckedBillCode() {
		return dgCheckedBillCode;
	}

	public void setDgCheckedBillCode(String dgCheckedBillCode) {
		this.dgCheckedBillCode = dgCheckedBillCode;
	}

	public String getDgCheckedBaCode() {
		return dgCheckedBaCode;
	}

	public void setDgCheckedBaCode(String dgCheckedBaCode) {
		this.dgCheckedBaCode = dgCheckedBaCode;
	}

	public String getDgCheckedMemberCode() {
		return dgCheckedMemberCode;
	}

	public void setDgCheckedMemberCode(String dgCheckedMemberCode) {
		this.dgCheckedMemberCode = dgCheckedMemberCode;
	}

	public String getDgCheckedMemberLevel() {
		return dgCheckedMemberLevel;
	}

	public void setDgCheckedMemberLevel(String dgCheckedMemberLevel) {
		this.dgCheckedMemberLevel = dgCheckedMemberLevel;
	}

	public String getDgCheckedCustomerType() {
		return dgCheckedCustomerType;
	}

	public void setDgCheckedCustomerType(String dgCheckedCustomerType) {
		this.dgCheckedCustomerType = dgCheckedCustomerType;
	}

	public String getDgCheckedSaleType() {
		return dgCheckedSaleType;
	}

	public void setDgCheckedSaleType(String dgCheckedSaleType) {
		this.dgCheckedSaleType = dgCheckedSaleType;
	}

	public String getDgCheckedBusinessDate() {
		return dgCheckedBusinessDate;
	}

	public void setDgCheckedBusinessDate(String dgCheckedBusinessDate) {
		this.dgCheckedBusinessDate = dgCheckedBusinessDate;
	}

	public String getDgCheckedBusinessTime() {
		return dgCheckedBusinessTime;
	}

	public void setDgCheckedBusinessTime(String dgCheckedBusinessTime) {
		this.dgCheckedBusinessTime = dgCheckedBusinessTime;
	}

	public String getDgCheckedTotalQuantity() {
		return dgCheckedTotalQuantity;
	}

	public void setDgCheckedTotalQuantity(String dgCheckedTotalQuantity) {
		this.dgCheckedTotalQuantity = dgCheckedTotalQuantity;
	}

	public String getDgCheckedTotalAmount() {
		return dgCheckedTotalAmount;
	}

	public void setDgCheckedTotalAmount(String dgCheckedTotalAmount) {
		this.dgCheckedTotalAmount = dgCheckedTotalAmount;
	}

	public String getDgCheckedTotalOriginalAmount() {
		return dgCheckedTotalOriginalAmount;
	}

	public void setDgCheckedTotalOriginalAmount(String dgCheckedTotalOriginalAmount) {
		this.dgCheckedTotalOriginalAmount = dgCheckedTotalOriginalAmount;
	}

	public String getDgCheckedRoundingAmount() {
		return dgCheckedRoundingAmount;
	}

	public void setDgCheckedRoundingAmount(String dgCheckedRoundingAmount) {
		this.dgCheckedRoundingAmount = dgCheckedRoundingAmount;
	}

	public String getDgCheckedCostPoint() {
		return dgCheckedCostPoint;
	}

	public void setDgCheckedCostPoint(String dgCheckedCostPoint) {
		this.dgCheckedCostPoint = dgCheckedCostPoint;
	}

	public String getDgCheckedCostPointAmount() {
		return dgCheckedCostPointAmount;
	}

	public void setDgCheckedCostPointAmount(String dgCheckedCostPointAmount) {
		this.dgCheckedCostPointAmount = dgCheckedCostPointAmount;
	}

	public String getBillSrDetailStr() {
		return billSrDetailStr;
	}

	public void setBillSrDetailStr(String billSrDetailStr) {
		this.billSrDetailStr = billSrDetailStr;
	}

	public String getReceivable() {
		return receivable;
	}

	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}

	public String getGiveChange() {
		return giveChange;
	}

	public void setGiveChange(String giveChange) {
		this.giveChange = giveChange;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getCashCard() {
		return cashCard;
	}

	public void setCashCard(String cashCard) {
		this.cashCard = cashCard;
	}

	public String getAliPay() {
		return aliPay;
	}

	public void setAliPay(String aliPay) {
		this.aliPay = aliPay;
	}

	public String getWechatPay() {
		return wechatPay;
	}

	public void setWechatPay(String wechatPay) {
		this.wechatPay = wechatPay;
	}

	public String getPointValue() {
		return pointValue;
	}

	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}

	public String getExchangeCash() {
		return exchangeCash;
	}
	
	public void setExchangeCash(String exchangeCash) {
		this.exchangeCash = exchangeCash;
	}

	public String getBillDetailShowType() {
		return billDetailShowType;
	}

	public void setBillDetailShowType(String billDetailShowType) {
		this.billDetailShowType = billDetailShowType;
	}
	
	public String getOrgAliPay() {
		return orgAliPay;
	}

	public void setOrgAliPay(String orgAliPay) {
		this.orgAliPay = orgAliPay;
	}

	public String getOrgWechatPay() {
		return orgWechatPay;
	}

	public void setOrgWechatPay(String orgWechatPay) {
		this.orgWechatPay = orgWechatPay;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<Map<String, Object>> getBillList() {
		return billList;
	}

	public void setBillList(List<Map<String, Object>> billList) {
		this.billList = billList;
	}

	public List<Map<String, Object>> getBillDetailList() {
		return billDetailList;
	}

	public void setBillDetailList(List<Map<String, Object>> billDetailList) {
		this.billDetailList = billDetailList;
	}

	public List<Map<String, Object>> getBaList() {
		return baList;
	}

	public void setBaList(List<Map<String, Object>> baList) {
		this.baList = baList;
	}

	public String getDgCardCode() {
		return dgCardCode;
	}

	public void setDgCardCode(String dgCardCode) {
		this.dgCardCode = dgCardCode;
	}

	public String getServiceJsonList() {
		return serviceJsonList;
	}

	public void setServiceJsonList(String serviceJsonList) {
		this.serviceJsonList = serviceJsonList;
	}

	public String getPaymentJsonList() {
		return paymentJsonList;
	}

	public void setPaymentJsonList(String paymentJsonList) {
		this.paymentJsonList = paymentJsonList;
	}

	public String getReturnbussinessDateStart() {
		return returnbussinessDateStart;
	}

	public void setReturnbussinessDateStart(String returnbussinessDateStart) {
		this.returnbussinessDateStart = returnbussinessDateStart;
	}

	public String getReturnbussinessDateEnd() {
		return returnbussinessDateEnd;
	}

	public void setReturnbussinessDateEnd(String returnbussinessDateEnd) {
		this.returnbussinessDateEnd = returnbussinessDateEnd;
	}

	public String getReturnbussinessDate() {
		return returnbussinessDate;
	}

	public void setReturnbussinessDate(String returnbussinessDate) {
		this.returnbussinessDate = returnbussinessDate;
	}

	public String getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(String totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getPointGet() {
		return pointGet;
	}

	public void setPointGet(String pointGet) {
		this.pointGet = pointGet;
	}

	public String getIsPermitMemPointNegative() {
		return isPermitMemPointNegative;
	}

	public void setIsPermitMemPointNegative(String isPermitMemPointNegative) {
		this.isPermitMemPointNegative = isPermitMemPointNegative;
	}
}
