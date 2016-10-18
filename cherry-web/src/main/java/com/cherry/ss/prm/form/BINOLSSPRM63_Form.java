/*
 * @(#)BINOLSSPRM63_Form.java     1.0 2013/01/25
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

/**
 * 
 * 入库Form
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM63_Form extends DataTable_BaseForm{
    /** 入库部门 */
    private String inOrganizationId;
    
    /** 实体仓库 */
    private String depotInfoId;
    
    /** 逻辑仓库*/
    private String logicDepotsInfoId;
    
    /** 入库理由*/
    private String reason;
    
    /** 促销品厂商ID*/
    private String[] prmtVendorIDArr;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 数量*/
    private String[] quantityArr;
    
    /** 备注*/
    private String[] reasonArr;
    
    /** 价格*/
    private String[] priceArr;
    
    /** 实体仓库 */
    private List<Map<String,Object>> depotsList;
    
    /** 逻辑仓库 List*/
    private List<Map<String,Object>> logicDepotsInfoList;
    
    /**往来单位*/
    private String bussinessPartnerId;
    
    /**入库时间*/
    private String inDepotDate;
    
    /**后台给柜台入库标志*/
    private String counterInDepotFlag;
    
    private Map<String,Object> initInfoMap;

    public String getInOrganizationId() {
        return inOrganizationId;
    }

    public void setInOrganizationId(String inOrganizationId) {
        this.inOrganizationId = inOrganizationId;
    }

    public String getDepotInfoId() {
        return depotInfoId;
    }

    public void setDepotInfoId(String depotInfoId) {
        this.depotInfoId = depotInfoId;
    }

    public String getLogicDepotsInfoId() {
        return logicDepotsInfoId;
    }

    public void setLogicDepotsInfoId(String logicDepotsInfoId) {
        this.logicDepotsInfoId = logicDepotsInfoId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String[] getPrmtVendorIDArr() {
        return prmtVendorIDArr;
    }

    public void setPrmtVendorIDArr(String[] prmtVendorIDArr) {
        this.prmtVendorIDArr = prmtVendorIDArr;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public List<Map<String, Object>> getDepotsList() {
        return depotsList;
    }

    public void setDepotsList(List<Map<String, Object>> depotsList) {
        this.depotsList = depotsList;
    }

    public List<Map<String, Object>> getLogicDepotsInfoList() {
        return logicDepotsInfoList;
    }

    public void setLogicDepotsInfoList(List<Map<String, Object>> logicDepotsInfoList) {
        this.logicDepotsInfoList = logicDepotsInfoList;
    }

    public String getBussinessPartnerId() {
        return bussinessPartnerId;
    }

    public void setBussinessPartnerId(String bussinessPartnerId) {
        this.bussinessPartnerId = bussinessPartnerId;
    }

    public String getInDepotDate() {
        return inDepotDate;
    }

    public void setInDepotDate(String inDepotDate) {
        this.inDepotDate = inDepotDate;
    }

    public String getCounterInDepotFlag() {
        return counterInDepotFlag;
    }

    public void setCounterInDepotFlag(String counterInDepotFlag) {
        this.counterInDepotFlag = counterInDepotFlag;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
    }
}