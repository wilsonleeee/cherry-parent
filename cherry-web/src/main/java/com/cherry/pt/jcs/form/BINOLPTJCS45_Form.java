/*  
 * @(#)BINOLPTJCS38_Form.java     1.0 2015/01/19      
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


public class BINOLPTJCS45_Form extends DataTable_BaseForm{
	
	/** 产品价格方案ID */
	private Integer productPriceSolutionID;
	
	/** 方案名称 */
	private String solutionName;
	
	/** 方案编码 */
	private String solutionCode;
	
	/** 价格类型 */
	private String priceType;
	
	/** 价格内容 (根据priceType)  */
	private String priceContent;
	
	/** 产品树形分配JSON */
	private String prtJson;
	
	/** 产品树形分配结束JSON  */
	private String prtSaveJson;
	
	/** 方案描述  */
	private String comments;
	
	/** 方案生效开始日期  */
	private String startTime;
	
	/** 方案生效结束日期  */
	private String endTime;
	
	/** 方案生效开始日期  */
	private String startDate;
	
	/** 方案生效结束日期  */
	private String endDate;
	
	/** 产品价格方案明细ID */
	private Integer productPriceSolutionDetailID;
	
	/** 产品ID */
	private Integer productID;
	
	/** 产品ID */
	private String productInfoIds;
	
	/** 价格Json */
	private String priceJson;
	
	/** 销售价格 */
	private String salePrice;
	
	/** 会员价格 */
	private String memPrice;
	
	private String soluUpdateTime;

	private String soluModifyCount;
	
	/** 销售最低价 */
    private String[] minSalePriceArr;
    /** 销售最高价 */
    private String[] maxSalePriceArr;
    
    /**价格*/
    private String[] salePriceArr;
    /**会员价格*/
    private String[] memPriceArr;
    /**产品ID */
    private String[] prtIdArr;
    
	/** 方案产品名称 */
    private String soluProductName;
    
    /** 方案产品名称集合 */
    private String [] soluProductNameArr;
	
	/** 节日  */
	private String holidays;
	
	private String validFlag;
	
	private String brandInfoId;
	
	private String[] solutionIdArr;
	
	private List<Map<String, Object>> solutionList;
	
	private String isSynchProductPrice;
	
	/** 产品分类信息 */
	private String cateInfo;
	
	public Integer getProductPriceSolutionID() {
		return productPriceSolutionID;
	}

	public void setProductPriceSolutionID(Integer productPriceSolutionID) {
		this.productPriceSolutionID = productPriceSolutionID;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	
	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPriceContent() {
		return priceContent;
	}

	public void setPriceContent(String priceContent) {
		this.priceContent = priceContent;
	}

	public String getPrtJson() {
		return prtJson;
	}

	public void setPrtJson(String prtJson) {
		this.prtJson = prtJson;
	}

	public String getPrtSaveJson() {
		return prtSaveJson;
	}

	public void setPrtSaveJson(String prtSaveJson) {
		this.prtSaveJson = prtSaveJson;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public Integer getProductPriceSolutionDetailID() {
		return productPriceSolutionDetailID;
	}

	public void setProductPriceSolutionDetailID(Integer productPriceSolutionDetailID) {
		this.productPriceSolutionDetailID = productPriceSolutionDetailID;
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

	public String getPriceJson() {
		return priceJson;
	}

	public void setPriceJson(String priceJson) {
		this.priceJson = priceJson;
	}
	
	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getMemPrice() {
		return memPrice;
	}

	public void setMemPrice(String memPrice) {
		this.memPrice = memPrice;
	}
	
	public String getSoluUpdateTime() {
		return soluUpdateTime;
	}

	public void setSoluUpdateTime(String soluUpdateTime) {
		this.soluUpdateTime = soluUpdateTime;
	}

	public String getSoluModifyCount() {
		return soluModifyCount;
	}

	public void setSoluModifyCount(String soluModifyCount) {
		this.soluModifyCount = soluModifyCount;
	}
	
	public String[] getMinSalePriceArr() {
		return minSalePriceArr;
	}

	public void setMinSalePriceArr(String[] minSalePriceArr) {
		this.minSalePriceArr = minSalePriceArr;
	}

	public String[] getMaxSalePriceArr() {
		return maxSalePriceArr;
	}

	public void setMaxSalePriceArr(String[] maxSalePriceArr) {
		this.maxSalePriceArr = maxSalePriceArr;
	}
	
    public String[] getSalePriceArr() {
		return salePriceArr;
	}

	public void setSalePriceArr(String[] salePriceArr) {
		this.salePriceArr = salePriceArr;
	}

	public String[] getMemPriceArr() {
		return memPriceArr;
	}

	public void setMemPriceArr(String[] memPriceArr) {
		this.memPriceArr = memPriceArr;
	}

	public String[] getPrtIdArr() {
		return prtIdArr;
	}

	public void setPrtIdArr(String[] prtIdArr) {
		this.prtIdArr = prtIdArr;
	}
	
    public String getSoluProductName() {
		return soluProductName;
	}

	public void setSoluProductName(String soluProductName) {
		this.soluProductName = soluProductName;
	}

	public String[] getSoluProductNameArr() {
		return soluProductNameArr;
	}

	public void setSoluProductNameArr(String[] soluProductNameArr) {
		this.soluProductNameArr = soluProductNameArr;
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
	
	public String[] getSolutionIdArr() {
		return solutionIdArr;
	}

	public void setSolutionIdArr(String[] solutionIdArr) {
		this.solutionIdArr = solutionIdArr;
	}

	public List<Map<String, Object>> getSolutionList() {
		return solutionList;
	}

	public void setSolutionList(List<Map<String, Object>> solutionList) {
		this.solutionList = solutionList;
	}
	
	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

	public String getIsSynchProductPrice() {
		return isSynchProductPrice;
	}

	public void setIsSynchProductPrice(String isSynchProductPrice) {
		this.isSynchProductPrice = isSynchProductPrice;
	}
}
