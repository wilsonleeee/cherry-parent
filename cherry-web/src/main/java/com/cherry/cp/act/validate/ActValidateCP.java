/*	
 * @(#)ActValidateCP.java     1.0 2013/12/26
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.cp.act.validate;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.googlecode.jsonplugin.JSONException;


/**
 * 优惠券Validate
 * 
 * @author LuoHong
 * @version 1.0 2013/12/26
 */
public class ActValidateCP extends ActValidate {
	/**
	 * 验证活动奖励模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BASE000061_check(Map<String, Object> map,
			Map<String, Object> baseMap) throws JSONException {
		List<Map<String, Object>> campList=(List<Map<String, Object>>) map.get(CampConstants.CAMP_LIST);
		if (campList != null) {
			for (int i = 0; i < campList.size(); i++) {
				Map<String, Object> tempMap = campList.get(i);
				//子活动编号
				String campNo = ConvertUtil.getString(tempMap
						.get(CampConstants.CAMP_NO));
				String rewardInfo = ConvertUtil.getString(tempMap
						.get(CampConstants.REWARD_INFO));
				//活动奖励类型
				String rewardType = ConvertUtil.getString(tempMap
						.get(CampConstants.REWARD_TYPE));
				//虚拟促销品条码
				String barCode = ConvertUtil.getString(tempMap
						.get(CherryConstants.BARCODE));
				String virtualPrmFlag = ConvertUtil.getString(tempMap
						.get(CampConstants.VIRT_PRM_FLAG));
				//前几页活动参数
				Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
				//活动名称
				String campName = ConvertUtil.getString(commMap
						.get(CampConstants.CAMP_NAME));
				//优惠券数量
				int couponCount = ConvertUtil.getInt(tempMap
						.get("couponCount"));
				//优惠券类型
				String couponType = ConvertUtil.getString(tempMap
						.get("couponType"));
				if(CampConstants.REWARD_TYPE_2.equals(rewardType)){
					if(CampConstants.VIRT_PRM_FLAG_2.equals(virtualPrmFlag)){
						validateVirtBarCode(i, barCode, campName, campList);
					}
					if(!CherryChecker.isNullOrEmpty(couponType)){
						//Coupon数量验证
						if("3".equals(couponType)){
							if(CherryChecker.isNullOrEmpty(tempMap.get("batchCode"))){
								//导入Coupon数量不能为空
								actionErrorList.add(new ActionErrorDTO(2, null, "ACT00004", new String[] {campName,PropertiesUtil.getText("ACT00050")}));
								break;
							}
						}else{
							if(couponCount==0){
								//Coupon数量必须大于0
								actionErrorList.add(new ActionErrorDTO(2, null, "ACT00001", new String[] {campName,PropertiesUtil.getText("ACT00049")}));
								break;
							}
						}
					}
					if(CampConstants.VIRT_PRM_FLAG_3.equals(virtualPrmFlag)){
						List<Map<String, Object>> prtlist = ConvertUtil.json2List(rewardInfo);
						//验证兑换礼品格式
						validatePrmORPro(campName ,prtlist);
					}else{
						if ("".equals(rewardInfo)) {
							//为空验证
							actionErrorList.add(new ActionErrorDTO(2, null, "ACT00004", new String[] {campName, PropertiesUtil.getText("ACT00046")}));
							break;
						}else {
							if(!CherryChecker.isFloatValid(rewardInfo, 10, 2)){
								//积分格式验证
								actionErrorList.add(new ActionErrorDTO(2, null, "ECM00083", 
										new String[] {campName, PropertiesUtil.getText("ACT00046"),"10","2"}));
								break;
							}
						}
					}
				}
			}
			if(!actionErrorList.isEmpty()){
				isCorrect = false;
			}
		}
	}
}