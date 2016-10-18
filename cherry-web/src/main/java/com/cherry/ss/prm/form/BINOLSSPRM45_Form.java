/*
 * @(#)BINOLSSPRM45_Form.java     1.0 2012/12/26
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
 * @author liumiminghao
 * @version 1.0 2012.12.26
 */
public class BINOLSSPRM45_Form extends BINOLCM13_Form {
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

	/** 促销品厂商ID */
	private String prmVendorId;

	/** 促销品名称 */
	private String promotionName;

	/** 账面数量 */
	private int quantity;

	/** 盘点理由 */
	private String Comments;
	
    /**编码合并类型*/
    private String codeMergeType;
	
    /**是否包括选择的促销品*/
    private String includeFlag;

    /**大分类列表*/
    private List<Map<String, Object>> largeCategoryList;
    
    /**大分类*/
	private String largeCategory;
	
    /**中分类*/
	private String middleCategory;
	
    /**小分类*/
	private String smallCategory;
	
	/**排除多个促销品 */
	private String selPrmVendorIdArr;
	
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

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
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

	public String getSelPrmVendorIdArr() {
		return selPrmVendorIdArr;
	}

	public void setSelPrmVendorIdArr(String selPrmVendorIdArr) {
		this.selPrmVendorIdArr = selPrmVendorIdArr;
	}

}
