package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH16_Form extends DataTable_BaseForm{
	
	private String entryID;
	
	private String actionID;
	
	private String workFlowID;
	
	private String saleId;
	
	private String historySaleID;
	
	private String saleBillType;
	
	private String customerType;
	
	private String customerOrganizationId;
	
	private String contactPerson;
	
	private String deliverAddress;
	
	/** 数据库中当前联系人 */
	private String curPerson;
	
	/** 数据库中当前送货地址 */
	private String curAddress;
	
	private String settlement;
	
	private String currency;
	
	private String comments;
	
	/** 客户仓库  */
	private String customerDepot;
	
	/** 客户逻辑仓库  */
	private String customerLogicDepot;
	
	/** 销售部门仓库  */
	private String saleDepot;
	
	/** 销售部门逻辑仓库  */
	private String saleLogicDepot;
	
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
	
	private String[] quantityuArr;
	
	private String[] productVendorIDArr;
	
	/** 销售明细列表 */
	private String saleDetailList;
	
	/** 销售单概要信息 */
    private Map<String,Object> saleOrdersMainData;
    
    /** 销售单详细信息 */
    private List<Map<String,Object>> saleOrderDetailList;
    
    /** 画面操作区分 */
    private String operateType;
    
    private String historyFlag;
    
    /**操作备注*/
    private String opComments;
    
    public String getEntryID() {
		return entryID;
	}

	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	public String getActionID() {
		return actionID;
	}

	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	public String getWorkFlowID() {
		return workFlowID;
	}

	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getSaleBillType() {
		return saleBillType;
	}

	public void setSaleBillType(String saleBillType) {
		this.saleBillType = saleBillType;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String[] getQuantityuArr() {
		return quantityuArr;
	}

	public void setQuantityuArr(String[] quantityuArr) {
		this.quantityuArr = quantityuArr;
	}

	public String[] getProductVendorIDArr() {
		return productVendorIDArr;
	}

	public void setProductVendorIDArr(String[] productVendorIDArr) {
		this.productVendorIDArr = productVendorIDArr;
	}

	public String getSaleDetailList() {
		return saleDetailList;
	}

	public void setSaleDetailList(String saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public Map<String, Object> getSaleOrdersMainData() {
		return saleOrdersMainData;
	}

	public void setSaleOrdersMainData(Map<String, Object> saleOrdersMainData) {
		this.saleOrdersMainData = saleOrdersMainData;
	}

	public List<Map<String, Object>> getSaleOrderDetailList() {
		return saleOrderDetailList;
	}

	public void setSaleOrderDetailList(List<Map<String, Object>> saleOrderDetailList) {
		this.saleOrderDetailList = saleOrderDetailList;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

    public String getHistorySaleID() {
        return historySaleID;
    }

    public void setHistorySaleID(String historySaleID) {
        this.historySaleID = historySaleID;
    }

    public String getHistoryFlag() {
        return historyFlag;
    }

    public void setHistoryFlag(String historyFlag) {
        this.historyFlag = historyFlag;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }
    
    
}
