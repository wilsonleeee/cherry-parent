package com.cherry.mb.rpt.form;


/**
 * 活动统计信息报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/12/29
 */
public class BINOLMBRPT08_Form {
	
	private String campaignCode;
	
	private String campaignName;
	
	private String startDate;
	
	private String endDate;

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

}
