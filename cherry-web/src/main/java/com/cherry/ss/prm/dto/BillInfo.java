package com.cherry.ss.prm.dto;

import java.util.List;
import java.util.Map;

public class BillInfo {
	/** 订单号 */
	private String billCode;
	/** 交易日期 */
	private String saleDate;
	/** 交易时间 */
	private String saleTime;
	/** 品牌代码 */
	private String brandCode;
	/** 组织代码 */
	private String orgCode;
	/** 品牌ID */
	private int brandInfoId;
	/** 组织ID */
	private int organizationInfoId;
	/** 柜台代号 */
	private String counterCode;
	/** 会员卡号 */
	private String memberCode;
	/** 会员手机号 */
	private String mobile;
	/** 会员等级 */
	private String levelCode;
	/** 会员等级 */
	private int levelId;
	/** 整单金额 */
	private double amount;
	/** 订单是否用券 */
	private boolean isUseCoupon;
	/** 本次订单生成的所有券 */
	private String allCoupon;
	/** 订单明细 */
	private List<Map<String, Object>> detailList;
	/** 会员BP号*/
	private String bpCode;
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	public String getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public int getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public List<Map<String, Object>> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public boolean isUseCoupon() {
		return isUseCoupon;
	}
	public void setUseCoupon(boolean isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}
	public int getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public String getAllCoupon() {
		return allCoupon;
	}
	public void setAllCoupon(String allCoupon) {
		this.allCoupon = allCoupon;
	}
	public String getBpCode() {
		return bpCode;
	}
	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}
	
}
