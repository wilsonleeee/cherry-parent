package com.cherry.bs.pat.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSPAT02_Form  extends DataTable_BaseForm{

	private String partnerId;
	/** 编码*/	
	private String code;
	
	/** 单位名称 */	
	private String nameCn;
	
	/** 单位名称 */	
	private String nameEn;
	
	/** 地址*/	
	private String address;
	
	/** 电话号码*/	
	private String phoneNumber;
	
	/** 邮编*/	
	private String postalCode;

	/** 品牌Id*/	
	private String brandInfoId;
	
	/** 县级市 */
	private String countyId;
	
	/** 城市 */
	private String cityId;
	
	/** 省份 */
	private String provinceId;
	
	/** 所属区域 */
	private String regionId;
	
	/** 送货地址*/
	private String deliverAddress;
	
	/** 联系地址*/
	private String contactAddress;
	
	/** 联系人*/
	private String contactPerson;
	
	public String getCountyId() {
		return countyId;
	}

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
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

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

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

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
}
