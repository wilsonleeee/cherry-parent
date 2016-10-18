/*
 * @(#)BINOLPLUPM06_Form.java     1.0 2010/12/28
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.pl.upm.form;

/**
 * 
 * 安全策略编辑Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
public class BINOLPLUPM06_Form {
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 密码配置ID */
	private String pwConfId;
	
	/** 密码有效期 */
	private String duration;
	
	/** 密码修改提醒提前天数 */
	private String remindAhead;
	
	/** 密码重复间隔 */
	private String repetitionInterval;
	
	/** 密码复杂度 */
	private String complexity;
	
	/** 包含英文 */
	private String hasAlpha;
	
	/** 包含英文 */
	private String hasNumeric;
	
	/** 包含其他字符 */
	private String hasOtherChar;
	
	/** 其他字符 */
	private String otherChar;
	
	/** 密码最小长度 */
	private String pwLength;
	
	/** 密码最小长度 */
	private String maxLength;
	
	/** 是否加密 */
	private String isEncryption;
	
	/** 重试次数 */
	private String retryTimes;
	
	/** 账号锁定时间 */
	private String lockPeriod;
	
	/** 访问跟踪 */
	private String isTracable;
	
	/** 密码找回 */
	private String isTrievable;
	
	/** 更新日时 */
	private String pwUpdateTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 密码过期策略 */
	private String overdueTactic;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getPwConfId() {
		return pwConfId;
	}

	public void setPwConfId(String pwConfId) {
		this.pwConfId = pwConfId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRemindAhead() {
		return remindAhead;
	}

	public void setRemindAhead(String remindAhead) {
		this.remindAhead = remindAhead;
	}

	public String getRepetitionInterval() {
		return repetitionInterval;
	}

	public void setRepetitionInterval(String repetitionInterval) {
		this.repetitionInterval = repetitionInterval;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}
	
	public String getHasAlpha() {
		return hasAlpha;
	}

	public void setHasAlpha(String hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	public String getHasNumeric() {
		return hasNumeric;
	}

	public void setHasNumeric(String hasNumeric) {
		this.hasNumeric = hasNumeric;
	}

	public String getHasOtherChar() {
		return hasOtherChar;
	}

	public void setHasOtherChar(String hasOtherChar) {
		this.hasOtherChar = hasOtherChar;
	}

	public String getOtherChar() {
		return otherChar;
	}

	public void setOtherChar(String otherChar) {
		this.otherChar = otherChar;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getPwLength() {
		return pwLength;
	}

	public void setPwLength(String pwLength) {
		this.pwLength = pwLength;
	}

	public String getIsEncryption() {
		return isEncryption;
	}

	public void setIsEncryption(String isEncryption) {
		this.isEncryption = isEncryption;
	}

	public String getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(String retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getLockPeriod() {
		return lockPeriod;
	}

	public void setLockPeriod(String lockPeriod) {
		this.lockPeriod = lockPeriod;
	}

	public String getIsTracable() {
		return isTracable;
	}

	public void setIsTracable(String isTracable) {
		this.isTracable = isTracable;
	}

	public String getIsTrievable() {
		return isTrievable;
	}

	public void setIsTrievable(String isTrievable) {
		this.isTrievable = isTrievable;
	}

	public String getPwUpdateTime() {
		return pwUpdateTime;
	}

	public void setPwUpdateTime(String pwUpdateTime) {
		this.pwUpdateTime = pwUpdateTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

    public String getOverdueTactic() {
        return overdueTactic;
    }

    public void setOverdueTactic(String overdueTactic) {
        this.overdueTactic = overdueTactic;
    }
}
