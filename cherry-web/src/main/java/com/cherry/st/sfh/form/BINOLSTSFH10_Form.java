/*  
 * @(#)BINOLPTODR01_Form.java    1.0 2011-8-10     
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

package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH10_Form extends DataTable_BaseForm {

	//品牌
	private String brandInfoId;
	
	//组织ID
	private String organizationId;
	
	//产品厂商ID
	private String productVendorId;
	
	//最低库存天数
	private String lowestStockDays;
	
	/** 旬环比增长系数 */
	private String growthFactor;
	
	/** 主推计划(调整系数)*/
	private String regulateFactor;
	
	/**月销售天数*/
	private String saleDaysOfMonth;
	
	/**对X天内有销售的产品生成建议发货数量*/
	private String daysOfProduct;
	
	//调整系数
	private String adtCoefficient;
	
	//订货间隔
	private String orderDays;
	
	//在途天数
	private String intransitDays;
	
	//商品名称
	private String productName;
	
	//部门名称
	private String departName;
	
	//年月
	private String date;
	
	//部门code
	private String departCode;
	
	//选中的产品
	private String checkedPrt;
	
	//选中的柜台
	private String checkedCut;
	
	//要获取的树节点标志
	private String treeNodesFlag;
	
	private String counterPrtOrParameterId;
	
	private String productOrderParameterId;
	
	private String counterOrderParameterId;
	
	/**全局订货参数ID*/
	private String globalParameterId;
	
	private String flag;
	
	public String getCheckedPrt() {
		return checkedPrt;
	}

	public void setCheckedPrt(String checkedPrt) {
		this.checkedPrt = checkedPrt;
	}

	public String getCheckedCut() {
		return checkedCut;
	}

	public void setCheckedCut(String checkedCut) {
		this.checkedCut = checkedCut;
	}

	public String getTreeNodesFlag() {
		return treeNodesFlag;
	}

	public void setTreeNodesFlag(String treeNodesFlag) {
		this.treeNodesFlag = treeNodesFlag;
	}

	public String getCounterPrtOrParameterId() {
		return counterPrtOrParameterId;
	}

	public void setCounterPrtOrParameterId(String counterPrtOrParameterId) {
		this.counterPrtOrParameterId = counterPrtOrParameterId;
	}

	public String getProductOrderParameterId() {
		return productOrderParameterId;
	}

	public void setProductOrderParameterId(String productOrderParameterId) {
		this.productOrderParameterId = productOrderParameterId;
	}

	public String getCounterOrderParameterId() {
		return counterOrderParameterId;
	}

	public void setCounterOrderParameterId(String counterOrderParameterId) {
		this.counterOrderParameterId = counterOrderParameterId;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getProductVendorId() {
		return productVendorId;
	}

	public void setProductVendorId(String productVendorId) {
		this.productVendorId = productVendorId;
	}

	public String getLowestStockDays() {
		return lowestStockDays;
	}

	public void setLowestStockDays(String lowestStockDays) {
		this.lowestStockDays = lowestStockDays;
	}

	public String getAdtCoefficient() {
		return adtCoefficient;
	}

	public void setAdtCoefficient(String adtCoefficient) {
		this.adtCoefficient = adtCoefficient;
	}

	public String getOrderDays() {
		return orderDays;
	}

	public void setOrderDays(String orderDays) {
		this.orderDays = orderDays;
	}

	public String getIntransitDays() {
		return intransitDays;
	}

	public void setIntransitDays(String intransitDays) {
		this.intransitDays = intransitDays;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSaleDaysOfMonth() {
		return saleDaysOfMonth;
	}

	public void setSaleDaysOfMonth(String saleDaysOfMonth) {
		this.saleDaysOfMonth = saleDaysOfMonth;
	}

	public String getGlobalParameterId() {
		return globalParameterId;
	}

	public void setGlobalParameterId(String globalParameterId) {
		this.globalParameterId = globalParameterId;
	}

	public String getGrowthFactor() {
		return growthFactor;
	}

	public void setGrowthFactor(String growthFactor) {
		this.growthFactor = growthFactor;
	}

	public String getRegulateFactor() {
		return regulateFactor;
	}

	public void setRegulateFactor(String regulateFactor) {
		this.regulateFactor = regulateFactor;
	}

	public String getDaysOfProduct() {
		return daysOfProduct;
	}

	public void setDaysOfProduct(String daysOfProduct) {
		this.daysOfProduct = daysOfProduct;
	}
	
}
