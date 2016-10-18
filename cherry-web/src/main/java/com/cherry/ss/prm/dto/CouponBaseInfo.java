package com.cherry.ss.prm.dto;

public class CouponBaseInfo {
	/** 券规则代码*/
	private String maincode;
	/** 优惠券类型 */
	private String couponType;
	/** 优惠券类型名称 */
	private String typeName;
	/** 优惠券编号 */
	private String couponCode;
	/** 优惠券名称 */
	private String couponName;
	/** 预计优惠金额 */
	private double planDiscountPrice;
	/** 实际优惠金额 */
	private double actualDiscountPrice;
	/** 使用开始时间*/
	private String startTime;
	/** 截止日期 */
	private String endTime;
	/** 优惠券描述 */
	private String descriptionDtl;
	/** 产商编码 */
	private String unicode;
	/** 产品条码 */
	private String barcode;
	/** 是否需要密码 */
	private String passwordFlag;
	/** 是否选中 */
	private String checkFlag;
	/** 资格券对应的maincode字符串 */
	private String ZGQArr;
	/** 最大选择数 */
	private int maxCount;
	/**产品条件*/
	private String fullFlag;
	
	public String getFullFlag() {
		return fullFlag;
	}
	public void setFullFlag(String fullFlag) {
		this.fullFlag = fullFlag;
	}
	public String getMaincode() {
		return maincode;
	}
	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public double getPlanDiscountPrice() {
		return planDiscountPrice;
	}
	public void setPlanDiscountPrice(double planDiscountPrice) {
		this.planDiscountPrice = planDiscountPrice;
	}
	public double getActualDiscountPrice() {
		return actualDiscountPrice;
	}
	public void setActualDiscountPrice(double actualDiscountPrice) {
		this.actualDiscountPrice = actualDiscountPrice;
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
	public String getDescriptionDtl() {
		return descriptionDtl;
	}
	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}
	public String getUnicode() {
		return unicode;
	}
	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getPasswordFlag() {
		return passwordFlag;
	}
	public void setPasswordFlag(String passwordFlag) {
		this.passwordFlag = passwordFlag;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getZGQArr() {
		return ZGQArr;
	}
	public void setZGQArr(String zGQArr) {
		ZGQArr = zGQArr;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
}
