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


import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSSPRM77_Form extends DataTable_BaseForm {
	/**品牌代码*/
	private String brandCode;
	/**加密后的全部Json串*/
	private String paramdata;
	/**返回页面的信息*/
	private Map<String,Object> result_map;
	/**打印URL中的参数*/
	private String paramdataStr;
	
	private String datasourceName;
	
	private List<Map<String,Object>> couponList;
	
	private String bpCode;//会员BP号
	private String memCount;//会员卡号
	private String memName;//会员姓名
	private String memPhone;//会员手机号
	private String memLevel;//会员等级
	
	private String status;
	
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getParamdata() {
		return paramdata;
	}
	public void setParamdata(String paramdata) {
		this.paramdata = paramdata;
	}
	public Map<String, Object> getResult_map() {
		return result_map;
	}
	public void setResult_map(Map<String, Object> result_map) {
		this.result_map = result_map;
	}
	public String getParamdataStr() {
		return paramdataStr;
	}
	public void setParamdataStr(String paramdataStr) {
		this.paramdataStr = paramdataStr;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public List<Map<String, Object>> getCouponList() {
		return couponList;
	}
	public void setCouponList(List<Map<String, Object>> couponList) {
		this.couponList = couponList;
	}
	public String getBpCode() {
		return bpCode;
	}
	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}
	public String getMemCount() {
		return memCount;
	}
	public void setMemCount(String memCount) {
		this.memCount = memCount;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemPhone() {
		return memPhone;
	}
	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}
	public String getMemLevel() {
		return memLevel;
	}
	public void setMemLevel(String memLevel) {
		this.memLevel = memLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
