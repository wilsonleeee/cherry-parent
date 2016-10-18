package com.cherry.bs.emp.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSEMP08_Form extends DataTable_BaseForm {
	
	/** 当前批次号 */
	private String batchCode;

	/** 二级代理商CODE */
	private String resellerCode;
	
	/** 一级代理商 */
	private String parentResellerCode;
	
	/** 优惠券码 */
	private String couponCode;
	
	/** 同步状态 */
	private String synchFlag;
	
	/** 代理商优惠券ID*/
	private String baCouponId;

	public String getResellerCode() {
		return resellerCode;
	}

	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBaCouponId() {
		return baCouponId;
	}

	public void setBaCouponId(String baCouponId) {
		this.baCouponId = baCouponId;
	}

	public String getSynchFlag() {
		return synchFlag;
	}

	public void setSynchFlag(String synchFlag) {
		this.synchFlag = synchFlag;
	}

	public String getParentResellerCode() {
		return parentResellerCode;
	}

	public void setParentResellerCode(String parentResellerCode) {
		this.parentResellerCode = parentResellerCode;
	}
	
}
