package com.cherry.st.sfh.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 订货（Excel导入）Form
 * @author menghao
 *
 */
public class BINOLSTSFH17_Form extends DataTable_BaseForm {
	/** 品牌ID */
	private String brandInfoId;
	/** 导入日期开始 */
	private String importStartTime;
	/** 导入日期结束 */
	private String importEndTime;
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

	public String getImportStartTime() {
		return importStartTime;
	}

	public void setImportStartTime(String importStartTime) {
		this.importStartTime = importStartTime;
	}

	public String getImportEndTime() {
		return importEndTime;
	}

	public void setImportEndTime(String importEndTime) {
		this.importEndTime = importEndTime;
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
