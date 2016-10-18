
/*  
 * @(#)BINOLSTBIL02_Form.java    1.0 2011-10-21     
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

public class BINOLSTBIL02_Form extends DataTable_BaseForm {

	/** 入库部门 */
    private String inOrganizationId;
    
    /** 实体仓库 */
    private String depotInfoId;
    
    /** 逻辑仓库*/
    private String logicDepotsInfoId;
    
    /** 入库理由*/
    private String reason;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    private String productVendorId;
    
    /** 厂商编码*/
    private String[] unitCodeArr;
    
    /** 条码*/
    private String[] barCodeArr;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 数量*/
    private String[] quantityArr;
    
    /** 备注*/
    private String[] commentsArr;
    
    /**参考价*/
    private String[] referencePriceArr;
    
    /** 价格*/
    private String[] priceUnitArr;
    
    /** 逻辑仓库 List*/
    private List<Map<String,Object>> logicDepotsInfoList;
    
    /**入库单ID*/
    private String productInDepotId;
    
    /**工作流ID*/
    private String workFlowID;
    
    /**工作模式*/
    private String operateType;

    /**入库单主表信息*/
    private Map InDepotMainMap;
    
    /**入库单明细信息*/
    private List InDepotDetailList;
    
    /***/
    private String entryID;
    
    /***/
    private String actionID;
    
    /**更新时间*/
    private String updateTime;
    
    /**更新次数*/
    private String modifyCount;
    
    /**操作备注*/
    private String opComments;
    
	public String getProductVendorId() {
		return productVendorId;
	}

	public void setProductVendorId(String productVendorId) {
		this.productVendorId = productVendorId;
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

	public Map getInDepotMainMap() {
		return InDepotMainMap;
	}

	public void setInDepotMainMap(Map inDepotMainMap) {
		InDepotMainMap = inDepotMainMap;
	}

	public List getInDepotDetailList() {
		return InDepotDetailList;
	}

	public void setInDepotDetailList(List inDepotDetailList) {
		InDepotDetailList = inDepotDetailList;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getWorkFlowID() {
		return workFlowID;
	}

	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

	public String getDepotInfoId() {
		return depotInfoId;
	}

	public void setDepotInfoId(String depotInfoId) {
		this.depotInfoId = depotInfoId;
	}

	public String getLogicDepotsInfoId() {
		return logicDepotsInfoId;
	}

	public void setLogicDepotsInfoId(String logicDepotsInfoId) {
		this.logicDepotsInfoId = logicDepotsInfoId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String[] getProductVendorIDArr() {
		return productVendorIDArr;
	}

	public void setProductVendorIDArr(String[] productVendorIDArr) {
		this.productVendorIDArr = productVendorIDArr;
	}

	public String[] getUnitCodeArr() {
		return unitCodeArr;
	}

	public void setUnitCodeArr(String[] unitCodeArr) {
		this.unitCodeArr = unitCodeArr;
	}

	public String[] getBarCodeArr() {
		return barCodeArr;
	}

	public void setBarCodeArr(String[] barCodeArr) {
		this.barCodeArr = barCodeArr;
	}

	public String[] getBatchNoArr() {
		return batchNoArr;
	}

	public void setBatchNoArr(String[] batchNoArr) {
		this.batchNoArr = batchNoArr;
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

	public String[] getPriceUnitArr() {
		return priceUnitArr;
	}

	public void setPriceUnitArr(String[] priceUnitArr) {
		this.priceUnitArr = priceUnitArr;
	}

	public List<Map<String, Object>> getLogicDepotsInfoList() {
		return logicDepotsInfoList;
	}

	public void setLogicDepotsInfoList(List<Map<String, Object>> logicDepotsInfoList) {
		this.logicDepotsInfoList = logicDepotsInfoList;
	}

	public String getProductInDepotId() {
		return productInDepotId;
	}

	public void setProductInDepotId(String productInDepotId) {
		this.productInDepotId = productInDepotId;
	}

    public String getOpComments() {
        return opComments;
    }

    public void setOpComments(String opComments) {
        this.opComments = opComments;
    }

    public String[] getReferencePriceArr() {
        return referencePriceArr;
    }

    public void setReferencePriceArr(String[] referencePriceArr) {
        this.referencePriceArr = referencePriceArr;
    }
}