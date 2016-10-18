/*
 * @(#)BINOLPTRPS33_Form.java     1.0 2014/9/24
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


import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 电商订单一览Form
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
public class BINOLPTRPS33_Form extends BINOLCM13_Form{
    /** 单据号 */
    private String billCode;
    
    /** 交易类型 */
    private String saleType;
    
    /** 开始日期 */
    private String startDate;
    
    /** 结束日期 */
    private String endDate;
    
    /** 单据付款开始日期 */
    private String payStartDate;
    
    /** 单据付款结束日期 */
    private String payEndDate;
    
    /** 有效性 */
    private String validFlag;
    
    /** 消费者类型 */
    private String consumerType;
    
    /** 会员卡号 */
    private String memCode;
    
    /** 柜台类型 */
    private String counterKind;
    
    /** 销售商品类型 */
    private String prtType;
    
    /** 流水号 */
    private String saleRecordCode;
    
    /** 销售人员Code **/
    private String employeeCode;
    
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
    
    /** 单据状态 */
    private String billState;
    
    /** 是否预售单（1：是   2：否）*/
    private String preSale;
    
    /** 假日 */
    private String holidays;
    
    /**  销售统计 */
    private Map<String, Object> sumInfo;
    
    /** 电商订单记录 */
    private List<Map<String,Object>> esOrderMainList;
    
    /** 是否是异常单（1：是   0：否）*/
    private  String exceptionList;
    
    
    /** 销售商品厂商ID(id,saleType) */
	private String prtVendorIdList;

	
	
	/**销售商品对应的关联关系*/
	private String saleProPrmConcatStr;
	
	/** 宝贝编码*/
	 private String outCode;
	 

		
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

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getMemCode() {
        return memCode;
    }

    public void setMemCode(String memCode) {
        this.memCode = memCode;
    }

    public String getCounterKind() {
        return counterKind;
    }

    public void setCounterKind(String counterKind) {
        this.counterKind = counterKind;
    }

    public String getPrtType() {
        return prtType;
    }

    public void setPrtType(String prtType) {
        this.prtType = prtType;
    }

    public String getSaleRecordCode() {
        return saleRecordCode;
    }

    public void setSaleRecordCode(String saleRecordCode) {
        this.saleRecordCode = saleRecordCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public List<Map<String, Object>> getEsOrderMainList() {
        return esOrderMainList;
    }

    public void setEsOrderMainList(List<Map<String, Object>> esOrderMainList) {
        this.esOrderMainList = esOrderMainList;
    }

    public String getBillState() {
        return billState;
    }

    public void setBillState(String billState) {
        this.billState = billState;
    }

	public String getPreSale() {
		return preSale;
	}

	public void setPreSale(String preSale) {
		this.preSale = preSale;
	}

	public String getPayStartDate() {
		return payStartDate;
	}

	public void setPayStartDate(String payStartDate) {
		this.payStartDate = payStartDate;
	}

	public String getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(String payEndDate) {
		this.payEndDate = payEndDate;
	}

	public String getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(String exceptionList) {
		this.exceptionList = exceptionList;
	}



	public String getPrtVendorIdList() {
		return prtVendorIdList;
	}

	public void setPrtVendorIdList(String prtVendorIdList) {
		this.prtVendorIdList = prtVendorIdList;
	}


	public String getSaleProPrmConcatStr() {
		return saleProPrmConcatStr;
	}

	public void setSaleProPrmConcatStr(String saleProPrmConcatStr) {
		this.saleProPrmConcatStr = saleProPrmConcatStr;
	}

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}


	
	
	
	
}