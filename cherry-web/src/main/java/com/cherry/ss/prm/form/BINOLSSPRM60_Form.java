/*		
 * @(#)BINOLSSPRM60_Form.java     1.0 2012/09/27		
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
 * 促销品移库Form
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM60_Form extends DataTable_BaseForm {
    /**厂商编码*/
    private String unitCode;
    
    /**促销品条码*/
    private String barCode;
    
    /**部门Code*/
    private String departCode;
    
    /**实体仓库ID*/
    private String depotInfoId;
    
    /**部门ID*/
    private String departId;
    
    /**出库逻辑仓库ID*/
    private String fromLogicInventoryInfoId;
    
    /**入库逻辑仓库ID*/
    private String toLogicInventoryInfoId;
    
    /**备注*/
    private String comments;
    
    /**促销品厂商ID组*/
    private String[] prmVendorIdArr;
    
    /**促销品数量组*/
    private String[] quantityArr;
    
    /**备注组*/
    private String[] commentsArr;
    
    /**促销品编码组*/
    private String[] unitCodeArr;
    
    /**促销品条码组*/
    private String[] barCodeArr;
    
    /**促销品厂商ID*/
    private String prmVendorId;
    
    /**价格*/
    private String[] priceArr;
    
    /**初始化部门信息*/
    private Map<String,Object> departInfoMap;
    
    /**逻辑仓库list*/
    private List<Map<String,Object>> logicInventoryList = null;
    
    /**初始化实体仓库*/
    private List<Map<String,Object>> depotList = null;
    
    /**支持终端移库标志*/
    private String counterShiftFlag;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getDepotInfoId() {
        return depotInfoId;
    }

    public void setDepotInfoId(String depotInfoId) {
        this.depotInfoId = depotInfoId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getFromLogicInventoryInfoId() {
        return fromLogicInventoryInfoId;
    }

    public void setFromLogicInventoryInfoId(String fromLogicInventoryInfoId) {
        this.fromLogicInventoryInfoId = fromLogicInventoryInfoId;
    }

    public String getToLogicInventoryInfoId() {
        return toLogicInventoryInfoId;
    }

    public void setToLogicInventoryInfoId(String toLogicInventoryInfoId) {
        this.toLogicInventoryInfoId = toLogicInventoryInfoId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String[] getPrmVendorIdArr() {
        return prmVendorIdArr;
    }

    public void setPrmVendorIdArr(String[] prmVendorIdArr) {
        this.prmVendorIdArr = prmVendorIdArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public String[] getUnitCodeArr() {
        return unitCodeArr;
    }

    public void setUnitCodeArr(String[] unitCodeArr) {
        this.unitCodeArr = unitCodeArr;
    }

    public String[] getBarCodeArr() {
        return barCodeArr;
    }

    public void setBarCodeArr(String[] barCodeArr) {
        this.barCodeArr = barCodeArr;
    }

    public String getPrmVendorId() {
        return prmVendorId;
    }

    public void setPrmVendorId(String prmVendorId) {
        this.prmVendorId = prmVendorId;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public List<Map<String, Object>> getLogicInventoryList() {
        return logicInventoryList;
    }

    public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
        this.logicInventoryList = logicInventoryList;
    }

    public List<Map<String, Object>> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Map<String, Object>> depotList) {
        this.depotList = depotList;
    }

    public Map<String, Object> getDepartInfoMap() {
        return departInfoMap;
    }

    public void setDepartInfoMap(Map<String, Object> departInfoMap) {
        this.departInfoMap = departInfoMap;
    }

    public String getCounterShiftFlag() {
        return counterShiftFlag;
    }

    public void setCounterShiftFlag(String counterShiftFlag) {
        this.counterShiftFlag = counterShiftFlag;
    }
}