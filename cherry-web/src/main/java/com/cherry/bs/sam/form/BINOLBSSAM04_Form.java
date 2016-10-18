package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM04_Form extends DataTable_BaseForm{
	private String recordId;
	/** 提成员工岗位 **/
	private String bonusEmployeePosition;
	/** 销售员工岗位 **/
	private String saleEmployeePosition;
	/** 销售柜台号 **/
	private String counterCode;
	/** 起算金额 **/
	private String beginAmount;
	/** 截止金额 **/
	private String endAmount;
	/** 提成百分率 **/
	private String bonusRate;
	/** 备注 **/
	private String memo;
	/** 修改数据Map **/
	private Map<String, Object> editSaleMap;
	
	private List<Map<String, Object>> resultSalesBonusRateList;
	/** 岗位列表 **/
	private List<Map<String, Object>> positionCategoryList;
	
	public String getBonusEmployeePosition() {
		return bonusEmployeePosition;
	}
	public void setBonusEmployeePosition(String bonusEmployeePosition) {
		this.bonusEmployeePosition = bonusEmployeePosition;
	}
	public String getSaleEmployeePosition() {
		return saleEmployeePosition;
	}
	public void setSaleEmployeePosition(String saleEmployeePosition) {
		this.saleEmployeePosition = saleEmployeePosition;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getBeginAmount() {
		return beginAmount;
	}
	public void setBeginAmount(String beginAmount) {
		this.beginAmount = beginAmount;
	}
	public String getEndAmount() {
		return endAmount;
	}
	public void setEndAmount(String endAmount) {
		this.endAmount = endAmount;
	}
	public String getBonusRate() {
		return bonusRate;
	}
	public void setBonusRate(String bonusRate) {
		this.bonusRate = bonusRate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public List<Map<String, Object>> getResultSalesBonusRateList() {
		return resultSalesBonusRateList;
	}
	public void setResultSalesBonusRateList(
			List<Map<String, Object>> resultSalesBonusRateList) {
		this.resultSalesBonusRateList = resultSalesBonusRateList;
	}
	public Map<String, Object> getEditSaleMap() {
		return editSaleMap;
	}
	public void setEditSaleMap(Map<String, Object> editSaleMap) {
		this.editSaleMap = editSaleMap;
	}
	public List<Map<String, Object>> getPositionCategoryList() {
		return positionCategoryList;
	}
	public void setPositionCategoryList(
			List<Map<String, Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}
}
