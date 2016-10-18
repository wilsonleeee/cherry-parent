package com.cherry.mb.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 回柜会员销售明细报表Form
 * 
 * @author WangCT
 * @version 1.0 2015/01/09
 */
public class BINOLMBRPT10_Form extends DataTable_BaseForm {
	
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
