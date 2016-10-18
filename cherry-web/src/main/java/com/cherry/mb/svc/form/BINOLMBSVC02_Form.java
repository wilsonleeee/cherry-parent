package com.cherry.mb.svc.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBSVC02_Form extends DataTable_BaseForm{
	/**卡号*/
	private String cardCode;
	/**预留手机号*/
	private String mobilePhone;
	/**储值卡类型*/
	private String cardType;
	/**有效标识*/
	private String validFlag;
	/**储值卡状态*/
	private String cardState;
	/**储值卡Id*/
	private String cardId;
	/**部门ID*/
	private String departId;
	/**交易类型*/
	private String transactionType;
	/**交易开始时间*/
	private String fromDate;
	/**交易截至时间*/
	private String toDate;
	/**充值金额*/
	private String rechargeValue;
	/**储值卡密码*/
	private String cardPassword;
	/**储值卡默认密码*/
	private String defaultPassword;
	/**储值卡数组json串*/
	private String cardArr;
	/**交易单号*/
	private String billCode;
	/**柜台号*/
	private String counterName;
	/**数据类型（用来备注是否是测试数据）*/
	private String dateType;
	/**柜台Code*/
	private String counterCode;
	/**验证号 (couponCode)*/
	private String verificationCode;
	/**新储存卡密码*/
	public String newPassword;
	/**密码修改验证方式*/
	private int verificationType;
	/**旧储存卡密码*/
	public String oldPassword;
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public int getVerificationType() {
		return verificationType;
	}
	public void setVerificationType(int verificationType) {
		this.verificationType = verificationType;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	private Map<String, Object> cardCountInfo;
	private List<Map<String,Object>> cardList;
	private Map<String, Object> saleCountInfo;
	private List<Map<String,Object>> saleList;
	private Map<String, Object> serviceCountInfo;
	private List<Map<String,Object>> serviceList;
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getCardState() {
		return cardState;
	}
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getRechargeValue() {
		return rechargeValue;
	}
	public void setRechargeValue(String rechargeValue) {
		this.rechargeValue = rechargeValue;
	}
	public String getCardPassword() {
		return cardPassword;
	}
	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}
	public String getDefaultPassword() {
		return defaultPassword;
	}
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	public String getCardArr() {
		return cardArr;
	}
	public void setCardArr(String cardArr) {
		this.cardArr = cardArr;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public Map<String, Object> getCardCountInfo() {
		return cardCountInfo;
	}
	public void setCardCountInfo(Map<String, Object> cardCountInfo) {
		this.cardCountInfo = cardCountInfo;
	}
	public List<Map<String, Object>> getCardList() {
		return cardList;
	}
	public void setCardList(List<Map<String, Object>> cardList) {
		this.cardList = cardList;
	}
	public Map<String, Object> getSaleCountInfo() {
		return saleCountInfo;
	}
	public void setSaleCountInfo(Map<String, Object> saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
	}
	public List<Map<String, Object>> getSaleList() {
		return saleList;
	}
	public void setSaleList(List<Map<String, Object>> saleList) {
		this.saleList = saleList;
	}
	public Map<String, Object> getServiceCountInfo() {
		return serviceCountInfo;
	}
	public void setServiceCountInfo(Map<String, Object> serviceCountInfo) {
		this.serviceCountInfo = serviceCountInfo;
	}
	public List<Map<String, Object>> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<Map<String, Object>> serviceList) {
		this.serviceList = serviceList;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	
}
