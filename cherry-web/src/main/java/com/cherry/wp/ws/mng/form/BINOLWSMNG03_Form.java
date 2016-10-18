/*
 * @(#)BINOLWSMNG03_Form.java     1.0 2014/10/22
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
 * 退库申请Form
 * 
 * @author niushunjie
 * @version 1.0 2014.10.22
 */
public class BINOLWSMNG03_Form extends BINOLCM13_Form{
    /** 退库申请单号 */
    private String billNo;
    
    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;

    /** 产品名称 */
    private String nameTotal;

    /** 审核者 */
    private String binEmployeeIDAudit;

    /** 品牌ID */
    private String brandInfoId;
    
    /**员工ID*/
    private String employeeId;
    
    /**业务类型*/
    private String tradeType;
    
    private List<Map<String,Object>> proReturnRequestList;

    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /** 业务日期 */
    private String bussinessDate;
    
    /** 申请日期 */
    private String applyDate;
    
    /**操作人员*/
    private String employeeName;
    
    private String entryID;
    private String actionID;
    private String workFlowID;
    /**退库申请单主表ID*/
    private String proReturnRequestID;
    
    /**操作备注*/
    private String opComments;

    /** 画面操作区分 */
    private String operateType;

    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**审核状态*/
    private String verifiedFlag;
    
    /**主表备注*/
    private String reason;
    
    /**主表退库部门ID*/
    private String organizationID;
    
    /**退库方仓库ID*/
    private String inventoryInfoID;
    
    /**退库方逻辑仓库ID*/
    private String logicInventoryInfoID;
    
    /**主表接受退库部门ID*/
    private String organizationIDReceive;
    
    /**接受方仓库ID*/
    private String inventoryInfoIDReceive;
    
    /**接受方逻辑仓库ID*/
    private String logicInventoryInfoIDReceive;
    
    /**从表产品厂商ID*/
    private String[] prtVendorId;

    /**从表数量*/
    private String[] quantityArr;
    
    /**从表价格*/
    private String[] priceUnitArr;
    
    /**从表备注*/
    private String[] commentsArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 退库申请单概要信息 */
    @SuppressWarnings("unchecked")
    private Map proReturnReqMainData;
    
    /** 退库申请单详细信息 */
    @SuppressWarnings("unchecked")
    private List proReturnReqDetailData;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**BAList*/
    private List<Map<String,Object>> counterBAList;
    
    /**柜台号*/
    private String counterCode;
    
    /**是否允许负库存操作*/
    private String nsOperation;

    /** （配置项）产品入库/发货使用价格 */
    private String sysConfigUsePrice;
    
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public String getNameTotal() {
        return nameTotal;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getBinEmployeeIDAudit() {
        return binEmployeeIDAudit;
    }

    public void setBinEmployeeIDAudit(String binEmployeeIDAudit) {
        this.binEmployeeIDAudit = binEmployeeIDAudit;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public List<Map<String, Object>> getProReturnRequestList() {
        return proReturnRequestList;
    }

    public void setProReturnRequestList(List<Map<String, Object>> proReturnRequestList) {
        this.proReturnRequestList = proReturnRequestList;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(String workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getProReturnRequestID() {
        return proReturnRequestID;
    }

    public void setProReturnRequestID(String proReturnRequestID) {
        this.proReturnRequestID = proReturnRequestID;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(String modifyCount) {
        this.modifyCount = modifyCount;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getInventoryInfoID() {
        return inventoryInfoID;
    }

    public void setInventoryInfoID(String inventoryInfoID) {
        this.inventoryInfoID = inventoryInfoID;
    }

    public String getLogicInventoryInfoID() {
        return logicInventoryInfoID;
    }

    public void setLogicInventoryInfoID(String logicInventoryInfoID) {
        this.logicInventoryInfoID = logicInventoryInfoID;
    }

    public String getOrganizationIDReceive() {
        return organizationIDReceive;
    }

    public void setOrganizationIDReceive(String organizationIDReceive) {
        this.organizationIDReceive = organizationIDReceive;
    }

    public String getInventoryInfoIDReceive() {
        return inventoryInfoIDReceive;
    }

    public void setInventoryInfoIDReceive(String inventoryInfoIDReceive) {
        this.inventoryInfoIDReceive = inventoryInfoIDReceive;
    }

    public String getLogicInventoryInfoIDReceive() {
        return logicInventoryInfoIDReceive;
    }

    public void setLogicInventoryInfoIDReceive(String logicInventoryInfoIDReceive) {
        this.logicInventoryInfoIDReceive = logicInventoryInfoIDReceive;
    }

    public String[] getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String[] prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public List<Map<String, Object>> getDepotsInfoList() {
        return depotsInfoList;
    }

    public void setDepotsInfoList(List<Map<String, Object>> depotsInfoList) {
        this.depotsInfoList = depotsInfoList;
    }

    public List<Map<String, Object>> getLogicDepotsInfoList() {
        return logicDepotsInfoList;
    }

    public void setLogicDepotsInfoList(List<Map<String, Object>> logicDepotsInfoList) {
        this.logicDepotsInfoList = logicDepotsInfoList;
    }

    public Map getProReturnReqMainData() {
        return proReturnReqMainData;
    }

    public void setProReturnReqMainData(Map proReturnReqMainData) {
        this.proReturnReqMainData = proReturnReqMainData;
    }

    public List getProReturnReqDetailData() {
        return proReturnReqDetailData;
    }

    public void setProReturnReqDetailData(List proReturnReqDetailData) {
        this.proReturnReqDetailData = proReturnReqDetailData;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
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

	public String getNsOperation() {
		return nsOperation;
	}

	public void setNsOperation(String nsOperation) {
		this.nsOperation = nsOperation;
	}

	public String getSysConfigUsePrice() {
		return sysConfigUsePrice;
	}

	public void setSysConfigUsePrice(String sysConfigUsePrice) {
		this.sysConfigUsePrice = sysConfigUsePrice;
	}
    
	
}