/*	
 * @(#)RuleFilterDTO.java     1.0 2012/02/03		
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
package com.cherry.dr.cmbussiness.dto.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则过滤器 DTO
 * 
 * @author hub 
 * @version 1.0 2012.02.03
 */
public class RuleFilterDTO {
	
	/** 所属规则 */
	private String ruleName;
	
	/** 产品List */
	private List<Map<String, Object>> products;
	
	/** 累积月份 */
	private int totalMonths;
	
	/** 累积金额下限*/
	private double lowTotalAmount;
	
	/** 累积金额上限 */
	private double highTotalAmount;
	
	/** 累积金额上限 */
	private boolean ruleCheck;
	
	/** 累积购买次数下限*/
	private int lowBuyTimes;
	
	/** LHS参数(条件参数)*/
	private Map<String, Object> params;
	
	/** RHS参数(结果参数)*/
	private Map<String, Object> rhsParams;
	
	public RuleFilterDTO() {
		this.totalMonths = -1;
		this.lowTotalAmount = -1;
		this.highTotalAmount = -1;
	}
	
	public boolean isRuleCheck() {
		return ruleCheck;
	}

	public void setRuleCheck(boolean ruleCheck) {
		this.ruleCheck = ruleCheck;
	}

	public Map<String, Object> getParams() {
		if (null == params || params.isEmpty()) {
			this.params = new HashMap<String, Object>();
		}
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public String getRuleName() {
		return ruleName;
	}
	
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public List<Map<String, Object>> getProducts() {
		return products;
	}

	public void setProducts(List<Map<String, Object>> products) {
		this.products = products;
	}

	public int getTotalMonths() {
		return totalMonths;
	}

	public void setTotalMonths(int totalMonths) {
		this.totalMonths = totalMonths;
	}

	public double getLowTotalAmount() {
		return lowTotalAmount;
	}

	public void setLowTotalAmount(double lowTotalAmount) {
		this.lowTotalAmount = lowTotalAmount;
	}

	public double getHighTotalAmount() {
		return highTotalAmount;
	}

	public void setHighTotalAmount(double highTotalAmount) {
		this.highTotalAmount = highTotalAmount;
	}

	public int getLowBuyTimes() {
		return lowBuyTimes;
	}

	public void setLowBuyTimes(int lowBuyTimes) {
		this.lowBuyTimes = lowBuyTimes;
	}

	public Map<String, Object> getRhsParams() {
		if (null == this.rhsParams || this.rhsParams.isEmpty()) {
			this.rhsParams = new HashMap<String, Object>();
		}
		return rhsParams;
	}

	public void setRhsParams(Map<String, Object> rhsParams) {
		this.rhsParams = rhsParams;
	}
}
