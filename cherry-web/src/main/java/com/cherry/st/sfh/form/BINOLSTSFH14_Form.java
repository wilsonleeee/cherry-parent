package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH14_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 销售单据号 */
	private String saleOrderNo;
	
	/** 销售部门ID */
	private String organizationId;
	
	/** 客户类型 */
	private String customerType;
	
	/** 客户ID */
	private String customerOrganizationId;
	
	/** 订单类型 */
	private String saleBillType;
	
	/** 单据状态 */
	private String billState;
	
	/** 销售总数量 */
	private String totalQuantity;
	
	/** 销售总金额  */
	private String totalAmount;
	
	/** 整单折扣前金额 */
	private String billTotalAmount;
	
	/** 整单折扣率 */
	private String totalDiscountRate;
	
	/** 整单折扣金额 */
	private String totalDiscountPrice;
	
	/** 结算方式 */
	private String settlement;
	
	/** 货币*/
	private String currency;
	
	/** 销售人员（业务员） */
	private String salesStaffId;
	
	/** 销售日期  */
	private String saleDate;
	
	/** 销售时间 */
	private String saleTime;
	
	/** 期望完成日期  */
	private String expectFinishDate;
	
	/** 客户仓库  */
	private String customerDepot;
	
	/** 客户逻辑仓库  */
	private String customerLogicDepot;
	
	/** 销售部门仓库  */
	private String saleDepot;
	
	/** 销售部门逻辑仓库  */
	private String saleLogicDepot;
	
	/** 联系人 */
	private String contactPerson;
	
	/** 送货地址*/
	private String deliverAddress;
	
	/** 数据库中当前联系人 */
	private String curPerson;
	
	/** 数据库中当前送货地址 */
	private String curAddress;
	
	/** 备注*/
	private String comments;
	
	/** 销售明细列表 */
	private String saleDetailList;

	/** 重复导入标识 */
	private String repeatFlag;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getSaleOrderNo() {
		return saleOrderNo;
	}

	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerOrganizationId() {
		return customerOrganizationId;
	}

	public void setCustomerOrganizationId(String customerOrganizationId) {
		this.customerOrganizationId = customerOrganizationId;
	}

	public String getSaleBillType() {
		return saleBillType;
	}

	public void setSaleBillType(String saleBillType) {
		this.saleBillType = saleBillType;
	}

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBillTotalAmount() {
		return billTotalAmount;
	}

	public void setBillTotalAmount(String billTotalAmount) {
		this.billTotalAmount = billTotalAmount;
	}

	public String getTotalDiscountRate() {
		return totalDiscountRate;
	}

	public void setTotalDiscountRate(String totalDiscountRate) {
		this.totalDiscountRate = totalDiscountRate;
	}

	public String getTotalDiscountPrice() {
		return totalDiscountPrice;
	}

	public void setTotalDiscountPrice(String totalDiscountPrice) {
		this.totalDiscountPrice = totalDiscountPrice;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSalesStaffId() {
		return salesStaffId;
	}

	public void setSalesStaffId(String salesStaffId) {
		this.salesStaffId = salesStaffId;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getExpectFinishDate() {
		return expectFinishDate;
	}

	public void setExpectFinishDate(String expectFinishDate) {
		this.expectFinishDate = expectFinishDate;
	}
	
	public String getCustomerDepot() {
		return customerDepot;
	}

	public void setCustomerDepot(String customerDepot) {
		this.customerDepot = customerDepot;
	}

	public String getCustomerLogicDepot() {
		return customerLogicDepot;
	}

	public void setCustomerLogicDepot(String customerLogicDepot) {
		this.customerLogicDepot = customerLogicDepot;
	}

	public String getSaleDepot() {
		return saleDepot;
	}

	public void setSaleDepot(String saleDepot) {
		this.saleDepot = saleDepot;
	}

	public String getSaleLogicDepot() {
		return saleLogicDepot;
	}

	public void setSaleLogicDepot(String saleLogicDepot) {
		this.saleLogicDepot = saleLogicDepot;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public String getCurPerson() {
		return curPerson;
	}

	public void setCurPerson(String curPerson) {
		this.curPerson = curPerson;
	}

	public String getCurAddress() {
		return curAddress;
	}

	public void setCurAddress(String curAddress) {
		this.curAddress = curAddress;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSaleDetailList() {
		return saleDetailList;
	}

	public void setSaleDetailList(String saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}
	
}
