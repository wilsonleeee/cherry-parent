package com.cherry.customize.campaign;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;

public class CampaignCode_BEN implements ICampaignCode{

	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Override
	public  String getSubCampaignCode(String brandCode, String orgId, String brandId,String userName) throws Exception {
		// 品牌代码+4位序列号
		Map<String, Object> map = (Map<String, Object>)binOLCM03_BL.getActivityCodeList(orgId, brandId, userName, "J", 1);
		int num = Integer.parseInt(map.get("maxnum").toString());
		return brandCode.toUpperCase() +getStrIdentity(num,4);
	}

	@Override
	public String getCampaignCode(String brandCode, String orgId, String brandId,String userName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private String getStrIdentity(int identity,int len){
		String temp = String.valueOf(identity);
		while(temp.length()<len){
			temp = "0"+temp;
		}
		return temp;
	}
}
