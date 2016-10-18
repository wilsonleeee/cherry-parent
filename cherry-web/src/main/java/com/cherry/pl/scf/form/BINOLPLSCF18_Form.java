package com.cherry.pl.scf.form;

import java.util.List;
import java.util.Map;

import com.cherry.pl.scf.dto.SystemConfigDTO;

	public class BINOLPLSCF18_Form {
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

	public String getBrandInfoId() {
		return brandInfoId;
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

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	
}
