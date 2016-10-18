/*		
 * @(#)BINOLSSPRM15_Form.java     1.0 2011/10/17		
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
public class BINOLSSPRM15_Form extends DataTable_BaseForm{
	/** 活动信息*/
	private List activeList;
	
	/** 活动id */
	private String activeID;
	
	/** 促销品类型 */
	private String prtType;
	
	/** 查询条件--促销活动名 */
	private String prmActiveName;
	
	/** 查询条件--促销柜台 */
	private String prmCounter;
	
	/** 查询条件--促销产品*/
	private String prmProduct;
	
	/** 查询条件--促销活动名 */
	private String activityCode;
	
	/** 查询条件--促销柜台 */
	private String prmCounterId;
	
	/** 查询条件--促销产品*/
	private String prmProductId;
	
	/** 查询条件--活动状态*/
	private String actState;
	
	/** 查询条件--促销活动起始日期*/
	private String startDate;
	
	/** 查询条件--促销活动结束日期*/
	private String endDate;
	
	/** 查询条件--停用柜台*/
	private String validFlag;
	
	/** 查询条件--模糊查询参数*/
	private String paramInfoStr;
	
	/** 假日 */
	private String holidays;
 
	private int number;
	
	public String getPrtType() {
		return prtType;
	}

	public void setPrtType(String prtType) {
		this.prtType = prtType;
	}

	public List getActiveList() {
		return activeList;
	}

	public void setActiveList(List activeList) {
		this.activeList = activeList;
	}

	public String getActiveID() {
		return activeID;
	}

	public void setActiveId(String activeID) {
		this.activeID = activeID;
	}

	public void setActiveID(String activeID) {
		this.activeID = activeID;
	}

	public String getPrmActiveName() {
		return prmActiveName;
	}

	public void setPrmActiveName(String prmActiveName) {
		this.prmActiveName = prmActiveName;
	}

	public String getPrmCounter() {
		return prmCounter;
	}

	public void setPrmCounter(String prmCounter) {
		this.prmCounter = prmCounter;
	}

	public String getPrmProduct() {
		return prmProduct;
	}

	public void setPrmProduct(String prmProduct) {
		this.prmProduct = prmProduct;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getPrmCounterId() {
		return prmCounterId;
	}

	public void setPrmCounterId(String prmCounterId) {
		this.prmCounterId = prmCounterId;
	}

	public String getPrmProductId() {
		return prmProductId;
	}

	public void setPrmProductId(String prmProductId) {
		this.prmProductId = prmProductId;
	}

	public String getParamInfoStr() {
		return paramInfoStr;
	}

	public void setParamInfoStr(String paramInfoStr) {
		this.paramInfoStr = paramInfoStr;
	}

	public String getActState() {
		return actState;
	}

	public void setActState(String actState) {
		this.actState = actState;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
