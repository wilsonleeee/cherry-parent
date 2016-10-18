/*
 * @(#)BINOLBSPOS03_Form.java     1.0 2010/10/27
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

/**
 * 更新岗位画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS03_Form {
	
	/** 岗位ID */
	private String positionId;
	
	/** 岗位节点位置 */
	private String positionPath;
	
	/** 原上级岗位节点位置 */
	private String higherPositionPath;
	
	/** 新上级岗位节点位置 */
	private String path;
	
	/** 所属部门ID */
	private String organizationId;
	
	/** 岗位名称 */
	private String positionName;
	
	/** 岗位外文名称 */
	private String positionNameForeign;
	
	/** 岗位描述 */
	private String positionDESC;
	
	/** 岗位类别ID */
	private String positionCategoryId;
	
	/** 是否负责人 */
	private String isManager;
	
	/** 成立日期 */
	private String foundationDate;
	
	/** 岗位类型 */
	private String positionType;

	/** 所属经销商 */
	private String resellerInfoId;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 迁移源 */
	private String fromPage;

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionPath() {
		return positionPath;
	}

	public void setPositionPath(String positionPath) {
		this.positionPath = positionPath;
	}

	public String getHigherPositionPath() {
		return higherPositionPath;
	}

	public void setHigherPositionPath(String higherPositionPath) {
		this.higherPositionPath = higherPositionPath;
	}

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

	public String getPositionNameForeign() {
		return positionNameForeign;
	}

	public void setPositionNameForeign(String positionNameForeign) {
		this.positionNameForeign = positionNameForeign;
	}

	public String getPositionDESC() {
		return positionDESC;
	}

	public void setPositionDESC(String positionDESC) {
		this.positionDESC = positionDESC;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getFoundationDate() {
		return foundationDate;
	}

	public void setFoundationDate(String foundationDate) {
		this.foundationDate = foundationDate;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getResellerInfoId() {
		return resellerInfoId;
	}

	public void setResellerInfoId(String resellerInfoId) {
		this.resellerInfoId = resellerInfoId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}
}
