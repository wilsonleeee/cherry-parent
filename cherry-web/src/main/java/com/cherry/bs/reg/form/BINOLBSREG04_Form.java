/*
 * @(#)BINOLBSREG04_Form.java     1.0 2011/11/23
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
package com.cherry.bs.reg.form;

/**
 * 区域添加画面Form
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG04_Form {

	/** 品牌ID **/
	private String brandInfoId;
	
	/** 区域中文名称 **/
	private String regionNameChinese;
	
	/** 区域英文名称 **/
	private String regionNameForeign;
	
	/** 区域类型 **/
	private String regionType;
	
	/** 区域代码 **/
	private String regionCode;
	
	/** 助记码 **/
	private String helpCode;
	
	/** 邮编 **/
	private String zipCode;
	
	/** 电话区号 **/
	private String teleCode;
	
	public String getHigherPath() {
		return higherPath;
	}

	public void setHigherPath(String higherPath) {
		this.higherPath = higherPath;
	}

	/** 柜台path */
	private String higherPath;
	
	/** 省份path */
	private String[] provincePath;
	

	/** 上级区域ID **/
	private String regionId;
	
	/** 是否忽略区域名称相似度验证 **/
	private String ignoreFlg;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getRegionNameChinese() {
		return regionNameChinese;
	}

	public void setRegionNameChinese(String regionNameChinese) {
		this.regionNameChinese = regionNameChinese;
	}

	public String getRegionNameForeign() {
		return regionNameForeign;
	}

	public void setRegionNameForeign(String regionNameForeign) {
		this.regionNameForeign = regionNameForeign;
	}

	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getHelpCode() {
		return helpCode;
	}

	public void setHelpCode(String helpCode) {
		this.helpCode = helpCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTeleCode() {
		return teleCode;
	}

	public void setTeleCode(String teleCode) {
		this.teleCode = teleCode;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getIgnoreFlg() {
		return ignoreFlg;
	}

	public void setIgnoreFlg(String ignoreFlg) {
		this.ignoreFlg = ignoreFlg;
	}

	public String[] getProvincePath() {
		return provincePath;
	}

	public void setProvincePath(String[] provincePath) {
		this.provincePath = provincePath;
	}
}
