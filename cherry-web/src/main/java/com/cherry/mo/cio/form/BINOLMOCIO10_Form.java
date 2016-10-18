package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO10_Form extends DataTable_BaseForm {

	//问卷权限
	private String paperRight;
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
	//分组 拼成的字符串
	private String gropStr;
	//评分标准拼成的字符串
	private String paperLevelStr;
	//问卷总分
	private String maxPoint;
	
	public String getPaperRight() {
		return paperRight;
	}
	public void setPaperRight(String paperRight) {
		this.paperRight = paperRight;
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
	public String getQueStr() {
		return queStr;
	}
	public void setQueStr(String queStr) {
		this.queStr = queStr;
	}
	public String getGropStr() {
		return gropStr;
	}
	public void setGropStr(String gropStr) {
		this.gropStr = gropStr;
	}
	public String getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(String maxPoint) {
		this.maxPoint = maxPoint;
	}
	public String getPaperLevelStr() {
		return paperLevelStr;
	}
	public void setPaperLevelStr(String paperLevelStr) {
		this.paperLevelStr = paperLevelStr;
	}
	
}
