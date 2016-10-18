/*  
 * @(#)BINOLSTBIL13_Form.java     1.0 2012/7/24      
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
 * 产品退库申请单Form
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
public class BINOLSTBIL13_Form extends BINOLCM13_Form {
	/** 退库申请单号 */
	private String billNo;

	/** 部门ID */
	private String organizationId;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;

	/** 退库仓库ID */
	private String depotId;

	/** 逻辑仓库ID */
	private String logicinventId;

	/** 产品名称 */
	private String nameTotal;
	
	/** 审核状态 */
	private String verifiedFlag;

	/** 审核者 */
	private String binEmployeeIDAudit;

	/** 品牌ID */
    private String brandInfoId;
  
	/**产品厂商ID*/
    private String prtVendorId;
    
    /**员工ID*/
	private String employeeId;
	
	/**业务类型*/
	private String tradeType;
	
	/**原因*/
	private String reason;
	
	private List<Map<String,Object>> proReturnRequestList;
	
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**选中单据ID*/
    private String[] checkedBillIdArr;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getNameTotal() {
        return nameTotal;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getBinEmployeeIDAudit() {
        return binEmployeeIDAudit;
    }

    public void setBinEmployeeIDAudit(String binEmployeeIDAudit) {
        this.binEmployeeIDAudit = binEmployeeIDAudit;
    }

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Map<String, Object>> getProReturnRequestList() {
        return proReturnRequestList;
    }

    public void setProReturnRequestList(List<Map<String, Object>> proReturnRequestList) {
        this.proReturnRequestList = proReturnRequestList;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }
}
