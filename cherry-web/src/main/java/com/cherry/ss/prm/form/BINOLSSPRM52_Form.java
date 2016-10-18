/*		
 * @(#)BINOLSSPRM52_Form.java     1.0 2010/11/29		
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


/**
 * 发货记录详细form
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 */
public class BINOLSSPRM52_Form{
	
	/** 发货单Id */
	private String deliverId;
	/** 发货记录信息 */
	private Map returnInfo;

	/** 发货详细记录LIST */
	private List returnList;
	
	/** 任务实例Id(工作流用) */
	private String taskInstanceID;
	/** 操作类型区分 */
	private String operationFlag;
	
	/** 发货部门ID */
	private String outOrganizationID;
	
	/** 收货部门ID */
	private String inOrganizationID;
	
    /**发货仓库 List */
    private List<Map<String, Object>> outDepotList;
    
    /**发货实体仓库*/
    private String outDepotId ;
    
    /**发货逻辑仓库*/
    private String outLoginDepotId;
    
    /**收货逻辑仓库*/
    private String inLogicDepotId;
    
    /**收货实体仓库 List */
    private List<Map<String, Object>> inDepotList;
    
    /**收货逻辑仓库List*/
    private List<Map<String, Object>> inLoginDepotList;
    
    /**收货仓库*/
    private String inDepotId ;
    
    /**审核区分*/
    private String verifiedFlag;
    
    /**入出库区分*/
    private String stockInFlag;
    /**发货单号*/
    private String deliverReceiveNOIF;
    
    /**修改时间*/
    private String updateTime ;
    
    /**修改次数*/
    private String modifyCount ;
    
    /**发货原因*/
    private String reasonAll ;
    
    private String workFlowID;
    
    /**产品厂商ID */
    private String[] promotionProductVendorIDArr;
    
    /**促销品单价 */
    private String[] priceUnitArr;    
    
	/**数量  基本单位*/
    private String[] quantityuArr;
    
	/**单条总价*/
    private String[] priceTotalArr;

    /**发货理由*/
    private String[] reasonArr;
    
	private String operateType;
	
	private String entryID;
	private String actionID;
	
	/** 检查库存大于发货数量标志 */
    private String checkStockFlag;
    
    /**库存锁定阶段*/
    private String lockSection;
    
    /**预计发货日期*/
    private String planArriveDate;
	
	public Map getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(Map returnInfo) {
		this.returnInfo = returnInfo;
	}

	public List getReturnList() {
		return returnList;
	}

	public void setReturnList(List returnList) {
		this.returnList = returnList;
	}

	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverId() {
		return deliverId;
	}
	/**
	 * @return the promotionProductVendorIDArr
	 */
	public String[] getPromotionProductVendorIDArr() {
		return promotionProductVendorIDArr;
	}

	/**
	 * @param promotionProductVendorIDArr the promotionProductVendorIDArr to set
	 */
	public void setPromotionProductVendorIDArr(String[] promotionProductVendorIDArr) {
		this.promotionProductVendorIDArr = promotionProductVendorIDArr;
	}

	/**
	 * @return the priceUnitArr
	 */
	public String[] getPriceUnitArr() {
		return priceUnitArr;
	}

	/**
	 * @param priceUnitArr the priceUnitArr to set
	 */
	public void setPriceUnitArr(String[] priceUnitArr) {
		this.priceUnitArr = priceUnitArr;
	}

	/**
	 * @return the quantityuArr
	 */
	public String[] getQuantityuArr() {
		return quantityuArr;
	}

	/**
	 * @param quantityuArr the quantityuArr to set
	 */
	public void setQuantityuArr(String[] quantityuArr) {
		this.quantityuArr = quantityuArr;
	}

	/**
	 * @return the priceTotalArr
	 */
	public String[] getPriceTotalArr() {
		return priceTotalArr;
	}

	/**
	 * @param priceTotalArr the priceTotalArr to set
	 */
	public void setPriceTotalArr(String[] priceTotalArr) {
		this.priceTotalArr = priceTotalArr;
	}

	/**
	 * @return the reasonArr
	 */
	public String[] getReasonArr() {
		return reasonArr;
	}

