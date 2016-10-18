/*	
 * @(#)BINOLCPACT12_Form.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 活动产品库存一览
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT12_Form extends BINOLCM13_Form{
	
	/**所属品牌ID*/
	private String brandInfoId;

	/** 主题活动代码 */
	private String campaignCode;

	/** 活动代码 */
	private String subCampCode;
	
	/** 主题活动模糊查询字符串 */
	private String campInfoStr;
	
	/** 活动模糊查询字符串 */
	private String subCampInfoStr;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/**柜台组织ID*/
	private String organizationId;
	
	/** 产品厂商ID*/
	private String[] productVendorIDArr;
	
	/**分配数量*/
	private String[] totalQuantityArr;
	
	/**安全数量*/
	private String[] safeQuantityArr;

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getSubCampCode() {
		return subCampCode;
	}

	public void setSubCampCode(String subCampCode) {
		this.subCampCode = subCampCode;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getCampInfoStr() {
		return campInfoStr;
	}

	public void setCampInfoStr(String campInfoStr) {
		this.campInfoStr = campInfoStr;
	}

	public String getSubCampInfoStr() {
		return subCampInfoStr;
	}

	public void setSubCampInfoStr(String subCampInfoStr) {
		this.subCampInfoStr = subCampInfoStr;
	}

	public String[] getProductVendorIDArr() {
		return productVendorIDArr;
	}

	public void setProductVendorIDArr(String[] productVendorIDArr) {
		this.productVendorIDArr = productVendorIDArr;
	}

	public String[] getTotalQuantityArr() {
		return totalQuantityArr;
	}

	public void setTotalQuantityArr(String[] totalQuantityArr) {
		this.totalQuantityArr = totalQuantityArr;
	}

	public String[] getSafeQuantityArr() {
		return safeQuantityArr;
	}

	public void setSafeQuantityArr(String[] safeQuantityArr) {
		this.safeQuantityArr = safeQuantityArr;
	}

}
