/*		
 * @(#)BINOLSSPRM14_Form.java     1.0 2010/10/27		
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
package com.cherry.ss.prm.form;

import java.util.List;

import com.cherry.cm.form.DataTable_BaseForm;
@SuppressWarnings("unchecked")
public class BINOLSSPRM14_Form extends DataTable_BaseForm{
	/** 活动信息List */
	private List activeInfoList;
	
	/** 促销品类型 */
	private String prtType;
	
	/** 活动名*/
	private String activeName;
	
	private String activityCode;
	
	private String groupCode;
	
	private int prmCounterId;
	
	private int check;
	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	/** 活动id */
	private String activeID;
	
	/** 查询条件--促销活动名 */
	private String searchPrmActiveName;
	
	/** 查询条件--促销主活动名 */
	private String searchPrmGrpName;
	
	/** 查询条件--促销地点 */
	private String searchPrmLocation;
	
	/** 查询条件--促销产品*/
	private String searchPrmProduct;
	
	/** 查询条件--促销产品*/
	private String prmProductId;
	
	/** 查询条件--促销活动起始日期*/
	private String searchPrmStartDate;
	
	/** 查询条件--促销活动结束日期*/
	private String searchPrmEndDate;
	
	private String actState;
	
	/** 假日 */
	private String holidays;
	
	/** 过滤区分*/
	private String filterFlg;
	
	/** 活动地点类型*/
	private String locationType;
	
	/** 有效区分 */
	private String actValidFlag;
	
	/** 活动类型*/
	private String activeType;
	
	/**自动匹配字符串 */
	private String paramInfoStr;
	
	private int number;
	
	private int userId;
	
	public String getActiveType() {
		return activeType;
	}

	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}

	public List getActiveInfoList() {
		return activeInfoList;
	}

	public void setActiveInfoList(List activeInfoList) {
		this.activeInfoList = activeInfoList;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getActiveID() {
		return activeID;
	}

	public void setActiveId(String activeID) {
		this.activeID = activeID;
	}

	public String getSearchPrmActiveName() {
		return searchPrmActiveName;
	}

	public void setSearchPrmActiveName(String searchPrmActiveName) {
		this.searchPrmActiveName = searchPrmActiveName;
	}

	public String getSearchPrmLocation() {
		return searchPrmLocation;
	}

	public void setSearchPrmLocation(String searchPrmLocation) {
		this.searchPrmLocation = searchPrmLocation;
	}

	public String getSearchPrmProduct() {
		return searchPrmProduct;
	}

	public void setSearchPrmProduct(String searchPrmProduct) {
		this.searchPrmProduct = searchPrmProduct;
	}

	public void setActiveID(String activeID) {
		this.activeID = activeID;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getSearchPrmStartDate() {
		return searchPrmStartDate;
	}

	public void setSearchPrmStartDate(String searchPrmStartDate) {
		this.searchPrmStartDate = searchPrmStartDate;
	}

	public String getSearchPrmEndDate() {
		return searchPrmEndDate;
	}

	public void setSearchPrmEndDate(String searchPrmEndDate) {
		this.searchPrmEndDate = searchPrmEndDate;
	}

	public String getFilterFlg() {
		return filterFlg;
	}

	public void setFilterFlg(String filterFlg) {
		this.filterFlg = filterFlg;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getActValidFlag() {
		return actValidFlag;
	}

	public void setActValidFlag(String actValidFlag) {
		this.actValidFlag = actValidFlag;
	}

	public String getSearchPrmGrpName() {
		return searchPrmGrpName;
	}

	public void setSearchPrmGrpName(String searchPrmGrpName) {
		this.searchPrmGrpName = searchPrmGrpName;
	}

	public String getParamInfoStr() {
		return paramInfoStr;
	}

	public void setParamInfoStr(String paramInfoStr) {
		this.paramInfoStr = paramInfoStr;
	}

	public String getPrtType() {
		return prtType;
	}

	public void setPrtType(String prtType) {
		this.prtType = prtType;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPrmProductId() {
		return prmProductId;
	}

	public void setPrmProductId(String prmProductId) {
		this.prmProductId = prmProductId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getActState() {
		return actState;
	}

	public void setActState(String actState) {
		this.actState = actState;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public int getPrmCounterId() {
		return prmCounterId;
	}

	public void setPrmCounterId(int prmCounterId) {
		this.prmCounterId = prmCounterId;
	}
}
