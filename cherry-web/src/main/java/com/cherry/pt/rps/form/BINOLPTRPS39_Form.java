package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLPTRPS39_Form  extends BINOLCM13_Form{
	
	/**单据ID*/
	private String reminderId;
	
	/**催单单据号 */
	private String reminderNo;
	
	/**货物类型,"N":产品;"P":促销品*/
	private String cargoType;
	
	/**发货单号 */
	private String deliverNo;
	
	/**延迟天数*/
	private String delayDate;
	
	/**单据类型*/
	private String reminderType;//0：收货延迟催单（从后台下发到终端的订单）；1：反向催单（从pos终端提交的单据）
	
	/**催单次数*/
	private String reminderCount;
	
	/**柜台号*/
	private String counterCode;
	
	/**BA工号*/
	private String employeeCode;
	
	/**收货数量*/
	private String receiveAmount;
	
	/**收货日期*/
	private String receiveDate;
	
	/**备注*/
	private String comment;
	
	/**开始日期*/
	private String startDate;
	
	/**结束日期*/
	private String endDate;
	
	/**催单状态*/
	private String status;

	/**审核状态*/
	private String verifiedFlag;
	
	/**处理状态*/
	private String tradeStatus;
	
	/**判断收货部门还是发货部门；"0":发货部门；"1":收货部门*/
	private String depFlag;
	
	private String departInit;
    
	private int organizationId;
    
	 /** （配置项）检查库存大于发货数量标志 */
    private String checkStockFlag;
    
    /** （配置项）发货画面设置清理建议明细按钮数量小于 */
    private String delQuantityLT;
    
    /** （配置项）产品发货使用价格 */
    private String sysConfigUsePrice;
	
	private String parentCsrftoken;

	private String stockInFlag;
	
	private String revCounterId;
	
	private String revCounterName;
	
	public String getDeliverNo() {
		return deliverNo;
	}

	public String getRevCounterId() {
		return revCounterId;
	}

	public void setRevCounterId(String revCounterId) {
		this.revCounterId = revCounterId;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getDelayDate() {
		return delayDate;
	}

	public void setDelayDate(String delayDate) {
		this.delayDate = delayDate;
	}

	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	public String getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(String reminderCount) {
		this.reminderCount = reminderCount;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(String receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getReminderNo() {
		return reminderNo;
	}

	public void setReminderNo(String reminderNo) {
		this.reminderNo = reminderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getDepFlag() {
		return depFlag;
	}

	public void setDepFlag(String depFlag) {
		this.depFlag = depFlag;
	}

	public String getDepartInit() {
		return departInit;
	}

	public void setDepartInit(String departInit) {
		this.departInit = departInit;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getCheckStockFlag() {
		return checkStockFlag;
	}

	public void setCheckStockFlag(String checkStockFlag) {
		this.checkStockFlag = checkStockFlag;
	}

	public String getDelQuantityLT() {
		return delQuantityLT;
	}

	public void setDelQuantityLT(String delQuantityLT) {
		this.delQuantityLT = delQuantityLT;
	}

	public String getSysConfigUsePrice() {
		return sysConfigUsePrice;
	}

	public void setSysConfigUsePrice(String sysConfigUsePrice) {
		this.sysConfigUsePrice = sysConfigUsePrice;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getParentCsrftoken() {
		return parentCsrftoken;
	}

	public void setParentCsrftoken(String parentCsrftoken) {
		this.parentCsrftoken = parentCsrftoken;
	}

	public String getStockInFlag() {
		return stockInFlag;
	}

	public void setStockInFlag(String stockInFlag) {
		this.stockInFlag = stockInFlag;
	}

	public String getRevCounterName() {
		return revCounterName;
	}

	public void setRevCounterName(String revCounterName) {
		this.revCounterName = revCounterName;
	}

}
