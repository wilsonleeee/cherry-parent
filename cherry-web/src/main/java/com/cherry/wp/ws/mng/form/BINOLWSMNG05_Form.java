/*
 * @(#)BINOLWSMNG05_Form.java     1.0 2014/10/10
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
package com.cherry.wp.ws.mng.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;
/**
 * 
 * 调出Form
 * 
 * @author niushunjie
 * @version 1.0 2014.10.10
 */
public class BINOLWSMNG05_Form extends BINOLCM13_Form{
    /** 员工ID */
    private String employeeId;
    
    /**员工姓名*/
    private String employeeName;

    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;
    
    /** 调拨单号 */
    private String allocationrNo;
    
    /** 调入申请ID */
    private String productAllocationID;
    
    /** 调出单ID */
    private String productAllocationOutID;

    /** 审核状态 */
    private String verifiedFlag;
    
    /** 处理状态 */
    private String tradeStatus;
    
    /** 业务日期 */
    private String bussinessDate;
    
    /** 申请日期 */
    private String applyDate;
    
    /** 调入部门ID */
    private String inOrganizationID;
    
    /** 调入实体仓库ID */
    private String inDepotID;
    
    /** 调入逻辑仓ID */
    private String inLogicDepotID;
    
    /**调出部门*/
    private String outOrganizationID;
    
    /** 明细 产品厂商ID数组 */
    private String[] productVendorIDArr;
    
    /** 明细 产品价格数组 */
    private String[] priceUnitArr;
    
    /** 明细 产品数量数组 */
    private String[] quantityArr;
    
    /** 明细 产品数量数组 */
    private String[] commentsArr;
    
    /** 操作类型 */
    private String operateType;
    
    /** 字符编码*/
    private String charset;
    
    /** 工作流ID */
    private String entryID;
    
    /** 执行ID */
    private String actionID;
    
    /** 工作流ID */
    private String workFlowID;
    
    /**操作备注*/
    private String opComments;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**调入申请单List*/
    private List<Map<String,Object>> productAllocationList;
    
    /**调出单List*/
    private List<Map<String,Object>> productAllocationOutList;
    
    /** 调拨单概要信息 */
    @SuppressWarnings("unchecked")
    private Map productAllocationMainData;
    
    /** 调拨单详细信息 */
    @SuppressWarnings("unchecked")
    private List productAllocationDetailData;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**BAList*/
    private List<Map<String,Object>> counterBAList;
    
    /**柜台号*/
    private String counterCode;
    
    /**选中单据ID*/
    private String[] checkedBillIdArr;
    
    /**是否允许负库存操作*/
    private String nsOperation;

    /**云POS调出是否存在拒绝操作*/
    private String outBackFlag;
    
    /**调出审核状态是否可见*/
    private String outputStockState;
    
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getAllocationrNo() {
        return allocationrNo;
    }

    public void setAllocationrNo(String allocationrNo) {
        this.allocationrNo = allocationrNo;
    }

    public String getProductAllocationID() {
        return productAllocationID;
    }

    public void setProductAllocationID(String productAllocationID) {
        this.productAllocationID = productAllocationID;
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

    public String getBussinessDate() {
        return bussinessDate;
    }

    public void setBussinessDate(String bussinessDate) {
        this.bussinessDate = bussinessDate;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getInOrganizationID() {
        return inOrganizationID;
    }

    public void setInOrganizationID(String inOrganizationID) {
        this.inOrganizationID = inOrganizationID;
    }

    public String getInDepotID() {
        return inDepotID;
    }

    public void setInDepotID(String inDepotID) {
        this.inDepotID = inDepotID;
    }

    public String getInLogicDepotID() {
        return inLogicDepotID;
    }

    public void setInLogicDepotID(String inLogicDepotID) {
        this.inLogicDepotID = inLogicDepotID;
    }

    public String getOutOrganizationID() {
        return outOrganizationID;
    }

    public void setOutOrganizationID(String outOrganizationID) {
        this.outOrganizationID = outOrganizationID;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public String getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(String workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public List<Map<String, Object>> getProductAllocationList() {
        return productAllocationList;
    }

    public void setProductAllocationList(List<Map<String, Object>> productAllocationList) {
        this.productAllocationList = productAllocationList;
    }

    public Map getProductAllocationMainData() {
        return productAllocationMainData;
    }

    public void setProductAllocationMainData(Map productAllocationMainData) {
        this.productAllocationMainData = productAllocationMainData;
    }

    public List getProductAllocationDetailData() {
        return productAllocationDetailData;
    }

    public void setProductAllocationDetailData(List productAllocationDetailData) {
        this.productAllocationDetailData = productAllocationDetailData;
    }

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

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public List<Map<String, Object>> getProductAllocationOutList() {
        return productAllocationOutList;
    }

    public void setProductAllocationOutList(List<Map<String, Object>> productAllocationOutList) {
        this.productAllocationOutList = productAllocationOutList;
    }

    public String getProductAllocationOutID() {
        return productAllocationOutID;
    }

    public void setProductAllocationOutID(String productAllocationOutID) {
        this.productAllocationOutID = productAllocationOutID;
    }

    public List<Map<String, Object>> getCounterBAList() {
        return counterBAList;
    }

    public void setCounterBAList(List<Map<String, Object>> counterBAList) {
        this.counterBAList = counterBAList;
    }

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }

	public String getNsOperation() {
		return nsOperation;
	}

	public void setNsOperation(String nsOperation) {
		this.nsOperation = nsOperation;
	}

	public String getOutBackFlag() {
		return outBackFlag;
	}

	public void setOutBackFlag(String outBackFlag) {
		this.outBackFlag = outBackFlag;
	}

	public String getOutputStockState() {
		return outputStockState;
	}

	public void setOutputStockState(String outputStockState) {
		this.outputStockState = outputStockState;
	}
	
	
}