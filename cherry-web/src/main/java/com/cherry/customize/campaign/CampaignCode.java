package com.cherry.customize.campaign;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.SpringBeanManager;

/**
 * 活动码生成专用类
 * 各品牌可能会定制编码规则，使用此类做为统一入口，方便编程。
 * 各品牌可以实现自己的活动码生成类，但是必须实现ICampaignCode接口，且配置在xml中时，命名必须形如：
 * <bean id="campaignCode_XXX" class="com.cherry.customize.campaign.CampaignCode_YYY"/>
 * 其中XXX必须是品牌代码,全大写，YYY则随意，只是用于区分不同的类名，统一设置为品牌代码最为明了。
 * @author dingyongchang
 *
 */
public class CampaignCode  {
		
	/**
	 * 获取子活动代码
	 * @param brandCode
	 * @param orgId
	 * @param brandId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public  static String getSubCampaignCode(String brandCode, String orgId, String brandId,String userName) throws Exception {
		Object obj = SpringBeanManager.getBean("campaignCode_"+brandCode.toUpperCase());		
		if(null!=obj){
			//如果品牌配置了专用类
			ICampaignCode bean = (ICampaignCode)obj;	
			return bean.getSubCampaignCode(brandCode, orgId, brandId,userName);
		}else{
			//使用默认的共通取号
			obj = SpringBeanManager.getBean("binOLCM03_BL");
			BINOLCM03_BL binOLCM03_BL = (BINOLCM03_BL)obj;
			return	binOLCM03_BL.getTicketNumber(orgId,brandId,userName,"AT");	
		}
	}


	public static String getCampaignCode(String brandCode, String orgId, String brandId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
