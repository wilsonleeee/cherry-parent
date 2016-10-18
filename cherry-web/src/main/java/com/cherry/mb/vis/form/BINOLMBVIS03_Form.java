package com.cherry.mb.vis.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员回访类型管理Form
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS03_Form extends DataTable_BaseForm {
	
	private String visitTypeCode;
	
	private String visitTypeName;
	
	private String visitObjJson;
	
	private String visitCategoryId;
	
	private String modifyTime;
	
	private String modifyCount;
	
	private String validFlag;

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getVisitObjJson() {
		return visitObjJson;
	}

	public void setVisitObjJson(String visitObjJson) {
		this.visitObjJson = visitObjJson;
	}

	public String getVisitCategoryId() {
		return visitCategoryId;
	}

	public void setVisitCategoryId(String visitCategoryId) {
		this.visitCategoryId = visitCategoryId;
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

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
