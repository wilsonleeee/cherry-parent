/*
 * @(#)BINOLMBRPT01_Form.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员信息完善度报表Form
 *
 * @author nanjunbo
 * @version 1.0 2017/01/23
 */
public class BINOLMBRPT14_Form extends DataTable_BaseForm {
    /**会员手机号*/
    private String mobilePhone;

    /**完善度起始*/
    private String completeStart;

    /**完善度结束*/
    private String completeEnd;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCompleteStart() {
        return completeStart;
    }

    public void setCompleteStart(String completeStart) {
        this.completeStart = completeStart;
    }

    public String getCompleteEnd() {
        return completeEnd;
    }

    public void setCompleteEnd(String completeEnd) {
        this.completeEnd = completeEnd;
    }
}
