package com.cherry.cm.core;

public class TmallKeyDTO {
	
	/**品牌名称*/
	private String brandName;
	
	/**品牌代码*/
	private String brandCode;
	
	/**组织代码*/
	private String orgCode;
	
	/**appKey*/
	private String appKey;
	
	/**appSecret*/
	private String appSecret;
	
	/**sessionKey*/
	private String sessionKey;
	
	/**mixKey*/
	private String mixKey;
	
	/**和天猫对接的柜台号*/
	private String tmallCounters;
	
	/**不执行的柜台号*/
	private String noExecCounts;
	
	/**合并会员区分*/
	private String mergeFlag;
	
	/**会员模式*/
	private String memberModel;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getMixKey() {
		return mixKey;
	}

	public void setMixKey(String mixKey) {
		this.mixKey = mixKey;
	}

	public String getTmallCounters() {
		return tmallCounters;
	}

	public void setTmallCounters(String tmallCounters) {
		this.tmallCounters = tmallCounters;
	}

	public String getNoExecCounts() {
		return noExecCounts;
	}

	public void setNoExecCounts(String noExecCounts) {
		this.noExecCounts = noExecCounts;
	}

	public String getMergeFlag() {
		return mergeFlag;
	}

	public void setMergeFlag(String mergeFlag) {
		this.mergeFlag = mergeFlag;
	}

	public String getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(String memberModel) {
		this.memberModel = memberModel;
	}
}
