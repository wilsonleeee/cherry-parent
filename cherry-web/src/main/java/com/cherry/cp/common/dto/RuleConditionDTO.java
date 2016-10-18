/*	
 * @(#)RuleConditionDTO.java     1.0 2011/11/01		
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
package com.cherry.cp.common.dto;

import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cp.common.util.CampUtil;

/**
 * 规则条件 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class RuleConditionDTO {
	
	/** 条件 */
	private String condition;
	
	/** 描述 */
	private String description;
	
	/** 验证方法名 */
	private String checkerMethod;
	
	/** 验证类名 */
	private String checkerClass;
	
	/** LHS参数(条件参数) */
	private Map<String, Object> lhsParams;
	
	/** RHS参数(结果参数) */
	private Map<String, Object> rhsParams;
	
	/** 共通：LHS参数(条件参数) */
	private Map<String, Object> commLhsParams;
	
	/** 共通：RHS参数(结果参数) */
	private Map<String, Object> commRhsParams;

	public String getCondition() {
		if (!CherryChecker.isNullOrEmpty(checkerMethod) 
				&& !CherryChecker.isNullOrEmpty(checkerClass)) {
			StringBuffer buffer = new StringBuffer();
			// 共通验证方法
			buffer.append(checkerClass).append(CampUtil.HALF_DOTS).append(CampUtil.CHECK_METHOD_NAME).append(CampUtil.LEFT_BRACKET)
			.append(CampUtil.CAMP_NAME).append(CampUtil.HALF_COMMA).append(CampUtil.CHECK_FILTERS_NAME).append(CampUtil.HALF_COMMA)
			.append(CampUtil.RULE_NAME).append(CampUtil.HALF_COMMA).append(CampUtil.HALF_QUOTE).append(checkerMethod)
			.append(CampUtil.HALF_QUOTE).append(CampUtil.RIGHT_BRACKET);
			return buffer.toString();
		}
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCheckerMethod() {
		return checkerMethod;
	}

	public void setCheckerMethod(String checkerMethod) {
		this.checkerMethod = checkerMethod;
	}

	public String getCheckerClass() {
		return checkerClass;
	}

	public void setCheckerClass(String checkerClass) {
		this.checkerClass = checkerClass;
	}

	public Map<String, Object> getLhsParams() {
		return lhsParams;
	}

	public void setLhsParams(Map<String, Object> lhsParams) {
		this.lhsParams = lhsParams;
	}

	public Map<String, Object> getRhsParams() {
		return rhsParams;
	}

	public void setRhsParams(Map<String, Object> rhsParams) {
		this.rhsParams = rhsParams;
	}

	public Map<String, Object> getCommLhsParams() {
		return commLhsParams;
	}

	public void setCommLhsParams(Map<String, Object> commLhsParams) {
		this.commLhsParams = commLhsParams;
	}

	public Map<String, Object> getCommRhsParams() {
		return commRhsParams;
	}

	public void setCommRhsParams(Map<String, Object> commRhsParams) {
		this.commRhsParams = commRhsParams;
	}
}
