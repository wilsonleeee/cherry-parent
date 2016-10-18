/*
 * @(#)BINOLMOWAT02_Form.java     1.0 2011/5/11
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
package com.cherry.mo.wat.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 销售异常数据监控Form
 * 
 * @author niushunjie
 * @version 1.0 2011.5.11
 */
public class BINOLMOWAT02_Form extends BINOLCM13_Form{
    /** 最大阈值 */
    private String maxLimit;
    
    /** 最小阈值 */
    private String minLimit;
    
    /** 开始日期 */
    private String startDate;
    
    /** 结束日期 */
    private String endDate;
    
    /** 最大数量 */
    private String maxQuantity;
    
    /** 最小数量 */
    private String minQuantity;

//    /** 销售金额或销售数量标志 */
//    private String radioRule;
    
    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
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

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

//    public void setRadioRule(String radioRule) {
//        this.radioRule = radioRule;
//    }
//
//    public String getRadioRule() {
//        return radioRule;
//    }
}
