package com.cherry.mb.vis.form;

import java.io.File;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员回访计划管理Form
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS02_Form extends DataTable_BaseForm {
	
	private String visitCategoryId;
	
	private String visitDes;
	
	private String visitState;
	
	private String validFlag;
	
	private String visitObjType;
	
	private String visitObjJson;
	
	private String visitObjCode;
	
	private String visitObjName;
	
	private String visitDateType;
	
	private String visitStartDate;
	
	private String visitEndDate;
	
	private String visitDateRelative;
	
	private String visitDateValue;
	
	private String visitDateUnit;
	
	private String validValue;
	
	private String validUnit;
	
	private String paperId;
	
	private String startDate;
	
	private String planDate;
	
	private String endDate;
	
	private String visitPlanId;
	
	private String modifyTime;
	
	private String modifyCount;
	
	/** 上传的文件 */
	private File upExcel;
	
	/** 上传类型(1:累加 2:覆盖) */
	private String importType;

	public String getVisitCategoryId() {
		return visitCategoryId;
	}

	public void setVisitCategoryId(String visitCategoryId) {
		this.visitCategoryId = visitCategoryId;
	}

	public String getVisitDes() {
		return visitDes;
	}

	public void setVisitDes(String visitDes) {
		this.visitDes = visitDes;
	}

	public String getVisitState() {
		return visitState;
	}

	public void setVisitState(String visitState) {
		this.visitState = visitState;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getVisitObjType() {
		return visitObjType;
	}

	public void setVisitObjType(String visitObjType) {
		this.visitObjType = visitObjType;
	}

	public String getVisitObjJson() {
		return visitObjJson;
	}

	public void setVisitObjJson(String visitObjJson) {
		this.visitObjJson = visitObjJson;
	}

	public String getVisitObjCode() {
		return visitObjCode;
	}

	public void setVisitObjCode(String visitObjCode) {
		this.visitObjCode = visitObjCode;
	}

	public String getVisitObjName() {
		return visitObjName;
	}

	public void setVisitObjName(String visitObjName) {
		this.visitObjName = visitObjName;
	}

	public String getVisitDateType() {
		return visitDateType;
	}

	public void setVisitDateType(String visitDateType) {
		this.visitDateType = visitDateType;
	}

	public String getVisitStartDate() {
		return visitStartDate;
	}

	public void setVisitStartDate(String visitStartDate) {
		this.visitStartDate = visitStartDate;
	}

	public String getVisitEndDate() {
		return visitEndDate;
	}

	public void setVisitEndDate(String visitEndDate) {
		this.visitEndDate = visitEndDate;
	}

	public String getVisitDateRelative() {
		return visitDateRelative;
	}

	public void setVisitDateRelative(String visitDateRelative) {
		this.visitDateRelative = visitDateRelative;
	}

	public String getVisitDateValue() {
		return visitDateValue;
	}

	public void setVisitDateValue(String visitDateValue) {
		this.visitDateValue = visitDateValue;
	}

	public String getVisitDateUnit() {
		return visitDateUnit;
	}

	public void setVisitDateUnit(String visitDateUnit) {
		this.visitDateUnit = visitDateUnit;
	}

	public String getValidValue() {
		return validValue;
	}

	public void setValidValue(String validValue) {
		this.validValue = validValue;
	}

	public String getValidUnit() {
		return validUnit;
	}

	public void setValidUnit(String validUnit) {
		this.validUnit = validUnit;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getVisitPlanId() {
		return visitPlanId;
	}

	public void setVisitPlanId(String visitPlanId) {
		this.visitPlanId = visitPlanId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

}
