package com.cherry.bs.emp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 生成代理商优惠券Form
 * @author menghao
 * @version 1.0 2014-08-27
 */
public class BINOLBSEMP07_Form extends DataTable_BaseForm {

	/** 查询时的批次号 */
	private String batchCode;
	
	/**二级代理商CODE【用于查询】*/
	private String resellerCode;
	
	/**一级代理商CODE*/
	private String parentResellerCode;
	
	/**代理商类型*/
	private String resellerType;
	
	/**【查询】生成开始日期*/
	private String startCreateDate;
	
	/**【查询】生成结束日期*/
	private String endCreateDate;
	
	/** 代理商组ID【用于生成优惠券】 */
	private String[] resellerInfoIdGrp;
	
	/** 生成优惠券时的批次号  */
	private String createBatchCode;
	
	/**一批次为代理商生成优惠券数量*/
	private String batchCouponCount;
	
	/** 选择模式【0：全部；1：仅选择；2：禁止选择】 */
	private String selectMode;
	
	/** 优惠券有效开始日期 */
	private String startDate;
	
	/** 优惠券有效结束日期 */
	private String endDate;
	
	/** 批次名称*/
	private String batchName;
	
	/** 优惠券类型*/
	private String couponType;
	
	/** 优惠券面值*/
	private String parValue;
	
	/** 优惠券可使用次数*/
	private String useTimes;
	
	/** 优惠券金额条件 */
	private String amountCondition;
	
	/** 代理商所属省份*/
	private String provinceId;
	
	/** 代理商所属城市 */
	private String cityId;
	
	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getParValue() {
		return parValue;
	}

	public void setParValue(String parValue) {
		this.parValue = parValue;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getUseTimes() {
		return useTimes;
	}

	public void setUseTimes(String useTimes) {
		this.useTimes = useTimes;
	}

	public String getAmountCondition() {
		return amountCondition;
	}

	public void setAmountCondition(String amountCondition) {
		this.amountCondition = amountCondition;
	}

	/** 同步状态 */
	private String synchFlag;

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getStartCreateDate() {
		return startCreateDate;
	}

	public void setStartCreateDate(String startCreateDate) {
		this.startCreateDate = startCreateDate;
	}

	public String getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(String endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String[] getResellerInfoIdGrp() {
		return resellerInfoIdGrp;
	}

	public void setResellerInfoIdGrp(String[] resellerInfoIdGrp) {
		this.resellerInfoIdGrp = resellerInfoIdGrp;
	}

	public String getSelectMode() {
		return selectMode;
	}

	public void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}

	public String getBatchCouponCount() {
		return batchCouponCount;
	}

	public void setBatchCouponCount(String batchCouponCount) {
		this.batchCouponCount = batchCouponCount;
	}

	public String getCreateBatchCode() {
		return createBatchCode;
	}

	public void setCreateBatchCode(String createBatchCode) {
		this.createBatchCode = createBatchCode;
	}

	public String getSynchFlag() {
		return synchFlag;
	}

	public void setSynchFlag(String synchFlag) {
		this.synchFlag = synchFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getResellerCode() {
		return resellerCode;
	}

	public void setResellerCode(String resellerCode) {
		this.resellerCode = resellerCode;
	}

	public String getParentResellerCode() {
		return parentResellerCode;
	}

	public void setParentResellerCode(String parentResellerCode) {
		this.parentResellerCode = parentResellerCode;
	}

	public String getResellerType() {
		return resellerType;
	}

	public void setResellerType(String resellerType) {
		this.resellerType = resellerType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
}
