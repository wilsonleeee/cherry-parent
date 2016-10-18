/*  
 * @(#)BINOLBSPAT01_BL.java     1.0 2011/10/19     
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
package com.cherry.bs.pat.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 
 * 单位一览
 * @author LuoHong
 *
 */
public class BINOLBSPAT01_Form extends DataTable_BaseForm {

	/** 往来单位ID */
	private String partnerId;
	
	/** 编码*/	
	private String code;
	
	/** 单位名称 */	
	private String name;
	
	/** 城市 */
	private String cityId;
	
	/** 省份 */
	private String provinceId;
	
	/** 地址*/	
	private String address;
	
	/** 电话号码*/	
	private String phoneNumber;
	
	/** 邮编*/	
	private String postalCode;
	
	private String validFlag;
	
	private String brandInfoId;
	
	private String[] partnerIdArr;
	
	public List<Map<String, Object>> getPartnerList() {
		return partnerList;
	}

	public void setPartnerList(List<Map<String, Object>> partnerList) {
		this.partnerList = partnerList;
	}

	private List<Map<String, Object>> partnerList;

	
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String[] getPartnerIdArr() {
		return partnerIdArr;
	}

	public void setPartnerIdArr(String[] partnerIdArr) {
		this.partnerIdArr = partnerIdArr;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	
	
	
	
}
