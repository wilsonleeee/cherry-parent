package com.cherry.ss.prm.dto;

public class CouponRuleDTO extends BaseDTO{
	
	private String ruleCode;
	private String ruleName;
	private String sendStartTime;
	private String sendEndTime;
	private String content;
	private String useTimeJson;
	private String description;
	private String sendCond;
	private String useCond;
	private String status;
	private String sumQuantity;
	private String limitQuantity;
	private String quantity;
	private String validMode;
	private String isGive;
	private String couponFlag;
	private int maxContentNo;
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUseTimeJson() {
		return useTimeJson;
	}
	public void setUseTimeJson(String useTimeJson) {
		this.useTimeJson = useTimeJson;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSendCond() {
		return sendCond;
	}
	public void setSendCond(String sendCond) {
		this.sendCond = sendCond;
	}
	public String getUseCond() {
		return useCond;
	}
	public void setUseCond(String useCond) {
		this.useCond = useCond;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSumQuantity() {
		return sumQuantity;
	}
	public void setSumQuantity(String sumQuantity) {
		if (!"".equals(sumQuantity)) {
			this.sumQuantity = sumQuantity;
		}
	}
	public String getLimitQuantity() {
		return limitQuantity;
	}
	public void setLimitQuantity(String limitQuantity) {
		if (!"".equals(limitQuantity)) {
			this.limitQuantity = limitQuantity;
		}
	}
	public String getQuantity() {
		if (null == quantity || "".equals(quantity)) {
			quantity = "1";
		}
		return quantity;
	}
	public void setQuantity(String quantity) {
		if (!"".equals(quantity)) {
			this.quantity = quantity;
		}
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
	public String getCouponFlag() {
		return couponFlag;
	}
	public void setCouponFlag(String couponFlag) {
		this.couponFlag = couponFlag;
	}
	public int getMaxContentNo() {
		return maxContentNo;
	}
	public void setMaxContentNo(int maxContentNo) {
		this.maxContentNo = maxContentNo;
	}
}
