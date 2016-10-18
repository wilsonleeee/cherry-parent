/*
 * @(#)BINOLPTRPS01_Form.java     1.0 2011/3/11
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
 * 盘点查询Form
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
public class BINOLSTBIL09_Form extends BINOLCM13_Form{
	/** 盘点单号 */
	private String stockTakingNo;
	
	/** 部门类型 */
	private String departType;
	
	/** 部门ID */
	private String organizationId;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 实体仓库ID */
	private String inventId;
	
	/** 盈亏 */
	private String profitKbn;
	
	/** 审核状态 */
	private String verifiedFlag;

	/** 品牌ID */
    private String brandInfoId;
	
    /**产品厂商ID*/
    private String prtVendorId;
    
    /**产品名称*/
    private String productName;
    
    /**选中单据ID*/
    private String[] checkedBillIdArr;
    
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsCAList;

	public String getStockTakingNo() {
		return stockTakingNo;
	}

	public void setStockTakingNo(String stockTakingNo) {
		this.stockTakingNo = stockTakingNo;
	}
	
	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
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

	public String getInventId() {
		return inventId;
	}

	public void setInventId(String inventId) {
		this.inventId = inventId;
	}

	public String getProfitKbn() {
		return profitKbn;
	}

	public void setProfitKbn(String profitKbn) {
		this.profitKbn = profitKbn;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public String getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String[] getCheckedBillIdArr() {
        return checkedBillIdArr;
    }

    public void setCheckedBillIdArr(String[] checkedBillIdArr) {
        this.checkedBillIdArr = checkedBillIdArr;
    }

    public List<Map<String, Object>> getVerifiedFlagsCAList() {
        return verifiedFlagsCAList;
    }

    public void setVerifiedFlagsCAList(List<Map<String, Object>> verifiedFlagsCAList) {
        this.verifiedFlagsCAList = verifiedFlagsCAList;
    }
}
