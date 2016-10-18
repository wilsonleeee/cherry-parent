/*
 * @(#)BINOLCTRPT06_Form.java     1.0 2014/11/12
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
package com.cherry.ct.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 会员沟通效果统计Form
 * 
 * @author ZhangGS
 * @version 1.0 2014.11.11
 */
public class BINOLCTRPT06_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;

	/**开始时间*/
	private String startDate;
	
	/**结束时间*/
	private String endDate;
	
	/**短信成本*/
	private String price;
	
	/** 沟通名称 */
	private String communicationName;
	
	/**沟通号*/
	private String communicationCode;
	
	/**明细查询的开始时间*/
	private String startDateDetail;
	
	/**明细查询的结束时间*/
	private String endDateDetail;
	
	/** 渠道 */
	private String channelId;
	
	/** 柜台 */
	private String counterCode;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCommunicationName() {
		return communicationName;
	}

	public void setCommunicationName(String communicationName) {
		this.communicationName = communicationName;
	}

	public String getCommunicationCode() {
		return communicationCode;
	}

	public void setCommunicationCode(String communicationCode) {
		this.communicationCode = communicationCode;
	}

	public String getStartDateDetail() {
		return startDateDetail;
	}

	public void setStartDateDetail(String startDateDetail) {
		this.startDateDetail = startDateDetail;
	}

	public String getEndDateDetail() {
		return endDateDetail;
	}

	public void setEndDateDetail(String endDateDetail) {
		this.endDateDetail = endDateDetail;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	
}
