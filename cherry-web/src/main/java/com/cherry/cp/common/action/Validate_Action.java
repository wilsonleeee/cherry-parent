/*	
 * @(#)Validate_Action.java     1.0 2011/7/18		
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
package com.cherry.cp.common.action;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cp.common.dto.CampaignDTO;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员活动验证共通 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class Validate_Action extends BaseAction{

	private static final long serialVersionUID = 6991710368069500049L;

	/** 参数验证结果 */
	public boolean isCorrect = true;
	
	/** 单次购买或者累积购买选中 */
	public boolean BUS000001_isChecked = false;

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
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private void invokeMd(String mdName, Map<String, Object> map, Map<String, Object> baseMap) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					method.invoke(this, map, baseMap);
					break;
				}
			}
		} catch (Exception e) {
			// return;
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param String
	 *            活动模板信息
	 * 
	 */
	public void validateForm(String camTemps, Map<String, Object> baseMap) {
		try {
			// 活动模板List
			List<Map<String, Object>> camTempList = (List<Map<String, Object>>) JSONUtil
					.deserialize(String.valueOf(camTemps));
			validateCamTemp(camTempList, baseMap);
		} catch (Exception e) {
			isCorrect = false;
		}
	}
	
	/**
	 * 验证提交的模板内容
	 * 
	 * @param List
	 *            活动模板List
	 * 
	 */
	private void validateCamTemp (List<Map<String, Object>> camTempList, Map<String, Object> baseMap) {
		if (null != camTempList) {
			for (Map<String, Object> camTemp : camTempList) {
				// 模板编号
				String tempCode = (String) camTemp.get("tempCode");
				invokeMd(tempCode + "_check", camTemp, baseMap);
				if (camTemp.containsKey("combTemps")) {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
					validateCamTemp(combTempList, baseMap);
				}
			}
		}
	}
	
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
			// 最低消费
			String minAmount = (String) map.get("minAmount");
			// 最高消费
			String maxAmount = (String) map.get("maxAmount");
			if (null != minAmount && !"".equals(minAmount)) {
				if (!CherryChecker.isFloatValid(minAmount, 10, 2)) {
					this.addFieldError("minAmount-"
							+ BASE000002_INDEX, getText("ECM00024",
							new String[] { getText("PMB00006"), "10", "2" }));
					isCorrect = false;
				}
			}
			if (null != maxAmount && !"".equals(maxAmount)) {
				if (!CherryChecker.isFloatValid(maxAmount, 10, 2)) {
					this.addFieldError("maxAmount-"
							+ BASE000002_INDEX, getText("ECM00024",
							new String[] { getText("PMB00007"), "10", "2" }));
					isCorrect = false;
				}
			}
			if(null != maxAmount && null != minAmount && !"".equals(maxAmount)&& !"".equals(minAmount)){
				if(Integer.parseInt(minAmount) > Integer.parseInt(maxAmount)){
					this.addFieldError("maxAmount-"
							+ BASE000002_INDEX, getText("ECM00033",
							new String[] { getText("PMB00007"), getText("PMB00006") }));
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
			// 月数
			String monthNum = (String) map.get("monthNum");
			// 月数验证
			if (null != monthNum && !"".equals(monthNum)) {
				// 是否为数字
				if (!CherryChecker.isNumeric(monthNum)) {
					this.addFieldError("monthNum-"
							+ BASE000003_INDEX, getText("ECM00021",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if (monthNum.length() > 2) {
					this.addFieldError("monthNum-"
							+ BASE000003_INDEX, getText("ECM00020",
							new String[] { getText("PMB00008"), "2" }));
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
		if(null != map){
			String FromDate = (String) baseMap.get("campaignFromDate");
			String ToDate = (String) baseMap.get("campaignToDate");
			if(!CherryChecker.checkDate((String) map.get("fromDate"))){
				this.addFieldError("fromDate", getText("ECM00022", new String[] {getText("PMB00009")}));
				isCorrect = false;
			}else if(!CherryChecker.checkDate((String) map.get("toDate"))){
				this.addFieldError("toDate", getText("ECM00022", new String[] {getText("PMB00010")}));
				isCorrect = false;
			}else if(CherryChecker.compareDate((String) map.get("fromDate"), FromDate) < 0){
				this.addFieldError("fromDate", getText("ECM00027", new String[] {getText("PMB00009"), getText("PMB00011")}));
				isCorrect = false;
			}else if(CherryChecker.compareDate((String) map.get("toDate"), ToDate) > 0){
				this.addFieldError("toDate", getText("ECM00033", new String[] {getText("PMB00010"), getText("PMB00012")}));
				isCorrect = false;
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
				this.addFieldError("errorMsg-" + BASE000001_INDEX, getText("ECM00045", new String[] {getText("PMB00013")}));
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
							this.addFieldError("upMemberLevelId-" + i, getText("ECM00032",
									new String[] { getText("PMB00014") }));
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
							this.addFieldError("downMemberLevelId-" + i, getText("ECM00032",
									new String[] { getText("PMB00014") }));
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
				if(CherryChecker.isNullOrEmpty((String) map.get("monthNumDown"))){
					this.addFieldError("monthNumDown-" + BASE000011_INDEX, getText("ECM00009",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("monthNumDown"))){
					this.addFieldError("monthNumDown-" + BASE000011_INDEX, getText("ECM00021",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(((String) map.get("monthNumDown")).length() > 2){
					this.addFieldError("monthNumDown-"
							+ BASE000011_INDEX, getText("ECM00020",
							new String[] { getText("PMB00008"), "2" }));
					isCorrect = false;
				}
				if(CherryChecker.isNullOrEmpty((String) map.get("minMoneyDown"))){
					this.addFieldError("minMoneyDown-" + BASE000011_INDEX, getText("ECM00009",
						new String[] { getText("PMB00015") }));
				isCorrect = false;
				}else if(!CherryChecker.isFloatValid((String) map.get("minMoneyDown"), 10, 2)){
					this.addFieldError("minMoneyDown-" + BASE000011_INDEX, getText("ECM00024",
							new String[] { getText("PMB00015"), "10", "2" }));
					isCorrect = false;
				}
				if(CherryChecker.isNullOrEmpty((String) map.get("minTimeDown"))){
					this.addFieldError("minTimeDown-" + BASE000011_INDEX, getText("ECM00009",
							new String[] { getText("PMB00016") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("minTimeDown"))){
					this.addFieldError("minTimeDown-" + BASE000011_INDEX, getText("ECM00021",
							new String[] { getText("PMB00016") }));
					isCorrect = false;
				}
			}else if("0".equals(map.get("downLevel"))){
				if(CherryChecker.isNullOrEmpty((String) map.get("fullMonthDown"))){
					this.addFieldError("fullMonthDown-" + BASE000011_INDEX, getText("ECM00009",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("fullMonthDown"))){
					this.addFieldError("fullMonthDown-" + BASE000011_INDEX, getText("ECM00021",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(((String) map.get("fullMonthDown")).length() > 2){
					this.addFieldError("fullMonthDown-"
							+ BASE000011_INDEX, getText("ECM00020",
							new String[] { getText("PMB00008"), "2" }));
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
				if(CherryChecker.isNullOrEmpty((String) map.get("monthNumLose"))){
					this.addFieldError("monthNumLose-" + BASE000013_INDEX, getText("ECM00009",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("monthNumLose"))){
					this.addFieldError("monthNumLose-" + BASE000013_INDEX, getText("ECM00021",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(((String) map.get("monthNumLose")).length() > 2){
					this.addFieldError("monthNumLose-"
							+ BASE000013_INDEX, getText("ECM00020",
							new String[] { getText("PMB00008"), "2" }));
					isCorrect = false;
				}
				if(CherryChecker.isNullOrEmpty((String) map.get("minMoneyLose"))){
					this.addFieldError("minMoneyLose-" + BASE000013_INDEX, getText("ECM00009",
						new String[] { getText("PMB00015") }));
				isCorrect = false;
				}else if(!CherryChecker.isFloatValid((String) map.get("minMoneyLose"), 10, 2)){
					this.addFieldError("minMoneyLose-" + BASE000013_INDEX, getText("ECM00024",
							new String[] { getText("PMB00015"), "10", "2" }));
					isCorrect = false;
				}
				if(CherryChecker.isNullOrEmpty((String) map.get("minTimeLose"))){
					this.addFieldError("minTimeLose-" + BASE000013_INDEX, getText("ECM00009",
							new String[] { getText("PMB00016") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("minTimeLose"))){
					this.addFieldError("minTimeLose-" + BASE000013_INDEX, getText("ECM00021",
							new String[] { getText("PMB00016") }));
					isCorrect = false;
				}
			}else if("0".equals(map.get("loseLevel"))){
				if(CherryChecker.isNullOrEmpty((String) map.get("fullMonthLose"))){
					this.addFieldError("fullMonthLose-" + BASE000013_INDEX, getText("ECM00009",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(!CherryChecker.isNumeric((String) map.get("fullMonthLose"))){
					this.addFieldError("fullMonthLose-" + BASE000013_INDEX, getText("ECM00021",
							new String[] { getText("PMB00008") }));
					isCorrect = false;
				}else if(((String) map.get("fullMonthLose")).length() > 2){
					this.addFieldError("fullMonthLose-"
							+ BASE000013_INDEX, getText("ECM00020",
							new String[] { getText("PMB00008"), "2" }));
					isCorrect = false;
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
	public boolean ValidateCamInfo(CampaignDTO campaignInfo) {
		if(CherryChecker.isEmptyString(campaignInfo.getCampaignName())){
			this.addFieldError("campInfo.campaignName",getText("ECM00009",new String[]{getText("PMB00017")}));
			isCorrect = false;
		}
		if(campaignInfo.getCampaignName().length() > 50){
			this.addFieldError("campInfo.campaignName",getText("ECM00020",new String[]{getText("PMB00017"),"50"}));
			isCorrect = false;
		}
		if(campaignInfo.getDescriptionDtl().length() > 300){
			this.addFieldError("campInfo.descriptionDtl",getText("ECM00020",new String[]{getText("PMB00018"),"300"}));
			isCorrect = false;
		}
		return isCorrect;
	}
}
