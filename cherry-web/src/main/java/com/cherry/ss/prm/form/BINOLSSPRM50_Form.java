/*		
 * @(#)BINOLSSPRM17_Form.java     1.0 2010/10/27		
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

public class BINOLSSPRM50_Form extends DataTable_BaseForm {
	
    /**发货日期 */
    private String deliverDate;
    
	/** 所属部门List */
	private List<Map<String, String>> outOrganizationList;	
	/** 发货部门 */
	private String outOrganizationId;
	
    /**发货仓库ID List */
    private List<Map<String, Object>> outDepotList;
    
    /**发货仓库ID */
    private String outDepot ;

    /**收货部门 */
    private String[] inOrganizationIDArr;
    
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
    /**发货理由 大*/
    private String reasonAll;

    /**逻辑仓库ID */
    private String[] logicInventoryInfoIDArr;
    /**包装类型ID */
    private String[] productVendorPackageIDArr;
    
    /**发货数量  包装单位*/
    private String[] quantitypArr;   

    /**关联的接口单号*/
    private String[] deliverReceiveNoIFArr; 
    
	/**用户所属的品牌	品牌ID */
	private String brandCode;
    
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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
	 * @return the logicInventoryInfoIDArr
	 */
	public String[] getLogicInventoryInfoIDArr() {
		return logicInventoryInfoIDArr;
	}

	/**
	 * @param logicInventoryInfoIDArr the logicInventoryInfoIDArr to set
	 */
	public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
		this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
	}

	/**
	 * @return the productVendorPackageIDArr
	 */
	public String[] getProductVendorPackageIDArr() {
		return productVendorPackageIDArr;
	}

	/**
	 * @param productVendorPackageIDArr the productVendorPackageIDArr to set
	 */
	public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
		this.productVendorPackageIDArr = productVendorPackageIDArr;
	}

	/**
	 * @return the quantitypArr
	 */
	public String[] getQuantitypArr() {
		return quantitypArr;
	}

	/**
	 * @param quantitypArr the quantitypArr to set
	 */
	public void setQuantitypArr(String[] quantitypArr) {
		this.quantitypArr = quantitypArr;
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
	 * @param outOrganizationList the outOrganizationList to set
	 */
	public void setOutOrganizationList(List<Map<String, String>> outOrganizationList) {
		this.outOrganizationList = outOrganizationList;
	}
	
	public List<Map<String, String>> getOutOrganizationList() {
		return outOrganizationList;
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

	public void clear(){
		//发货日期  
		deliverDate=null;		
		//所属部门List  
		outOrganizationList=null;
		//发货仓库ID  
		outDepotList=null;		
		//收货部门  
		inOrganizationIDArr=null;		
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
	 * @return the outDepot
	 */
	public String getOutDepot() {
		return outDepot;
	}

	/**
	 * @param outDepot the outDepot to set
	 */
	public void setOutDepot(String outDepot) {
		this.outDepot = outDepot;
	}

	public String[] getDeliverReceiveNoIFArr() {
		return deliverReceiveNoIFArr;
	}

	public void setDeliverReceiveNoIFArr(String[] deliverReceiveNoIFArr) {
		this.deliverReceiveNoIFArr = deliverReceiveNoIFArr;
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
}
