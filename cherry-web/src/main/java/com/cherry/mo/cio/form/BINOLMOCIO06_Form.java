package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO06_Form extends DataTable_BaseForm {

	//品牌id
	private String brandInfoId;
	//下发类型
	private String issuedType;
	//树节点
	private String treeNodes;
	//问卷id
	private String paperId;
	//组织ID
	private String organizationInfoId;
	//问题类型
	private String paperType;
	//问题名称
	private String paperName;
	//问卷总分
	private String maxPoint;
	//判断是否已经下发的字段
	private String isIssued;
	//要插入问卷禁止表中的柜台信息
	private String checkedCounter;
	//要下发到老后台的柜台信息
	private String unCheckedCounter;
	
	public String getCheckedCounter() {
		return checkedCounter;
	}
	public void setCheckedCounter(String checkedCounter) {
		this.checkedCounter = checkedCounter;
	}
	public String getUnCheckedCounter() {
		return unCheckedCounter;
	}
	public void setUnCheckedCounter(String unCheckedCounter) {
		this.unCheckedCounter = unCheckedCounter;
	}
	public String getIsIssued() {
		return isIssued;
	}
	public void setIsIssued(String isIssued) {
		this.isIssued = isIssued;
	}
	public String getPaperType() {
		return paperType;
	}
	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public String getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(String maxPoint) {
		this.maxPoint = maxPoint;
	}
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getIssuedType() {
		return issuedType;
	}
	public void setIssuedType(String issuedType) {
		this.issuedType = issuedType;
	}
	public String getTreeNodes() {
		return treeNodes;
	}
	public void setTreeNodes(String treeNodes) {
		this.treeNodes = treeNodes;
	}
	
}