	/**
	 * @param reasonArr the reasonArr to set
	 */
	public void setReasonArr(String[] reasonArr) {
		this.reasonArr = reasonArr;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the modifyCount
	 */
	public String getModifyCount() {
		return modifyCount;
	}

	/**
	 * @param modifyCount the modifyCount to set
	 */
	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	/**
	 * @return the reasonAll
	 */
	public String getReasonAll() {
		return reasonAll;
	}

	/**
	 * @param reasonAll the reasonAll to set
	 */
	public void setReasonAll(String reasonAll) {
		this.reasonAll = reasonAll;
	}

	/**
	 * @return the verifiedFlag
	 */
	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	/**
	 * @param verifiedFlag the verifiedFlag to set
	 */
	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	/**
	 * @return the taskInstanceID
	 */
	public String getTaskInstanceID() {
		return taskInstanceID;
	}

	/**
	 * @param taskInstanceID the taskInstanceID to set
	 */
	public void setTaskInstanceID(String taskInstanceID) {
		this.taskInstanceID = taskInstanceID;
	}

	/**
	 * @return the outDepotList
	 */
	public List<Map<String, Object>> getOutDepotList() {
		return outDepotList;
	}

	/**
	 * @param outDepotList the outDepotList to set
	 */
	public void setOutDepotList(List<Map<String, Object>> outDepotList) {
		this.outDepotList = outDepotList;
	}

	/**
	 * @return the outDepotId
	 */
	public String getOutDepotId() {
		return outDepotId;
	}

	/**
	 * @param outDepotId the outDepotId to set
	 */
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}

	/**
	 * @return the operationFlag
	 */
	public String getOperationFlag() {
		return operationFlag;
	}

	/**
	 * @param operationFlag the operationFlag to set
	 */
	public void setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
	}

	/**
	 * @return the outOrganizationID
	 */
	public String getOutOrganizationID() {
		return outOrganizationID;
	}

	/**
	 * @param outOrganizationID the outOrganizationID to set
	 */
	public void setOutOrganizationID(String outOrganizationID) {
		this.outOrganizationID = outOrganizationID;
	}

	/**
	 * @return the stockInFlag
	 */
	public String getStockInFlag() {
		return stockInFlag;
	}

	/**
	 * @param stockInFlag the stockInFlag to set
	 */
	public void setStockInFlag(String stockInFlag) {
		this.stockInFlag = stockInFlag;
	}

	/**
	 * @return the inOrganizationID
	 */
	public String getInOrganizationID() {
		return inOrganizationID;
	}

	/**
	 * @param inOrganizationID the inOrganizationID to set
	 */
	public void setInOrganizationID(String inOrganizationID) {
		this.inOrganizationID = inOrganizationID;
	}

	/**
	 * @return the deliverReceiveNO
	 */
	public String getDeliverReceiveNOIF() {
		return deliverReceiveNOIF;
	}

	/**
	 * @param deliverReceiveNO the deliverReceiveNO to set
	 */
	public void setDeliverReceiveNOIF(String deliverReceiveNO) {
		this.deliverReceiveNOIF = deliverReceiveNO;
	}

	/**
	 * @return the operateType
	 */
	public String getOperateType() {
		return operateType;
	}

	/**
	 * @param operateType the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	/**
	 * @return the entryID
	 */
	public String getEntryID() {
		return entryID;
	}

	/**
	 * @param entryID the entryID to set
	 */
	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	/**
	 * @return the actionID
	 */
	public String getActionID() {
		return actionID;
	}

	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	/**
	 * @return the workFlowID
	 */
	public String getWorkFlowID() {
		return workFlowID;
	}

	/**
	 * @param workFlowID the workFlowID to set
	 */
	public void setWorkFlowID(String workFlowID) {
		this.workFlowID = workFlowID;
	}

	/**
	 * @return the inDepotList
	 */
	public List<Map<String, Object>> getInDepotList() {
		return inDepotList;
	}

	/**
	 * @param inDepotList the inDepotList to set
	 */
	public void setInDepotList(List<Map<String, Object>> inDepotList) {
		this.inDepotList = inDepotList;
	}

	/**
	 * @return the inDepotId
	 */
	public String getInDepotId() {
		return inDepotId;
	}

	/**
	 * @param inDepotId the inDepotId to set
	 */
	public void setInDepotId(String inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getOutLoginDepotId() {
		return outLoginDepotId;
	}

	public void setOutLoginDepotId(String outLoginDepotId) {
		this.outLoginDepotId = outLoginDepotId;
	}

	public List<Map<String, Object>> getInLoginDepotList() {
		return inLoginDepotList;
	}

	public void setInLoginDepotList(List<Map<String, Object>> inLoginDepotList) {
		this.inLoginDepotList = inLoginDepotList;
	}

	public String getInLogicDepotId() {
		return inLogicDepotId;
	}

	public void setInLogicDepotId(String inLogicDepotId) {
		this.inLogicDepotId = inLogicDepotId;
	}

    public String getCheckStockFlag() {
        return checkStockFlag;
    }

    public void setCheckStockFlag(String checkStockFlag) {
        this.checkStockFlag = checkStockFlag;
    }

    public String getLockSection() {
        return lockSection;
    }

    public void setLockSection(String lockSection) {
        this.lockSection = lockSection;
    }

    public String getPlanArriveDate() {
        return planArriveDate;
    }

    public void setPlanArriveDate(String planArriveDate) {
        this.planArriveDate = planArriveDate;
    }
	
}
