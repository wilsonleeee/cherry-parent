/*
 * @(#)BINOLPTRPS01_Form.java     1.0 2012/12/04
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 盘点查询Form
 * 
 * 
 * 
 * @author liumiminghao
 * @version 1.0 2012.12.04
 */
public class BINOLPTRPS16_Form extends BINOLCM13_Form {
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

	/** 产品厂商ID */
	private String prtVendorId;

	/** 产品名称 */
	private String productName;

	/** 账面数量 */
	private int quantity;

	/** 盘点理由 */
	private String Comments;
	
	/**编码合并类型*/
	private String codeMergeType;
	
	/**是否包括选择的产品*/
	private String includeFlag;

	/**查询条件中“已选择的产品”*/
	private String selPrtVendorIdArr;	
	
	/** 产品分类 */
	private String cateInfo;
	
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

    public String getCodeMergeType() {
        return codeMergeType;
    }

    public void setCodeMergeType(String codeMergeType) {
        this.codeMergeType = codeMergeType;
    }

    public String getIncludeFlag() {
        return includeFlag;
    }

    public void setIncludeFlag(String includeFlag) {
        this.includeFlag = includeFlag;
    }

	public String getSelPrtVendorIdArr() {
		return selPrtVendorIdArr;
	}

	public void setSelPrtVendorIdArr(String selPrtVendorIdArr) {
		this.selPrtVendorIdArr = selPrtVendorIdArr;
	}

	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

}
