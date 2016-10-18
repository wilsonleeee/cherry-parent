/*		
 * @(#)BINOLSSPRM61_Form.java     1.0 2012/09/27		
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 促销品移库一览Form
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM61_Form extends BINOLCM13_Form {
    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;
    
    /**促销品厂商ID*/
    private String prmVendorId;
    
    /** 促销品名称 */
    private String nameTotal;
    
    /** 单号  */
    private String billNo;
    
    /** 审核区分  */
    private String verifiedFlag;

    private List<Map<String, Object>> shiftList;
    
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

    public void setShiftList(List<Map<String, Object>> shiftList) {
        this.shiftList = shiftList;
    }

    public List<Map<String, Object>> getShiftList() {
        return shiftList;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setPrmVendorId(String prmVendorId) {
        this.prmVendorId = prmVendorId;
    }

    public String getPrmVendorId() {
        return prmVendorId;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getNameTotal() {
        return nameTotal;
    }
}