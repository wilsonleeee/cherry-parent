/*		
 * @(#)SaleReturnMainDataDTO.java     1.0 2012/05/25		
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
package com.cherry.mq.mes.dto;

import java.util.List;

/**
 * mq主数据行映射DTO
 * 销售/退货单(老后台发送到新后台)，新销售退货的消息体的TYPE=0007
 * 
 * @author niushunjie
 * 
 */
public class SaleReturnMainDataDTO extends MainDataDTO{
	/** 单据号 */
	private String TradeNoIF;
	
	/** 修改回数 */
	private String ModifyCounts;
	
	/** 柜台号 */
	private String CounterCode;
	
	/** 关联柜台号/办事处代号 */
	private String RelevantCounterCode;
	
	/** 总数量 */
	private String TotalQuantity;
	
	/** 总金额 */
	private String TotalAmount;
	
	/** 子类型 */
	private String SubType;
	
	/** 关联单号 */
	private String RelevantNo;
	
	/** 入出库理由 */
	private String Reason;
	
	/** 出入库日期；YYYY-MM-DD */
	private String TradeDate;
	
	/** 出入库时间；HH:mm:ss */
	private String TradeTime;

	/** 入出库前柜台库存总金额 */
	private String TotalAmountBefore;
	
	/** 入出库后柜台库存总金额 */
	private String TotalAmountAfter;
	
	/** 会员号 */
	private String MemberCode;
	
	// MQ接口对应 1.10版 Start //
	
	/** 柜台销售单据号 */
	private String Counter_ticket_code;
	
	/** 前置关联的柜台销售单据号 */
	private String Counter_ticket_code_pre;
	
	/** 单据类型,表示补登，修改等 */
	private String Ticket_type;
	
	/** 该数据状态标志 */
	private String Sale_status;
	
	/** 消费者类型,普通个人或者普通团购 */
	private String Consumer_type;
	
	/** 会员等级代码 */
	private String Member_level;
	
	/** 折前金额（已经进行过单品折扣和其他促销） */
	private String Original_amount;
	
	/** 整单折扣率  */
	private String Discount;
	
	/** 折后金额（整单折扣后的金额）  */
	private String Pay_amount;
	
	/** 整单去零  */
	private String Decrease_amount;
	
	/** 花费积分  */
	private String Costpoint;
	
	/** 花费积分对应的抵扣金额  */
	private String Costpoint_amount;
	
	/** 在终端的单据生成时间  */
	private String Sale_ticket_time;
	
	/** 数据来源  */
	private String Data_source;
	
	/** 退货的标识  */
	private String SaleSRtype;
	
	/** BA卡号 */
	private String BAcode;
	
	/** 业务代行部门代号 */
	private String DepartCodeDX;
	
	/** 业务代行员工代号 */
	private String EmployeeCodeDX;
	
	/** 俱乐部代号 */
	private String clubCode;
	
	/**单据模式*/
	private String BillModel;
	
	/**原始数据来源*/
	private String OriginalDataSource;
	
	/**开发票的标识*/
	private String InvoiceFlag;
	
	/** 销售/退货原因 */
	private String SaleReason;
	
	/** 是否是否计算积分 */
    private String IsPoint;
    
    /** 敏感性建议书版本号  薇诺娜*/
    private String SensitiveSuggestVersion;
    
    /** 干燥性建议书版本号  薇诺娜*/
    private String DrySuggestVersion;
    
    /**  库存标记  */
    private String StockFlag;
    /**  实际处理库存的柜台 **/
    private String StockCounter;
    
    /**  预计提货日期 **/
    private String PickupDate;
    
    /**  预留手机号 **/
    private String MobilePhone;
    
    /**  销售退货申请单号 **/
    private String SaleReturnRequestCode;
	
	
	// MQ接口对应 1.10版 End //
	/**新销售体的明细*/
	private List<SaleReturnDetailDataDTO> DetailDataDTOList;


	public String getTradeNoIF() {
		return TradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		TradeNoIF = tradeNoIF;
	}

	public String getModifyCounts() {
		return ModifyCounts;
	}

	public void setModifyCounts(String modifyCounts) {
		ModifyCounts = modifyCounts;
	}

	public String getCounterCode() {
		return CounterCode;
	}

	public void setCounterCode(String counterCode) {
		CounterCode = counterCode;
	}

	public String getRelevantCounterCode() {
		return RelevantCounterCode;
	}

	public void setRelevantCounterCode(String relevantCounterCode) {
		RelevantCounterCode = relevantCounterCode;
	}

	public String getTotalQuantity() {
		return TotalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		TotalQuantity = totalQuantity;
	}

