/*	
 * @(#)BINOLSTCM15_Form     1.0 2012/06/26		
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
package com.cherry.st.common.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品发货接收方库存弹出table共通Form
 * 
 * @author niushunjie
 * @version 1.0 2012.09.10
 */
public class BINOLSTCM15_Form extends DataTable_BaseForm{
    /**初始化类型 仓库不可选用于进入工作流的单据，仓库可选用于尚未保存或没有进入工作流的单据如产品发货画面*/
    private String initType;
    
    /**品牌ID*/
    private String brandInfoID;
    
    /**工作流ID*/
    private String entryID;
    
    /**订单ID*/
    private String productOrderID;
    
    /**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /**部门ID*/
    private String departID;
    
    /**实体仓库ID*/
    private String inventoryInfoID;
    
    /**逻辑仓库ID*/
    private String logicInventoryInfoID;
    
    /**产品ID数组*/
    private String[] productVendorIDArr;
    
    /**仓库信息*/
    private Map<String,Object> inventoryMap;
    
    /**发货单List*/
    private List<Map<String,Object>> stockList;
    
    /**实体仓库List*/
    private List<Map<String,Object>> depotList;
    
    /**逻辑仓库List*/
    private List<Map<String,Object>> logicDepotList;
    
    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public List<Map<String, Object>> getStockList() {
        return stockList;
    }

    public void setStockList(List<Map<String, Object>> stockList) {
        this.stockList = stockList;
    }

    public Map<String, Object> getInventoryMap() {
        return inventoryMap;
    }

    public void setInventoryMap(Map<String, Object> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
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

    public String getProductOrderID() {
        return productOrderID;
    }

    public void setProductOrderID(String productOrderID) {
        this.productOrderID = productOrderID;
    }

    public String getDepartID() {
        return departID;
    }

    public void setDepartID(String departID) {
        this.departID = departID;
    }

    public String getInitType() {
        return initType;
    }

    public void setInitType(String initType) {
        this.initType = initType;
    }

    public String getBrandInfoID() {
        return brandInfoID;
    }

    public void setBrandInfoID(String brandInfoID) {
        this.brandInfoID = brandInfoID;
    }

    public List<Map<String, Object>> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Map<String, Object>> depotList) {
        this.depotList = depotList;
    }

    public List<Map<String, Object>> getLogicDepotList() {
        return logicDepotList;
    }

    public void setLogicDepotList(List<Map<String, Object>> logicDepotList) {
        this.logicDepotList = logicDepotList;
    }
}