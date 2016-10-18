/*	
 * @(#)BINOLBSCNT01_Form.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 	柜台查询画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT01_Form extends DataTable_BaseForm {
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 柜台中文名称 */
	private String counterNameIF;
	
	/** 渠道ID */
	private String channelId;
	
	/** 柜台状态 */
	private String status;
	
	/** 柜台地址 */
	private String counterAddress;
	
	/** 城市 */
	private String cityId;
	
	/** 省份 */
	private String provinceId;
	
	/** 有效区分 */
	private String validFlag;
	
	/**柜台主管名*/
	private String counterBAS;
	
	/**柜台主管名下拉框显示行数*/
	private int number;
	
	/** 是否支持柜台协同维护*/
	private boolean maintainCoutSynergy;

	/**业务负责人*/
	private String busniessPrincipal;
	
	
	public int getNumber() {
		return number;
	}

	public boolean isMaintainCoutSynergy() {
		return maintainCoutSynergy;
	}

	public void setMaintainCoutSynergy(boolean maintainCoutSynergy) {
		this.maintainCoutSynergy = maintainCoutSynergy;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCounterBAS() {
		return counterBAS;
	}

	public void setCounterBAS(String counterBAS) {
		this.counterBAS = counterBAS;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterNameIF() {
		return counterNameIF;
	}

	public void setCounterNameIF(String counterNameIF) {
		this.counterNameIF = counterNameIF;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCounterAddress() {
		return counterAddress;
	}

	public void setCounterAddress(String counterAddress) {
		this.counterAddress = counterAddress;
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

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getBusniessPrincipal() {
		return busniessPrincipal;
	}

	public void setBusniessPrincipal(String busniessPrincipal) {
		this.busniessPrincipal = busniessPrincipal;
	}
	
	

}
