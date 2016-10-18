/*	
 * @(#)CamBasePropDTO.java     1.0 2012/03/29	
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

import com.cherry.cm.cmbussiness.dto.BaseDTO;

/**
 * 会员活动基础属性DTO
 * 
 * @author hub
 * @version 1.0 2012.03.29
 */
public class CampBasePropDTO extends BaseDTO{
	
	/** 基础属性ID */
	private int campBasePropId;
	
	/** 条件名称 */
	private String condition;
	
	/** 属性条件 */
	private String propertyName;
	
	/** 属性字段类型 */
	private int fieldType;

	public int getCampBasePropId() {
		return campBasePropId;
	}

	public void setCampBasePropId(int campBasePropId) {
		this.campBasePropId = campBasePropId;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
}
