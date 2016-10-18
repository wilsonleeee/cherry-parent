/*  
 * @(#)BINOLSTSFH01_Form.java     1.0 2012/11/13      
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
package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品订货Form
 * 
 * @author niushunjie
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH01_Form extends DataTable_BaseForm {
   
    /**订货业务日期 */
    private String date;
    
    /** 期望发货日期 */
    private String expectDeliverDate;
    
    /**订货类型*/
    private String orderType;

    /**订货方*/
    private String inOrganizationId;
    
    /**订货仓库ID*/
    private String inDepotId ;
    
    /**订货逻辑仓库ID*/
    private String inLogicDepotId ;
    
    /**发货方*/
    private String outOrganizationId;
    
    /**订货仓库ID List */
    private List<Map<String, Object>> inDepotList;
    
    /**订货逻辑仓库ID List */
    private List<Map<String, Object>> inLogicDepotList;
    
    /**订货理由  主表*/
    private String reasonAll;
    
    /**产品厂商ID */
    private String[] productVendorIDArr;
    
    /**产品单价 */
    private String[] priceUnitArr;
    
    /**订货数量  基本单位*/
    private String[] quantityArr;
    
    /**价格*/
    private String[] priceArr;
        
    /**订货理由  明细*/
    private String[] reasonArr;
    
    /**逻辑仓库ID */
    private String[] logicInventoryInfoIDArr;
    
    /**包装类型ID */
    private String[] productVendorPackageIDArr;
    
    /**画面初始化信息*/
    private Map<String,Object> initInfoMap;
    
    /**来源（1：WEBPOS null：其他）*/
    private String fromPage;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInOrganizationId() {
        return inOrganizationId;
    }

    public void setInOrganizationId(String inOrganizationId) {
        this.inOrganizationId = inOrganizationId;
    }

    public String getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(String inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getInLogicDepotId() {
        return inLogicDepotId;
    }

    public void setInLogicDepotId(String inLogicDepotId) {
        this.inLogicDepotId = inLogicDepotId;
    }

    public String getOutOrganizationId() {
        return outOrganizationId;
    }

    public void setOutOrganizationId(String outOrganizationId) {
        this.outOrganizationId = outOrganizationId;
    }

    public List<Map<String, Object>> getInDepotList() {
        return inDepotList;
    }

    public void setInDepotList(List<Map<String, Object>> inDepotList) {
        this.inDepotList = inDepotList;
    }

    public List<Map<String, Object>> getInLogicDepotList() {
        return inLogicDepotList;
    }

    public void setInLogicDepotList(List<Map<String, Object>> inLogicDepotList) {
        this.inLogicDepotList = inLogicDepotList;
    }

    public String getReasonAll() {
        return reasonAll;
    }

    public void setReasonAll(String reasonAll) {
        this.reasonAll = reasonAll;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getPriceUnitArr() {
        return priceUnitArr;
    }

    public void setPriceUnitArr(String[] priceUnitArr) {
        this.priceUnitArr = priceUnitArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
    }

    public String[] getLogicInventoryInfoIDArr() {
        return logicInventoryInfoIDArr;
    }

    public void setLogicInventoryInfoIDArr(String[] logicInventoryInfoIDArr) {
        this.logicInventoryInfoIDArr = logicInventoryInfoIDArr;
    }

    public String[] getProductVendorPackageIDArr() {
        return productVendorPackageIDArr;
    }

    public void setProductVendorPackageIDArr(String[] productVendorPackageIDArr) {
        this.productVendorPackageIDArr = productVendorPackageIDArr;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
    }

    public String getExpectDeliverDate() {
        return expectDeliverDate;
    }

    public void setExpectDeliverDate(String expectDeliverDate) {
        this.expectDeliverDate = expectDeliverDate;
    }

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }
}
