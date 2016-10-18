package com.cherry.cm.core;

/**
 * 
 * @ClassName: ThirdPartyConfigDTO 
 * @Description: TODO(penkonws项目的Webservice接入方配置DTO) 
 * @author menghao
 * @version v1.0.0 2016-7-15 
 *
 */
public class ThirdPartyConfigDTO {
	
	/**组织CODE*/
	private String orgCode;
	
	/**品牌CODE*/
	private String brandCode;
	
	/**接入方名称*/
	private String appName;
	
	/**接入方身份标识*/
	private String appID;
	
	/**Secret*/
	private String appSecret;
	
	/**当前的AES密钥*/
	private String dynamicAESKey;
	
	/**AES密钥过期时间*/
	private String aESKeyExpireTime;
	
	/**有效区分*/
	private String validFlag;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

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

	public String getaESKeyExpireTime() {
		return aESKeyExpireTime;
	}

	public void setaESKeyExpireTime(String aESKeyExpireTime) {
		this.aESKeyExpireTime = aESKeyExpireTime;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	
}
