package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO16_Form extends DataTable_BaseForm {
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
	/**导入批次是否从导入文件中获取*/
	private String isChecked;
	/**导入柜台消息后是否立即下发*/
	private String isPublish;
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	
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
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	public String getPrivilegeFlag() {
		return privilegeFlag;
	}
	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

}
