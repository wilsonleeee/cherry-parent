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
 * @author nanjunbo
 * @version 1.0 2016/07/15
 * 
 * */
public class BINOLPTRPS42_Form extends BINOLCM13_Form {

	//预付单编号
	private String prePayNo;
	//预付单开始日期
	private String prePayStartDate;
	//预付单结束日期
	private String prePayEndDate;
	//预付金额起始
	private String prePayAmountStart;
	//预付金额结束
	private String prePayAmountEnd;
	//预留手机号
	private String phoneNo;
	//下次提货起始时间
	private String pickUpStartDate;
	//下次提货结束时间
	private String pickUpEndDate;
	//包含无下次提货时间
	private String includeNoPickUpTime;
	//预付单主表id
	private String prePayBillMainId;
	
	public String getPrePayNo() {
		return prePayNo;
	}
	public void setPrePayNo(String prePayNo) {
		this.prePayNo = prePayNo;
	}
	public String getPrePayStartDate() {
		return prePayStartDate;
	}
	public void setPrePayStartDate(String prePayStartDate) {
		this.prePayStartDate = prePayStartDate;
	}
	public String getPrePayEndDate() {
		return prePayEndDate;
	}
	public void setPrePayEndDate(String prePayEndDate) {
		this.prePayEndDate = prePayEndDate;
	}
	public String getPrePayAmountStart() {
		return prePayAmountStart;
	}
	public void setPrePayAmountStart(String prePayAmountStart) {
		this.prePayAmountStart = prePayAmountStart;
	}
	public String getPrePayAmountEnd() {
		return prePayAmountEnd;
	}
	public void setPrePayAmountEnd(String prePayAmountEnd) {
		this.prePayAmountEnd = prePayAmountEnd;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPickUpStartDate() {
		return pickUpStartDate;
	}
	public void setPickUpStartDate(String pickUpStartDate) {
		this.pickUpStartDate = pickUpStartDate;
	}
	public String getPickUpEndDate() {
		return pickUpEndDate;
	}
	public void setPickUpEndDate(String pickUpEndDate) {
		this.pickUpEndDate = pickUpEndDate;
	}
	public String getIncludeNoPickUpTime() {
		return includeNoPickUpTime;
	}
	public void setIncludeNoPickUpTime(String includeNoPickUpTime) {
		this.includeNoPickUpTime = includeNoPickUpTime;
	}
	public String getPrePayBillMainId() {
		return prePayBillMainId;
	}
	public void setPrePayBillMainId(String prePayBillMainId) {
		this.prePayBillMainId = prePayBillMainId;
	}		

}
