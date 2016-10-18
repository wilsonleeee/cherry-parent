package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO04_Form extends DataTable_BaseForm {

	//问卷类型
	private String paperType;
	//问卷状态
	private String paperStatus;
	//问卷名称
	private String paperName;
	//开始时间
	private String startDate;
	//结束时间
	private String endDate;
	//品牌
	private String brandInfoId;
	//问卷ID
	private String paperId;
	//问题拼成的字符串
	private String queStr;
	//问卷总分
	private String maxPoint;
	//问题list
	private String questionList;
	
	public String getQuestionList() {
		return questionList;
	}
	public void setQuestionList(String questionList) {
		this.questionList = questionList;
	}
	public String getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(String maxPoint) {
		this.maxPoint = maxPoint;
	}
	public String getQueStr() {
		return queStr;
	}
	public void setQueStr(String queStr) {
		this.queStr = queStr;
	}
	public String getPaperType() {
		return paperType;
	}
	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}
	public String getPaperStatus() {
		return paperStatus;
	}
	public void setPaperStatus(String paperStatus) {
		this.paperStatus = paperStatus;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
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
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	
}
