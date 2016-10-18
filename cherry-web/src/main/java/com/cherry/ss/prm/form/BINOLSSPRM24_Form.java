/*		
 * @(#)BINOLSSPRM24_Form.java     1.0 2010/12/03		
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
 * 盘点画面的form
 * @author dingyc
 *
 */
public class BINOLSSPRM24_Form  {
	
	/**品牌ID*/
	private String brandInfoId;

    /**盘点日期 */
    private String deliverDate;
    
	/** 盘点部门 */
	private String organizationId;
    
	/** 盘点部门List */
	private List<Map<String, String>> organizationList;	

    /**盘点仓库*/
    private String depot ;
    
    /**盘点仓库hidden*/
    private String hidDepot ;
    
    /**盘点逻辑仓库*/
    private String logicDepot;
    
    /**盘点逻辑仓库hidden*/
    private String hidLogicDepot;
    
    
    /**盘点仓库 List */
    private List<Map<String, Object>> depotList;
    
    /**逻辑仓库 List */
    private List<Map<String, Object>> logicDepotList;
    
    
	/**库存促销品列表*/
	private List<Map<String, Object>> stockPromotionList;
	
    /**盘点原因*/
    private String reasonAll;
    
    /**该仓库中是否存在该促销品*/
    private String[] hasprmflagArr;
    /**产品厂商ID */
    private String[] promotionProductVendorIDArr;
    
    /**账面数量 */
    private String[] bookCountArr;
    
    /**盘差数量 */
    private String[] gainCountArr;
    
    /**盘差金额 */
    private String[] gainMoneyArr;
    
    /**促销品单价 */
    private String[] priceUnitArr;    
    
	/**数量  基本单位*/
    private String[] quantityuArr;
    
	/**发货金额  单价* 发货数量  基本单位*/
    private String[] priceTotalArr;
    
    /**Price*/
    private String[] priceArr;
    
    /**发货理由*/
    private String[] reasonArr;

    /**数量允许负号标志*/
    private String allowNegativeFlag;
    
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
	 * @param organizationList the organizationList to set
	 */
	public void setOrganizationList(List<Map<String, String>> organizationList) {
		this.organizationList = organizationList;
	}
	
	public List<Map<String, String>> getOrganizationList() {
		return organizationList;
	}

	/**
	 * @return the depotList
	 */
	public List<Map<String, Object>> getDepotList() {
		return depotList;
	}

	/**
	 * @param depotList the depotList to set
	 */
	public void setDepotList(List<Map<String, Object>> depotList) {
		this.depotList = depotList;
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
	public void clear(){

		//发货日期  
		deliverDate=null;		
		//所属部门List  
		organizationList=null;		
	
		//发货仓库ID  
		depotList=null;		
	
		//促销品单价  
		priceUnitArr=null;		
		//发货数量  基本单位 
		quantityuArr=null;		
		//发货金额  单价* 发货数量  基本单位 
		priceTotalArr=null;		    
		//Price 
		priceArr=null;		
		//发货理由 
		reasonArr=null;
	}

	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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
	 * @return the depot
	 */
	public String getDepot() {
		return depot;
	}

	/**
	 * @param depot the depot to set
	 */
	public void setDepot(String depot) {
		this.depot = depot;
	}


	/**
	 * @return the stockPromotionList
	 */
	public List<Map<String, Object>> getStockPromotionList() {
		return stockPromotionList;
	}

	/**
	 * @param stockPromotionList the stockPromotionList to set
	 */
	public void setStockPromotionList(List<Map<String, Object>> stockPromotionList) {
		this.stockPromotionList = stockPromotionList;
	}

	/**
	 * @return the gainCount
	 */
	public String[] getGainCountArr() {
		return gainCountArr;
	}

	/**
	 * @param gainCount the gainCount to set
	 */
	public void setGainCountArr(String[] gainCountArr) {
		this.gainCountArr = gainCountArr;
	}

	/**
	 * @return the gainMoney
	 */
	public String[] getGainMoneyArr() {
		return gainMoneyArr;
	}

	/**
	 * @param gainMoney the gainMoney to set
	 */
	public void setGainMoneyArr(String[] gainMoneyArr) {
		this.gainMoneyArr = gainMoneyArr;
	}

	/**
	 * @return the bookCountArr
	 */
	public String[] getBookCountArr() {
		return bookCountArr;
	}

	/**
	 * @param bookCountArr the bookCountArr to set
	 */
	public void setBookCountArr(String[] bookCountArr) {
		this.bookCountArr = bookCountArr;
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
	 * @return the hidDepot
	 */
	public String getHidDepot() {
		return hidDepot;
	}

	/**
	 * @param hidDepot the hidDepot to set
	 */
	public void setHidDepot(String hidDepot) {
		this.hidDepot = hidDepot;
	}

	/**
	 * @return the hasprmflagArr
	 */
	public String[] getHasprmflagArr() {
		return hasprmflagArr;
	}

	/**
	 * @param hasprmflagArr the hasprmflagArr to set
	 */
	public void setHasprmflagArr(String[] hasprmflagArr) {
		this.hasprmflagArr = hasprmflagArr;
	}

    public String getLogicDepot() {
        return logicDepot;
    }

    public void setLogicDepot(String logicDepot) {
        this.logicDepot = logicDepot;
    }

    public String getHidLogicDepot() {
        return hidLogicDepot;
    }

    public void setHidLogicDepot(String hidLogicDepot) {
        this.hidLogicDepot = hidLogicDepot;
    }

    public List<Map<String, Object>> getLogicDepotList() {
        return logicDepotList;
    }

    public void setLogicDepotList(List<Map<String, Object>> logicDepotList) {
        this.logicDepotList = logicDepotList;
    }

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

    public String getAllowNegativeFlag() {
        return allowNegativeFlag;
    }

    public void setAllowNegativeFlag(String allowNegativeFlag) {
        this.allowNegativeFlag = allowNegativeFlag;
    }
    
}
