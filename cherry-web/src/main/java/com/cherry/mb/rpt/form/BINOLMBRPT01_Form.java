/*
 * @(#)BINOLMBRPT01_Form.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.form;

/**
 * 会员销售报表Form
 * 
 * @author WangCT
 * @version 1.0 2013/10/12
 */
public class BINOLMBRPT01_Form {
	
	/** 日期模式 */
	private String dateMode;
	
	/** 销售时间上限 */
	private String saleDateStart;
	
	/** 销售时间下限 */
	private String saleDateEnd;
	
	/** 销售财务年 */
	private String fiscalYear;
	
	/** 销售财务月 */
	private String fiscalMonth;
	
	/** 渠道ID */
	private String channelId;
	
	/** 柜台部门ID */
	private String organizationId;
	
	/** 渠道名称 */
	private String channelName;
	
	/** 柜台部门名称 */
	private String organizationName;

	public String getDateMode() {
		return dateMode;
	}

	public void setDateMode(String dateMode) {
		this.dateMode = dateMode;
	}

	public String getSaleDateStart() {
		return saleDateStart;
	}

	public void setSaleDateStart(String saleDateStart) {
		this.saleDateStart = saleDateStart;
	}

	public String getSaleDateEnd() {
		return saleDateEnd;
	}

	public void setSaleDateEnd(String saleDateEnd) {
		this.saleDateEnd = saleDateEnd;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public String getFiscalMonth() {
		return fiscalMonth;
	}

	public void setFiscalMonth(String fiscalMonth) {
		this.fiscalMonth = fiscalMonth;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

}
