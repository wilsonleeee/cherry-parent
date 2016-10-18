/*	
 * @(#)ActValidateBIR.java     1.0 2013/01/24
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
import com.cherry.cp.common.dto.CampaignDTO;
import com.googlecode.jsonplugin.JSONException;

/**
 * 领用活动Validate
 * 
 * @author lipc
 * @version 1.0 2013/01/24
 */
public class ActValidateLYHD extends ActValidate {
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
				CampaignDTO dto = (CampaignDTO)baseMap.get(CampConstants.CAMP_INFO);
				//前几页活动参数
				Map<String, Object> commMap = ActUtil.getCampMap(baseMap, campNo);
				//活动名称
				String campName = ConvertUtil.getString(commMap
						.get(CampConstants.CAMP_NAME));
				if(CampConstants.REWARD_TYPE_2.equals(rewardType)){
					if(CampConstants.VIRT_PRM_FLAG_2.equals(virtualPrmFlag)){
						validateVirtBarCode(i, barCode, campName, campList);
					}
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
				}else{
					rewardInfo = ActUtil.replaceJson(rewardInfo);
					if("1".equals(dto.getGroupFlag())){
						Map<String,Object> rewardMap = ConvertUtil.json2Map(rewardInfo);
						List<Map<String,Object>> logicOptArr = (List<Map<String,Object>>)rewardMap.get(CampConstants.LOGIC_OPT_ARR);
						if(null != logicOptArr && logicOptArr.size() > 0){
							for(Map<String,Object> box : logicOptArr){
								String logicOpt2 = ConvertUtil.getString(box.get(CampConstants.LOGIC_OPT));
								List<Map<String,Object>> logicOptArr2 = (List<Map<String,Object>>)box.get(CampConstants.LOGIC_OPT_ARR);
								if(null != logicOptArr2 && logicOptArr2.size() > 0){
									//验证兑换礼品格式
									validatePrmORPro(campName ,logicOptArr2);
								}else{
									//  **组合内容不能为空
									String msg = "";
									if("AND".equalsIgnoreCase(logicOpt2)){
										msg = PropertiesUtil.getText("ACT00048");
									}else{
										msg = PropertiesUtil.getText("ACT00047");
									}
									actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {campName,msg}));
								}
							}
						}else{
							//  奖励内容不能为空
							actionErrorList.add(new ActionErrorDTO(2, null, "ECM00078", new String[] {campName,PropertiesUtil.getText("ACT00014")}));
						}
					}else{
						List<Map<String, Object>> prtlist = ConvertUtil.json2List(rewardInfo);
						//验证兑换礼品格式
						validatePrmORPro(campName ,prtlist);
					}
				}
			}
			if(!actionErrorList.isEmpty()){
				isCorrect = false;
			}
		}
	}
}
