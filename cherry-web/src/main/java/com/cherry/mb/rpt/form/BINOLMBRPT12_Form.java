package com.cherry.mb.rpt.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员连带销售报表Form
 * 
 * @author hub
 * @version 1.0 2016-04-12
 */
public class BINOLMBRPT12_Form extends DataTable_BaseForm{
	
	private String startDate;
	private String endDate;
	private String mainType;
	private String mainPrtId;
	private String jointPrtId;
	private String mainCateId;
	private String jointCateId;
	private String exportType;
	private String mainPrtName;
	private String jointPrtName;
	private String mainCateName;
	private String jointCateName;
	
	private List<Map<String, Object>> prtList;
	private List<Map<String, Object>> cateList;
	private List<Map<String, Object>> memList;
	
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
	public String getMainType() {
		return mainType;
	}
	public void setMainType(String mainType) {
		this.mainType = mainType;
	}
	public String getMainPrtId() {
		return mainPrtId;
	}
	public void setMainPrtId(String mainPrtId) {
		this.mainPrtId = mainPrtId;
	}
	public String getJointPrtId() {
		return jointPrtId;
	}
	public void setJointPrtId(String jointPrtId) {
		this.jointPrtId = jointPrtId;
	}
	public String getMainCateId() {
		return mainCateId;
	}
	public void setMainCateId(String mainCateId) {
		this.mainCateId = mainCateId;
	}
	public String getJointCateId() {
		return jointCateId;
	}
	public void setJointCateId(String jointCateId) {
		this.jointCateId = jointCateId;
	}
	public List<Map<String, Object>> getPrtList() {
		return prtList;
	}
	public void setPrtList(List<Map<String, Object>> prtList) {
		this.prtList = prtList;
	}
	public List<Map<String, Object>> getCateList() {
		return cateList;
	}
	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}
	public List<Map<String, Object>> getMemList() {
		return memList;
	}
	public void setMemList(List<Map<String, Object>> memList) {
		this.memList = memList;
	}
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	public String getMainPrtName() {
		return mainPrtName;
	}
	public void setMainPrtName(String mainPrtName) {
		this.mainPrtName = mainPrtName;
	}
	public String getJointPrtName() {
		return jointPrtName;
	}
	public void setJointPrtName(String jointPrtName) {
		this.jointPrtName = jointPrtName;
	}
	public String getMainCateName() {
		return mainCateName;
	}
	public void setMainCateName(String mainCateName) {
		this.mainCateName = mainCateName;
	}
	public String getJointCateName() {
		return jointCateName;
	}
	public void setJointCateName(String jointCateName) {
		this.jointCateName = jointCateName;
	}
}
