/*  
 * @(#)BINOLSTBIL17_Form.java     1.0 2012/11/28      
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
package com.cherry.st.bil.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 产品调拨单Form
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
public class BINOLSTBIL17_Form extends BINOLCM13_Form {
    /** 品牌ID */
    private String brandInfoId;

    /** 员工ID */
    private String employeeId;

    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;

    /** 产品名称 */
    private String nameTotal;

    /** 部门ID */
    private String organizationId;
    
    /** 仓库ID */
    private String depotId;
    
    /** 逻辑仓库ID */
    private String logicinventId;

    /**产品厂商ID*/
    private String prtVendorId;

    /** 调拨单号 */
    private String allocationrNo;

    /** 审核状态 */
    private String verifiedFlag;
    
    /** 处理状态 */
    private String tradeStatus;
    
    /** 字符编码 */
    private String charset;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**调拨单List*/
    private List<Map<String,Object>> productAllocationList;
    
    /**选中单据ID*/
    private String[] checkedBillIdArr;
    
    /**页面ID*/
    private String currentMenuID;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getNameTotal() {
        return nameTotal;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getLogicinventId() {
        return logicinventId;
    }

    public void setLogicinventId(String logicinventId) {
        this.logicinventId = logicinventId;
    }

    public String getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String getAllocationrNo() {
        return allocationrNo;
    }

    public void setAllocationrNo(String allocationrNo) {
        this.allocationrNo = allocationrNo;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public List<Map<String, Object>> getProductAllocationList() {
        return productAllocationList;
    }

    public void setProductAllocationList(List<Map<String, Object>> productAllocationList) {
        this.productAllocationList = productAllocationList;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }
    
    public String getCurrentMenuID() {
        return currentMenuID;
    }

    public void setCurrentMenuID(String currentMenuID) {
        this.currentMenuID = currentMenuID;
    }
}
