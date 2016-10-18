package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO09_Form extends DataTable_BaseForm {

	//考核问卷名称
	private String paperName;
	//考核问卷状态
	private String paperStatus;
	//发布时间
	private String publishTime;
	//发发布人
	private String publisher;
	//起始时间
	private String startDate;
	//结束时间
	private String endDate;
	//问卷总分
	private String maxPoint;
	//权限级别
	private String paperRight;
	//所属组织
	private String organizationInfoId;
	//所属品牌
	private String brandInfoId;
	//问卷ID
	private String paperId;
	//标记问卷是否已经下发的字段
	private String issuedStatus;
	//删除问卷时的问卷ID拼成的String
	private String paperIdStr;
	
	public String getPaperIdStr() {
		return paperIdStr;
	}
	public void setPaperIdStr(String paperIdStr) {
		this.paperIdStr = paperIdStr;
	}
	public String getIssuedStatus() {
		return issuedStatus;
	}
	public void setIssuedStatus(String issuedStatus) {
		this.issuedStatus = issuedStatus;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public String getPaperStatus() {
		return paperStatus;
	}
	public void setPaperStatus(String paperStatus) {
		this.paperStatus = paperStatus;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
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
	public String getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(String maxPoint) {
		this.maxPoint = maxPoint;
	}
	public String getPaperRight() {
		return paperRight;
	}
	public void setPaperRight(String paperRight) {
		this.paperRight = paperRight;
	}
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
}
