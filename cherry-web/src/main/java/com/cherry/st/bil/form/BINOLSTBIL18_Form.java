/*  
 * @(#)BINOLSTBIL18_Form.java     1.0 2012/11/28      
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
package com.cherry.st.bil.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品调拨单详细Form
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
public class BINOLSTBIL18_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
    /**产品调拨单主表ID*/
    private String productAllocationID;
    
    /**产品调入单ID*/
    private String productAllocationInID;
    
    /**产品调出单ID*/
    private String productAllocationOutID;
    
    /**业务类型*/
    private String tradeType;
    
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
    
    /** 调入部门 */
    private String organizationID;
       
    /**调入仓库ID*/
    private String inventoryInfoIDIn;
    
    /**调入逻辑仓库ID*/
    private String logicInventoryInfoIDIn;
    
    /**调出仓库ID*/
    private String inventoryInfoIDOut;    
    
    /**调出逻辑仓库ID*/
    private String logicInventoryInfoIDOut;
    
    /**从表产品厂商ID*/
    private String[] prtVendorId;
    
    /**从表数量*/
    private String[] quantityArr;
    
    /**从表价格*/
    private String[] priceUnitArr;
    
    /**从表包装类型ID*/
    private String[] productVendorPackageIDArr;
    
    /**从表备注*/
    private String[] commentsArr;
    
    /**调入实体仓库List*/
    private List<Map<String,Object>> depotsInfoListIn;
    
    /**调入逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoListIn;
    
    /**调出实体仓库List*/
    private List<Map<String,Object>> depotsInfoListOut;
    
    /**调出逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoListOut;
    
    /** 调拨单概要信息 */
    @SuppressWarnings("unchecked")
    private Map productAllocationMainData;
    
    /** 调拨单详细信息 */
    @SuppressWarnings("unchecked")
    private List productAllocationDetailData;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**是否审核调出单标志*/
    private String auditLGFlag;
    
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

    public String getProductAllocationID() {
        return productAllocationID;
    }

    public void setProductAllocationID(String productAllocationID) {
        this.productAllocationID = productAllocationID;
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

    public String getInventoryInfoIDIn() {
        return inventoryInfoIDIn;
    }

    public void setInventoryInfoIDIn(String inventoryInfoIDIn) {
        this.inventoryInfoIDIn = inventoryInfoIDIn;
    }

    public String getLogicInventoryInfoIDIn() {
        return logicInventoryInfoIDIn;
    }

    public void setLogicInventoryInfoIDIn(String logicInventoryInfoIDIn) {
        this.logicInventoryInfoIDIn = logicInventoryInfoIDIn;
    }

    public String getInventoryInfoIDOut() {
        return inventoryInfoIDOut;
    }

    public void setInventoryInfoIDOut(String inventoryInfoIDOut) {
        this.inventoryInfoIDOut = inventoryInfoIDOut;
    }

    public String getLogicInventoryInfoIDOut() {
        return logicInventoryInfoIDOut;
    }

    public void setLogicInventoryInfoIDOut(String logicInventoryInfoIDOut) {
        this.logicInventoryInfoIDOut = logicInventoryInfoIDOut;
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

    public String[] getProductVendorPackageIDArr() {
        return productVendorPackageIDArr;
    }

    public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
        this.productVendorPackageIDArr = productVendorPackageIDArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public List<Map<String, Object>> getDepotsInfoListIn() {
        return depotsInfoListIn;
    }

    public void setDepotsInfoListIn(List<Map<String, Object>> depotsInfoListIn) {
        this.depotsInfoListIn = depotsInfoListIn;
    }

    public List<Map<String, Object>> getLogicDepotsInfoListIn() {
        return logicDepotsInfoListIn;
    }

    public void setLogicDepotsInfoListIn(
            List<Map<String, Object>> logicDepotsInfoListIn) {
        this.logicDepotsInfoListIn = logicDepotsInfoListIn;
    }

    public List<Map<String, Object>> getDepotsInfoListOut() {
        return depotsInfoListOut;
    }

    public void setDepotsInfoListOut(List<Map<String, Object>> depotsInfoListOut) {
        this.depotsInfoListOut = depotsInfoListOut;
    }

    public List<Map<String, Object>> getLogicDepotsInfoListOut() {
        return logicDepotsInfoListOut;
    }

    public void setLogicDepotsInfoListOut(
            List<Map<String, Object>> logicDepotsInfoListOut) {
        this.logicDepotsInfoListOut = logicDepotsInfoListOut;
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

    public String getProductAllocationInID() {
        return productAllocationInID;
    }

    public void setProductAllocationInID(String productAllocationInID) {
        this.productAllocationInID = productAllocationInID;
    }

    public String getProductAllocationOutID() {
        return productAllocationOutID;
    }

    public void setProductAllocationOutID(String productAllocationOutID) {
        this.productAllocationOutID = productAllocationOutID;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

    public String getAuditLGFlag() {
        return auditLGFlag;
    }

    public void setAuditLGFlag(String auditLGFlag) {
        this.auditLGFlag = auditLGFlag;
    }

}