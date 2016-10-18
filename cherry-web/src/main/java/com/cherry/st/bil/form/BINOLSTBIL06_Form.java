/*  
 * @(#)BINOLSTBIL06_Form.java     1.0 2011/10/20      
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
 * 产品报损单详细Form
 * @author niushunjie
 * @version 1.0 2011.10.20
 */
public class BINOLSTBIL06_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
    /**报损单主表ID*/
    private String outboundFreeID;
    
    /** 画面操作区分 */
    private String operateType;

    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**审核状态*/
    private String verifiedFlag;
    
    /**主表备注*/
    private String comments;
    
    /**主表部门ID*/
    private String organizationID;
    
    /**主表实体仓库ID*/
    private String depotInfoID;
    
    /**主表逻辑仓库ID*/
    private String logicInventoryInfoID;
    
    /**从表产品厂商ID*/
    private String[] productVendorIDArr;

    /**从表数量*/
    private String[] quantityArr;
    
    /**从表价格*/
    private String[] priceUnitArr;
    
    /**从表包装类型ID*/
    private String[] productVendorPackageIDArr;
    
    /**从表仓库ID*/
    private String[] inventoryInfoIDArr;
    
    /**从表逻辑仓库ID*/
    private String[] logicInventoryInfoIDArr;
    
    /**从表仓库库位ID*/
    private String[] storageLocationInfoIDArr;
    
    /**从表备注*/
    private String[] commentsArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 报损单概要信息 */
    @SuppressWarnings("unchecked")
    private Map outboundFreeMainData;
    
    /** 报损单详细信息 */
    @SuppressWarnings("unchecked")
    private List outboundFreeDetailData;
    
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

    public String getOutboundFreeID() {
        return outboundFreeID;
    }

    public void setOutboundFreeID(String outboundFreeID) {
        this.outboundFreeID = outboundFreeID;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getDepotInfoID() {
        return depotInfoID;
    }

    public void setDepotInfoID(String depotInfoID) {
        this.depotInfoID = depotInfoID;
    }

    public String getLogicInventoryInfoID() {
        return logicInventoryInfoID;
    }

    public void setLogicInventoryInfoID(String logicInventoryInfoID) {
        this.logicInventoryInfoID = logicInventoryInfoID;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
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

    public String[] getInventoryInfoIDArr() {
        return inventoryInfoIDArr;
    }

    public void setInventoryInfoIDArr(String[] inventoryInfoIDArr) {
        this.inventoryInfoIDArr = inventoryInfoIDArr;
    }

    public String[] getLogicInventoryInfoIDArr() {
        return logicInventoryInfoIDArr;
    }

    public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
        this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
    }

    public String[] getStorageLocationInfoIDArr() {
        return storageLocationInfoIDArr;
    }

    public void setStorageLocationInfoIDArr(String[] storageLocationInfoIDArr) {
        this.storageLocationInfoIDArr = storageLocationInfoIDArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
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

    public Map getOutboundFreeMainData() {
        return outboundFreeMainData;
    }

    public void setOutboundFreeMainData(Map outboundFreeMainData) {
        this.outboundFreeMainData = outboundFreeMainData;
    }

    public List getOutboundFreeDetailData() {
        return outboundFreeDetailData;
    }

    public void setOutboundFreeDetailData(List outboundFreeDetailData) {
        this.outboundFreeDetailData = outboundFreeDetailData;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }
}
