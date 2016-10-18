/*
 * @(#)BINOLSTIOS11_Form.java     1.0 2015/02/04
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 退库申请（Excel导入）Form
 * 
 * @author niushunjie
 * @version 1.0 2015.02.04
 */
public class BINOLSTIOS11_Form extends DataTable_BaseForm {
    /** 品牌ID */
    private String brandInfoId;
    
    /** 导入日期开始 */
    private String importStartTime;
    
    /** 导入日期结束 */
    private String importEndTime;
    
    /** 导入原因 */
    private String comments;
    
    /** 导入批次号 */
    private String importBatchCode;
    
    /** 是否允许导入重复数据 */
    private String importRepeat;
    
    /** 导入批次是否从导入文件中获取 */
    private String isChecked;
    
    /**产品退库申请单Excel ID*/
    private String proReturnRequestExcelID;
    
    /**弹出框 单据号*/
    private String billNo;
    
    /**弹出框 导入开始日期*/
    private String importDateStart;
    
    /**弹出框 导入结束日期*/
    private String importDateEnd;
    
    /**弹出框 导入批次号*/
    private String importBatchId;

    /**弹出框 导入结果**/
    private String importResult;
    
    /**弹出框 退库部门ID*/
    private String organizationIdFrom;
    
    /**弹出框 接收退库部门ID*/
    private String organizationIdTo;
    
    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getImportStartTime() {
        return importStartTime;
    }

    public void setImportStartTime(String importStartTime) {
        this.importStartTime = importStartTime;
    }

    public String getImportEndTime() {
        return importEndTime;
    }

    public void setImportEndTime(String importEndTime) {
        this.importEndTime = importEndTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getImportBatchCode() {
        return importBatchCode;
    }

    public void setImportBatchCode(String importBatchCode) {
        this.importBatchCode = importBatchCode;
    }

    public String getImportRepeat() {
        return importRepeat;
    }

    public void setImportRepeat(String importRepeat) {
        this.importRepeat = importRepeat;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getProReturnRequestExcelID() {
        return proReturnRequestExcelID;
    }

    public void setProReturnRequestExcelID(String proReturnRequestExcelID) {
        this.proReturnRequestExcelID = proReturnRequestExcelID;
    }

    public String getImportBatchId() {
        return importBatchId;
    }

    public void setImportBatchId(String importBatchId) {
        this.importBatchId = importBatchId;
    }

    public String getImportResult() {
        return importResult;
    }

    public void setImportResult(String importResult) {
        this.importResult = importResult;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getImportDateStart() {
        return importDateStart;
    }

    public void setImportDateStart(String importDateStart) {
        this.importDateStart = importDateStart;
    }

    public String getImportDateEnd() {
        return importDateEnd;
    }

    public void setImportDateEnd(String importDateEnd) {
        this.importDateEnd = importDateEnd;
    }

    public String getOrganizationIdFrom() {
        return organizationIdFrom;
    }

    public void setOrganizationIdFrom(String organizationIdFrom) {
        this.organizationIdFrom = organizationIdFrom;
    }

    public String getOrganizationIdTo() {
        return organizationIdTo;
    }

    public void setOrganizationIdTo(String organizationIdTo) {
        this.organizationIdTo = organizationIdTo;
    }
}