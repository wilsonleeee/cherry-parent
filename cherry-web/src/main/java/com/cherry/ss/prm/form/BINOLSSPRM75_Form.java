/*		
 * @(#)BINOLSSPRM75_Form.java     1.0 2016/05/05		
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

/**
 * 优惠券一览Form
 * @author ghq
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM75_Form extends DataTable_BaseForm{
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	/** 券号 */
	private String couponNo;
	
	/** 券类型 */
	private String couponType;
	
	/** 规则编码 */
	private String ruleCode;
	
	/**券状态 */
	private String status;
	
	/**券规则代码 */
	private String couponRule;
	
	/**有效区分*/
	private String validFlag;

	/**使用开始时间 */
	private String startTime;
	
	/**使用截止时间 */
	private String endTime;
	
	/**发券关联单号 */
	private String relationBill;
	
	/**会员BP号 */
	private String BPCode;
	
	public String getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(String couponRule) {
		this.couponRule = couponRule;
	}

	public String getRelationBill() {
		return relationBill;
	}

	public void setRelationBill(String relationBill) {
		this.relationBill = relationBill;
	}

	public String getBPCode() {
		return BPCode;
	}

	public void setBPCode(String bPCode) {
		BPCode = bPCode;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	}
