/*
 * @(#)BINOLSTBIL10_Form.java     1.0 2010/11/05
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

/**
 * 
 * 盘点单明细Form
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.11.05
 */
public class BINOLSTBIL10_Form {
    private String entryID;
    private String actionID;
    private String workFlowID;
	
	/** 产品盘点ID */
	private String stockTakingId;

    /** 画面操作区分 */
    private String operateType;
	
    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**审核状态*/
    private String verifiedFlag;
    
    /**主表部门ID*/
    private String organizationID;
    
    /**主表实体仓库ID*/
    private String depotInfoID;
    
    /**主表逻辑仓库ID*/
    private String logicInventoryInfoID;
    
    /**主表批次盘点*/
    private String isBatch;
    
    /**主表备注*/
    private String comments;
    
    /**从表批次号*/
    private String[] batchNoArr;
    
    /**从表账面数量*/
    private String[] bookQuantityArr;
    
    /**实盘数量*/
    private String[] quantityArr;
    
    /**从表厂商ID*/
    private String[] productVendorIDArr;
    
    /**从表盘差*/
    private String[] gainQuantityArr;

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
    
    /**盘点处理方式*/
    private String[] htArr;
    
    /** 盈亏 */
    private String profitKbn;
    
    /**操作备注*/
    private String opComments;
    
    /**品牌ID*/
    private String brandInfoId;

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

    public String getStockTakingId() {
        return stockTakingId;
    }

    public void setStockTakingId(String stockTakingId) {
        this.stockTakingId = stockTakingId;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getGainQuantityArr() {
        return gainQuantityArr;
    }

    public void setGainQuantityArr(String[] gainQuantityArr) {
        this.gainQuantityArr = gainQuantityArr;
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

    public String getProfitKbn() {
        return profitKbn;
    }

    public void setProfitKbn(String profitKbn) {
        this.profitKbn = profitKbn;
    }

    public String getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(String isBatch) {
        this.isBatch = isBatch;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
    }

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String[] getHtArr() {
        return htArr;
    }

    public void setHtArr(String[] htArr) {
        this.htArr = htArr;
    }

    public String[] getBookQuantityArr() {
        return bookQuantityArr;
    }

    public void setBookQuantityArr(String[] bookQuantityArr) {
        this.bookQuantityArr = bookQuantityArr;
    }

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
    
    
}
