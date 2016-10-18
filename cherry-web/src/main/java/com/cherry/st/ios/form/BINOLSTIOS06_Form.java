/*
 * @(#)BINOLSTIOS06_Form.java     1.0 2012/11/27
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
 * 产品调拨Form
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
public class BINOLSTIOS06_Form extends DataTable_BaseForm{
    /** 调拨日期 */
    private String deliverDate;
    
    /** 调入部门 */
    private String inOrganizationID;

    /** 调入仓库 */
    private String inDepotID;

    /** 调入逻辑仓库 */
    private String inLogicDepotID;
    
    /** 调出部门 */
    private String outOrganizationID;

    /** 调拨原因 */
    private String commentsAll;

    /** 产品厂商ID */
    private String[] productVendorIDArr;
    
    /** 申请数量 */
    private String[] quantityArr;
    
    /** 产品单价 */
    private String[] priceUnitArr;

    /** 调拨理由 */
    private String[] commentsArr;

    /** 初始化信息 */
    private Map<String,Object> initInfoMap;
    
    /**调入方仓库*/
    private List<Map<String,Object>> inDepotList;
    
    /**调入方逻辑仓库*/
    private List<Map<String,Object>> inLogicDepotList;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getInOrganizationID() {
        return inOrganizationID;
    }

    public void setInOrganizationID(String inOrganizationID) {
        this.inOrganizationID = inOrganizationID;
    }

    public String getInDepotID() {
        return inDepotID;
    }

    public void setInDepotID(String inDepotID) {
        this.inDepotID = inDepotID;
    }

    public String getInLogicDepotID() {
        return inLogicDepotID;
    }

    public void setInLogicDepotID(String inLogicDepotID) {
        this.inLogicDepotID = inLogicDepotID;
    }

    public String getOutOrganizationID() {
        return outOrganizationID;
    }

    public void setOutOrganizationID(String outOrganizationID) {
        this.outOrganizationID = outOrganizationID;
    }

    public String getCommentsAll() {
        return commentsAll;
    }

    public void setCommentsAll(String commentsAll) {
        this.commentsAll = commentsAll;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
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

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
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

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }
}