/*
 * @(#)BINOLSSPRM71_Form.java     1.0 2015/09/21	
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 促销品关联查询实体类
 * @author Hujh
 *
 */
public class BINOLSSPRM71_Form extends DataTable_BaseForm {
	
	/**所管辖的品牌List*/
	private List<Map<String, Object>> brandInfoList = new ArrayList<Map<String,Object>>();
	
	private List<Map<String, Object>> prmList = new ArrayList<Map<String, Object>>();
	
	private List<Map<String, Object>> detailPrmList = new ArrayList<Map<String, Object>>();
	
	/** 品牌信息ID*/
	private String brandInfoId;
	
	/**关联表中的分组ID*/
	private String groupId;
	
	/**促销品关联表中的促销品厂商ID*/
	private String prmVendorId;

	/**促销品全称*/
	private String nameTotal;
	
	/**厂商编码*/
	private String unitCode;
	
	/**促销品条码*/
	private String barCode;
	
	/**促销品类型*/
	private String prmCate;
	
	/**有效区分*/
	private String validFlag;
	
	public String getGroupId() {
		return groupId;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getPrmCate() {
		return prmCate;
	}

	public void setPrmCate(String prmCate) {
		this.prmCate = prmCate;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public List<Map<String, Object>> getPrmList() {
		return prmList;
	}

	public void setPrmList(List<Map<String, Object>> prmList) {
		this.prmList = prmList;
	}

	public List<Map<String, Object>> getDetailPrmList() {
		return detailPrmList;
	}

	public void setDetailPrmList(List<Map<String, Object>> detailPrmList) {
		this.detailPrmList = detailPrmList;
	}

}
