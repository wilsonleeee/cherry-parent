package com.cherry.ss.prm.dto;

import java.util.List;
import java.util.Map;

public class CouponEngineDTO {
	
	private String ruleCode;
	private String ruleName;
	private String sendStartTime;
	private String sendEndTime;
	private List<Map<String, Object>> contentList;
	private Map<String, Object> useTimeInfo;
	private Map<String, Object> sendCondInfo;
	private Map<String, Object> useCondInfo;
	private int sumQuantity;
	private int limitQuantity;
	private int quantity;
	private String validMode;
	private String isGive;
	private int version;
	private int couponNum;
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getSendStartTime() {
		return sendStartTime;
	}
	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}
	public String getSendEndTime() {
		return sendEndTime;
	}
	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	
	public List<Map<String, Object>> getContentList() {
		return contentList;
	}
	public void setContentList(List<Map<String, Object>> contentList) {
		this.contentList = contentList;
	}
	public Map<String, Object> getUseTimeInfo() {
		return useTimeInfo;
	}
	public void setUseTimeInfo(Map<String, Object> useTimeInfo) {
		this.useTimeInfo = useTimeInfo;
	}
	public Map<String, Object> getSendCondInfo() {
		return sendCondInfo;
	}
	public void setSendCondInfo(Map<String, Object> sendCondInfo) {
		this.sendCondInfo = sendCondInfo;
	}
	public Map<String, Object> getUseCondInfo() {
		return useCondInfo;
	}
	public void setUseCondInfo(Map<String, Object> useCondInfo) {
		this.useCondInfo = useCondInfo;
	}
	public int getSumQuantity() {
		return sumQuantity;
	}
	public void setSumQuantity(int sumQuantity) {
		this.sumQuantity = sumQuantity;
	}
	public int getLimitQuantity() {
		return limitQuantity;
	}
	public void setLimitQuantity(int limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getValidMode() {
		return validMode;
	}
	public void setValidMode(String validMode) {
		this.validMode = validMode;
	}
	public String getIsGive() {
		return isGive;
	}
	public void setIsGive(String isGive) {
		this.isGive = isGive;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}
}
