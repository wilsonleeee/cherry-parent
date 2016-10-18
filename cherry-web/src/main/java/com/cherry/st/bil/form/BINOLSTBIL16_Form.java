/*  
 * @(#)BINOLSTBIL16_Form.java     1.0 2012/8/23      
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
 * 产品盘点申请单详细Form
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
public class BINOLSTBIL16_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
    /**退库申请单主表ID*/
    private String proStocktakeRequestID;
    
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
    
    /**主表部门ID*/
    private String organizationID;
    
    /**仓库ID*/
    private String inventoryInfoID;
    
    /**逻辑仓库ID*/
    private String logicInventoryInfoID;
    
    /**从表产品厂商ID*/
    private String[] prtVendorId;
    
    /**从表产品批次ID*/
    private String[] productBatchIDArr;

    /**从表账面数量*/
    private String[] bookQuantityArr;
    
    /**从表实盘数量*/
    private String[] checkQuantityArr;
    
    /**从表盘差*/
    private String[] gainQuantityArr;
    
    /**页面盘差（兼容云POS过来的单据）*/
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
    
    /**从表处理方式 */
    private String[] handleTypeArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 盘点申请单概要信息 */
    @SuppressWarnings("unchecked")
    private Map proStocktakeRequestMainData;
    
    /** 盘点申请单详细信息 */
    @SuppressWarnings("unchecked")
    private List proStocktakeRequestDetailData;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**数量允许负号标志*/
    private String allowNegativeFlag;

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

    public String getProStocktakeRequestID() {
        return proStocktakeRequestID;
    }

    public void setProStocktakeRequestID(String proStocktakeRequestID) {
        this.proStocktakeRequestID = proStocktakeRequestID;
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

    public String[] getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String[] prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String[] getProductBatchIDArr() {
        return productBatchIDArr;
    }

    public void setProductBatchIDArr(String[] productBatchIDArr) {
        this.productBatchIDArr = productBatchIDArr;
    }

    public String[] getBookQuantityArr() {
        return bookQuantityArr;
    }

    public void setBookQuantityArr(String[] bookQuantityArr) {
        this.bookQuantityArr = bookQuantityArr;
    }

    public String[] getCheckQuantityArr() {
        return checkQuantityArr;
    }

    public void setCheckQuantityArr(String[] checkQuantityArr) {
        this.checkQuantityArr = checkQuantityArr;
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

    public String[] getHandleTypeArr() {
        return handleTypeArr;
    }

    public void setHandleTypeArr(String[] handleTypeArr) {
        this.handleTypeArr = handleTypeArr;
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

    public Map getProStocktakeRequestMainData() {
        return proStocktakeRequestMainData;
    }

    public void setProStocktakeRequestMainData(Map proStocktakeRequestMainData) {
        this.proStocktakeRequestMainData = proStocktakeRequestMainData;
    }

    public List getProStocktakeRequestDetailData() {
        return proStocktakeRequestDetailData;
    }

    public void setProStocktakeRequestDetailData(List proStocktakeRequestDetailData) {
        this.proStocktakeRequestDetailData = proStocktakeRequestDetailData;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public String getAllowNegativeFlag() {
        return allowNegativeFlag;
    }

    public void setAllowNegativeFlag(String allowNegativeFlag) {
        this.allowNegativeFlag = allowNegativeFlag;
    }

	public String[] getQuantityArr() {
		return quantityArr;
	}

	public void setQuantityArr(String[] quantityArr) {
		this.quantityArr = quantityArr;
	}
}
