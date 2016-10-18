/*  
 * @(#)BINOLCM13_Form.java     1.0 2011/05/31      
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
package com.cherry.cm.cmbussiness.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCM13_Form extends DataTable_BaseForm{
	
	/** 共通条参数 */
	private String params;
	
	/** 部门ID */
	private String departId;
	
	/** 仓库ID */
	private String depotId;
	
	/** 部门类型 */
	private String departType;
	
	/** 渠道ID */
	private String channelId;
	
	/** 业务类型  */
	private String businessType;
	
	/** 操作类型  */
	private String operationType;
	
	/** 区域ID */
	private String regionId;
	
	/** 省份ID  */
	private String provinceId;
	
	/** 城市ID  */
	private String cityId;
	
	/** 操作类型  */
	private String countyId;
	
	/** 模式显示 */
	private String mode;
	
	/** 部门有效状态 */
	private String orgValid;
	
	/**包含停用部门*/
	private String orgValidAll;
	
	/**包含停用部门复选框是否显示，0或者空：不显示；1：显示*/
	private String orgValidType;
	
	/** 逻辑仓库是否显示 */
	private int showLgcDepot;
	
	/** 部门类型是否显示 */
	private String showType;
	
	/** 测试，正式数据区分 */
	private String testType;
	
	/** 最多显示条数 */
	private int number;

	/** 输入部门查询字符串 */
	private String departInfoStr;
	
	/** 输入仓库查询字符串 */
	private String depotInfoStr;
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public int getShowLgcDepot() {
		return showLgcDepot;
	}

	public void setShowLgcDepot(int showLgcDepot) {
		this.showLgcDepot = showLgcDepot;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDepartInfoStr() {
		return departInfoStr;
	}

	public void setDepartInfoStr(String departInfoStr) {
		this.departInfoStr = departInfoStr;
	}

	public String getDepotInfoStr() {
		return depotInfoStr;
	}

	public void setDepotInfoStr(String depotInfoStr) {
		this.depotInfoStr = depotInfoStr;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String getOrgValid() {
		return orgValid;
	}

	public void setOrgValid(String orgValid) {
		this.orgValid = orgValid;
	}

	public String getOrgValidAll() {
		return orgValidAll;
	}

	public void setOrgValidAll(String orgValidAll) {
		this.orgValidAll = orgValidAll;
	}

	public String getOrgValidType() {
		return orgValidType;
	}

	public void setOrgValidType(String orgValidType) {
		this.orgValidType = orgValidType;
	}
}
