/*
 * @(#)BINOLPTJCS04_Form.java     1.0 2011/03/28
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
package com.cherry.pt.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品查询Form
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.03.28
 */
public class BINOLPTJCS04_Form extends DataTable_BaseForm {
	/** 品牌ID */
	private String brandInfoId;
	/** 产品名称 */
	private String nameTotal;
	/** 厂商编码 */
	private String unitCode;
	/** 产品条码 */
	private String barCode;
	/** 产品类型 */
	private String mode;
	/** 子品牌 **/
	private String originalBrand;
	/** 有效区分 */
	private String validFlag;
	/** 有效状态 */
	private String status;
	/** 积分兑换 */
	private String isExchanged;
	/** 分类路径 */
	private String path;
	/** 定位条件 */
	private String locationPosition;
	/** 产品ID */
	private int productId;
	/** 产品IDs */
	private String[] productInfoIds;

	/** 更新时间 */
	private String prtUpdateTime;
	/** 更新次数 */
	private int prtModifyCount;
	/** invlidUBFlag 0:全部无效编码条码  1:存在有效编码条码 */
	private String invlidUBFlag;
	/** 产品厂商ID */
	private int prtVendorId;
	/** 产品分类*/
	private String cateInfo;

	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}
	
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getLocationPosition() {
		return locationPosition;
	}

	public void setLocationPosition(String locationPosition) {
		this.locationPosition = locationPosition;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String[] getProductInfoIds() {
		return productInfoIds;
	}

	public void setProductInfoIds(String[] productInfoIds) {
		this.productInfoIds = productInfoIds;
	}
	public String getPrtUpdateTime() {
		return prtUpdateTime;
	}

	public void setPrtUpdateTime(String prtUpdateTime) {
		this.prtUpdateTime = prtUpdateTime;
	}

	public int getPrtModifyCount() {
		return prtModifyCount;
	}

	public void setPrtModifyCount(int prtModifyCount) {
		this.prtModifyCount = prtModifyCount;
	}
	public String getInvlidUBFlag() {
		return invlidUBFlag;
	}

	public void setInvlidUBFlag(String invlidUBFlag) {
		this.invlidUBFlag = invlidUBFlag;
	}
	
	public int getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(int prtVendorId) {
		this.prtVendorId = prtVendorId;
	}
	public String getIsExchanged() {
		return isExchanged;
	}

	public void setIsExchanged(String isExchanged) {
		this.isExchanged = isExchanged;
	}
}
