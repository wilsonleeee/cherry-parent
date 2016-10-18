/*
 * @(#)BINOLPTRPS34_Form.java     1.0 2014/9/24
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
package com.cherry.pt.rps.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 电商订单详细Form
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
public class BINOLPTRPS34_Form extends BINOLCM13_Form{
    private String esOrderMainID;
    
    /**系统配置项是否显示唯一码*/
    private String sysConfigShowUniqueCode;
    
    /**电商订单主表信息*/
    private Map esOrderMain;
    
    /**电商订单详细信息*/
    private List esOrderDetail;
    
    /**电商订单支付详细信息*/
    private List payDetail;

    public String getEsOrderMainID() {
        return esOrderMainID;
    }

    public void setEsOrderMainID(String esOrderMainID) {
        this.esOrderMainID = esOrderMainID;
    }

    public String getSysConfigShowUniqueCode() {
        return sysConfigShowUniqueCode;
    }

    public void setSysConfigShowUniqueCode(String sysConfigShowUniqueCode) {
        this.sysConfigShowUniqueCode = sysConfigShowUniqueCode;
    }

    public Map getEsOrderMain() {
        return esOrderMain;
    }

    public void setEsOrderMain(Map esOrderMain) {
        this.esOrderMain = esOrderMain;
    }

    public List getEsOrderDetail() {
        return esOrderDetail;
    }

    public void setEsOrderDetail(List esOrderDetail) {
        this.esOrderDetail = esOrderDetail;
    }

    public List getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(List payDetail) {
        this.payDetail = payDetail;
    }
}