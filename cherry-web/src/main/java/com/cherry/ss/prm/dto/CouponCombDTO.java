package com.cherry.ss.prm.dto;

import java.util.List;
import java.util.Map;

public class CouponCombDTO {
	
	private BillInfo billInfo;
	private CouponInfo couponInfo;
	private List<Map<String, Object>> actList;
	private List<Map<String, Object>> couponList;
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	
	public CouponInfo getCouponInfo() {
		return couponInfo;
	}
	public void setCouponInfo(CouponInfo couponInfo) {
		this.couponInfo = couponInfo;
	}
	public List<Map<String, Object>> getActList() {
		return actList;
	}
	public void setActList(List<Map<String, Object>> actList) {
		this.actList = actList;
	}
	public List<Map<String, Object>> getCouponList() {
		return couponList;
	}
	public void setCouponList(List<Map<String, Object>> couponList) {
		this.couponList = couponList;
	}
}
