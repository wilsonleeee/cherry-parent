/*	
 * @(#)ActValidatePY.java     1.0 2013/01/24
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
 * 积分兑现 Validate
 * 
 * @author lipc
 * @version 1.0 2013/01/24
 */
public class ActValidatePY extends ActValidate {
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
				//活动编号
				String campNo = ConvertUtil.getString(tempMap
						.get(CampConstants.CAMP_NO));
				//积分
				String exPoint= ConvertUtil.getString(tempMap
						.get(CampConstants.EXPOINT));
				//礼品信息
				String rewardInfo = ConvertUtil.getString(tempMap
						.get(CampConstants.REWARD_INFO));
				String virtualPrmFlag = ConvertUtil.getString(tempMap
						.get(CampConstants.VIRT_PRM_FLAG));
				//虚拟促销品条码
				String barCode = ConvertUtil.getString(tempMap
						.get(CherryConstants.BARCODE));
				//前几页活动参数
				Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
				//活动名称
				String campName = ConvertUtil.getString(commMap.get(CampConstants.CAMP_NAME));
				
				if(CampConstants.VIRT_PRM_FLAG_2.equals(virtualPrmFlag)){
					validateVirtBarCode(i, barCode, campName, campList);
				}
				if(!CampConstants.VIRT_PRM_FLAG_3.equals(virtualPrmFlag)){
					//积分值验证
					if ("".equals(exPoint)) {
						//积分为空验证
						actionErrorList.add(new ActionErrorDTO(2, null, "ACT00004", new String[] {campName, PropertiesUtil.getText("ACT00013")}));
					}else {
						if(exPoint.length()>10){
							//积分长度验证
							actionErrorList.add(new ActionErrorDTO(2, null, "ECM00079", new String[] {campName, PropertiesUtil.getText("ACT00013"),"10"}));
							break;
						}
						if(!CherryChecker.isNumeric(exPoint)){
							//积分格式验证
							actionErrorList.add(new ActionErrorDTO(2, null, "ACT00005", new String[] {campName, PropertiesUtil.getText("ACT00013")}));
							break;
						}
					}
				}else{
					if(!"".equals(rewardInfo)){
						List<Map<String, Object>> prtlist = ConvertUtil.json2List(rewardInfo);
						validatePrmORPro(campName, prtlist);
					}
				}
			}
		}
		if(!actionErrorList.isEmpty()){
			isCorrect = false;
		}
	}
}
