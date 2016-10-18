/*  
 * @(#)BINOLSTIOS01_Action.java     1.0 2011/09/29      
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
/**
 * 报损单一览
 * @author LuoHong
 *
 */
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLSTBIL05_Form extends BINOLCM13_Form {
	
	/** 报损单号 */
	private String billNo;
	
	/** 部门ID */
	private String organizationId;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;

	/** 实体仓库ID */
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
	
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsLSList;
   
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
	
	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
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

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
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
	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
	public String getBinEmployeeIDAudit() {
		return binEmployeeIDAudit;
	}

	public void setBinEmployeeIDAudit(String binEmployeeIDAudit) {
		this.binEmployeeIDAudit = binEmployeeIDAudit;
	}

    public List<Map<String, Object>> getVerifiedFlagsLSList() {
        return verifiedFlagsLSList;
    }

    public void setVerifiedFlagsLSList(List<Map<String, Object>> verifiedFlagsLSList) {
        this.verifiedFlagsLSList = verifiedFlagsLSList;
    }
}
