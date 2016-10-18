/*	
 * @(#)JonValidate.java     1.0 2011/11/01		
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
package com.cherry.cp.jon.validate;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.act.util.ActUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.cherry.cp.common.validate.BaseValidate;

/**
 * 会员入会升降级 Validate
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class JonValidate extends BaseValidate{
	
	/** 购买模板索引 */
	private int BASE000002_INDEX;

	/** 累积时间索引 */
	private int BASE000003_INDEX;
	
	/** 购买产品索引 */
	private int BASE000001_INDEX;

	/** 升级索引 */
	private int BUS000006_INDEX;
	
	/** 降级索引 */
	private int BUS000010_INDEX;
	
	/** 降级设置索引 */
	private int BASE000011_INDEX;
	
	/** 失效设置索引 */
	private int BASE000013_INDEX;
	
	/**
	 * 验证购买模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000002_check(Map<String, Object> map, Map<String, Object> baseMap) {
		if (null != map) {
			// 最低消费金额
			String minAmount = (String) map.get("minAmount");
			// 最高消费金额
			String maxAmount = (String) map.get("maxAmount");
			// 最低消费金额不为空
			boolean isNotNullMin = !CherryChecker.isNullOrEmpty(minAmount);
			// 最高消费金额不为空
			boolean isNotNullMax = !CherryChecker.isNullOrEmpty(maxAmount);
			if (isNotNullMin) {
				// 检测指定字符串是否符合浮点数:整数10位,小数2位
				if (!CherryChecker.isFloatValid(minAmount, 10, 2)) {
					actionErrorList.add(new ActionErrorDTO(1, "minAmount-"
							+ BASE000002_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00006"), "10", "2" })) ;
					isCorrect = false;
				}
			}
			if (isNotNullMax) {
				if (!CherryChecker.isFloatValid(maxAmount, 10, 2)) {
					actionErrorList.add(new ActionErrorDTO(1, "maxAmount-"
							+ BASE000002_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00006"), "10", "2" })) ;
					isCorrect = false;
				}
			}
			if(isNotNullMin && isNotNullMax){
				if(Integer.parseInt(minAmount) > Integer.parseInt(maxAmount)){
					actionErrorList.add(new ActionErrorDTO(1, "maxAmount-"
							+ BASE000002_INDEX, "ECM00033", new String[] { PropertiesUtil.getText("PMB00007"), PropertiesUtil.getText("PMB00006") })) ;
					isCorrect = false;
				}
			}
			BASE000002_INDEX++;
		}
	}

	/**
	 * 累积时间模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000003_check(Map<String, Object> map, Map<String, Object> baseMap) {
		if (null != map) {
			if("1".equals(map.get("plusChoice"))){
				if("0".equals(map.get("plusTime"))){
					if(CherryChecker.isNullOrEmpty(map.get("monthNum"), true)){
						actionErrorList.add(new ActionErrorDTO(1, "monthNum-"
								+ BASE000003_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00008") })) ;
						isCorrect = false;
					}else if(!CherryChecker.isNumeric((String) map.get("monthNum"))){
						actionErrorList.add(new ActionErrorDTO(1, "monthNum-"
								+ BASE000003_INDEX, "ECM00030", new String[] { PropertiesUtil.getText("PMB00008") })) ;
						isCorrect = false;
					}
				}else if("1".equals(map.get("plusTime"))){
					if(CherryChecker.isNullOrEmpty(map.get("yearNum"), true)){
						actionErrorList.add(new ActionErrorDTO(1, "yearNum-"
								+ BASE000003_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00021") })) ;
						isCorrect = false;
					}else if(!CherryChecker.isNumeric((String) map.get("yearNum"))){
						actionErrorList.add(new ActionErrorDTO(1, "yearNum-"
								+ BASE000003_INDEX, "ECM00030", new String[] { PropertiesUtil.getText("PMB00021") })) ;
						isCorrect = false;
					}
					
				}else if("2".equals(map.get("plusTime"))){
					if(CherryChecker.isNullOrEmpty(map.get("normalYearNum"), true)){
						actionErrorList.add(new ActionErrorDTO(1, "normalYearNum-"
								+ BASE000003_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00021") })) ;
						isCorrect = false;
					}else if(!CherryChecker.isNumeric((String) map.get("normalYearNum"))){
						actionErrorList.add(new ActionErrorDTO(1, "normalYearNum-"
								+ BASE000003_INDEX, "ECM00030", new String[] { PropertiesUtil.getText("PMB00021") })) ;
						isCorrect = false;
					}
					
				}
			}
			// 最低消费
			String minAmount = (String) map.get("plusminAmount");
			// 最高消费
			String maxAmount = (String) map.get("plusmaxAmount");
			if(CherryChecker.isNullOrEmpty(minAmount, true)){
				actionErrorList.add(new ActionErrorDTO(1, "plusminAmount-" + 
						BASE000003_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00006") })) ;
				isCorrect = false;
			}else if (!CherryChecker.isFloatValid(minAmount, 10, 2)) {
				actionErrorList.add(new ActionErrorDTO(1, "plusminAmount-"
						+ BASE000003_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00006"), "10", "2" })) ;
				isCorrect = false;
			}
			if (null != maxAmount && !"".equals(maxAmount)) {
				if (!CherryChecker.isFloatValid(maxAmount, 10, 2)) {
					actionErrorList.add(new ActionErrorDTO(1, "plusmaxAmount-"
							+ BASE000003_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00006"), "10", "2" })) ;
					isCorrect = false;
				}
			}
			if(null != maxAmount && null != minAmount && !"".equals(maxAmount)&& !"".equals(minAmount)){
				if(Integer.parseInt(minAmount) > Integer.parseInt(maxAmount)){
					actionErrorList.add(new ActionErrorDTO(1, "plusmaxAmount-"
							+ BASE000003_INDEX, "ECM00033", new String[] { PropertiesUtil.getText("PMB00007"), PropertiesUtil.getText("PMB00006") })) ;
					isCorrect = false;
				}
			}
			BASE000003_INDEX++;
		}
	}
	
	/**
	 * 等级和有效期模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000010_check(Map<String, Object> map, Map<String, Object> baseMap){
		if (null == map || map.isEmpty()) {
			return;
		}
		if ("1".equals(map.get("isCounter"))) {
			try {
				//活动地点
				String placelist = ConvertUtil.getString(map
						.get(CampConstants.PLACE_JSON));
				List<Map<String, Object>> list = ConvertUtil.json2List(placelist);
				if(list==null||list.isEmpty()){
					 //活动地点不能为空验证
					 actionErrorList.add(new ActionErrorDTO(2, null, "ECM00009", new String[] {PropertiesUtil.getText("PCP00057")}));
				}
				if(!actionErrorList.isEmpty()){
					isCorrect = false;
				} 
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 购买产品模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000001_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			List<Map<String, Object>> productList = (List<Map<String, Object>>) map.get("productList");
			if("1".equals(map.get("prtRadio")) && null != productList && productList.isEmpty()){
				actionErrorList.add(new ActionErrorDTO(1, "errorMsg-" + BASE000001_INDEX, "ECM00054", new String[] { PropertiesUtil.getText("PMB00013")})) ;
				isCorrect = false;
			}
			BASE000001_INDEX++;
		}
	}
	
	/**
	 * 升级模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000006_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			List<Map<String, Object>> memberList = (List<Map<String, Object>>) map.get("combTemps");
			String compareLevel = (String) memberList.get(0).get("upMemberLevelId");
			String levelIdArr = null;
			for(int i = 1; i < memberList.size();i++){
				Map<String, Object> member = memberList.get(i);
				levelIdArr = (String) member.get("upMemberLevelId");
				compareLevel = compareLevel + "_" + levelIdArr;
			}
			if(null != levelIdArr){
				String[] str = compareLevel.split("_");
				for(int i = str.length - 1;i > 0;i--){
					for(int j = i-1;j >= 0;j--){
						if(str[i].equals(str[j])){
							actionErrorList.add(new ActionErrorDTO(1, "upMemberLevelId-" + i, "ECM00032", new String[] { PropertiesUtil.getText("PMB00014") })) ;
							isCorrect = false;
							i = 0;
							break;
						}
					}
				}
//				if(!CherryChecker.isNoDupString(compareLevel, "_")){
//					this.addFieldError("memberLevelId-" + BUS000006_INDEX, getText("ECM00032",
//							new String[] { "等级" }));
//					isCorrect = false;
//				}
			}
		}
		BUS000006_INDEX++;
	}
	
	/**
	 * 降级模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BUS000010_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			List<Map<String, Object>> memberList = (List<Map<String, Object>>) map.get("combTemps");
			String compareLevel = (String) memberList.get(0).get("downMemberLevelId");
			String levelIdArr = null;
			for(int i = 1; i < memberList.size();i++){
				Map<String, Object> member = memberList.get(i);
				levelIdArr = (String) member.get("downMemberLevelId");
				compareLevel = compareLevel + "_" + levelIdArr;
			}
			if(null != levelIdArr){
				String[] str = compareLevel.split("_");
				for(int i = str.length - 1;i > 0;i--){
					for(int j = i-1;j >= 0;j--){
						if(str[i].equals(str[j])){
							actionErrorList.add(new ActionErrorDTO(1, "downMemberLevelId-" + i, "ECM00032", new String[] { PropertiesUtil.getText("PMB00014") })) ;
							isCorrect = false;
							i = 0;
							break;
						}
					}
				}
			}
		}
		BUS000010_INDEX++;
	}
	/**
	 * 降级设置模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000011_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			if("1".equals(map.get("downLevel"))){
//				if(CherryChecker.isNullOrEmpty((String) map.get("minMoneyDown"))){
//					if(CherryChecker.isNullOrEmpty((String) map.get("minMoney"))){
//						actionErrorList.add(new ActionErrorDTO(1, "minMoneyDown-" + BASE000011_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00015") })) ;
//						isCorrect = false;
//					}
//				}else if(!CherryChecker.isFloatValid((String) map.get("minMoneyDown"), 10, 2)){
//					actionErrorList.add(new ActionErrorDTO(1, "minMoneyDown-" + BASE000011_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00015"), "10", "2" })) ;
//					isCorrect = false;
//				}
				boolean isNoMinTime = true;
				// 最低消费次数
				if(!CherryChecker.isNullOrEmpty((String) map.get("minTimeDown"))){
					isNoMinTime = false;
					if(!CherryChecker.isNumeric((String) map.get("minTimeDown"))){
						actionErrorList.add(new ActionErrorDTO(1, "minTimeDown-" + BASE000011_INDEX, "ECM00021", new String[] { PropertiesUtil.getText("PMB00016") })) ;
						isCorrect = false;
					}
				}
				boolean isNoMinMoney = true;
				// 最低消费金额
				if(!CherryChecker.isNullOrEmpty((String) map.get("minMoney"))){
					isNoMinMoney = false;
					if(!CherryChecker.isFloatValid((String) map.get("minMoney"), 10, 2)){
						actionErrorList.add(new ActionErrorDTO(1, "minMoney-" + BASE000011_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00015"), "10", "2" })) ;
						isCorrect = false;
					}
				}
				// 最低消费次数和最低消费金额都没有设置时
				if (isNoMinTime && isNoMinMoney) {
					actionErrorList.add(new ActionErrorDTO(1, "minMoney-" + BASE000011_INDEX, "ECP00029", null )) ;
					isCorrect = false;
				}
			}
			BASE000011_INDEX++;
		}
	}
	
	/**
	 * 失效设置模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000013_check(Map<String, Object> map, Map<String, Object> baseMap){
		if(null != map){
			if("1".equals(map.get("loseLevel"))){
				if(CherryChecker.isNullOrEmpty((String) map.get("minMoneyLose"))){
					actionErrorList.add(new ActionErrorDTO(1, "minMoneyLose-" + BASE000013_INDEX, "ECM00009", new String[] { PropertiesUtil.getText("PMB00015") })) ;
				isCorrect = false;
				}else if(!CherryChecker.isFloatValid((String) map.get("minMoneyLose"), 10, 2)){
					actionErrorList.add(new ActionErrorDTO(1, "minMoneyLose-" + BASE000013_INDEX, "ECM00024", new String[] { PropertiesUtil.getText("PMB00015"), "10", "2" })) ;
					isCorrect = false;
				}
				if(!CherryChecker.isNullOrEmpty((String) map.get("minTimeLose"))){
					if(!CherryChecker.isNumeric((String) map.get("minTimeLose"))){
						actionErrorList.add(new ActionErrorDTO(1, "minTimeLose-" + BASE000013_INDEX, "ECM00021", new String[] { PropertiesUtil.getText("PMB00016") })) ;
						isCorrect = false;
					}
				}
			}
			BASE000013_INDEX++;
		}
	}
	
	/**
	 * 首页模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
//	public boolean ValidateCamInfo(CampaignDTO campaignInfo) {
//		if(CherryChecker.isEmptyString(campaignInfo.getCampaignName())){
//			actionErrorList.add(new ActionErrorDTO(1, "campInfo.campaignName", "ECM00009", new String[] { PropertiesUtil.getText("PMB00017") })) ;
//			isCorrect = false;
//		}
//		if(campaignInfo.getCampaignName().length() > 50){
//			actionErrorList.add(new ActionErrorDTO(1, "campInfo.campaignName", "ECM00020", new String[] {PropertiesUtil.getText("PMB00017"),"50"})) ;
//			isCorrect = false;
//		}
//		if(campaignInfo.getDescriptionDtl().length() > 300){
//			actionErrorList.add(new ActionErrorDTO(1, "campInfo.descriptionDtl", "ECM00020", new String[] { PropertiesUtil.getText("PMB00018"),"300" })) ;
//			isCorrect = false;
//		}
//		return isCorrect;
//	}

}
