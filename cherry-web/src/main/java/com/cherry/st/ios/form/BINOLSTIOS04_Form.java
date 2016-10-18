/*
 * @(#)BINOLSTIOS04_Form.java     1.0 2011/9/28
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
 * 商品盘点Form
 * 
 * @author niushunjie
 * @version 1.0 2011.9.28
 */
public class BINOLSTIOS04_Form extends DataTable_BaseForm{
    /**按批次*/
    private String isBatchStockTaking;
    
    /** 部门*/
    private String organizationId;
    
    /** 实体仓库*/
    private String depotInfoId;
    
    /** 逻辑仓库*/
    private String logicDepotsInfoId;
    
    /** 分类Array*/
    private String[] categoryArr;
    
    /** 盘点理由*/
    private String comments;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 账面数量*/
    private String[] bookCountArr;
    
    /** 盘差*/
    private String[] gainCountArr;
    
    /** 盘点数量*/
    private String[] quantityArr;
    
    /** 价格*/
    private String[] priceArr;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    
    /** 备注*/
    private String[] reasonArr;
    
    /** 是否盲盘*/
    private String blindFlag;
    
    /**数量允许负号标志*/
    private String allowNegativeFlag;

    /**是否开启批次盘点*/
    private String batchFlag;
    
    public String getDepartInit() {
		return departInit;
	}

	public void setDepartInit(String departInit) {
		this.departInit = departInit;
	}

	/**部门初始化 */
    private String departInit;
    
    /** 实体仓库 List*/
    private List<Map<String,Object>> depotsInfoList;

    /** 逻辑仓库 List*/
    private List<Map<String,Object>> logicDepotsInfoList;

    /** 分类List*/
    private List<List<Map<String,Object>>> prtCategoryList;

    /** 产品List*/
    private List<Map<String,Object>> productList;
    
    /**是否支持终端判断*/
    private String witposStaking;
    
    public String getIsBatchStockTaking() {
        return isBatchStockTaking;
    }

    public void setIsBatchStockTaking(String isBatchStockTaking) {
        this.isBatchStockTaking = isBatchStockTaking;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String[] getCategoryArr() {
        return categoryArr;
    }

    public void setCategoryArr(String[] categoryArr) {
        this.categoryArr = categoryArr;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
    }

    public String[] getBookCountArr() {
        return bookCountArr;
    }

    public void setBookCountArr(String[] bookCountArr) {
        this.bookCountArr = bookCountArr;
    }

    public String[] getGainCountArr() {
        return gainCountArr;
    }

    public void setGainCountArr(String[] gainCountArr) {
        this.gainCountArr = gainCountArr;
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

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
    }

    public String getBlindFlag() {
        return blindFlag;
    }

    public void setBlindFlag(String blindFlag) {
        this.blindFlag = blindFlag;
    }

    public List<Map<String, Object>> getDepotsInfoList() {
        return depotsInfoList;
    }

    public void setDepotsInfoList(List<Map<String, Object>> depotsInfoList) {
        this.depotsInfoList = depotsInfoList;
    }

    public List<Map<String, Object>> getLogicDepotsInfoList() {
        return logicDepotsInfoList;
    }

    public void setLogicDepotsInfoList(List<Map<String, Object>> logicDepotsInfoList) {
        this.logicDepotsInfoList = logicDepotsInfoList;
    }

    public List<List<Map<String, Object>>> getPrtCategoryList() {
        return prtCategoryList;
    }

    public void setPrtCategoryList(List<List<Map<String, Object>>> prtCategoryList) {
        this.prtCategoryList = prtCategoryList;
    }

    public List<Map<String,Object>> getProductList() {
        return productList;
    }

    public void setProductList(List<Map<String,Object>> productList) {
        this.productList = productList;
    }

	public String getWitposStaking() {
		return witposStaking;
	}

	public void setWitposStaking(String witposStaking) {
		this.witposStaking = witposStaking;
	}

    public String getAllowNegativeFlag() {
        return allowNegativeFlag;
    }

    public void setAllowNegativeFlag(String allowNegativeFlag) {
        this.allowNegativeFlag = allowNegativeFlag;
    }
    
    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }
}