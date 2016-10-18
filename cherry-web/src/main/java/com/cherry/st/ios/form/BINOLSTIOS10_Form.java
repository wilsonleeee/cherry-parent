/*
 * @(#)BINOLSTIOS10_Form.java     1.0 2013/08/16
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
package com.cherry.st.ios.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 退库申请Form
 * 
 * @author niushunjie
 * @version 1.0 2013.08.16
 */
public class BINOLSTIOS10_Form extends DataTable_BaseForm{
    /** 接收退库部门 */
    private String inOrganizationID;
    
    /** 退库部门 */
    private String outOrganizationID;
    
    /** 退库实体仓库 */
    private String outInventoryInfoID;
    
    /** 退库逻辑仓库*/
    private String outLogicInventoryInfoID;
    
    /** 退库理由*/
    private String reason;
    
    /** 厂商ID*/
    private String[] prtVendorId;
    
    /** 数量*/
    private String[] quantityArr;
    
    /** 价格*/
    private String[] priceArr;
    
    /** 备注*/
    private String[] reasonArr;
    
    /** 初始化退库实体仓库List*/
    private List<Map<String,Object>> outDepotList;
    
    /** 初始化退库逻辑仓库 List*/
    private List<Map<String,Object>> outLogicDepotList;
    
    /** 初始化部门信息*/
    private Map<String,Object> initInfoMap;
    
    /** 产品List */
    private List<Map<String,Object>> productList;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /** （配置项）产品发货使用价格 */
    private String sysConfigUsePrice;
    
    /** （配置项）检查库存大于发货数量标志 */
    private String checkStockFlag;

    public String getInOrganizationID() {
        return inOrganizationID;
    }

    public void setInOrganizationID(String inOrganizationID) {
        this.inOrganizationID = inOrganizationID;
    }

    public String getOutOrganizationID() {
        return outOrganizationID;
    }

    public void setOutOrganizationID(String outOrganizationID) {
        this.outOrganizationID = outOrganizationID;
    }

    public String getOutInventoryInfoID() {
        return outInventoryInfoID;
    }

    public void setOutInventoryInfoID(String outInventoryInfoID) {
        this.outInventoryInfoID = outInventoryInfoID;
    }

    public String getOutLogicInventoryInfoID() {
        return outLogicInventoryInfoID;
    }

    public void setOutLogicInventoryInfoID(String outLogicInventoryInfoID) {
        this.outLogicInventoryInfoID = outLogicInventoryInfoID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public List<Map<String, Object>> getProductList() {
        return productList;
    }

    public void setProductList(List<Map<String, Object>> productList) {
        this.productList = productList;
    }

    public List<Map<String, Object>> getOutDepotList() {
        return outDepotList;
    }

    public void setOutDepotList(List<Map<String, Object>> outDepotList) {
        this.outDepotList = outDepotList;
    }

    public List<Map<String, Object>> getOutLogicDepotList() {
        return outLogicDepotList;
    }

    public void setOutLogicDepotList(List<Map<String, Object>> outLogicDepotList) {
        this.outLogicDepotList = outLogicDepotList;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
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