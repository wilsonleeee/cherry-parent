/*
 * @(#)BINOLMOWAT03_Form.java     1.0 2011/6/24
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
 * 会员异常数据监控Form
 * 
 * @author niushunjie
 * @version 1.0 2011.6.24
 */
public class BINOLMOWAT03_Form extends BINOLCM13_Form{
    /** 最大购买次数 */
    private String maxCount;
    
    /** 开始日期 */
    private String startDate;
    
    /** 结束日期 */
    private String endDate;
    
    public String getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
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

}
