package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO05_Form extends DataTable_BaseForm {

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
	//上一次的更新时间
	private String updateTime;
	//目前的更新次数
	private String modifyCount;
	//开始时间--时
	private String startHour;
	//开始时间--分
	private String startMinute;
	//开始时间--秒
	private String startSecond;
	//结束时间--时
	private String endHour;
	//结束时间--分
	private String endMinute;
	//结束时间--秒
	private String endSecond;
	/**是否为编辑（0：编辑 1：复制）*/
	private String editFlag;
	
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	public String getStartSecond() {
		return startSecond;
	}
	public void setStartSecond(String startSecond) {
		this.startSecond = startSecond;
	}
	public String getEndHour() {
		return endHour;
	}
	public String getEditFlag() {
		return editFlag;
	}
	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	public String getEndSecond() {
		return endSecond;
	}
	public void setEndSecond(String endSecond) {
		this.endSecond = endSecond;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getModifyCount() {
		return modifyCount;
	}
	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}
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
