package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL03_Form extends DataTable_BaseForm {
	
	// 收款页面类型（SAL：销售收款，RET退货收款）
	private String collectPageType;
	
	// 单据分类（DHCH为积分兑换活动）
	private String billClassify;
	
	// BA姓名
	private String baName;
	
	// 会员姓名
	private String memberName;
	
	// 柜台号
	private String counterCode;
	
	// 业务状态
	private String businessState;
	
	// 销售类型
	private String saleType;
	
	// BA编号
	private String baCode;
	
	// 单据号
	private String billCode;
	
	// 业务时间
	private String businessDate;
	
	// 搜索内容
	private String searchStr;
	
	// 会员系统标识号
	private String memberInfoId;
	
	// 会员卡号
	private String memberCode;
	
	// 储值卡号
	private String cardCode;
	
	// 会员等级
	private String memberLevel;
	
	// 整单数量
	private String totalQuantity;
	
	// 整单金额
	private String totalAmount;
	
	// 整单原金额
	private String originalAmount;
	
	// 整单折扣率
	private String totalDiscountRate;
	
	// 整单去零金额
	private String roundingAmount;
	
	// 销售明细列表（Json格式字符串）
	private String saleDetailList;
	
	// 促销活动列表（Json字符串）
	private String promotionList;
	
	// 应收金额
	private String receivable;
	
	// 找零金额
	private String giveChange;
	
	// 现金收款金额
	private String cash;
	
	// 信用卡收款金额
	private String creditCard;
	
	// 银行卡收款金额
	private String bankCard;
	
	private String cashCard;
	
	// 支付宝收款金额
	private String aliPay;
	
	// 微信支付收款金额
	private String wechatPay;
	
	// 积分值
	private String pointValue;
	
	// 积分兑换金额
	private String exchangeCash;
	
	// 信用卡支付方式
	private String creditCardPayment;
	
	// 银行卡支付方式
	private String bankCardPayment;
	
	private String cashCardPayment;
	
	// 支付宝支付方式
	private String aliPayment;
	
	// 微信支付方式
	private String wechatPayment;
	
	// 积分支付方式
	private String pointsPayment;
	
	// 积分抵扣比率
	private String pointRatio;
	
	// 支付类型
	private String payType;
	
	// 扫描号
	private String authCode;
	
	// 备注
	private String comments;
	
	// 验证方式
	private String VerificationType;
	
	// 验证码
	private String verificationCode;
	
	// 服务类型
	private String serviceJsonList;
	
	// 验证码用途
	private String usesType;

	// 支付方式
	private List<Map<String, Object>> paymentList;
	
	// 设置支付方式的json
	private String paymentJsonList;
	
	//单据ID
	private String billId;
	
	public String getCollectPageType() {
		return collectPageType;
	}

	public void setCollectPageType(String collectPageType) {
		this.collectPageType = collectPageType;
	}

	public String getBillClassify() {
		return billClassify;
	}

	public void setBillClassify(String billClassify) {
		this.billClassify = billClassify;
	}

	public String getBaName() {
		return baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getBusinessState() {
		return businessState;
	}

	public void setBusinessState(String businessState) {
		this.businessState = businessState;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getBaCode() {
		return baCode;
	}

	public void setBaCode(String baCode) {
		this.baCode = baCode;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getTotalDiscountRate() {
		return totalDiscountRate;
	}

	public void setTotalDiscountRate(String totalDiscountRate) {
		this.totalDiscountRate = totalDiscountRate;
	}

	public String getRoundingAmount() {
		return roundingAmount;
	}

	public void setRoundingAmount(String roundingAmount) {
		this.roundingAmount = roundingAmount;
	}

	public String getSaleDetailList() {
		return saleDetailList;
	}

	public void setSaleDetailList(String saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public String getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(String promotionList) {
		this.promotionList = promotionList;
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

	public String getCreditCardPayment() {
		return creditCardPayment;
	}

	public void setCreditCardPayment(String creditCardPayment) {
		this.creditCardPayment = creditCardPayment;
	}

	public String getBankCardPayment() {
		return bankCardPayment;
	}

	public void setBankCardPayment(String bankCardPayment) {
		this.bankCardPayment = bankCardPayment;
	}

	public String getCashCardPayment() {
		return cashCardPayment;
	}

	public void setCashCardPayment(String cashCardPayment) {
		this.cashCardPayment = cashCardPayment;
	}

	public String getAliPayment() {
		return aliPayment;
	}

	public void setAliPayment(String aliPayment) {
		this.aliPayment = aliPayment;
	}

	public String getWechatPayment() {
		return wechatPayment;
	}

	public void setWechatPayment(String wechatPayment) {
		this.wechatPayment = wechatPayment;
	}

	public String getPointsPayment() {
		return pointsPayment;
	}

	public void setPointsPayment(String pointsPayment) {
		this.pointsPayment = pointsPayment;
	}

	public String getPointRatio() {
		return pointRatio;
	}

	public void setPointRatio(String pointRatio) {
		this.pointRatio = pointRatio;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getServiceJsonList() {
		return serviceJsonList;
	}

	public void setServiceJsonList(String serviceJsonList) {
		this.serviceJsonList = serviceJsonList;
	}

	public String getVerificationType() {
		return VerificationType;
	}

	public void setVerificationType(String verificationType) {
		VerificationType = verificationType;
	}

	public String getUsesType() {
		return usesType;
	}

	public void setUsesType(String usesType) {
		this.usesType = usesType;
	}

	public List<Map<String, Object>> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<Map<String, Object>> paymentList) {
		this.paymentList = paymentList;
	}

	public String getPaymentJsonList() {
		return paymentJsonList;
	}

	public void setPaymentJsonList(String paymentJsonList) {
		this.paymentJsonList = paymentJsonList;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	
}
