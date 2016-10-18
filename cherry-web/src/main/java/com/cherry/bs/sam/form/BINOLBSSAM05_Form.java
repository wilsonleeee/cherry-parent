package com.cherry.bs.sam.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSSAM05_Form extends DataTable_BaseForm{
	/** 品牌ID */
	private String brandInfoId;
	/** 是否相同数据 */
	private String importRepeat;
	
	/** 员工考核ID **/
	private String recordId;
	/** 员工姓名 **/
	private String employeeID;
	/** 考核年份 **/
	private String assessmentYear;
	/** 考核月份 **/
	private String assessmentMonth;
	/** 考核人 **/
	private String assessmentEmployee;
	/** 考核得分 **/
	private String score;
	/** 考核日期 **/
	private String assessmentDate;
	/** 备注 **/
	private String memo;
	/** 员工考核List **/
	private List<Map<String, Object>> assessmentScoreList;
	/** 取编辑的数据Map **/
	private Map<String, Object> editSaleMap;
	/** 查询参数 **/
	private String startDate;
	private String endDate;
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getAssessmentYear() {
		return assessmentYear;
	}

	public void setAssessmentYear(String assessmentYear) {
		this.assessmentYear = assessmentYear;
	}

	public String getAssessmentMonth() {
		return assessmentMonth;
	}

	public void setAssessmentMonth(String assessmentMonth) {
		this.assessmentMonth = assessmentMonth;
	}

	public String getAssessmentEmployee() {
		return assessmentEmployee;
	}

	public void setAssessmentEmployee(String assessmentEmployee) {
		this.assessmentEmployee = assessmentEmployee;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(String assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<Map<String, Object>> getAssessmentScoreList() {
		return assessmentScoreList;
	}

	public void setAssessmentScoreList(List<Map<String, Object>> assessmentScoreList) {
		this.assessmentScoreList = assessmentScoreList;
	}

	public Map<String, Object> getEditSaleMap() {
		return editSaleMap;
	}

	public void setEditSaleMap(Map<String, Object> editSaleMap) {
		this.editSaleMap = editSaleMap;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getImportRepeat() {
		return importRepeat;
	}

	public void setImportRepeat(String importRepeat) {
		this.importRepeat = importRepeat;
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
	
}
