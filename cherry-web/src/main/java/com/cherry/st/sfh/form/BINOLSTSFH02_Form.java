/*  
 * @(#)BINOLSTSFH02_Form.java     1.0 2011/05/31      
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
package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLSTSFH02_Form extends BINOLCM13_Form{
	
	/** 订货单ID */
	private String binProductOrderID;
	
	/** 订单单号 */
	private String orderNo;
	
	/** 接口单号 */
	private String orderNoIF;
	
	/** 关联单号 */
	private String relevanceNo;
	
	/** 申请部门 */
	private String binOrganizationID;
	
	/** 接受订货部门 */
	private String binOrganizationIDAccept;	
	
	/** 制单员工 */
	private String binEmployeeID;
	
	/** 审核者 */
	private String binEmployeeIDAudit;
	
	/** 建议数量 */
	private String suggestedQuantity;
	
	/** 总数量 */
	private String totalQuantity;
	
	/** 总金额 */
	private String totalAmount;
	
	/** 审核区分 */
	private String verifiedFlag;
	
	/** 处理状态 */
	private String tradeStatus;
	
	/** 物流ID */
	private String binLogisticInfoID;
	
	/** 备注 */
	private String comments;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	private List<Map<String, Object>> productOrderList;
	
    /**产品厂商ID*/
    private String prtVendorId;
    
    /** 产品名称 */
	private String nameTotal;
	
    /**选中单据ID*/
    private String[] checkedBillIdArr;
	
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

	public String getBinProductOrderID() {
		return binProductOrderID;
	}

	public void setBinProductOrderID(String binProductOrderID) {
		this.binProductOrderID = binProductOrderID;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderNoIF() {
		return orderNoIF;
	}

	public void setOrderNoIF(String orderNoIF) {
		this.orderNoIF = orderNoIF;
	}

	public String getRelevanceNo() {
		return relevanceNo;
	}

	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
	}

	public String getBinOrganizationID() {
		return binOrganizationID;
	}

	public void setBinOrganizationID(String binOrganizationID) {
		this.binOrganizationID = binOrganizationID;
	}

	public String getBinOrganizationIDAccept() {
		return binOrganizationIDAccept;
	}

	public void setBinOrganizationIDAccept(String binOrganizationIDAccept) {
		this.binOrganizationIDAccept = binOrganizationIDAccept;
	}

	public String getBinEmployeeID() {
		return binEmployeeID;
	}

	public void setBinEmployeeID(String binEmployeeID) {
		this.binEmployeeID = binEmployeeID;
	}

	public String getBinEmployeeIDAudit() {
		return binEmployeeIDAudit;
	}

	public void setBinEmployeeIDAudit(String binEmployeeIDAudit) {
		this.binEmployeeIDAudit = binEmployeeIDAudit;
	}

	public String getSuggestedQuantity() {
		return suggestedQuantity;
	}

	public void setSuggestedQuantity(String suggestedQuantity) {
		this.suggestedQuantity = suggestedQuantity;
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

	public String getBinLogisticInfoID() {
		return binLogisticInfoID;
	}

	public void setBinLogisticInfoID(String binLogisticInfoID) {
		this.binLogisticInfoID = binLogisticInfoID;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setProductOrderList(List<Map<String, Object>> productOrderList) {
		this.productOrderList = productOrderList;
	}

	public List<Map<String, Object>> getProductOrderList() {
		return productOrderList;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	private String date;

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }	
}
