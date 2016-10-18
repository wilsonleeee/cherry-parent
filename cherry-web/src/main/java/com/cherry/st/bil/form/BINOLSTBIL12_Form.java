/*  
 * @(#)BINOLSTBIL12_Form.java     1.0 2011/11/2      
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
 * 产品退库单详细Form
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
public class BINOLSTBIL12_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
    /**退库单主表ID*/
    private String productReturnID;
    
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
    
    /**主表退库部门ID*/
    private String organizationID;
    
    /**主表接受退库部门ID*/
    private String organizationIDReceive;
    
    /**接受退库仓库ID*/
    private String inventoryInfoIDReceive;
    
    /**接受退库逻辑仓库ID*/
    private String logicInventoryInfoIDReceive;
    
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
    private String[] reasonArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 退库单概要信息 */
    @SuppressWarnings("unchecked")
    private Map productReturnMainData;
    
    /** 退库单详细信息 */
    @SuppressWarnings("unchecked")
    private List productReturnDetailData;

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

    public String getProductReturnID() {
        return productReturnID;
    }

    public void setProductReturnID(String productReturnID) {
        this.productReturnID = productReturnID;
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

    public String getOrganizationIDReceive() {
        return organizationIDReceive;
    }

    public void setOrganizationIDReceive(String organizationIDReceive) {
        this.organizationIDReceive = organizationIDReceive;
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

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
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

    public Map getProductReturnMainData() {
        return productReturnMainData;
    }

    public void setProductReturnMainData(Map productReturnMainData) {
        this.productReturnMainData = productReturnMainData;
    }

    public List getProductReturnDetailData() {
        return productReturnDetailData;
    }

    public void setProductReturnDetailData(List productReturnDetailData) {
        this.productReturnDetailData = productReturnDetailData;
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
}
