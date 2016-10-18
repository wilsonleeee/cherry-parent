/*
 * @(#)BINOLMOWAT04_Form.java     1.0 2011/5/26
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
 * 异常盘点次数监控Form
 * 
 * @author niushunjie
 * @version 1.0 2011.5.26
 */
public class BINOLMOWAT04_Form extends BINOLCM13_Form{
    /** 最大盘点次数阈值 */
    private String maxLimit;
    
    /** 开始日期 */
    private String startDate;
    
    /** 结束日期 */
    private String endDate;
    
    /** 产品盘差>= */
    private String gainQuantityGE;
    
    ///** 产品盘差<= */
    //private String gainQuantityLE;
    
    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
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

    public String getGainQuantityGE() {
        return gainQuantityGE;
    }

    public void setGainQuantityGE(String gainQuantityGE) {
        this.gainQuantityGE = gainQuantityGE;
    }

    //public String getGainQuantityLE() {
    //    return gainQuantityLE;
    //}

    //public void setGainQuantityLE(String gainQuantityLE) {
    //    this.gainQuantityLE = gainQuantityLE;
    //}

}
