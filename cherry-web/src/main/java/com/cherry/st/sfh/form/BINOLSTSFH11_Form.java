/*		
 * @(#)BINOLSTSFH11_Form.java     1.0 2012/11/15		
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 产品收货一览form
 * 
 * @author niushunjie
 * @version 1.0 2012.11.15
 */
public class BINOLSTSFH11_Form extends BINOLCM13_Form{
    /** 产品发货单信息 */
    private String deliverInfo;
    
    /** 发货单号 */
    private String deliverNo;
    
    /** 部门类型 */
    private String departType;
    
    /** 部门ID */
    private String organizationId;
    
    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;
    
    /** 实体仓库ID */
    private String inventId;
    
    /** 节日  */
    private String holidays;    

    /**产品厂商ID*/
    private String prtVendorId;
    
    /**产品名称*/
    private String productName;
    
    /** 部门类型List */
    private List<Map<String, Object>> departTypeList;

    /** 部门List */
    private List<Map<String, Object>> organizationList;

    /** 实体仓库List */
    private List<Map<String, Object>> inventoryList;
    
    /** 发货单List */
    private List<Map<String,Object>> deliverList;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;

    public String getDeliverInfo() {
        return deliverInfo;
    }

    public void setDeliverInfo(String deliverInfo) {
        this.deliverInfo = deliverInfo;
    }

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

    public String getDepartType() {
        return departType;
    }

    public void setDepartType(String departType) {
        this.departType = departType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getInventId() {
        return inventId;
    }

    public void setInventId(String inventId) {
        this.inventId = inventId;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Map<String, Object>> getDepartTypeList() {
        return departTypeList;
    }

    public void setDepartTypeList(List<Map<String, Object>> departTypeList) {
        this.departTypeList = departTypeList;
    }

    public List<Map<String, Object>> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Map<String, Object>> organizationList) {
        this.organizationList = organizationList;
    }

    public List<Map<String, Object>> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Map<String, Object>> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<Map<String, Object>> getDeliverList() {
        return deliverList;
    }

    public void setDeliverList(List<Map<String, Object>> deliverList) {
        this.deliverList = deliverList;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}