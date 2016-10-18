package com.cherry.jn.man.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLJNMAN04_Form extends DataTable_BaseForm{

	/** 活动类型 */
	private String campaignType;
	
	/** 类型名 */
	private String campaignTypeName;
	
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;
	
	/** 活动组 */
	private String campGroup;
	
	/** 规则类型*/
	private String pointRuleType;
	
	/** 开始日期*/
	private String fromDate;
	
	/** 结束日期*/
	private String toDate;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 会员俱乐部名称 */
	private String memberClubName;
	
	/** 积分活动List*/
	private List<Map<String, Object>> camtempList;

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCampaignTypeName() {
		return campaignTypeName;
	}

	public void setCampaignTypeName(String campaignTypeName) {
		this.campaignTypeName = campaignTypeName;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getCamtempList() {
		return camtempList;
	}

	public void setCamtempList(List<Map<String, Object>> camtempList) {
		this.camtempList = camtempList;
	}

	public String getCampGroup() {
		return campGroup;
	}

	public void setCampGroup(String campGroup) {
		this.campGroup = campGroup;
	}

	public String getPointRuleType() {
		return pointRuleType;
	}

	public void setPointRuleType(String pointRuleType) {
		this.pointRuleType = pointRuleType;
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

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getMemberClubName() {
		return memberClubName;
	}

	public void setMemberClubName(String memberClubName) {
		this.memberClubName = memberClubName;
	}
}