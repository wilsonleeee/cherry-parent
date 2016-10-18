/*  
 * @(#)BINOLSTBIL15_Form.java     1.0 2012/8/23      
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
 * 产品盘点申请单Form
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
public class BINOLSTBIL15_Form extends BINOLCM13_Form {
	/** 品牌ID */
    private String brandInfoId;

    /** 退库仓库ID */
	private String depotId;

    /** 员工ID */
	private String employeeId;

    /** 结束日 */
	private String endDate;

    /** 逻辑仓库ID */
	private String logicinventId;

    /** 产品名称 */
	private String nameTotal;

    /** 部门ID */
	private String organizationId;

    /**产品厂商ID*/
    private String prtVendorId;

    /** 开始日 */
	private String startDate;

    /** 盘点申请单号 */
	private String stockTakingNo;

    /** 审核状态 */
	private String verifiedFlag;
	
	/**业务类型*/
    private String tradeType;
	
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
	
    /**盘点申请List*/
    private List<Map<String,Object>> proStocktakeRequestList;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStockTakingNo() {
        return stockTakingNo;
    }

    public void setStockTakingNo(String stockTakingNo) {
        this.stockTakingNo = stockTakingNo;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public List<Map<String, Object>> getProStocktakeRequestList() {
        return proStocktakeRequestList;
    }

    public void setProStocktakeRequestList(
            List<Map<String, Object>> proStocktakeRequestList) {
        this.proStocktakeRequestList = proStocktakeRequestList;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
