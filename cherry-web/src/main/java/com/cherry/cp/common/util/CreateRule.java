/*	
 * @(#)CreateRule.java     1.0 2011/11/01		
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
package com.cherry.cp.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CodeTable;
import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleConditionDTO;
import com.cherry.cp.common.interfaces.CreateRule_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;

/**
 * 创建规则处理
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class CreateRule implements CreateRule_IF{
	
	protected static final Logger logger = LoggerFactory.getLogger(CreateRule.class);
		
	@Resource
	protected BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	protected CodeTable CodeTable;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	protected BINBEMQMES98_BL binBEMQMES98_BL;
	
	/** 促销品代码和名称*/
	protected static enum RELATION_OPERATOR {
		andRelation("0", "&&", "并且"),
		orRelation("1", "||", "或者"),
		geRelation("2", ">=", "大于等于"),
		ltRelation("3", "<", "小于");
		public static String getRelatName(Object key) {
			return getValue(key, 1);
		}
		
		public static String getRelatOper(Object key) {
			return getValue(key, 0);
		}
		
		public static String getValue(Object key, int kbn) {
			if(null == key || "".equals(key)) {
				return null;
			}
			RELATION_OPERATOR[] opers = RELATION_OPERATOR.values();
			for(RELATION_OPERATOR oper : opers) {
				if(oper.getKey().equals(key)) {
					if (0 == kbn) {
						return oper.getRelatOper();
					} else {
						return oper.getRelatName();
					}
				}
			}
			return null;
		}
		
		RELATION_OPERATOR(String key, String relatOper, String relatName) {
			this.key = key;
			this.relatOper = relatOper;
			this.relatName = relatName;
		}
		
		public String getKey() {
			return key;
		}

		public String getRelatOper() {
			return relatOper;
		}

		public String getRelatName() {
			return relatName;
		}
		
		private String key;
		private String relatOper;
		private String relatName;
	}
	
	protected RuleBodyDTO createRuleBody(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		List<RuleConditionDTO> ruleCondtionList = new ArrayList<RuleConditionDTO>();
		// 取得规则内容 DTO
		getRuleBody(paramMap, tempMap, ruleCondtionList, ruleBody);
		// 取得规则条件 DTO
		getRuleCondition(tempMap, ruleCondtionList, ruleBody);
		return ruleBody;
	}
	/**
	 * 取得规则内容 DTO
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param tempMap
	 *            规则条件List
	 * @throws Exception
	 * 
	 */
	protected void getRuleBody(Map<String, Object> paramMap, Map<String, Object> tempMap, List<RuleConditionDTO> ruleCondtionList, RuleBodyDTO ruleBody) throws Exception {
		// 集合标识符
		String groupCode = (String) tempMap.get("groupCode");
		List<Map<String, Object>> combTempList = (List<Map<String, Object>>) tempMap.get("combTemps");
		if (null != combTempList) {
			List<RuleConditionDTO> ruleCondList = new ArrayList<RuleConditionDTO>();
			for (Map<String, Object> combTemp : combTempList) {
				combTemp.put("RULE_PARENT_CODE", groupCode);
				getRuleBody(paramMap, combTemp, ruleCondList, ruleBody);
			}
			RuleConditionDTO ruleCondComb = new RuleConditionDTO();
			// 取得规则条件 DTO
			getRuleCondition(tempMap, ruleCondList, ruleCondComb);
			ruleCondtionList.add(ruleCondComb);
		} else {
			RuleConditionDTO ruleConditon = this.invokeMdCond(groupCode + "_RuleCond", paramMap, tempMap);
			if (null != ruleConditon) {
				ruleCondtionList.add(ruleConditon);
				// 条件参数
				Map<String, Object> params = ruleConditon.getLhsParams();
				// 结果参数
				Map<String, Object> rhsParams = ruleConditon.getRhsParams();
				if (null != params && !params.isEmpty()) {
					ruleBody.getRuleFilter().getParams().putAll(params);
				}
				if (null != rhsParams && !rhsParams.isEmpty()) {
					ruleBody.getRuleFilter().getRhsParams().putAll(rhsParams);
					ruleBody.getExtParams().putAll(rhsParams);
				}
			}
		}
	}
	
	/**
	 * 取得规则条件 DTO
	 * 
	 * @param tempMap
	 *            模板Map
	 * @param ruleCondList
	 *            规则条件List
	 * @param ruleCondtion
	 *            规则条件      
	 * @throws Exception
	 * 
	 */
	protected void getRuleCondition(Map<String, Object> tempMap, List<RuleConditionDTO> ruleCondList, RuleConditionDTO ruleCondtion) {
		// 关系符的key
		String relatOperKey = (String) tempMap.get("RELAT_OPER_KEY");
		// 关系符
		String relatOper = RELATION_OPERATOR.getRelatOper(relatOperKey);
		// 关系符名称
		String relatName = RELATION_OPERATOR.getRelatName(relatOperKey);
		// 条件
		StringBuffer condBuffer = new StringBuffer();
		// 描述
		StringBuffer despBuffer = new StringBuffer();
		int size = ruleCondList.size();
		for (int i = 0; i < size; i++) {
			RuleConditionDTO ruleCondition = ruleCondList.get(i);
			// 条件
			String condition = ruleCondition.getCondition();
			// 描述
			String description = ruleCondition.getDescription();
			if (!CherryChecker.isNullOrEmpty(condition)) {
				if (0 != i && !CherryChecker.isNullOrEmpty(condBuffer.toString())) {
					condBuffer.append(CampUtil.SPACE).append(relatOper).append(CampUtil.SPACE);
				}
				if (size > 1) {
					condBuffer.append("(").append(condition).append(")");
				} else {
					condBuffer.append(condition);
				}
			}
			if (!CherryChecker.isNullOrEmpty(description)) {
				if (0 != i && !CherryChecker.isNullOrEmpty(despBuffer.toString())) {
					despBuffer.append(CampUtil.SPACE).append(relatName).append(CampUtil.SPACE);
				}
				if (size > 1) {
					despBuffer.append("(").append(description).append(")");
				} else {
					despBuffer.append(description);
				}
			}
		}
		ruleCondtion.setCondition(condBuffer.toString());
		ruleCondtion.setDescription(despBuffer.toString());
	}
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@Override
	public RuleBodyDTO invokeMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		Method[] mdArr = this.getClass().getMethods();
		for (Method method : mdArr) {
			if (method.getName().equals(mdName)) {
				return (RuleBodyDTO) method.invoke(this, paramMap, tempMap);
			}
		}
		return null;
	}
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@Override
	public void invokeTestMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap, CampBaseDTO campBaseDTO) throws Exception {
		Method[] mdArr = this.getClass().getMethods();
		for (Method method : mdArr) {
			if (method.getName().equals(mdName)) {
				method.invoke(this, paramMap, tempMap, campBaseDTO);
				break;
			}
		}
	}
	
	/**
	 * 运行指定名称的方法(规则条件)
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@Override
	public RuleConditionDTO invokeMdCond(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		Method[] mdArr = this.getClass().getMethods();
		for (Method method : mdArr) {
			if (method.getName().equals(mdName)) {
				return (RuleConditionDTO)method.invoke(this, paramMap, tempMap);
			}
		}
		return null;
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
	public void G00000000030_Test(Map<String, Object> paramMap, Map<String, Object> tempMap, CampBaseDTO campBaseDTO) throws Exception {
		// 购买日期
		String ticketDate = (String) tempMap.get("ticketDate");
		// 消费金额
		String amount = (String) tempMap.get("amount");
		campBaseDTO.setTicketDate(ticketDate);
		campBaseDTO.setBusinessDate(ticketDate);
		if (!CherryChecker.isNullOrEmpty(amount)) {
			campBaseDTO.setAmount(Double.valueOf(amount));
		}
		paramMap.put("ticketDate", ticketDate);
	}
}
