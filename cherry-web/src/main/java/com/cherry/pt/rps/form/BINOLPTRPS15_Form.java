/*	
 * @(#)BINOLPTRPS15_Form.java     1.0 2012/10/31		
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 统计销售信息Form
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public class BINOLPTRPS15_Form extends BINOLCM13_Form {
	
	// 销售时间上限
	private String saleTimeStart;
	
	// 销售时间下限
	private String saleTimeEnd;
		
	// 产品厂商ID
	private String prtVendorId;
		
	// 销售统计模式
	private String countModel;
	
	/** 产品分类*/
	private String cateInfo;
	
	/**品牌ID*/
	private String brandInfoId;
	
	/**选中的产品组*/
	private String selPrtVendorIdArr;
	
	/** 发货模式 */
	private String deliveryModel;
	
	/**是否排除该渠道*/
	private String excludeFlag;
	
	/**主页面上的用于是否排除的渠道ID*/
	private String channelIdClude;
	
	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

	/**起止日期是否属于同一财务月*/
	private String fiscalMonthFlag;
	
	public String getFiscalMonthFlag() {
		return fiscalMonthFlag;
	}

	public void setFiscalMonthFlag(String fiscalMonthFlag) {
		this.fiscalMonthFlag = fiscalMonthFlag;
	}

	public String getSaleTimeStart() {
		return saleTimeStart;
	}

	public void setSaleTimeStart(String saleTimeStart) {
		this.saleTimeStart = saleTimeStart;
	}

	public String getSaleTimeEnd() {
		return saleTimeEnd;
	}

	public void setSaleTimeEnd(String saleTimeEnd) {
		this.saleTimeEnd = saleTimeEnd;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getCountModel() {
		return countModel;
	}

	public void setCountModel(String countModel) {
		this.countModel = countModel;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getSelPrtVendorIdArr() {
		return selPrtVendorIdArr;
	}

	public void setSelPrtVendorIdArr(String selPrtVendorIdArr) {
		this.selPrtVendorIdArr = selPrtVendorIdArr;
	}

	public String getDeliveryModel() {
		return deliveryModel;
	}

	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}

	public String getExcludeFlag() {
		return excludeFlag;
	}

	public void setExcludeFlag(String excludeFlag) {
		this.excludeFlag = excludeFlag;
	}

	public String getChannelIdClude() {
		return channelIdClude;
	}

	public void setChannelIdClude(String channelIdClude) {
		this.channelIdClude = channelIdClude;
	}

}
