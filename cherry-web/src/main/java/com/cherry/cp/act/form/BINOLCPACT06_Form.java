package com.cherry.cp.act.form;

import com.cherry.cm.form.DataTable_BaseForm;

import java.util.List;
import java.util.Map;

public class BINOLCPACT06_Form extends DataTable_BaseForm{
	 /** 单据号*/
	 private String tradeNoIF;
	 /** 业务类型*/
	 private String tradeType;
	 /** 会员卡号*/
	 private String memCode;
	 /** 领取柜台*/
	 private String counterGot;
	 /** 手机*/
	 private String mobile;
	 /** 预约单状态*/
	 private String state;
	 /** 预约单状态*/
	 private String newState;
	 /** COUPONCODE*/
	 private String couponCode;
	 /**活动编码*/
	 private String campaignCode;
	 /** counterFlag*/
	 private String counterFlag;
	 /** 预约结果Id*/
	 private String campOrderId;
	 /**开始日期*/
	 private String startDate; 
	 /**结束日期*/
	 private String endDate;
	 /**预约柜台*/
	 private String  counterOrder;
	 /**测试区分*/
	 private String  testType;
	 /**下发区分*/
	 private String sendFlag;
	 /********活动编辑所需字段*************/
	 /** 编辑领用柜台字段*/
	 private String counterGotOld;
	 /** 编辑领用柜台字段*/
	 private String counterCode;
	 /**更新时间*/
	 private String modifyTime;
	 /**更新次数*/
	 private String modifyCount;
	 /** 编辑领用结束日期字段*/
	 private String endTime;
	 /** 编辑领用结束日期字段*/
	 private String getToTimeOld;
	 /** 编辑领用开始日期字段*/
	 private String fromTime;
	 /** 编辑领用开始日期字段*/
	 private String getFromTimeOld;
	 /**单据状态*/
	 private String campState;
	 /**单据个数*/
	 private int billNum;
	 /**批量更新领用柜台*/
	 private String batchCounter;
	 /**批量更新领用开始*/
	 private String batchFromTime;
	 /**批量更新领用结束*/
	 private String batchToTime;
	 /**领用柜台类型*/
	 private String counterType;
	 /**会员所属柜台**/
	 private String counterBelong;
	/**发货开始时间**/
	 private String sendStartDate;
	/**发货截止时间**/
	private String sendEndDate;
	private String expressCode;
	private String expressNo;

	/**订货单据List**/
	private List<Map<String,Object>> campDispatchOrderList;

	public String getTradeNoIF() {
		return tradeNoIF;
	}
	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getMemCode() {
		return memCode;
	}
	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}
	public String getCounterGot() {
		return counterGot;
	}
	public void setCounterGot(String counterGot) {
		this.counterGot = counterGot;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	public String getCounterFlag() {
		return counterFlag;
	}
	public void setCounterFlag(String counterFlag) {
		this.counterFlag = counterFlag;
	}
	public String getCampOrderId() {
		return campOrderId;
	}
	public void setCampOrderId(String campOrderId) {
		this.campOrderId = campOrderId;
	}
	public String getNewState() {
		return newState;
	}
	public void setNewState(String newState) {
		this.newState = newState;
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
	public String getCounterOrder() {
		return counterOrder;
	}
	public void setCounterOrder(String counterOrder) {
		this.counterOrder = counterOrder;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyCount() {
		return modifyCount;
	}
	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}
	public String getCounterGotOld() {
		return counterGotOld;
	}
	public void setCounterGotOld(String counterGotOld) {
		this.counterGotOld = counterGotOld;
	}
	public String getGetToTimeOld() {
		return getToTimeOld;
	}
	public void setGetToTimeOld(String getToTimeOld) {
		this.getToTimeOld = getToTimeOld;
	}
	public String getGetFromTimeOld() {
		return getFromTimeOld;
	}
	public void setGetFromTimeOld(String getFromTimeOld) {
		this.getFromTimeOld = getFromTimeOld;
	}
	public String getCampState() {
		return campState;
	}
	public void setCampState(String campState) {
		this.campState = campState;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public int getBillNum() {
		return billNum;
	}
	public void setBillNum(int billNum) {
		this.billNum = billNum;
	}
	public String getBatchCounter() {
		return batchCounter;
	}
	public void setBatchCounter(String batchCounter) {
		this.batchCounter = batchCounter;
	}
	public String getBatchFromTime() {
		return batchFromTime;
	}
	public void setBatchFromTime(String batchFromTime) {
		this.batchFromTime = batchFromTime;
	}
	public String getBatchToTime() {
		return batchToTime;
	}
	public void setBatchToTime(String batchToTime) {
		this.batchToTime = batchToTime;
	}

	public String getSendStartDate() {
		return sendStartDate;
	}

	public void setSendStartDate(String sendStartDate) {
		this.sendStartDate = sendStartDate;
	}

	public String getSendEndDate() {
		return sendEndDate;
	}

	public void setSendEndDate(String sendEndDate) {
		this.sendEndDate = sendEndDate;
	}

	public List<Map<String, Object>> getCampDispatchOrderList() {
		return campDispatchOrderList;
	}

	public void setCampDispatchOrderList(List<Map<String, Object>> campDispatchOrderList) {
		this.campDispatchOrderList = campDispatchOrderList;
	}

	/** 改变领用日期参考类型*/
	private String referFromType;
	
	/** 提前延后*/
	private String referFromParam;
	
	/** 天\月*/
	private String referFromDate;
	
	/** 参考值*/
	private String referFromValue;
	
	/** 改变领用日期参考类型*/
	private String referToType;
	
	/** 提前延后*/
	private String referToParam;
	
	/** 天\月*/
	private String referToDate;
	
	/** 参考值*/
	private String referToValue;
	
	public String getReferFromType() {
		return referFromType;
	}
	public void setReferFromType(String referFromType) {
		this.referFromType = referFromType;
	}
	public String getReferFromParam() {
		return referFromParam;
	}
	public void setReferFromParam(String referFromParam) {
		this.referFromParam = referFromParam;
	}
	public String getReferFromDate() {
		return referFromDate;
	}
	public void setReferFromDate(String referFromDate) {
		this.referFromDate = referFromDate;
	}
	public String getReferFromValue() {
		return referFromValue;
	}
	public void setReferFromValue(String referFromValue) {
		this.referFromValue = referFromValue;
	}
	public String getReferToType() {
		return referToType;
	}
	public void setReferToType(String referToType) {
		this.referToType = referToType;
	}
	public String getReferToParam() {
		return referToParam;
	}
	public void setReferToParam(String referToParam) {
		this.referToParam = referToParam;
	}
	public String getReferToDate() {
		return referToDate;
	}
	public void setReferToDate(String referToDate) {
		this.referToDate = referToDate;
	}
	public String getReferToValue() {
		return referToValue;
	}
	public void setReferToValue(String referToValue) {
		this.referToValue = referToValue;
	}
	public String getCounterType() {
		return counterType;
	}
	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}
	public String getCounterBelong() {
		return counterBelong;
	}
	public void setCounterBelong(String counterBelong) {
		this.counterBelong = counterBelong;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
}
