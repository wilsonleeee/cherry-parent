/*
 * @(#)BINOLPTJCS42_Form.java     1.0 2015/01/19
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
package com.cherry.pt.jcs.form;

import java.util.List;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 产品信息二维码维护Form
 * 
 * @author niushunjie
 * @version 1.0 2015.01.19
 */
public class BINOLPTJCS42_Form extends BINOLCM13_Form{
    /** 品牌ID */
    private String brandInfoId;
    
    /** 产品厂商ID */
    private String prtVendorId;
    
    /** 产品名称 */
    private String productName;
    
    /** 经销商ID*/
    private String resellerId;
    
    /** 经销商Code*/
    private String resellerCode;

    /** 经销商名称*/
    private String resellerName;
    
    /**URL前缀*/
    private String prefixURL;
    
    /**有效区分*/
    private String validFlag;
    
    /** 经销商List*/
    private List resellerList;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
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

    public String getResellerCode() {
        return resellerCode;
    }

    public void setResellerCode(String resellerCode) {
        this.resellerCode = resellerCode;
    }

    public String getResellerName() {
        return resellerName;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public String getPrefixURL() {
        return prefixURL;
    }

    public void setPrefixURL(String prefixURL) {
        this.prefixURL = prefixURL;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public List getResellerList() {
        return resellerList;
    }

    public void setResellerList(List resellerList) {
        this.resellerList = resellerList;
    }

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }
}