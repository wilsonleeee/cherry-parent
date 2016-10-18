/*
 * @(#)BINOLBSPOS01_Form.java     1.0 2010/10/27
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

package com.cherry.bs.pos.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 岗位一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS01_Form extends DataTable_BaseForm {
	
	/** 上级岗位节点位置 */
	private String path;
	
	/** 所属部门ID */
	private String organizationId;
	
	/** 岗位名称 */
	private String positionName;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 岗位信息List */
	private List<Map<String, Object>> positionList;
	
	/** 部门List */
	private List<Map<String, Object>> orgList;
	
	/** 上级岗位信息List */
	List<Map<String, Object>> higherPositionList;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Map<String, Object>> positionList) {
		this.positionList = positionList;
	}

	public List<Map<String, Object>> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}

	public List<Map<String, Object>> getHigherPositionList() {
		return higherPositionList;
	}

	public void setHigherPositionList(List<Map<String, Object>> higherPositionList) {
		this.higherPositionList = higherPositionList;
	}

}
