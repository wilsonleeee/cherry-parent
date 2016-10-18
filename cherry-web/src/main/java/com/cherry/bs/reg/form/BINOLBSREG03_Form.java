/*
 * @(#)BINOLBSREG03_Form.java     1.0 2011/11/23
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
 * 区域更新画面Form
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG03_Form {
	
	/** 区域ID **/
	private String regionId;
	
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
	
	/** 区域原上级 **/
	private String oldHigherPath;
	
	/** 区域新上级 **/
	private String higherPath;
	
	/** 区域节点 **/
	private String regionPath;
	
	/** 省份节点 **/
	private String[] provincePath;
	
	/** 原来省份节点 **/
	private String[] oldProvincePath;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 是否忽略区域名称相似度验证 **/
	private String ignoreFlg;
	
	/** 原区域代码 **/
	private String oldRegionCode;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

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

	public String getOldHigherPath() {
		return oldHigherPath;
	}

	public void setOldHigherPath(String oldHigherPath) {
		this.oldHigherPath = oldHigherPath;
	}

	public String getHigherPath() {
		return higherPath;
	}

	public void setHigherPath(String higherPath) {
		this.higherPath = higherPath;
	}

	public String getRegionPath() {
		return regionPath;
	}

	public void setRegionPath(String regionPath) {
		this.regionPath = regionPath;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getIgnoreFlg() {
		return ignoreFlg;
	}

	public void setIgnoreFlg(String ignoreFlg) {
		this.ignoreFlg = ignoreFlg;
	}

	public String getOldRegionCode() {
		return oldRegionCode;
	}

	public void setOldRegionCode(String oldRegionCode) {
		this.oldRegionCode = oldRegionCode;
	}

	public String[] getProvincePath() {
		return provincePath;
	}

	public void setProvincePath(String[] provincePath) {
		this.provincePath = provincePath;
	}
	public String[] getOldProvincePath() {
		return oldProvincePath;
	}

	public void setOldProvincePath(String[] oldProvincePath) {
		this.oldProvincePath = oldProvincePath;
	}

}
