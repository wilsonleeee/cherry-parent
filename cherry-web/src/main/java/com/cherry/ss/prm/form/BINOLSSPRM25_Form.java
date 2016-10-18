/*
 * @(#)BINOLSSPRM25_Form.java     1.0 2010/10/27
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
 * 盘点查询Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.10.27
 */
public class BINOLSSPRM25_Form extends BINOLCM13_Form{
	/** 盘点单号 */
	private String stockTakingNo;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 盈亏 */
	private String profitKbn;

	/** 促销产品名称 */
	private String promotionProductName;
	
	/** 审核状态 */
	private String verifiedFlag;

	/**促销产品厂商ID*/
	private String prmVendorId;

	/** 部门ID */
	private String organizationId;

	/**大分类列表*/
    private List<Map<String, Object>> largeCategoryList;
    
    /**大分类*/
	private String largeCategory;
	
    /**中分类*/
	private String middleCategory;
	
    /**小分类*/
	private String smallCategory;
	
	public String getStockTakingNo() {
		return stockTakingNo;
	}

	public void setStockTakingNo(String stockTakingNo) {
		this.stockTakingNo = stockTakingNo;
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

	public String getProfitKbn() {
		return profitKbn;
	}

	public void setProfitKbn(String profitKbn) {
		this.profitKbn = profitKbn;
	}

	public String getPromotionProductName() {
		return promotionProductName;
	}

	public void setPromotionProductName(String promotionProductName) {
		this.promotionProductName = promotionProductName;
	}

	
	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

    public String getPrmVendorId() {
        return prmVendorId;
    }

    public void setPrmVendorId(String prmVendorId) {
        this.prmVendorId = prmVendorId;
    }

	public List<Map<String, Object>> getLargeCategoryList() {
		return largeCategoryList;
	}

	public void setLargeCategoryList(List<Map<String, Object>> largeCategoryList) {
		this.largeCategoryList = largeCategoryList;
	}

	public String getLargeCategory() {
		return largeCategory;
	}

	public void setLargeCategory(String largeCategory) {
		this.largeCategory = largeCategory;
	}

	public String getMiddleCategory() {
		return middleCategory;
	}

	public void setMiddleCategory(String middleCategory) {
		this.middleCategory = middleCategory;
	}

	public String getSmallCategory() {
		return smallCategory;
	}

	public void setSmallCategory(String smallCategory) {
		this.smallCategory = smallCategory;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
    
}
