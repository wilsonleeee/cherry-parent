package com.cherry.mb.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 活动预约明细报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/12/29
 */
public class BINOLMBRPT07_Form extends DataTable_BaseForm {
	
	private String campaignCode;
	
	private String campaignName;
	
	private String startDate;
	
	private String endDate;
	
	private String memberInfoId;

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
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

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

}