	public String getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		TotalAmount = totalAmount;
	}

	public String getSubType() {
		return SubType;
	}

	public void setSubType(String subType) {
		SubType = subType;
	}

	public String getRelevantNo() {
		return RelevantNo;
	}

	public void setRelevantNo(String relevantNo) {
		RelevantNo = relevantNo;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(String tradeDate) {
		TradeDate = tradeDate;
	}

	public String getTradeTime() {
		return TradeTime;
	}

	public void setTradeTime(String tradeTime) {
		TradeTime = tradeTime;
	}

	public String getTotalAmountBefore() {
		return TotalAmountBefore;
	}

	public void setTotalAmountBefore(String totalAmountBefore) {
		TotalAmountBefore = totalAmountBefore;
	}

	public String getTotalAmountAfter() {
		return TotalAmountAfter;
	}

	public void setTotalAmountAfter(String totalAmountAfter) {
		TotalAmountAfter = totalAmountAfter;
	}

	public String getMemberCode() {
		return MemberCode;
	}

	public void setMemberCode(String memberCode) {
		MemberCode = memberCode;
	}

	public List<SaleReturnDetailDataDTO> getDetailDataDTOList() {
		return DetailDataDTOList;
	}

	public void setDetailDataDTOList(List<SaleReturnDetailDataDTO> detailDataDTOList) {
		DetailDataDTOList = detailDataDTOList;
	}

	public String getCounter_ticket_code() {
		return Counter_ticket_code;
	}

	public void setCounter_ticket_code(String counterTicketCode) {
		Counter_ticket_code = counterTicketCode;
	}

	public String getCounter_ticket_code_pre() {
		return Counter_ticket_code_pre;
	}

	public void setCounter_ticket_code_pre(String counterTicketCodePre) {
		Counter_ticket_code_pre = counterTicketCodePre;
	}

	public String getTicket_type() {
		return Ticket_type;
	}

	public void setTicket_type(String ticketType) {
		Ticket_type = ticketType;
	}

	public String getSale_status() {
		return Sale_status;
	}

	public void setSale_status(String saleStatus) {
		Sale_status = saleStatus;
	}

	public String getConsumer_type() {
		return Consumer_type;
	}

	public void setConsumer_type(String consumerType) {
		Consumer_type = consumerType;
	}

	public String getMember_level() {
		return Member_level;
	}

	public void setMember_level(String memberLevel) {
		Member_level = memberLevel;
	}

	public String getOriginal_amount() {
		return Original_amount;
	}

	public void setOriginal_amount(String originalAmount) {
		Original_amount = originalAmount;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getPay_amount() {
		return Pay_amount;
	}

	public void setPay_amount(String payAmount) {
		Pay_amount = payAmount;
	}

	public String getDecrease_amount() {
		return Decrease_amount;
	}

	public void setDecrease_amount(String decreaseAmount) {
		Decrease_amount = decreaseAmount;
	}

	public String getCostpoint() {
		return Costpoint;
	}

	public void setCostpoint(String costpoint) {
		Costpoint = costpoint;
	}

	public String getCostpoint_amount() {
		return Costpoint_amount;
	}

	public void setCostpoint_amount(String costpointAmount) {
		Costpoint_amount = costpointAmount;
	}

	public String getSale_ticket_time() {
		return Sale_ticket_time;
	}

	public void setSale_ticket_time(String saleTicketTime) {
		Sale_ticket_time = saleTicketTime;
	}

	public String getData_source() {
		return Data_source;
	}

	public void setData_source(String dataSource) {
		Data_source = dataSource;
	}
	
    public String getSaleSRtype() {
        return SaleSRtype;
    }

    public void setSaleSRtype(String saleSRtype) {
        SaleSRtype = saleSRtype;
    }

    public String getBAcode() {
        return BAcode;
    }

    public void setBAcode(String bAcode) {
        BAcode = bAcode;
    }

    public String getDepartCodeDX() {
        return DepartCodeDX;
    }

    public void setDepartCodeDX(String departCodeDX) {
        DepartCodeDX = departCodeDX;
    }

    public String getEmployeeCodeDX() {
        return EmployeeCodeDX;
    }

    public void setEmployeeCodeDX(String employeeCodeDX) {
        EmployeeCodeDX = employeeCodeDX;
    }

	public String getClubCode() {
		return clubCode;
	}

	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}

    public String getBillModel() {
        return BillModel;
    }

    public void setBillModel(String billModel) {
        BillModel = billModel;
    }

    public String getOriginalDataSource() {
        return OriginalDataSource;
    }

    public void setOriginalDataSource(String originalDataSource) {
        OriginalDataSource = originalDataSource;
    }

	public String getInvoiceFlag() {
		return InvoiceFlag;
	}

	public void setInvoiceFlag(String invoiceFlag) {
		InvoiceFlag = invoiceFlag;
	}

	public String getSaleReason() {
		return SaleReason;
	}

	public void setSaleReason(String saleReason) {
		SaleReason = saleReason;
	}

	public String getIsPoint() {
		return IsPoint;
	}

	public void setIsPoint(String isPoint) {
		IsPoint = isPoint;
	}

	public String getSensitiveSuggestVersion() {
		return SensitiveSuggestVersion;
	}

	public void setSensitiveSuggestVersion(String sensitiveSuggestVersion) {
		SensitiveSuggestVersion = sensitiveSuggestVersion;
	}

	public String getDrySuggestVersion() {
		return DrySuggestVersion;
	}

	public void setDrySuggestVersion(String drySuggestVersion) {
		DrySuggestVersion = drySuggestVersion;
	}

	public String getStockFlag() {
		return StockFlag;
	}

	public void setStockFlag(String stockFlag) {
		StockFlag = stockFlag;
	}

	public String getStockCounter() {
		return StockCounter;
	}

	public void setStockCounter(String stockCounter) {
		StockCounter = stockCounter;
	}

	public String getPickupDate() {
		return PickupDate;
	}

	public void setPickupDate(String pickupDate) {
		PickupDate = pickupDate;
	}

	public String getMobilePhone() {
		return MobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		MobilePhone = mobilePhone;
	}

	public String getSaleReturnRequestCode() {
		return SaleReturnRequestCode;
	}

	public void setSaleReturnRequestCode(String saleReturnRequestCode) {
		SaleReturnRequestCode = saleReturnRequestCode;
	}
	
	
}
