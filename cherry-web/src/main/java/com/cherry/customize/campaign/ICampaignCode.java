package com.cherry.customize.campaign;

public interface ICampaignCode {
	public String getSubCampaignCode(String brandCode,String orgId,String brandId,String userName) throws Exception;
	public String getCampaignCode(String brandCode,String orgId,String brandId,String userName) throws Exception;
}
