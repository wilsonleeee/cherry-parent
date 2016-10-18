/*
 * @(#)BINOLPTJCS07_Form.java     1.0 2014/06/17
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

import com.cherry.cm.form.BaseForm;

/**
 * 
 * 柜台产品价格维护
 * 
 * 
 * 
 * @author jijw
 * @version 1.0 2014.06.17
 */
public class BINOLPTJCS14_Form extends BaseForm{

	/** 柜台产品配置主表ID **/
	private Integer departProductConfigID;
	
	/** 配置名称 **/
	private String configName;
	
	/** 分配地点类型 */
	private String placeType;
	
	/** 分配地点JSON */
	private String placeJson;
	
	/** 分配地点结束JSON */
	private String saveJson;
	
	/** 柜台产品配置明细ID */
	private Integer departProductConfigDetailID;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 产品价格方案ID */
	private Integer productPriceSolutionID;
	
	/** 方案名称 */
	private String solutionName;
	
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
	
	/** 产品价格方案明细ID */
	private Integer productPriceSolutionDetailID;
	
	/** 产品ID */
	private Integer productID;
	
	/** 价格Json */
	private String priceJson;
	
	/* ========================================================================================================= */
	
	/** 柜台树形结束 节点Id  **/
	private String objId;
	
	/** 柜台号数组集合  **/
	private String cntArr;
	
	/** 地点数组集合  **/
	private String locationArr;
	
	/** 分类路径 */
	private String path;
	
	/** 是否产品的最后一个分类层级 */
	private Boolean isLastCate;
	
	/** 销售最低价 */
	private String minSalePrice;
	
	/** 销售最高价 */
	private String maxSalePrice;
	
	/** 原方案地点类型 */
	private String placeTypeOld;
	
	/** 是否门店自设 */
	private String cntOwnOpt;

	public Integer getDepartProductConfigID() {
		return departProductConfigID;
	}

	public void setDepartProductConfigID(Integer departProductConfigID) {
		this.departProductConfigID = departProductConfigID;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}
	
	public String getPlaceJson() {
		return placeJson;
	}

	public void setPlaceJson(String placeJson) {
		this.placeJson = placeJson;
	}

	public String getSaveJson() {
		return saveJson;
	}

	public void setSaveJson(String saveJson) {
		this.saveJson = saveJson;
	}

	public Integer getDepartProductConfigDetailID() {
		return departProductConfigDetailID;
	}

	public void setDepartProductConfigDetailID(Integer departProductConfigDetailID) {
		this.departProductConfigDetailID = departProductConfigDetailID;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

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

	public void setEndTime(String endDate) {
		this.endTime = endDate;
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

	public String getPriceJson() {
		return priceJson;
	}

	public void setPriceJson(String priceJson) {
		this.priceJson = priceJson;
	}
	
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
	
	public String getCntArr() {
		return cntArr;
	}

	public void setCntArr(String cntArr) {
		this.cntArr = cntArr;
	}
	
	public String getLocationArr() {
		return locationArr;
	}

	public void setLocationArr(String locationArr) {
		this.locationArr = locationArr;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Boolean getIsLastCate() {
		return isLastCate;
	}

	public void setIsLastCate(Boolean isLastCate) {
		this.isLastCate = isLastCate;
	}
	
	public String getMinSalePrice() {
		return minSalePrice;
	}

	public void setMinSalePrice(String minSalePrice) {
		this.minSalePrice = minSalePrice;
	}

	public String getMaxSalePrice() {
		return maxSalePrice;
	}

	public void setMaxSalePrice(String maxSalePrice) {
		this.maxSalePrice = maxSalePrice;
	}
	
	public String getPlaceTypeOld() {
		return placeTypeOld;
	}

	public void setPlaceTypeOld(String placeTypeOld) {
		this.placeTypeOld = placeTypeOld;
	}
	
	public String getCntOwnOpt() {
		return cntOwnOpt;
	}

	public void setCntOwnOpt(String cntOwnOpt) {
		this.cntOwnOpt = cntOwnOpt;
	}
	
}
