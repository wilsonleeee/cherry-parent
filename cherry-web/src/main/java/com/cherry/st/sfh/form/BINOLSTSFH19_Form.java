package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTSFH19_Form extends DataTable_BaseForm {
	/** 品牌ID */
	private String brandInfoId;
	/** 导入日期开始 */
	private String importStartDate;
	/** 导入日期结束 */
	private String importEndDate;
	/** 导入原因 */
	private String comments;
	/** 导入批次号 */
	private String importBatchCode;
	/** 是否允许导入重复数据 */
	private String importRepeat;
	/** 导入批次是否从导入文件中获取 */
	private String isChecked;
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getImportStartDate() {
		return importStartDate;
	}
	public void setImportStartDate(String importStartDate) {
		this.importStartDate = importStartDate;
	}
	public String getImportEndDate() {
		return importEndDate;
	}
	public void setImportEndDate(String importEndDate) {
		this.importEndDate = importEndDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getImportBatchCode() {
		return importBatchCode;
	}
	public void setImportBatchCode(String importBatchCode) {
		this.importBatchCode = importBatchCode;
	}
	public String getImportRepeat() {
		return importRepeat;
	}
	public void setImportRepeat(String importRepeat) {
		this.importRepeat = importRepeat;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

}
