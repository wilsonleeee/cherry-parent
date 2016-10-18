/*
 * @(#)BINOLSTIOS01_Form.java     1.0 2011/09/06
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
 * 入库Form
 * 
 * @author niushunjie
 * @version 1.0 2011.09.06
 */
public class BINOLSTIOS01_Form extends DataTable_BaseForm{
    /** 入库部门 */
    private String inOrganizationId;
    
    /** 实体仓库 */
    private String depotInfoId;
    
    /** 逻辑仓库*/
    private String logicDepotsInfoId;
    
    /** 入库理由*/
    private String reason;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 数量*/
    private String[] quantityuArr;
    
    /** 备注*/
    private String[] reasonArr;
    
    /** 参考价*/
    private String[] referencePriceArr;
    
    /** 执行价*/
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
    
    /** （配置项）产品发货入库使用价格 */
    private String sysConfigUsePrice;

    public String getInDepotDate() {
		return inDepotDate;
	}

	public void setInDepotDate(String inDepotDate) {
		this.inDepotDate = inDepotDate;
	}

	public String getBussinessPartnerId() {
		return bussinessPartnerId;
	}

	public void setBussinessPartnerId(String bussinessPartnerId) {
		this.bussinessPartnerId = bussinessPartnerId;
	}

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

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
    }

    public String[] getQuantityuArr() {
        return quantityuArr;
    }

    public void setQuantityuArr(String[] quantityuArr) {
        this.quantityuArr = quantityuArr;
    }

    public String[] getReasonArr() {
        return reasonArr;
    }

    public void setReasonArr(String[] reasonArr) {
        this.reasonArr = reasonArr;
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

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public Map<String, Object> getInitInfoMap() {
        return initInfoMap;
    }

    public void setInitInfoMap(Map<String, Object> initInfoMap) {
        this.initInfoMap = initInfoMap;
    }

    public String getCounterInDepotFlag() {
        return counterInDepotFlag;
    }

    public void setCounterInDepotFlag(String counterInDepotFlag) {
        this.counterInDepotFlag = counterInDepotFlag;
    }

    public String[] getReferencePriceArr() {
        return referencePriceArr;
    }

    public void setReferencePriceArr(String[] referencePriceArr) {
        this.referencePriceArr = referencePriceArr;
    }

	public String getSysConfigUsePrice() {
		return sysConfigUsePrice;
	}

	public void setSysConfigUsePrice(String sysConfigUsePrice) {
		this.sysConfigUsePrice = sysConfigUsePrice;
	}
}