package com.cherry.mo.mup.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 盘点机盘点软件版本信息
 * @author Wangminze
 *
 */
public class BINOLMOMUP01_Form  extends DataTable_BaseForm {
	
	/**
	 * 盘点机盘点软件版本
	 */
	private String version;
	
	/**
	 * 软件安装文件下载地址
	 */
	private String downloadUrl;
	
	/**
	 * 有效标志
	 */
	private String validFlag;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	@Override
	public String toString() {
		return "BINOLMOMUP01_Form [version=" + version + ", downloadUrl="
				+ downloadUrl + ", validFlag=" + validFlag + "]";
	}
	
}
