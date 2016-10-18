/*
 * @(#)ExtendPropertyDto.java     1.0 2011/03/22
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

package com.cherry.cm.dto;

/**
 * 扩展属性数据接口定义
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class ExtendPropertyDto {
	
	/** 问卷ID */
	private String paperId;
	
	/** 扩展属性ID */
	private String extendPropertyId;
	
	/** 扩展属性值 */
	private String propertyValue;
	
	/** 扩展属性值（数组） */
	private String[] propertyValues;
	
	/** 扩展属性类型 */
	private String propertyType;

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getExtendPropertyId() {
		return extendPropertyId;
	}

	public void setExtendPropertyId(String extendPropertyId) {
		this.extendPropertyId = extendPropertyId;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String[] getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(String[] propertyValues) {
		this.propertyValues = propertyValues;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

}
