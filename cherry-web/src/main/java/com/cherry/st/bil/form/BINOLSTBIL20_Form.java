/*  
 * @(#)BINOLSTBIL12_Form.java     1.0 2012/7/24      
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
 * 销售退货申请单详细Form
 * @author nanjunbo
 * @version 1.0 2016.08.29
 */
public class BINOLSTBIL20_Form extends DataTable_BaseForm{
	private String entryID;
	private String actionID;
	private String workFlowID;
    /**退库申请单主表ID*/
    private String saleReturnRequestID;
    
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
    private String[] reasonArr;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotsInfoList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotsInfoList;
      
    /** 退库单概要信息 */
    @SuppressWarnings("unchecked")
    private Map proReturnReqMainData;
    
    /** 退库单详细信息 */
    @SuppressWarnings("unchecked")
    private List proReturnReqDetailData;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /** （配置项）产品发货使用价格 */
    private String sysConfigUsePrice;
    
    /** 检查库存大于发货数量标志 */
    private String checkStockFlag;
    
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

    public String getSaleReturnRequestID() {
		return saleReturnRequestID;
	}

	public void setSaleReturnRequestID(String saleReturnRequestID) {
		this.saleReturnRequestID = saleReturnRequestID;
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

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

	public String getSysConfigUsePrice() {
		return sysConfigUsePrice;
	}

	public void setSysConfigUsePrice(String sysConfigUsePrice) {
		this.sysConfigUsePrice = sysConfigUsePrice;
	}

	public String getCheckStockFlag() {
		return checkStockFlag;
	}

	public void setCheckStockFlag(String checkStockFlag) {
		this.checkStockFlag = checkStockFlag;
	}
}
