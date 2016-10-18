/*
 * @(#)BINOLSTIOS05_Form.java     1.0 2011/8/31
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
 * 自由盘点Form
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public class BINOLSTIOS05_Form extends DataTable_BaseForm{
    /** 实体仓库*/
    private String depotInfoId;
    
    /**盘点部门*/
    private String organizationId;
    
    /** 逻辑仓库*/
    private String logicDepotsInfoId;
    
    /** 盘点理由*/
    private String reason;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 账面数量*/
    private String[] bookCountArr;
    
    /** 盘差*/
    private String[] gainCountArr;
    
    /** 盘点数量*/
    private String[] quantityArr;
    
    /** 厂商编码*/
    private String[] unitCodeArr;
    
    /** 条码*/
    private String[] barCodeArr;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    
    /** 备注*/
    private String[] reasonArr;
    
    /**价格*/
    private String[] priceArr;
    
    /**盘点处理方式*/
    private String[] htArr;
    
    /** 是否盲盘*/
    private String blindFlag;
    
    /** 实体仓库 List*/
    private List<Map<String,Object>> depotsInfoList;

    /** 逻辑仓库 List*/
    private List<Map<String,Object>> logicDepotsInfoList;
    
    /**是否批次盘点*/
    private String isBatchStockTaking;
    
    /**是否支持终端判断*/
    private String witposStaking;
    
    /**品牌ID*/
    private String brandInfoId;
    
    /**部门初始化 */
    private String departInit;
    
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

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getIsBatchStockTaking() {
		return isBatchStockTaking;
	}

	public void setIsBatchStockTaking(String isBatchStockTaking) {
		this.isBatchStockTaking = isBatchStockTaking;
	}

	public String getWitposStaking() {
		return witposStaking;
	}

	public void setWitposStaking(String witposStaking) {
		this.witposStaking = witposStaking;
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

    public String[] getHtArr() {
        return htArr;
    }

    public void setHtArr(String[] htArr) {
        this.htArr = htArr;
    }
    
    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }
}