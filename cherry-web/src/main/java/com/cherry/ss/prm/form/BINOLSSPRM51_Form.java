/*		
 * @(#)BINOLSSPRM51_Form.java     1.0 2010/11/19		
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
 * @author dingyc
 *
 */
public class BINOLSSPRM51_Form  {
	
    /**发货日期 */
    private String deliverDate;
    
	/** 所属部门List */
	private List<Map<String, String>> inOrganizationList;	
	/** 收货部门 */
	private String inOrganizationId;
	
	/** 收货部门  给弹出画面用*/
	private String inOrganizationIdHid;
	
	/** 收货部门  给弹出画面用*/
	private String inOrganizationNameHid;
	
    /**收货仓库ID List */
    private List<Map<String, Object>> inDepotList;
    
    /**收货仓库ID */
    private String inDepot ;

    /**发货单数据 概要*/
    private List<Map<String, Object>> deliverDataList;
    
    /**发货单数据 详细*/
    private List<Map<String, Object>> deliverDataDetailList;

    /**子画面上的选择框 */
    private String[] deliverchkbox;
    
	/**产品厂商ID */
    private String[] promotionProductVendorIDArr;
    
    /**unitcode 厂商编码 */
    private String[] unitCodeArr;
    
    /**barcode 产品条码 */
    private String[] barCodeArr;
    
    /**促销品单价 */
    private String[] priceUnitArr;    
    
	/**发货数量  基本单位*/
    private String[] quantityuArr; 
    
    /**Price*/
    private String[] priceArr;
    
    /**发货理由*/
    private String[] reasonArr;  

    /**更新时间*/
    private String[] outUpdateTimeArr;    
    
    /**更新次数*/
    private String[] outModifyCountArr;  
    
    /**发货部门*/
    private String[] outOrganizationIDArr;  
    
    /**发货单总单的ID*/
    private String[] promotionDeliverIDArr;  
    
    /**发货单的单据号*/
    private String[] deliverNoArrIF;
    
    /**任务实例ID*/
    private String[] taskInstanceIDArr;
    
	public void clear(){
		//发货日期  
		deliverDate=null;		
		//所属部门List  
		inOrganizationList=null;
		//发货仓库ID  
		inDepotList=null;	
		//unitcode 厂商编码  
		unitCodeArr=null;		
		//barcode 产品条码  
		barCodeArr=null;		
		//促销品单价  
		priceUnitArr=null;		
		//发货数量  基本单位 
		quantityuArr=null;   
		//Price 
		priceArr=null;		
		//发货理由 
		reasonArr=null;

		deliverDataList=null;
		deliverDataDetailList=null;
	}
	/**
	 * @return the deliverDate
	 */
	public String getDeliverDate() {
		return deliverDate;
	}

	/**
	 * @param deliverDate the deliverDate to set
	 */
	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
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
	 * @return the priceArr
	 */
	public String[] getPriceArr() {
		return priceArr;
	}

	/**
	 * @param priceArr the priceArr to set
	 */
	public void setPriceArr(String[] priceArr) {
		this.priceArr = priceArr;
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
	 * @param inOrganizationList the inOrganizationList to set
	 */
	public void setInOrganizationList(List<Map<String, String>> inOrganizationList) {
		this.inOrganizationList = inOrganizationList;
	}
	
	public List<Map<String, String>> getInOrganizationList() {
		return inOrganizationList;
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
	 * @return the unitCodeArr
	 */
	public String[] getUnitCodeArr() {
		return unitCodeArr;
	}

	/**
	 * @param unitCodeArr the unitCodeArr to set
	 */
	public void setUnitCodeArr(String[] unitCodeArr) {
		this.unitCodeArr = unitCodeArr;
	}

	/**
	 * @return the barCodeArr
	 */
	public String[] getBarCodeArr() {
		return barCodeArr;
	}

	/**
	 * @param barCodeArr the barCodeArr to set
	 */
	public void setBarCodeArr(String[] barCodeArr) {
		this.barCodeArr = barCodeArr;
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
	 * @return the inDepot
	 */
	public String getInDepot() {
		return inDepot;
	}

	/**
	 * @param inDepot the inDepot to set
	 */
	public void setInDepot(String inDepot) {
		this.inDepot = inDepot;
	}
	public List<Map<String, Object>> getDeliverDataList() {
		return deliverDataList;
	}

	public void setDeliverDataList(List<Map<String, Object>> deliverDataList) {
		this.deliverDataList = deliverDataList;
	}

	public List<Map<String, Object>> getDeliverDataDetailList() {
		return deliverDataDetailList;
	}

	public void setDeliverDataDetailList(
			List<Map<String, Object>> deliverDataDetailList) {
		this.deliverDataDetailList = deliverDataDetailList;
	}
	/**
	 * @return the deliverchkbox
	 */
	public String[] getDeliverchkbox() {
		return deliverchkbox;
	}
	/**
	 * @param deliverchkbox the deliverchkbox to set
	 */
	public void setDeliverchkbox(String[] deliverchkbox) {
		this.deliverchkbox = deliverchkbox;
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
	 * @return the outOrganizationIDArr
	 */
	public String[] getOutOrganizationIDArr() {
		return outOrganizationIDArr;
	}
	/**
	 * @param outOrganizationIDArr the outOrganizationIDArr to set
	 */
	public void setOutOrganizationIDArr(String[] outOrganizationIDArr) {
		this.outOrganizationIDArr = outOrganizationIDArr;
	}
	/**
	 * @return the promotionDeliverIDArr
	 */
	public String[] getPromotionDeliverIDArr() {
		return promotionDeliverIDArr;
	}
	/**
	 * @param promotionDeliverIDArr the promotionDeliverIDArr to set
	 */
	public void setPromotionDeliverIDArr(String[] promotionDeliverIDArr) {
		this.promotionDeliverIDArr = promotionDeliverIDArr;
	}
	/**
	 * @return the deliverNoArr
	 */
	public String[] getDeliverNoArrIF() {
		return deliverNoArrIF;
	}
	/**
	 * @param deliverNoArr the deliverNoArr to set
	 */
	public void setDeliverNoArrIF(String[] deliverNoArr) {
		this.deliverNoArrIF = deliverNoArr;
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
	 * @return the inOrganizationIdHid
	 */
	public String getInOrganizationIdHid() {
		return inOrganizationIdHid;
	}
	/**
	 * @param inOrganizationIdHid the inOrganizationIdHid to set
	 */
	public void setInOrganizationIdHid(String inOrganizationIdHid) {
		this.inOrganizationIdHid = inOrganizationIdHid;
	}
	/**
	 * @return the inOrganizationNameHid
	 */
	public String getInOrganizationNameHid() {
		return inOrganizationNameHid;
	}
	/**
	 * @param inOrganizationNameHid the inOrganizationNameHid to set
	 */
	public void setInOrganizationNameHid(String inOrganizationNameHid) {
		this.inOrganizationNameHid = inOrganizationNameHid;
	}
}
