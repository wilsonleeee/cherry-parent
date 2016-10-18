package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 销售明细报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/09/15
 */
public class BINOLWRSRP02_Form extends DataTable_BaseForm {
	
	/** 单据号 */
	private String billCode;
	
	/** 流水号 */
	private String saleRecordCode;
	
	/** 员工ID **/
	private String employeeId;
	
	/** 活动类型（0：会员活动，1：促销活动） **/
	private String campaignMode;
	
	/** 活动代码 **/
	private String campaignCode;
	
	/** 活动名称（用于导出报表显示此查询条件）*/
	private String campaignName;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 开始日期 */
	private String startDate;
	
	/** 结束日期 */
	private String endDate;
	
	/** 交易类型 */
	private String saleType;
	
	/** 消费者类型 */
	private String consumerType;
	
	/** 支付方式Code */
	private String payTypeCode;
	
	/** 单据类型 **/
	private String ticketType;
	
	/** 字符编码 **/
	private String charset;
	
	/** 营业员姓名 **/
	private String employeeName;
	
	/** 数据来源 **/
	private String channel;
	
	/** 产品类型 **/
	private String productType;
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getSaleRecordCode() {
		return saleRecordCode;
	}

	public void setSaleRecordCode(String saleRecordCode) {
		this.saleRecordCode = saleRecordCode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
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

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	public String getPayTypeCode() {
		return payTypeCode;
	}

	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
