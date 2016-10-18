/*		
 * @(#)SalMainDataDTO.java     1.0 2010/12/01		
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
 * 销售库存主数据行映射DTO
 * @author huzude
 *
 */
public class SalMainDataDTO extends MainDataDTO{
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
	
	// MQ接口对应 1.10版 End //
	
	/**礼品领用 获知活动方式 */
	private String InformType;
	
	/**期望发货日期*/
    private String ExpectDeliverDate;
    
    /**是否启用了工作流*/
    private String WorkFlow;
    
    /** 盘点原因CODE */
    private String StockReason;
    
    /** BA卡号*/
    private String BAcode;
    
    /** 出库部门代号（TradeType=KS） */
    private String OutDepartCode;
	
	private List<SalDetailDataDTO> DetailDataDTOList;


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

	public List<SalDetailDataDTO> getDetailDataDTOList() {
		return DetailDataDTOList;
	}

	public void setDetailDataDTOList(List<SalDetailDataDTO> detailDataDTOList) {
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

    public String getInformType() {
        return InformType;
    }

    public void setInformType(String informType) {
        InformType = informType;
    }

    public String getExpectDeliverDate() {
        return ExpectDeliverDate;
    }

    public void setExpectDeliverDate(String expectDeliverDate) {
        ExpectDeliverDate = expectDeliverDate;
    }

    public String getWorkFlow() {
        return WorkFlow;
    }

    public void setWorkFlow(String workFlow) {
        WorkFlow = workFlow;
    }

	public String getStockReason() {
		return StockReason;
	}

	public void setStockReason(String stockReason) {
		StockReason = stockReason;
	}

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getOutDepartCode() {
		return OutDepartCode;
	}

	public void setOutDepartCode(String outDepartCode) {
		OutDepartCode = outDepartCode;
	}
}
