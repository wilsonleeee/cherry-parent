/*
 * @(#)BINOLPLSCF01_Form.java     1.0 2010/10/27
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

package com.cherry.pl.scf.form;

import java.util.List;
import java.util.Map;

import com.cherry.pl.scf.dto.SystemConfigDTO;

/**
 * 基本配置管理Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF01_Form {

	/** 品牌ID */
	private String brandInfoId;

	/** 基本配置信息List */
	private List<SystemConfigDTO> bsCfInfoList;

	/** 基本配置信息List */
	private List<Map<String, Object>> systemConfigList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 分组编号 */
	private String groupNo;
	
	/**配置项编码*/
	private String configCode;
	
	/**配置项值*/
	private List<String> configValue;
	
	/**配置项类型*/
	private String type;

	public List<Map<String, Object>> getSystemConfigList() {
		return systemConfigList;
	}

	public void setSystemConfigList(List<Map<String, Object>> systemConfigList) {
		this.systemConfigList = systemConfigList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<SystemConfigDTO> getBsCfInfoList() {
		return bsCfInfoList;
	}

	public void setBsCfInfoList(List<SystemConfigDTO> bsCfInfoList) {
		this.bsCfInfoList = bsCfInfoList;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public List<String> getConfigValue() {
		return configValue;
	}

	public void setConfigValue(List<String> configValue) {
		this.configValue = configValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
