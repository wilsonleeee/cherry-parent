/*	
 * @(#)JonCreateRule.java     1.0 2011/11/02		
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
package com.cherry.cp.jon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.PropertiesUtil;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleConditionDTO;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.CreateRule;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;

/**
 * 会员入会和升降级创建规则处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class JonCreateRule extends CreateRule{
	
	
	
	/**
	 * 等级和有效期模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO 
	 * 				规则体
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000001_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
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
		ruleBody.setCheckerMethod("checkDateG01");
		// 扩展参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		// 有效期开始日
		lhsParams.put("campaignFromDate", campaignFromDate);
		// 有效期开始日
		lhsParams.put("campaignToDate", campaignToDate);
		// 会员当前等级
		lhsParams.put("curLevelId", (String) tempMap.get("memberLevelId"));
		// 活动类型
		lhsParams.put("campaignType", campaignDTO.getCampaignType());
		if ("1".equals(tempMap.get("isCounter"))) {
			// 柜台类别
			lhsParams.put("counterKbn", tempMap.get("counterKbn"));
			// 柜台类型
			lhsParams.put("locationType", tempMap.get("locationType"));
			// 柜台范围
			lhsParams.put("saveJson", tempMap.get("saveJson"));
		}
		// 条件参数
		ruleBody.setLhsParams(lhsParams);
		return ruleBody;
	}
	
	/**
	 * 入会条件模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO 
	 * 				规则体
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000014_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = null;
		// 会员子活动信息
		CampaignDTO campaignDTO = (CampaignDTO) paramMap.get("campInfo");
		if (!"2".equals(tempMap.get("levelCalcKbn"))) {
			ruleBody = createRuleBody(paramMap, tempMap);
			// 卡号开头字符
			String cardStart = (String) tempMap.get("cardStart");
			if (!CherryChecker.isNullOrEmpty(cardStart, true)) {
				cardStart = cardStart.trim();
				RuleConditionDTO ruleCondition = new RuleConditionDTO();
				// 验证类名
				ruleCondition.setCheckerClass(CampUtil.CHECKER_CLASS1);
				// 验证方法名:验证会员卡号
				ruleCondition.setCheckerMethod("checkMemCard");
				// 卡号开头字符
				ruleBody.getRuleFilter().getParams().put("cardStart", cardStart);
				List<RuleConditionDTO> ruleCondList = new ArrayList<RuleConditionDTO>();
				ruleCondList.add(ruleCondition);
				ruleCondList.add(ruleBody);
				// 取得规则条件 DTO
				getRuleCondition(tempMap, ruleCondList, ruleBody);
			}
			// 描述
			String description = ruleBody.getDescription();
			// 会员等级ID
			String memberLevelId = campaignDTO.getMemberLevelId();
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 会员等级
			searchMap.put("memberLevelId", memberLevelId);
			// 通过会员等级ID取得对应的名称
			String memberLevelName = binolcpcom02_Service.getMemLevelNameById(searchMap);
			StringBuffer buffer = new StringBuffer();
			buffer.append(description).append(CampUtil.FULL_COMMA).append(PropertiesUtil.getText("PCP00010")).append(memberLevelName);
			description = buffer.toString();
			ruleBody.setDescription(description);
			// 扩展参数
			Map<String, Object> extParams = new HashMap<String, Object>();
			// 会员等级ID
			extParams.put("MEMBER_LEVEL_ID", memberLevelId);
			ruleBody.setExtParams(extParams);
		} else {
			ruleBody = new RuleBodyDTO();
			// 验证类名
			ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS1);
			// 验证方法名:验证卡号
			ruleBody.setCheckerMethod("checkCurCard");
			// 扩展参数
			Map<String, Object> lhsParams = new HashMap<String, Object>();
			// 卡号编码规则
			lhsParams.put("cardReg", tempMap.get("cardReg"));
			// 条件参数
			ruleBody.setLhsParams(lhsParams);
			// 扩展参数
			Map<String, Object> extParams = new HashMap<String, Object>();
			// 会员等级ID
			extParams.put("MEMBER_LEVEL_ID", campaignDTO.getMemberLevelId());
			ruleBody.setExtParams(extParams);
			ruleBody.setDescription(campaignDTO.getCampaignName());
		}
		return ruleBody;
	}
	
	/**
	 * 升级条件模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO 
	 * 				规则体
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000009_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		paramMap.put("RECORD_UPAMOUNT", "1");
		RuleBodyDTO ruleBody = createRuleBody(paramMap, tempMap);
		// 描述
		String description = ruleBody.getDescription();
		// 升级后等级
		String upMemberLevelId = (String) tempMap.get("upMemberLevelId");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员等级
		searchMap.put("memberLevelId", upMemberLevelId);
		// 通过会员等级ID取得对应的名称
		String memberLevelName = binolcpcom02_Service.getMemLevelNameById(searchMap);
		StringBuffer buffer = new StringBuffer();
		buffer.append(description).append(CampUtil.FULL_COMMA).append(PropertiesUtil.getText("PCP00012")).append(memberLevelName);
		description = buffer.toString();
		ruleBody.setDescription(description);
		// 扩展参数
		Map<String, Object> extParams = new HashMap<String, Object>();
		// 会员等级ID
		extParams.put("MEMBER_LEVEL_ID", upMemberLevelId);
		ruleBody.setExtParams(extParams);
		return ruleBody;
	}
	
	/**
	 * 降级模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO 
	 * 				规则体
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000011_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		// 执行时不需要优先级控制
		ruleBody.setPriority(false);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证是否需要降级
		ruleBody.setCheckerMethod("checkLevelDown");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 规则描述
		StringBuffer despBuffer = new StringBuffer();
		// 有效期内
		despBuffer.append(PropertiesUtil.getText("PCP00021"));
		// 当前累计金额下限
		String minMoneyDown = (String) tempMap.get("minMoneyDown");
		// 累计金额下限
		String minMoney = (String) tempMap.get("minMoney");
		// 消费次数下限
		String minTime = (String) tempMap.get("minTimeDown");
		// 降级条件区分
		String downKbn = (String) tempMap.get("downLevel");
		lhsParams.put("downKbn", downKbn);
		lhsParams.put("minMoney", minMoney);
		lhsParams.put("minMoneyDown", minMoneyDown);
		lhsParams.put("minTime", minTime);
		// 是否包含首单
		lhsParams.put("plusfirstDownSel", tempMap.get("plusfirstDownSel"));
		// 指定月内未升级
		if ("0".equals(downKbn)) {
			// 未升级
			despBuffer.append(PropertiesUtil.getText("PCP00023"));
		} else {
			String downTStime = (String) tempMap.get("downTStime");
			if (!CherryChecker.isNullOrEmpty(downTStime)) {
				lhsParams.put("downTStime", downTStime);
				if ("1".equals(downTStime)) {
					lhsParams.put("tsdMonth", tempMap.get("tsdMonth"));
					lhsParams.put("tsdDay", tempMap.get("tsdDay"));
				}
			}
			// 是否设置了当前累计金额下限
			boolean isMinMoneyDown = !CherryChecker.isNullOrEmpty(minMoneyDown);
			if (isMinMoneyDown) {
				// 累计金额小于多少元
				despBuffer.append(PropertiesUtil.getText("PCP00032")).append(PropertiesUtil.getText("PCP00020"))
				.append(minMoneyDown).append(PropertiesUtil.getText("PCP00004"));
			}
			// 是否设置了会籍有效期内累计消费金额
			boolean isMinMoney = !CherryChecker.isNullOrEmpty(minMoney);
			if (isMinMoney) {
				if (isMinMoneyDown) {
					despBuffer.append(CampUtil.FULL_COMMA);
				}
				// 累计金额小于多少元
				despBuffer.append(PropertiesUtil.getText("PCP00031")).append(PropertiesUtil.getText("PCP00020"))
				.append(minMoney).append(PropertiesUtil.getText("PCP00004"));
			}
			if (!CherryChecker.isNullOrEmpty(minTime)) {
				if (isMinMoneyDown || isMinMoney) {
					despBuffer.append(CampUtil.FULL_COMMA);
				}
				// 消费次数小于多少次
				despBuffer.append(PropertiesUtil.getText("PCP00022")).append(PropertiesUtil.getText("PCP00020"))
				.append(minTime).append(PropertiesUtil.getText("PCP00024"));
			}
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员等级
		String memberLevelId = (String) tempMap.get("downMemberLevelId");
		searchMap.put("memberLevelId", tempMap.get("downMemberLevelId"));
		// 通过会员等级ID取得对应的名称
		String memberLevelName = binolcpcom02_Service.getMemLevelNameById(searchMap);
		despBuffer.append(CampUtil.FULL_COMMA).append(PropertiesUtil.getText("PCP00025")).append(memberLevelName);
		ruleBody.setDescription(despBuffer.toString());
		// 扩展参数
		Map<String, Object> extParams = new HashMap<String, Object>();
		// 会员等级ID
		extParams.put("MEMBER_LEVEL_ID", memberLevelId);
		ruleBody.setExtParams(extParams);
		return ruleBody;
	}
	
	/**
	 * 失效条件模板规则处理
	 * 
	 * @param paramMap
	 *            	参数集合
	 * @param tempMap
	 *            	模板Map
	 * @return RuleBodyDTO 
	 * 				规则体
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO G00000000013_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		// 执行时不需要优先级控制
		ruleBody.setPriority(false);
		// 验证类名
		ruleBody.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证是否需要降级
		ruleBody.setCheckerMethod("checkLevelDown");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		ruleBody.setLhsParams(lhsParams);
		// 规则描述
		StringBuffer despBuffer = new StringBuffer();
		// 有效期内
		despBuffer.append(PropertiesUtil.getText("PCP00021"));
		// 累计金额下限
		String minMoney = (String) tempMap.get("minMoneyLose");
		// 消费次数下限
		String minTime = (String) tempMap.get("minTimeLose");
		// 降级条件区分
		String downKbn = (String) tempMap.get("loseLevel");
		lhsParams.put("downKbn", downKbn);
		lhsParams.put("minMoney", minMoney);
		lhsParams.put("minTime", minTime);
		// 指定月内未升级
		if ("0".equals(downKbn)) {
			// 未升级
			despBuffer.append(PropertiesUtil.getText("PCP00023"));
		} else {
			// 是否设置了累计金额下限
			boolean isMinMoney = !CherryChecker.isNullOrEmpty(minMoney);
			if (isMinMoney) {
				// 累计金额小于多少元
				despBuffer.append(PropertiesUtil.getText("PCP00017")).append(PropertiesUtil.getText("PCP00020"))
				.append(minMoney).append(PropertiesUtil.getText("PCP00004"));
			}
			if (!CherryChecker.isNullOrEmpty(minTime)) {
				if (isMinMoney) {
					despBuffer.append(CampUtil.FULL_COMMA);
				}
				// 消费次数小于多少次
				despBuffer.append(PropertiesUtil.getText("PCP00022")).append(PropertiesUtil.getText("PCP00020"))
				.append(minTime).append(PropertiesUtil.getText("PCP00024"));
			}
		}
		despBuffer.append(CampUtil.FULL_COMMA).append(PropertiesUtil.getText("PCP00026"));
		ruleBody.setDescription(despBuffer.toString());
		return ruleBody;
	}
	
	/**
	 * 购买产品模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleConditionDTO G00000000004_RuleCond(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		StringBuffer buffer = new StringBuffer();
		RuleConditionDTO ruleCondition = new RuleConditionDTO();
		// 选择了指定产品
		if ("1".equals(tempMap.get("prtRadio"))) {
			// 验证类名
			ruleCondition.setCheckerClass(CampUtil.CHECKER_CLASS1);
			// 验证方法名:验证购买产品(是否包含指定产品)
			ruleCondition.setCheckerMethod("checkProducts");
			// 所选择的产品
			List<Map<String, Object>> products = (List<Map<String, Object>>) tempMap.get("productList");
			// 条件参数
			Map<String, Object> lhsParams = new HashMap<String, Object>();
			lhsParams.put("productList", products);
			ruleCondition.setLhsParams(lhsParams);
			// 购买产品包含：
			buffer.append(PropertiesUtil.getText("PCP00005"));
			for (int i = 0; i < products.size(); i++) {
				// 产品信息
				Map<String, Object> product = products.get(i);
				// 产品名称
				String name = (String) product.get("nameTotal");
				if (0 != i) {
					buffer.append(CampUtil.MARK_COMMA);
				}
				if (2 == i) {
					// 等
					buffer.append(PropertiesUtil.getText("PCP00006"));
					break;
				}
				buffer.append(name);
			}
		} else {
			// 购买任意产品
			buffer.append(PropertiesUtil.getText("PCP00007"));
		}
		ruleCondition.setDescription(buffer.toString());
		return ruleCondition;
	}
	
	/**
	 * 累积时间模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleConditionDTO G00000000005_RuleCond(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		StringBuffer buffer = new StringBuffer();
		RuleConditionDTO ruleCondition = new RuleConditionDTO();
		// 验证类名
		ruleCondition.setCheckerClass(CampUtil.CHECKER_CLASS2);
		// 验证方法名:验证购买产品(是否包含指定产品)
		ruleCondition.setCheckerMethod("checkTotalAmount");
		// 条件参数
		Map<String, Object> lhsParams = new HashMap<String, Object>();
		String plusChoice = (String) tempMap.get("plusChoice");
		lhsParams.put("plusChoice", plusChoice);
		// 是否包含首单
		lhsParams.put("plusfirstBillSel", tempMap.get("plusfirstBillSel"));
		if ("0".equals(plusChoice)) {
			buffer.append(PropertiesUtil.getText("PCP00013"));
		} else if ("1".equals(plusChoice)){
			// 累积时间区分
			String plusTime = (String) tempMap.get("plusTime");
			lhsParams.put("plusTime", plusTime);
			String totalStime = (String) tempMap.get("totalStime");
			if (!CherryChecker.isNullOrEmpty(totalStime)) {
				lhsParams.put("totalStime", totalStime);
				if ("1".equals(totalStime)) {
					lhsParams.put("tsMonth", tempMap.get("tsMonth"));
					lhsParams.put("tsDay", tempMap.get("tsDay"));
				}
			}
			// 时间数
			String dateNum = null;
			// 多少个月内
			if ("0".equals(plusTime)) {
				// 月数
				dateNum = (String) tempMap.get("monthNum");
				buffer.append(dateNum).append(PropertiesUtil.getText("PCP00014"));
				// 年数
			} else if ("1".equals(plusTime)) {
				dateNum = (String) tempMap.get("yearNum");
				buffer.append(dateNum).append(PropertiesUtil.getText("PCP00015"));
				// 多少个自然年内
			} else if ("2".equals(plusTime)) {
				// 自然年数
				dateNum = (String) tempMap.get("normalYearNum");
				buffer.append(dateNum).append(PropertiesUtil.getText("PCP00016"));
			} 
			lhsParams.put("dateNum", dateNum);
		} else {
			buffer.append(PropertiesUtil.getText("PCP00013"));
		}
		String lastKbn = (String) tempMap.get("lastKbn");
		String fromKbn = (String) tempMap.get("fromKbn");
		if ("1".equals(lastKbn)) {
			lhsParams.put("lastKbn", lastKbn);
		}
		if ("1".equals(fromKbn)) {
			lhsParams.put("fromKbn", fromKbn);
		}
		buffer.append(CampUtil.FULL_COMMA).append(PropertiesUtil.getText("PCP00017"));
		// 最低消费
		String minAmount = (String) tempMap.get("plusminAmount");
		// 最高消费
		String maxAmount = (String) tempMap.get("plusmaxAmount");
		if (CherryChecker.isNullOrEmpty(minAmount)) {
			throw new CherryException("ECP00018", new String[]{PropertiesUtil.getText("PCP00018")});
		}
		// 满多少元
		buffer.append(PropertiesUtil.getText("PCP00019")).append(minAmount).append(PropertiesUtil.getText("PCP00004"));
		if (!CherryChecker.isNullOrEmpty(maxAmount)) {
			// 小于多少元
			buffer.append(PropertiesUtil.getText("PCP00020")).append(maxAmount).append(PropertiesUtil.getText("PCP00004"));
		}
		String yltKbn = (String) tempMap.get("yltKbn"); 
		if ("1".equals(yltKbn)) {
			lhsParams.put("yltKbn", yltKbn);
			lhsParams.put("ylz", (String) tempMap.get("ylz"));
			lhsParams.put("yll", (String) tempMap.get("yll"));
		}
		String bstime = (String) tempMap.get("bstime");
		String betime = (String) tempMap.get("betime");
		boolean isBstime = !CherryChecker.isNullOrEmpty(bstime);
		boolean isBetime = !CherryChecker.isNullOrEmpty(betime);
		if (isBstime || isBetime) {
			lhsParams.put("timeCond", tempMap.get("timeCond"));
			if (isBstime) {
				lhsParams.put("bstime", bstime.trim());
			}
			if (isBetime) {
				lhsParams.put("betime", betime.trim());
			}
		}
		lhsParams.put("minAmount", minAmount);
		lhsParams.put("maxAmount", maxAmount);
		ruleCondition.setLhsParams(lhsParams);
		ruleCondition.setDescription(buffer.toString());
		return ruleCondition;
	}
	
	/**
	 * 消费金额模板规则处理
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleConditionDTO G00000000006_RuleCond(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 购买金额
		String amountKey = "amount";
		// 购买金额(描述)
		String amountDesp = PropertiesUtil.getText("PCP00003");
		RuleConditionDTO ruleCondition = new RuleConditionDTO();
		// 条件
		StringBuffer buffer = new StringBuffer();
		// 描述
		StringBuffer despBuffer = new StringBuffer();
		// 最低消费
		String minAmount = (String) tempMap.get("minAmount");
		// 最高消费
		String maxAmount = (String) tempMap.get("maxAmount");
		boolean isNullMin = CherryChecker.isNullOrEmpty(minAmount);
		boolean isNullMax = CherryChecker.isNullOrEmpty(maxAmount);
		if (isNullMin && isNullMax) {
			return null;
		}
		// 是否统计下次升级所需金额
		String ulaFlag = (String) paramMap.get("ULAFLAG");
		if (null == ulaFlag) {
			// 组织代码
			String orgCode = (String) paramMap.get("OrgCode");
			// 品牌代码
			String brandCode = (String) paramMap.get("BrandCode");
			// 计算升级所需金额处理
			CampRuleExec_IF campRuleExec01 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE01);
			if (null != campRuleExec01) {
				// 需要计算
				ulaFlag = "1";
			} else {
				// 不需要计算
				ulaFlag = "0";
			}
			paramMap.put("ULAFLAG", ulaFlag);
		}
		if (!isNullMin) {
			buffer.append(amountKey).append(RELATION_OPERATOR.getRelatOper("2")).append(minAmount);
			despBuffer.append(amountDesp).append(RELATION_OPERATOR.getRelatName("2"))
			.append(minAmount).append(PropertiesUtil.getText("PCP00004"));
		}
		if (!isNullMax) {
			if (!isNullMin) {
				despBuffer.append(CampUtil.SPACE).append(RELATION_OPERATOR.getRelatName("0")).append(CampUtil.SPACE);
			} else {
				buffer.append(amountKey).append(RELATION_OPERATOR.getRelatOper("2")).append(0);
			}
			buffer.append(CampUtil.SPACE).append(RELATION_OPERATOR.getRelatOper("0")).append(CampUtil.SPACE);
			buffer.append(amountKey).append(RELATION_OPERATOR.getRelatOper("3")).append(maxAmount);
			despBuffer.append(amountDesp).append(RELATION_OPERATOR.getRelatName("3")).append(maxAmount).append(PropertiesUtil.getText("PCP00004"));
		}
		if ("1".equals(paramMap.get("RECORD_UPAMOUNT")) && "1".equals(ulaFlag) && !isNullMin) {
			// 规则条件中加上计算升级所需金额方法
			buffer.append(CampUtil.SPACE).append(RELATION_OPERATOR.getRelatOper("1")).append(CampUtil.SPACE)
			.append("RuleFilterUtil.calcUpAmount(this,").append(minAmount).append(")");
		}
		ruleCondition.setCondition(buffer.toString());
		ruleCondition.setDescription(despBuffer.toString());
		return ruleCondition;
	}
}
