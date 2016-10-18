package com.cherry.cp.act.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCPACT01_Form extends DataTable_BaseForm{
	/**主题活动名称 */ 
	private String campCode;

	/**活动名称 */ 
	private String subcampCode;
	
	/**主活动类型 */ 
	private String campType;
	
	/**活动类型 */ 
	private String subcampType;
	
	/**活动状态*/ 
	private String campState;
	
	/**品牌 */ 
	private int brandInfoId;
	
	/**查询flag */  
	private String searchMode;
	
	/**保存状态 */ 
	private String saveStatus;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 主题活动Id */
	private String campaignId;
	
	/** 活动Id */
	private String subCampaignId;
	
	/** 主题活动模糊查询字符串 */
	private String campInfoStr;
	
	/** 活动模糊查询字符串 */
	private String subCampInfoStr;

	private int loginUserId;
	
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getCampCode() {
		return campCode;
	}
	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}
	public String getSubcampCode() {
		return subcampCode;
	}
	public void setSubcampCode(String subcampCode) {
		this.subcampCode = subcampCode;
	}
	public String getCampType() {
		return campType;
	}
	public void setCampType(String campType) {
		this.campType = campType;
	}
	public String getSubcampType() {
		return subcampType;
	}
	public void setSubcampType(String subcampType) {
		this.subcampType = subcampType;
	}
	public String getCampState() {
		return campState;
	}
	public void setCampState(String campState) {
		this.campState = campState;
	}
	public int getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getSearchMode() {
		return searchMode;
	}
	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}
	public String getSaveStatus() {
		return saveStatus;
	}
	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}
	public String getCampInfoStr() {
		return campInfoStr;
	}
	public void setCampInfoStr(String campInfoStr) {
		this.campInfoStr = campInfoStr;
	}
	public String getSubCampInfoStr() {
		return subCampInfoStr;
	}
	public void setSubCampInfoStr(String subCampInfoStr) {
		this.subCampInfoStr = subCampInfoStr;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getSubCampaignId() {
		return subCampaignId;
	}
	public void setSubCampaignId(String subCampaignId) {
		this.subCampaignId = subCampaignId;
	}
	public int getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(int loginUserId) {
		this.loginUserId = loginUserId;
	}
}
