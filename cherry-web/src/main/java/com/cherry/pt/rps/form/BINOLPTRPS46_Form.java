/*		
 * @(#)BINOLPTRPS13_Form.java     1.0 2010/03/16		
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
 * 预付单查询BINOLPTRPS42_Form
 * 
 * @author lianmh
 * @version 1.0 2016/10/18
 * 
 * */
public class BINOLPTRPS46_Form extends BINOLCM13_Form {
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;
	
	/** 所属系统*/
	private String belongFaction;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品ID */
	private String productId;
	
	/** 字符编码 **/
	private String charset;
	
	/** 逻辑仓库ID **/
	private String lgcInventoryId;
	
	/** 大分类ID **/
	private String catePropValId;
	
	/** 大分类名称 **/
	private String bigClassName;
	
	/** 产品分类 */
	private String cateInfo;
	
	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
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

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getLgcInventoryId() {
		return lgcInventoryId;
	}

	public void setLgcInventoryId(String lgcInventoryId) {
		this.lgcInventoryId = lgcInventoryId;
	}

	public String getCatePropValId() {
		return catePropValId;
	}

	public void setCatePropValId(String catePropValId) {
		this.catePropValId = catePropValId;
	}

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}

	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

	public String getBelongFaction() {
		return belongFaction;
	}

	public void setBelongFaction(String belongFaction) {
		this.belongFaction = belongFaction;
	}
}
