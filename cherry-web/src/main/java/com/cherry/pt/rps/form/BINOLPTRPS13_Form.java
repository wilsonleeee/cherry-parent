/*		
 * @(#)BINOLPTRPS13_Form.java     1.0 2010/03/16		
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

package com.cherry.pt.rps.form;

import java.util.ArrayList;
import java.util.List;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 销售记录查询一览BINOLPTRPS13_Form
 * 
 * @author zgl
 * @version 1.0 2011/03/16
 * 
 * */
public class BINOLPTRPS13_Form extends BINOLCM13_Form {

	//单据号
	private String billCode;
	//交易类型
	private String saleType;
	//开始日期
	private String startDate;
	//结束日期
	private String endDate;
	//有效性
	private String validFlag;
	//消费者类型
	private String consumerType;
	//产品ID
	private String productId;
	//会员卡号
	private String memCode;
	//商品unitCode
	private String unitCode;
	//商品barCode
	private String barCode;
	//柜台类型
	private String counterKind;
	//商品名称
	private String productName;
	
	private String prtVendorId;
	
	private String deliveryModel;
	
	/** 子品牌 */
	private String originalBrand;
	
	/** 查询条件--模糊查询参数*/
	private String paramInfoStr;
	
	/** 查询条件--销售商品名称*/
	private String searchPrmProduct;
	
	/** 查询条件--销售商品厂商Id*/
	private String prmProductId;
	
	/** 销售商品类型 */
	private String prtType;
	
	/** 是否显示产品入出库成本 */
	private String isShow;
	
	/** 流水号 */
	private String saleRecordCode;
	
	/** 销售商品厂商ID(id,prtType) */
	private List<String> salePrtVendorId = new ArrayList<String>();
	
	/**销售商品名称*/
	private List<String> saleProPrmName = new ArrayList<String>();
	
	/**销售商品ID*/
	private List<String> saleRecordId = new ArrayList<String>();
	
	/**销售商品拼接查询条件(OR/AND) */
	private String saleProPrmConcatStr;
	
	/** 连带商品厂商ID(id,prtType) */
	private List<String> joinPrtVendorId = new ArrayList<String>();
	
	/** 连带商品名称*/
	private List<String> joinProPrmName = new ArrayList<String>();
	
	/**连带商品拼接查询条件(OR/AND) */
	private String jointProPrmConcatStr;
	
	/** 销售人员Code **/
	private String employeeCode;
	
	/** 查看各商品统计信息的查询参数（最后一次查询时使用的参数） **/
	private String paramsBak;
	
	/** 查询条件中选择销售商品及连带商品的总个数(用于动态显示查看各商品统计信息DIV的高度) **/
	private Long prtVendorIdLength;
	
	/** 活动类型（0：会员活动，1：促销活动） **/
	private String campaignMode;
	
	/** 活动代码 **/
	private String campaignCode;
	
	/** 活动名称（用于导出报表显示此查询条件）*/
	private String campaignName;
	
	/** 支付方式Code */
	private String payTypeCode;
	
	/** 字符编码 **/
	private String charset;
	
	/** 单据类型 **/
	private String ticketType;
	/** 发票类型 **/
	private String invoiceFlag;
	/** 订单来源 **/
	private String originalDataSource;
	/** 订单状态**/
	private String pickupStatus;
	/**会员ID*/
	private String memberInfoId;

	public String getInvoiceFlag() {
		return invoiceFlag;
	}
	public void setInvoiceFlag(String invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}
	public String getPrtVendorId() {
		return prtVendorId;
	}
	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCounterKind() {
		return counterKind;
	}
	public void setCounterKind(String counterKind) {
		this.counterKind = counterKind;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMemCode() {
		return memCode;
	}
	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}
	public String getConsumerType() {
		return consumerType;
	}
	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
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
	
	public String getParamInfoStr() {
		return paramInfoStr;
	}
	public void setParamInfoStr(String paramInfoStr) {
		this.paramInfoStr = paramInfoStr;
	}
	public String getSearchPrmProduct() {
		return searchPrmProduct;
	}
	public void setSearchPrmProduct(String searchPrmProduct) {
		this.searchPrmProduct = searchPrmProduct;
	}
	public String getPrmProductId() {
		return prmProductId;
	}
	public void setPrmProductId(String prmProductId) {
		this.prmProductId = prmProductId;
	}
	public String getPrtType() {
		return prtType;
	}
	public void setPrtType(String prtType) {
		this.prtType = prtType;
	}
	public List<String> getSalePrtVendorId() {
		return salePrtVendorId;
	}
	public void setSalePrtVendorId(List<String> salePrtVendorId) {
		if(null != salePrtVendorId){
			this.salePrtVendorId = salePrtVendorId;
		} 
	}
	public List<String> getSaleProPrmName() {
		return saleProPrmName;
	}
	public void setSaleProPrmName(List<String> saleProPrmName) {
		if(null != saleProPrmName){
			this.saleProPrmName = saleProPrmName;
		}
	}
	public String getSaleProPrmConcatStr() {
		return saleProPrmConcatStr;
	}
	public void setSaleProPrmConcatStr(String saleProPrmConcatStr) {
		this.saleProPrmConcatStr = saleProPrmConcatStr;
	}
	public List<String> getJoinPrtVendorId() {
		return joinPrtVendorId;
	}
	public void setJoinPrtVendorId(List<String> joinPrtVendorId) {
		if(null != joinPrtVendorId){
			this.joinPrtVendorId = joinPrtVendorId;
		}
	}
	public List<String> getJoinProPrmName() {
		return joinProPrmName;
	}
	public void setJoinProPrmName(List<String> joinProPrmName) {
		if(null != joinProPrmName){
			this.joinProPrmName = joinProPrmName;
		}
	}
	public String getJointProPrmConcatStr() {
		return jointProPrmConcatStr;
	}
	public void setJointProPrmConcatStr(String jointProPrmConcatStr) {
		this.jointProPrmConcatStr = jointProPrmConcatStr;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getParamsBak() {
		return paramsBak;
	}
	public void setParamsBak(String paramsBak) {
		this.paramsBak = paramsBak;
	}
	
	public Long getPrtVendorIdLength() {
		return prtVendorIdLength;
	}
	public void setPrtVendorIdLength(Long prtVendorIdLength) {
		this.prtVendorIdLength = prtVendorIdLength;
	}
	
	public String getCampaignMode() {
		return campaignMode;
	}
	public void setCampaignMode(String campaignMode) {
		this.campaignMode = campaignMode;
	}
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	
	public String getPayTypeCode() {
		return payTypeCode;
	}
	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getSaleRecordCode() {
		return saleRecordCode;
	}
	public void setSaleRecordCode(String saleRecordCode) {
		this.saleRecordCode = saleRecordCode;
	}
	
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public List<String> getSaleRecordId() {
		return saleRecordId;
	}
	public void setSaleRecordId(List<String> saleRecordId) {
		this.saleRecordId = saleRecordId;
	}
	public String getDeliveryModel() {
		return deliveryModel;
	}
	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}
	public String getOriginalBrand() {
		return originalBrand;
	}
	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getOriginalDataSource() {
		return originalDataSource;
	}
	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}
	public String getPickupStatus() {
		return pickupStatus;
	}
	public void setPickupStatus(String pickupStatus) {
		this.pickupStatus = pickupStatus;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}
}
