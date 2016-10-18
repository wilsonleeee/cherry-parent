/*
 * @(#)BIRptQryDefDto.java     1.0 2011/03/22
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

package com.cherry.rp.query.dto;

/**
 * BI报表查询条件
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BIRptQryDefDto {
	
	/** 查询字段设定值(显示用) */
	private String queryPropValue;
	
	/** 查询字段设定值（表中的字段名） */
	private String queryPropTbl;
	
	/** 查询字段的列号 */
	private String defColumnNo;
	
	/** 查询条件运算符 */
	private String queryCondition;
	
	/** 查询条件画面输入值 */
	private String[] queryValue;
	
	/** 是否显示 */
	private String isVisible;
	
	/** 字段类型 */
	private String queryPropType;

	public String getQueryPropValue() {
		return queryPropValue;
	}

	public void setQueryPropValue(String queryPropValue) {
		this.queryPropValue = queryPropValue;
	}

	public String getQueryPropTbl() {
		return queryPropTbl;
	}

	public void setQueryPropTbl(String queryPropTbl) {
		this.queryPropTbl = queryPropTbl;
	}

	public String getDefColumnNo() {
		return defColumnNo;
	}

	public void setDefColumnNo(String defColumnNo) {
		this.defColumnNo = defColumnNo;
	}

	public String getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}

	public String[] getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String[] queryValue) {
		this.queryValue = queryValue;
	}

	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	public String getQueryPropType() {
		return queryPropType;
	}

	public void setQueryPropType(String queryPropType) {
		this.queryPropType = queryPropType;
	}

}
