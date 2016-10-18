/*		
 * @(#)BINOLSSPRM56_Form.java     1.0 2010/11/29		
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
 * 调拨记录详细form
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 */
public class BINOLSSPRM56_Form{
	
	/** 调拨单Id */
	private String proAllocationId;
	
	/** 调拨单单号 */
	private String proAllocationNOIF;
	
	/** 调出部门ID */
	private String outOrganizationId;
	
	/** 调入部门ID */
	private String inOrganizationId;
	
	/** 调拨记录信息 */
	private Map returnInfo;

	/** 调拨详细记录LIST */
	private List returnList;
	
	/** 任务实例Id(工作流用) */
	private String taskInstanceID;
	/** 操作类型区分 */
	private String operationFlag;
	
    /**调入仓库 List */
    private List<Map<String, Object>> inDepotList;
    /**调入仓库*/
    private String inDepotId ;
    
    /**调出仓库 List */
    private List<Map<String, Object>> outDepotList;
    /**调出仓库*/
    private String outDepotId ;
    
    /**调入逻辑仓库List*/
    private List<Map<String,Object>> inLogicList;
    
    /**调入逻辑仓库*/
    private String inLogicId;
    
    /**调出逻辑仓库List*/
    private List<Map<String,Object>> outLogicList;
    
    /**调出逻辑仓库*/
    private String outLogicId;
    
    /**审核区分*/
    private String verifiedFlag;
    
    /**修改时间*/
    private String updateTime ;
    
    /**修改次数*/
    private String modifyCount ;
    
    /**发货原因*/
    private String reasonAll ;
    
    /**产品厂商ID */
    private String[] promotionProductVendorIDArr;
    
    /**促销品单价 */
    private String[] priceUnitArr;    
    
	/**数量  基本单位*/
    private String[] quantityuArr;
    
	/**单条总价*/
    private String[] priceTotalArr;

    /**调拨理由*/
    private String[] reasonArr;
    /**工作流实例ID*/
    private String workFlowID;
    
    /**操作区分*/
    private String operateType;
    
    private String entryID;
    private String actionID;
    
	
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

	public void setProAllocationId(String proAllocationId) {
		this.proAllocationId = proAllocationId;
	}

	public String getProAllocationId() {
		return proAllocationId;
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
	 * @return the proAllocationNO
	 */
	public String getProAllocationNOIF() {
		return proAllocationNOIF;
	}

	/**
	 * @param proAllocationNO the proAllocationNO to set
	 */
	public void setProAllocationNOIF(String proAllocationNO) {
		this.proAllocationNOIF = proAllocationNO;
	}

	/**
	 * @return the outOrganizationId
	 */
	public String getOutOrganizationId() {
		return outOrganizationId;
	}

	/**
	 * @param outOrganizationId the outOrganizationId to set
	 */
	public void setOutOrganizationId(String outOrganizationId) {
		this.outOrganizationId = outOrganizationId;
	}

	/**
	 * @return the inOrganizationId
	 */
	public String getInOrganizationId() {
		return inOrganizationId;
	}

	/**
	 * @param inOrganizationId the inOrganizationId to set
	 */
	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
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

    public List<Map<String, Object>> getInLogicList() {
        return inLogicList;
    }

    public void setInLogicList(List<Map<String, Object>> inLogicList) {
        this.inLogicList = inLogicList;
    }

    public String getInLogicId() {
        return inLogicId;
    }

    public void setInLogicId(String inLogicId) {
        this.inLogicId = inLogicId;
    }

    public List<Map<String, Object>> getOutLogicList() {
        return outLogicList;
    }

    public void setOutLogicList(List<Map<String, Object>> outLogicList) {
        this.outLogicList = outLogicList;
    }

    public String getOutLogicId() {
        return outLogicId;
    }

    public void setOutLogicId(String outLogicId) {
        this.outLogicId = outLogicId;
    }
}
