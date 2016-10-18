package com.cherry.ss.prm.dto;

import java.util.List;
import java.util.Map;

public class CouponInfo {
	/** 优惠券基本信息*/
	private CouponBaseInfo couponBaseInfo;
	
	/** 券验证码*/
	private String couponCode;
	
	/** 对应券内容的编号*/
	private String contentNo;
	
	/** 券状态*/
	private String status;
	
	/** 券所属会员Code*/
	private String memCode;
	
	/** 券所属会员手机号*/
	private String mobile;
	
	/** 可否赠送*/
	private String isGive;
	
	/** 代物券对应的产品 */
	private List<Map<String, Object>> productList ;
	
	/** 券所属会员BP号 */
	private String bpCode;

	public String getBpCode() {
		return bpCode;
	}

	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}

	public CouponBaseInfo getCouponBaseInfo() {
		return couponBaseInfo;
	}

	public void setCouponBaseInfo(CouponBaseInfo couponBaseInfo) {
		this.couponBaseInfo = couponBaseInfo;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getContentNo() {
		return contentNo;
	}

	public void setContentNo(String contentNo) {
		this.contentNo = contentNo;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsGive() {
		return isGive;
	}

	public void setIsGive(String isGive) {
		this.isGive = isGive;
	}

	public List<Map<String, Object>> getProductList() {
		return productList;
	}

	public void setProductList(List<Map<String, Object>> productList) {
		this.productList = productList;
	}
}
