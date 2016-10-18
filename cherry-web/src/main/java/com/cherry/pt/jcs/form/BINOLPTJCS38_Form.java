/*  
 * @(#)BINOLPTJCS18_Form.java     1.0 2015/01/19      
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
package com.cherry.pt.jcs.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品功能开启时间
 * 
 * @author JiJW
 * @version 1.0 2015-1-19
 */
public class BINOLPTJCS38_Form extends DataTable_BaseForm{
	
	/** 产品功能开启时间ID */
	private Integer productFunctionID;
	
	/** 功能开启时间Code */
	private String prtFunDateCode;
	
	/** 功能开启时间Name */
	private String prtFunDateName;
	
	/** 功能类别 */
	private String prtFunType;
	
	/** 方案生效开始日期  */
	private String startDate;
	
	/** 方案生效结束日期  */
	private String endDate;
	
	/** 产品功能开启时间明细ID */
	private Integer productFunctionDetailID;
	
	/** 产品ID */
	private Integer productID;
	
	/** 产品ID */
	private String productInfoIds;
	
	private String prtFunUpdateTime;

	private String prtFunModifyCount;
	
    /**产品ID */
    private String[] prtIdArr;
	
	/** 节日  */
	private String holidays;
	
	private String validFlag;
	
	private String brandInfoId;
	
	private String[] prtFunIdArr;
	
	private List<Map<String, Object>> prtFunList;
	
	/** 产品分类信息 */
	private String cateInfo;
	
	public Integer getProductFunctionID() {
		return productFunctionID;
	}

	public void setProductFunctionID(Integer productFunctionID) {
		this.productFunctionID = productFunctionID;
	}

	public String getPrtFunDateCode() {
		return prtFunDateCode;
	}

	public void setPrtFunDateCode(String prtFunDateCode) {
		this.prtFunDateCode = prtFunDateCode;
	}
	
	public String getPrtFunDateName() {
		return prtFunDateName;
	}

	public void setPrtFunDateName(String prtFunDateName) {
		this.prtFunDateName = prtFunDateName;
	}
	
	public String getPrtFunType() {
		return prtFunType;
	}

	public void setPrtFunType(String prtFunType) {
		this.prtFunType = prtFunType;
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

	public Integer getProductFunctionDetailID() {
		return productFunctionDetailID;
	}

	public void setProductFunctionDetailID(Integer productFunctionDetailID) {
		this.productFunctionDetailID = productFunctionDetailID;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getProductInfoIds() {
		return productInfoIds;
	}

	public void setProductInfoIds(String productInfoIds) {
		this.productInfoIds = productInfoIds;
	}
	
	public String getPrtFunUpdateTime() {
		return prtFunUpdateTime;
	}

	public void setPrtFunUpdateTime(String prtFunUpdateTime) {
		this.prtFunUpdateTime = prtFunUpdateTime;
	}

	public String getPrtFunModifyCount() {
		return prtFunModifyCount;
	}

	public void setPrtFunModifyCount(String prtFunModifyCount) {
		this.prtFunModifyCount = prtFunModifyCount;
	}

	public String[] getPrtIdArr() {
		return prtIdArr;
	}

	public void setPrtIdArr(String[] prtIdArr) {
		this.prtIdArr = prtIdArr;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
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

	public String[] getPrtFunIdArr() {
		return prtFunIdArr;
	}

	public void setPrtFunIdArr(String[] prtFunIdArr) {
		this.prtFunIdArr = prtFunIdArr;
	}

	public List<Map<String, Object>> getPrtFunList() {
		return prtFunList;
	}

	public void setPrtFunList(List<Map<String, Object>> prtFunList) {
		this.prtFunList = prtFunList;
	}
	
	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}
	
}
