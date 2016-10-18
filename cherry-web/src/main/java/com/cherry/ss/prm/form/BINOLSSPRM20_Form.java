/*		
 * @(#)BINOLSSPRM20_Form.java     1.0 2010/12/16		
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
 * 调拨画面的form
 * @author dingyc
 *
 */
public class BINOLSSPRM20_Form  {
    
    //out    
    /**岗位ID List */
	private List<Map<String, String>> positionIdList; 
    /**调出仓库 List */
    private List<Map<String, Object>> outDepotList;
	/** 调出部门List */
	private List<Map<String, String>> outOrganizationList;

	/** 单据List */
	private List<Map<String, Object>> allocationList;
	
    /**子画面上的选择框 */
    private String[] allocationchkbox;
    
	/** 明细数据List */
	private List<Map<String, Object>> allocationDetailList;
	   
    /**岗位ID */
    private String positionId;
    
	/** 调出部门 */
	private String outOrganizationId;
	
    /**调出仓库*/
    private String outDepotId ;
    
    /**调拨原因*/
    private String reasonAll;    
    
    /**调拨单据 */
    private String[] allocationIDArr;
    /**调拨单据号*/
    private String[]  relevanceNoArrIF; 
    
    /**产品厂商ID */
    private String[] promotionProductVendorIDArr;
    
    /**促销品单价 */
    private String[] priceUnitArr;    
    
	/**数量  基本单位*/
    private String[] quantityuArr;

    /**发货理由*/
    private String[] reasonArr;
    
    /**更新时间*/
    private String[]  outUpdateTimeArr;

    /**更新次数*/
    private String[]  outModifyCountArr;	
    /**调入部门*/
    private String[]  inOrganizationIDArr;
    /**调入仓库*/
    private String[]  inventoryIDArr;
    
    /**任务实例ID*/
    private String[] taskInstanceIDArr;
	/** 调拨记录信息 */
	private Map returnInfo;
	
    public void clear(){
		positionIdList = null; 
		outDepotList = null;
		outOrganizationList = null;
		allocationList = null;
		allocationchkbox = null;
		allocationDetailList = null;
		positionId = null;
		outOrganizationId = null;
		outDepotId = null;
		reasonAll = null;    
		allocationIDArr = null;
		relevanceNoArrIF = null; 
		promotionProductVendorIDArr = null;
		priceUnitArr = null;    
		quantityuArr = null;
		reasonArr = null;
		outUpdateTimeArr = null;
		outModifyCountArr = null;	
		inOrganizationIDArr = null;
		inventoryIDArr = null;
    }
	/**
	 * @return the positionIdList
	 */
	public List<Map<String, String>> getPositionIdList() {
		return positionIdList;
	}

	/**
	 * @param positionIdList the positionIdList to set
	 */
	public void setPositionIdList(List<Map<String, String>> positionIdList) {
		this.positionIdList = positionIdList;
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
	 * @return the outOrganizationList
	 */
	public List<Map<String, String>> getOutOrganizationList() {
		return outOrganizationList;
	}

	/**
	 * @param outOrganizationList the outOrganizationList to set
	 */
	public void setOutOrganizationList(List<Map<String, String>> outOrganizationList) {
		this.outOrganizationList = outOrganizationList;
	}

	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
	 * @return the allocationList
	 */
	public List<Map<String, Object>> getAllocationList() {
		return allocationList;
	}

	/**
	 * @param allocationList the allocationList to set
	 */
	public void setAllocationList(List<Map<String, Object>> allocationList) {
		this.allocationList = allocationList;
	}

	/**
	 * @return the allocationchkbox
	 */
	public String[] getAllocationchkbox() {
		return allocationchkbox;
	}

	/**
	 * @param allocationchkbox the allocationchkbox to set
	 */
	public void setAllocationchkbox(String[] allocationchkbox) {
		this.allocationchkbox = allocationchkbox;
	}

	/**
	 * @return the allocationDetailList
	 */
	public List<Map<String, Object>> getAllocationDetailList() {
		return allocationDetailList;
	}

	/**
	 * @param allocationDetailList the allocationDetailList to set
	 */
	public void setAllocationDetailList(
			List<Map<String, Object>> allocationDetailList) {
		this.allocationDetailList = allocationDetailList;
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
	 * @return the allocationIDArr
	 */
	public String[] getAllocationIDArr() {
		return allocationIDArr;
	}

	/**
	 * @param allocationIDArr the allocationIDArr to set
	 */
	public void setAllocationIDArr(String[] allocationIDArr) {
		this.allocationIDArr = allocationIDArr;
	}

	/**
	 * @return the outUpdateTimeArr
	 */
	public String[] getOutUpdateTimeArr() {
		return outUpdateTimeArr;
	}

	/**
	 * @param outUpdateTimeArr the outUpdateTimeArr to set
	 */
	public void setOutUpdateTimeArr(String[] outUpdateTimeArr) {
		this.outUpdateTimeArr = outUpdateTimeArr;
	}

	/**
	 * @return the outModifyCountArr
	 */
	public String[] getOutModifyCountArr() {
		return outModifyCountArr;
	}

	/**
	 * @param outModifyCountArr the outModifyCountArr to set
	 */
	public void setOutModifyCountArr(String[] outModifyCountArr) {
		this.outModifyCountArr = outModifyCountArr;
	}

	/**
	 * @return the inventoryIDArr
	 */
	public String[] getInventoryIDArr() {
		return inventoryIDArr;
	}

	/**
	 * @param inventoryIDArr the inventoryIDArr to set
	 */
	public void setInventoryIDArr(String[] inventoryIDArr) {
		this.inventoryIDArr = inventoryIDArr;
	}

	/**
	 * @return the inOrganizationIDArr
	 */
	public String[] getInOrganizationIDArr() {
		return inOrganizationIDArr;
	}

	/**
	 * @param inOrganizationIDArr the inOrganizationIDArr to set
	 */
	public void setInOrganizationIDArr(String[] inOrganizationIDArr) {
		this.inOrganizationIDArr = inOrganizationIDArr;
	}

	/**
	 * @return the relevanceNoArr
	 */
	public String[] getRelevanceNoArrIF() {
		return relevanceNoArrIF;
	}

	/**
	 * @param relevanceNoArr the relevanceNoArr to set
	 */
	public void setRelevanceNoArrIF(String[] relevanceNoArr) {
		this.relevanceNoArrIF = relevanceNoArr;
	}
	/**
	 * @return the taskInstanceIDArr
	 */
	public String[] getTaskInstanceIDArr() {
		return taskInstanceIDArr;
	}
	/**
	 * @param taskInstanceIDArr the taskInstanceIDArr to set
	 */
	public void setTaskInstanceIDArr(String[] taskInstanceIDArr) {
		this.taskInstanceIDArr = taskInstanceIDArr;
	}
	/**
	 * @return the returnInfo
	 */
	public Map getReturnInfo() {
		return returnInfo;
	}
	/**
	 * @param returnInfo the returnInfo to set
	 */
	public void setReturnInfo(Map returnInfo) {
		this.returnInfo = returnInfo;
	}

}
