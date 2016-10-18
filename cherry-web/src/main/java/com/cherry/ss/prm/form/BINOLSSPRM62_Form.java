/*		
 * @(#)BINOLSSPRM62_Form.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 促销品移库详细Form
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM62_Form extends DataTable_BaseForm {
    private String entryID;
    private String actionID;
    private String workFlowID;
    /**移库单主表ID*/
    private String promotionShiftID;
    
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
    
    /** 部门*/
    private String organizationID;
    
    /**主表备注*/
    private String comments;
    
    /**从表产品厂商ID*/
    private String[] prmVendorIDArr;
    
    /**从表数量*/
    private String[] quantityArr;
    
    /**从表价格*/
    private String[] priceUnitArr;
    
    /**从表包装类型ID*/
    private String[] productVendorPackageIDArr;
    
    /**从表移出仓库ID*/
    private String[] fromDepotInfoIDArr;
    
    /**从表移出逻辑仓库ID*/
    private String[] fromLogicInventoryInfoIDArr;
    
    /**从表移出仓库库位ID*/
    private String[] fromStorageLocationInfoIDArr;
    
    /**从表移入仓库ID*/
    private String[] toDepotInfoIDArr;
    
    /**从表移入逻辑仓库ID*/
    private String[] toLogicInventoryInfoIDArr;
    
    /**从表移入仓库库位ID*/
    private String[] toStorageLocationInfoIDArr;
    
    /**从表备注*/
    private String[] commentsArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 移库单概要信息 */
    private Map shiftMainData;
    
    /** 移库单详细信息 */
    private List shiftDetailData;

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

    public String getPromotionShiftID() {
        return promotionShiftID;
    }

    public void setPromotionShiftID(String promotionShiftID) {
        this.promotionShiftID = promotionShiftID;
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

    public String[] getPrmVendorIDArr() {
        return prmVendorIDArr;
    }

    public void setPrmVendorIDArr(String[] prmVendorIDArr) {
        this.prmVendorIDArr = prmVendorIDArr;
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

    
    public Map getShiftMainData() {
        return shiftMainData;
    }

    public void setShiftMainData(Map shiftMainData) {
        this.shiftMainData = shiftMainData;
    }

    public List getShiftDetailData() {
        return shiftDetailData;
    }

    public void setShiftDetailData(List shiftDetailData) {
        this.shiftDetailData = shiftDetailData;
    }

    public String[] getFromDepotInfoIDArr() {
        return fromDepotInfoIDArr;
    }

    public void setFromDepotInfoIDArr(String[] fromDepotInfoIDArr) {
        this.fromDepotInfoIDArr = fromDepotInfoIDArr;
    }

    public String[] getFromLogicInventoryInfoIDArr() {
        return fromLogicInventoryInfoIDArr;
    }

    public void setFromLogicInventoryInfoIDArr(String[] fromLogicInventoryInfoIDArr) {
        this.fromLogicInventoryInfoIDArr = fromLogicInventoryInfoIDArr;
    }

    public String[] getFromStorageLocationInfoIDArr() {
        return fromStorageLocationInfoIDArr;
    }

    public void setFromStorageLocationInfoIDArr(
            String[] fromStorageLocationInfoIDArr) {
        this.fromStorageLocationInfoIDArr = fromStorageLocationInfoIDArr;
    }

    public String[] getToDepotInfoIDArr() {
        return toDepotInfoIDArr;
    }

    public void setToDepotInfoIDArr(String[] toDepotInfoIDArr) {
        this.toDepotInfoIDArr = toDepotInfoIDArr;
    }

    public String[] getToLogicInventoryInfoIDArr() {
        return toLogicInventoryInfoIDArr;
    }

    public void setToLogicInventoryInfoIDArr(String[] toLogicInventoryInfoIDArr) {
        this.toLogicInventoryInfoIDArr = toLogicInventoryInfoIDArr;
    }

    public String[] getToStorageLocationInfoIDArr() {
        return toStorageLocationInfoIDArr;
    }

    public void setToStorageLocationInfoIDArr(String[] toStorageLocationInfoIDArr) {
        this.toStorageLocationInfoIDArr = toStorageLocationInfoIDArr;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }
}
