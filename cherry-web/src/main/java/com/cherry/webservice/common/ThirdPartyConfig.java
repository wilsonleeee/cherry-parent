package com.cherry.webservice.common;

public class ThirdPartyConfig {
	String brandCode;
	String appName;
	String appID;
	String appSecret;
	String dynamicAESKey;
	String aesKeyExpireTime;
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getDynamicAESKey() {
		return dynamicAESKey;
	}
	public void setDynamicAESKey(String dynamicAESKey) {
		this.dynamicAESKey = dynamicAESKey;
	}
	public String getAesKeyExpireTime() {
		return aesKeyExpireTime;
	}
	public void setAesKeyExpireTime(String aesKeyExpireTime) {
		this.aesKeyExpireTime = aesKeyExpireTime;
	}
}
