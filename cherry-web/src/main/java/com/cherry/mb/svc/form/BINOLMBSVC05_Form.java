package com.cherry.mb.svc.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBSVC05_Form extends DataTable_BaseForm{
	/**卡号*/
	private String cardCode;
	/**预留手机号*/
	private String mobilePhone;
	/**储值卡类型*/
	private String cardType;
	/**储值卡交易List*/
	private List<Map<String,Object>> tradeList;
	/**开始时间*/
	private String startDate;
	/**结束时间*/
	private String endDate;
	/**交易类型*/
	private String transactionType;
	/**储值卡状态*/
	private String state;
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
	public List<Map<String, Object>> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<Map<String, Object>> tradeList) {
		this.tradeList = tradeList;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
