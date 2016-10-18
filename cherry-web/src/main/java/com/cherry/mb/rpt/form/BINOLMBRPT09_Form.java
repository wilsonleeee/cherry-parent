package com.cherry.mb.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 按柜台统计活动信息报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/12/25
 */
public class BINOLMBRPT09_Form extends DataTable_BaseForm {
	
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
