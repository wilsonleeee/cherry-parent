/*
 * @(#)SystemConfigDTO.java     1.0 2010/10/27
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

package com.cherry.pl.scf.dto;

/**
 * 基本配置信息DTO
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class SystemConfigDTO {
	
	/** 配置项代码 */
	private String configCode;
	
	/** 配置项的展示类型 */
	private String type;
	
	/** 配置项的值 */
	private String[] configValue;

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String[] configValue) {
		this.configValue = configValue;
	}

}
