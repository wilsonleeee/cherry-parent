/*	
 * @(#)PointCreateRule.java     1.0 2012/02/20		
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
package com.cherry.cp.point.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleConditionDTO;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.CreateRule;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员积分创建规则处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class PointCreateRule extends CreateRule{
	
	/**
	 * 等级和有效期模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO
	 * 				规则内容
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000015_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		// 有效期开始日
		String campaignFromDate = campaignDTO.getCampaignFromDate();
		// 有效期结束日
		String campaignToDate = campaignDTO.getCampaignToDate();
		if (CherryChecker.isNullOrEmpty(campaignFromDate)) {
			throw new CherryException("ECP00002", new String[]{PropertiesUtil.getText("PCP00008"), PropertiesUtil.getText("PCP00009")});
		}
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		// 共通条件
		ruleBody.setCommCondition(true);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS1);
		// 验证方法名:验证有效期
		ruleBody.setCheckerMethod("checkDateG15");
		// 扩展参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		// 有效期开始日
		lhsParams.put("campaignFromDate", campaignFromDate);
		// 有效期开始日
		lhsParams.put("campaignToDate", campaignToDate);
		// 会员等级区分
		lhsParams.put("levelKbn", tempMap.get("member"));
		// 会员等级List
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) tempMap.get("memberLevelList");
		List<Map<String, Object>> memLevelList = new ArrayList<Map<String, Object>>();
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				if ("1".equals(levelInfo.get("memberShowFlag"))) {
					Map<String, Object> memLevel = new HashMap<String, Object>();
					memLevel.put("memberLevelId", levelInfo.get("memberLevelId"));
					memLevelList.add(memLevel);
				}
			}
		}
		if (!memLevelList.isEmpty()) {
			lhsParams.put("memberLevelList", memLevelList);
		}
		List<Map<String, Object>> chlCdList = (List<Map<String, Object>>) tempMap.get("chlCdList");
		if (null != chlCdList && !chlCdList.isEmpty()) {
			lhsParams.put("chlCdList", chlCdList);
		}
		if ("PT01".equals(campaignDTO.getTemplateType()) || "PT05".equals(campaignDTO.getTemplateType())
				|| "PT04".equals(campaignDTO.getTemplateType())) {
			// 规则模板类型
			lhsParams.put("TemType", campaignDTO.getTemplateType());
		}
		// 推荐区分
		if ("1".equals(tempMap.get("referKbn"))) {
			lhsParams.put("referKbn", "1");
		}
		if ("1".equals(tempMap.get("refPTKbn"))) {
			lhsParams.put("refPTKbn", "1");
		}
		// 首单时间
		if ("2".equals(tempMap.get("datecp"))) {
			lhsParams.put("datecp", "2");
		}
		// 积分范围
		if ("2".equals(tempMap.get("mPoint"))) {
			lhsParams.put("mPoint", "2");
			lhsParams.put("minpt", tempMap.get("minpt"));
			lhsParams.put("maxpt", tempMap.get("maxpt"));
		}
		// 入会日期
		lhsParams.put("jnDateTy", tempMap.get("jnDate"));
		// 会员生肖
		lhsParams.put("zodiac", tempMap.get("zodiac"));
		// 柜台类别
		lhsParams.put("counterKbn", tempMap.get("counterKbn"));
		// 非开卡柜台消费时
		if ("1".equals(tempMap.get("ctKbn"))) {
			lhsParams.put("ctKbn", tempMap.get("ctKbn"));
		}
		// 地点区分
		lhsParams.put("locationType", tempMap.get("locationType"));
		// 地点列表
		lhsParams.put("nodesList", tempMap.get("nodesList"));
		// 是否精确到秒
		lhsParams.put("timeSetting", tempMap.get("timeSetting"));
		// 开始时
		String startHH = (String) tempMap.get("startHH");
		lhsParams.put("startHH", startHH);
		// 开始分
		String startMM = (String) tempMap.get("startMM");
		lhsParams.put("startMM", startMM);
		// 开始秒
		String startSS = (String) tempMap.get("startSS");
		lhsParams.put("startSS", tempMap.get("startSS"));
		// 结束时
		lhsParams.put("endHH", tempMap.get("endHH"));
		// 结束分
		lhsParams.put("endMM", tempMap.get("endMM"));
		// 结束秒
		lhsParams.put("endSS", tempMap.get("endSS"));
		// 条件参数
		ruleBody.setLhsParams(lhsParams);
		Map<String, Object> commRhsParams = new HashMap<String, Object>();
		// 积分类型
		commRhsParams.put("POINT_TYPE", tempMap.get("pointType"));
		// 附属方式
		commRhsParams.put("SUBRULE_KBN", tempMap.get("subRuleKbn"));
		// 规则属性
		commRhsParams.put("POINTRULE_KBN", tempMap.get("pointRuleKbn"));
		String fromTime = "00:00:00";
		if (!CherryChecker.isNullOrEmpty(startHH, true) && !CherryChecker.isNullOrEmpty(startMM, true) &&
					!CherryChecker.isNullOrEmpty(startSS, true)) {
			fromTime = startHH + ":" + startMM + ":" + startSS;
		}
		// 活动开始日期
		commRhsParams.put("RULEFROMDATE", (campaignFromDate + " " + fromTime));
		ruleBody.setCommRhsParams(commRhsParams);
		return ruleBody;
	}
	
	/**
	 * 积分清零条件
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO
	 * 				规则内容
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000008000_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		// 有效期开始日
		String campaignFromDate = campaignDTO.getCampaignFromDate();
		// 有效期结束日
		String campaignToDate = campaignDTO.getCampaignToDate();
		if (CherryChecker.isNullOrEmpty(campaignFromDate)) {
			throw new CherryException("ECP00002", new String[]{PropertiesUtil.getText("PCP00008"), PropertiesUtil.getText("PCP00009")});
		}
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		// 共通条件
		ruleBody.setCommCondition(true);
		// 执行时不需要优先级控制
		ruleBody.setPriority(false);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证有效期
		ruleBody.setCheckerMethod("checkPCRule");
		// 扩展参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		// 有效期开始日
		lhsParams.put("campaignFromDate", campaignFromDate);
		// 有效期开始日
		lhsParams.put("campaignToDate", campaignToDate);
		// 会员等级区分
		lhsParams.put("levelKbn", tempMap.get("member"));
		// 会员等级List
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) tempMap.get("memberLevelList");
		List<Map<String, Object>> memLevelList = new ArrayList<Map<String, Object>>();
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				if ("1".equals(levelInfo.get("memberShowFlag"))) {
					Map<String, Object> memLevel = new HashMap<String, Object>();
					memLevel.put("memberLevelId", levelInfo.get("memberLevelId"));
					memLevelList.add(memLevel);
				}
			}
		}
		if (!memLevelList.isEmpty()) {
			lhsParams.put("memberLevelList", memLevelList);
		}
		// 地点区分
		lhsParams.put("locationType", tempMap.get("locationType"));
		// 地点列表
		lhsParams.put("nodesList", tempMap.get("nodesList"));
		// 条件参数
		ruleBody.setLhsParams(lhsParams);
		Map<String, Object> commRhsParams = new HashMap<String, Object>();
		String fromTime = "00:00:00";
		// 活动开始日期
		commRhsParams.put("RULEFROMDATE", (campaignFromDate + " " + fromTime));
		ruleBody.setCommRhsParams(commRhsParams);
		return ruleBody;
	}
	
	/**
	 * 会员入会积分奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000031_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证会员入会
		ruleBody.setCheckerMethod("checkJoinPoint");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 入会等级 
		lhsParams.put("memberLevelId", tempMap.get("memberLevelId"));
		// 赠与积分时间
		String jonDate = (String) tempMap.get("jonDate");
		lhsParams.put("joinDateKbn", jonDate);
		// 入会后(天)
		if ("2".equals(jonDate)) {
			// 入会后天数
			lhsParams.put("afterDays", tempMap.get("jonDateLimit"));
			// 是否包含入会当天
			lhsParams.put("theDay", tempMap.get("theDay"));
			// 入会后(月)
		} else if ("3".equals(jonDate)) {
			// 入会后月数
			lhsParams.put("afterDays", tempMap.get("jonMonthLimit"));
			// 是否包含入会当天
			lhsParams.put("theMonth", tempMap.get("theMonth"));
			// 是否截止到月末
			lhsParams.put("monthEnd", tempMap.get("monthEnd"));
		}
		// 选择单次
		String firstBillSel = (String) tempMap.get("firstBillSel");
		lhsParams.put("firstBillSel", firstBillSel);
		// 指定单次
		if ("3".equals(firstBillSel)) {
			lhsParams.put("billTime", tempMap.get("billTime"));
		}
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 单次购买积分奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000032_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS1);
		// 验证方法名:验证单次购买
		ruleBody.setCheckerMethod("checkNormalBuy");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 入会等级 
		lhsParams.put("noReturn", "1");
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 默认积分奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000068_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		// 结果参数
		Map<String, Object> rhsParams = new HashMap<String, Object>();
		// 赠送积分值
		String multipleMark = (String) tempMap.get("pointMultiple");
		// 赠送积分方式
		rhsParams.put("calcuKbn", "1");
		// 赠送积分值
		rhsParams.put("calcuVal", multipleMark);
		List<Map<String, Object>> memLevelList = (List<Map<String, Object>>) tempMap.get("memLevelList");
		if (null != memLevelList && !memLevelList.isEmpty()) {
			List<Map<String, Object>> memberLevelList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> memLevel : memLevelList) {
				Map<String, Object> memberLevel = new HashMap<String, Object>();
				memberLevel.put("levelId", memLevel.get("memberLevelId"));
				memberLevel.put("ptmlt", memLevel.get("ptmlt"));
				memberLevelList.add(memberLevel);
			}
			rhsParams.put("MLELList", memberLevelList);
		}
		ruleBody.setRhsParams(rhsParams);
		return ruleBody;
	}
	
	/**
	 * 积分清零方式
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000008001_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 执行时不需要优先级控制
		ruleBody.setPriority(false);
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		// 结果参数
		Map<String, Object> rhsParams = new HashMap<String, Object>();
		// 起始日
		String pcFromDate = (String) tempMap.get("pcFromDate");
		// 时长
		String lengthKbn = (String) tempMap.get("lengthKbn");
		// 清零日
		String pcExecDate = (String) tempMap.get("pcExecDate");
		// 起始日
		rhsParams.put("pcFromDate", pcFromDate);
		// 时长
		rhsParams.put("lengthKbn", lengthKbn);
		// 清零日
		rhsParams.put("pcExecDate", pcExecDate);
		if ("0".equals(lengthKbn)) {
			// 间隔时间
			rhsParams.put("clearNum", tempMap.get("clearNum"));
			// 月/年
			rhsParams.put("numType", tempMap.get("numType"));
			if ("1".equals(tempMap.get("curKbn"))) {
				rhsParams.put("curKbn", "1");
			}
		} else {
			// 间隔年数
			rhsParams.put("yearNo", tempMap.get("yearNo"));
			// 失效日(月)
			rhsParams.put("disMonth", tempMap.get("disMonth"));
			// 失效日(日)
			rhsParams.put("disDay", tempMap.get("disDay"));
		}
		if ("2".equals(pcExecDate)) {
			// 执行月
			rhsParams.put("execMonth", tempMap.get("execMonth"));
			// 执行日
			rhsParams.put("execDay", tempMap.get("execDay"));
		} else if ("3".equals(pcExecDate)) {
			// 执行日
			rhsParams.put("pcexDay", tempMap.get("pcexDay"));
		}
		// 特殊参数
		if ("1".equals(tempMap.get("keysCheck"))) {
			String speciKeys = (String) tempMap.get("speciKeys");
			if (!CherryChecker.isNullOrEmpty(speciKeys, true)) {
				speciKeys = "{" + speciKeys + "}";
				Map<String, Object> speciKeysMap = (Map<String, Object>) JSONUtil.deserialize(speciKeys);
				rhsParams.putAll(speciKeysMap);
			}
		}
		// 清零延期
		if ("1".equals(tempMap.get("yanqiCheck"))) {
			List<Map<String, Object>> prtList = (List<Map<String, Object>>) tempMap.get("productList");
			String yqprt = (String) tempMap.get("yanqiprt");
			// 特定商品
			if (null != prtList && !prtList.isEmpty() && !CherryChecker.isNullOrEmpty(yqprt)) {
				List<Map<String, Object>> prtRuleList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> prtMap : prtList) {
					Map<String, Object> prtInfo = new HashMap<String, Object>();
					// 产品
					if ("1".equals(yqprt)) {
						prtInfo.put("proId", prtMap.get("proId"));
						// 促销礼品
					} else {
						prtInfo.put("prmId", prtMap.get("prmId"));
					}
					prtRuleList.add(prtInfo);
				}
				rhsParams.put("yqprt", yqprt);
				rhsParams.put("prtList", prtRuleList);
			}
			if (!CherryChecker.isNullOrEmpty(tempMap.get("ptreson"))) {
				rhsParams.put("ptreson", tempMap.get("ptreson"));
			}
			rhsParams.put("yanqi", "1");
			rhsParams.put("yanqiNum", tempMap.get("yanqiNum"));
			rhsParams.put("yanqiType", tempMap.get("yanqiType"));
		}
		ruleBody.setRhsParams(rhsParams);
		return ruleBody;
	}
	
	/**
	 * 特定时间奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000019_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证特定时间
		ruleBody.setCheckerMethod("checkSpecDate");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 首单时间 
		lhsParams.put("firstBillDate", tempMap.get("firstBillDate"));
		// 指定的月份
		lhsParams.put("billMonth", tempMap.get("billMonth"));
		// 指定月
		lhsParams.put("billStartMonth", tempMap.get("billStartMonth"));
		// 指定日
		lhsParams.put("billStartDay", tempMap.get("billStartDay"));
		// 指定的开始日期
		lhsParams.put("billStartTime", tempMap.get("billStartTime"));
		// 指定的结束日期
		lhsParams.put("billEndTime", tempMap.get("billEndTime"));
		if ("6".equals(tempMap.get("firstBillDate"))) {
			lhsParams.put("bindDayLimit", tempMap.get("bindDayLimit"));
			if ("1".equals(tempMap.get("bindDay"))) {
				lhsParams.put("bindDay", "1");
			}
		}
		// 选择单次
		String firstBillSel = (String) tempMap.get("firstBillSel");
		lhsParams.put("firstBillSel", firstBillSel);
		// 指定单次
		if ("3".equals(firstBillSel)) {
			lhsParams.put("billTime", tempMap.get("billTime"));
		}
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 会员生日奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000022_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证会员生日
		ruleBody.setCheckerMethod("checkBirthday");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 活动周期
		String birthday = (String) tempMap.get("birthday");
		lhsParams.put("birthday", birthday);
		// 生日当周
		if ("2".equals(birthday)) {
			// 前几天
			lhsParams.put("befDay", tempMap.get("befDay"));
			// 后几天
			lhsParams.put("aftDay", tempMap.get("aftDay"));
			// 是否包含生日当天
			lhsParams.put("theBirth", tempMap.get("theBirth"));
		} else if ("3".equals(birthday)) {
			// 生日月
			lhsParams.put("spMonth", tempMap.get("spMonth"));
			// 生日日
			lhsParams.put("spDay", tempMap.get("spDay"));
		}
		// 选择单次
		String firstBillSel = (String) tempMap.get("firstBillSel");
		lhsParams.put("firstBillSel", firstBillSel);
		// 指定单次
		if ("3".equals(firstBillSel)) {
			lhsParams.put("billTime", tempMap.get("billTime"));
		}
		// 特殊生日
		if ("1".equals(tempMap.get("specBirth"))) {
			lhsParams.put("specBirth", tempMap.get("specBirth"));
		}
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 会员日奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000023_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS1);
		// 验证方法名:验证会员日
		ruleBody.setCheckerMethod("checkMemberDay");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 活动周期
		lhsParams.put("dayFlag", tempMap.get("dayFlag"));
		// 周期：月
		lhsParams.put("monthText", tempMap.get("monthText"));
		// 周期：天
		lhsParams.put("dayText", tempMap.get("dayText"));
		// 周期：月
		lhsParams.put("monthEveText", tempMap.get("monthEveText"));
		// 第几个星期
		lhsParams.put("dayNum", tempMap.get("dayNum"));
		// 星期几
		lhsParams.put("dayWeek", tempMap.get("dayWeek"));
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 购买产品模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000021_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		List<Map<String, Object>> prtList = (List<Map<String, Object>>) tempMap.get("productList");
		if (!"5".equals(tempMap.get("product"))) {
			// 特定商品List
			lhsParams.put("productList", prtList);
		} else {
			if (prtList != null && !prtList.isEmpty()) {
				List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> prtInfo : prtList) {
					Map<String, Object> prtMap = new HashMap<String, Object>();
					prtMap.put("proId", prtInfo.get("proId"));
					prtMap.put("saleType", prtInfo.get("saleType"));
					productList.add(prtMap);
				}
				// 特定商品List
				lhsParams.put("productList", productList);
			}
		}
		// 特定商品区分
		lhsParams.put("product", (String) tempMap.get("product"));
		// 商品关系
		lhsParams.put("proCond", (String) tempMap.get("proCond"));
		// 奖励范围
		ruleBody.getRuleFilter().getRhsParams().put("RANGE_FLAG", (String) tempMap.get("rangeFlag"));
		// 单品数量上限
		lhsParams.put("rangeNum", (String) tempMap.get("rangeNum"));
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 促销活动模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000069_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 促销活动LIST
		lhsParams.put("actList", tempMap.get("actList"));
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 会员升级奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000033_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证会员生日
		ruleBody.setCheckerMethod("checkUpPoint");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 升级前等级
		lhsParams.put("memberformLevelId", tempMap.get("memberformLevelId"));
		// 升级后等级
		lhsParams.put("membertoLevelId", tempMap.get("membertoLevelId"));
		// 会员活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		ruleBody.setDescription(campaignDTO.getCampaignName());
		return ruleBody;
	}
	
	/**
	 * 赠品不计积分模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public void G00000000049_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap, List<RuleBodyDTO> ruleBodyList) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		RuleFilterDTO filter = new RuleFilterDTO();
		Map<String, Object> params = new HashMap<String, Object>();
		// 产品列表
		params.put("productList", tempMap.get("productList"));
		// 促销品列表
		params.put("prmProductList", tempMap.get("prmProductList"));
		// 不计积分类型
		params.put("discount", tempMap.get("discount"));
		// 赠品不计积分设置
		params.put("mdName", "prmNoPointSetting");
		filter.setParams(params);
		ruleBody.setRuleFilter(filter);
		ruleBody.setDescription("赠品不计积分");
		ruleBody.setPageTemp(tempMap);
		ruleBody.setGroupCode((String) tempMap.get("groupCode"));
		ruleBodyList.add(ruleBody);
	}
	
	
	/**
	 * 积分奖励模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleConditionDTO G00000000017_RuleCond(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleConditionDTO ruleCondition = new RuleConditionDTO();
		// 结果参数
		Map<String, Object> rhsParams = new HashMap<String, Object>();
		// 赠送积分方式
		rhsParams.put("calcuKbn", tempMap.get("calcuKbn"));
		// 赠送积分值
		rhsParams.put("calcuVal", tempMap.get("multipleMark"));
		// 是否继续执行默认规则
		rhsParams.put("defaultExecSel", tempMap.get("defaultExecSel"));
		// 按金额分段
		List<Map<String, Object>> segmePointList = (List<Map<String, Object>>) tempMap.get("segmePoints");
		if (null != segmePointList && !segmePointList.isEmpty()) {
			rhsParams.put("segmePoints", segmePointList);
		}
		ruleCondition.setRhsParams(rhsParams);
		return ruleCondition;
	}
	
	/**
	 * 计算设置模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO
	 * 				规则内容
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000018_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		// 共通条件
		ruleBody.setCommCondition(true);
		Map<String, Object> commRhsParams = new HashMap<String, Object>();
		// 积分上限设置信息
		Map<String, Object> pointLimitInfo = new HashMap<String, Object>();
		// 每单上限
		String oneLimit = (String) tempMap.get("oneLimit");
		if ("1".equals(oneLimit)) {
			pointLimitInfo.put("oneLimit", oneLimit);
			// 每单上限区分
			String onePoint = (String) tempMap.get("onePoint");
			pointLimitInfo.put("onePoint", onePoint);
			// 每单积分上限,剩余不计积分
			if ("2".equals(onePoint)) {
				pointLimitInfo.put("maxPoint", (String) tempMap.get("maxPoint"));
				// 每单积分上限,剩余计算积分
			} else {
				pointLimitInfo.put("maxGivePoint", (String) tempMap.get("maxGivePoint"));
				// 剩余计算积分倍数
				pointLimitInfo.put("mulGive", (String) tempMap.get("mulGive"));
			}
		}
		// 每天上限
		String allLimit = (String) tempMap.get("allLimit");
		if ("1".equals(allLimit)) {
			pointLimitInfo.put("allLimit", allLimit);
			// 每天上限区分
			String allPoint = (String) tempMap.get("allPoint");
			pointLimitInfo.put("allPoint", allPoint);
			// 每天积分上限,剩余不计积分
			if ("2".equals(allPoint)) {
				pointLimitInfo.put("allLimitPoint", (String) tempMap.get("allLimitPoint"));
				// 每天积分上限,剩余计算积分
			} else {
				pointLimitInfo.put("maxAllGivePoint", (String) tempMap.get("maxAllGivePoint"));
				// 剩余计算积分倍数
				pointLimitInfo.put("mulAllGive", (String) tempMap.get("mulAllGive"));
			}
		}
		// 活动期间上限
		String actLimit = (String) tempMap.get("actLimit");
		if ("1".equals(actLimit)) {
			pointLimitInfo.put("actLimit", actLimit);
			// 活动期间上限区分
			String actPoint = (String) tempMap.get("actPoint");
			pointLimitInfo.put("actPoint", actPoint);
			// 活动期间积分上限,剩余不计积分
			if ("2".equals(actPoint)) {
				pointLimitInfo.put("actLimitPoint", (String) tempMap.get("actLimitPoint"));
				// 活动期间积分上限,剩余计算积分
			} else {
				pointLimitInfo.put("actLimitGivePoint", (String) tempMap.get("actLimitGivePoint"));
				// 剩余计算积分倍数
				pointLimitInfo.put("actGivePoint", (String) tempMap.get("actGivePoint"));
			}
		}
		if (!pointLimitInfo.isEmpty()) {
			commRhsParams.put("POINTLIMITINFO", pointLimitInfo);
			ruleBody.setCommRhsParams(commRhsParams);
		}
		return ruleBody;
	}
}
