package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBMBM19_Form extends DataTable_BaseForm{
	/**导入名称*/
	private String importName;
	
	/**导入原因*/
	private String reason;
	
	/**导入方式*/
	private String importType;
	
	/**是否开启新增*/
	private boolean addMemflag;
	
	/**是否开启更新*/
	private boolean updateMemflag;
	
	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public boolean isAddMemflag() {
		return addMemflag;
	}

	public void setAddMemflag(boolean addMemflag) {
		this.addMemflag = addMemflag;
	}

	public boolean isUpdateMemflag() {
		return updateMemflag;
	}

	public void setUpdateMemflag(boolean updateMemflag) {
		this.updateMemflag = updateMemflag;
	}
	
}
