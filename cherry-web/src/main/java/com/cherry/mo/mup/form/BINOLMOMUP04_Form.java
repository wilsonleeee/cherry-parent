package com.cherry.mo.mup.form;

import java.util.Date;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 盘点机盘点软件版本信息
 * @author Wangminze
 *
 */
public class BINOLMOMUP04_Form  extends DataTable_BaseForm {
	
	/**
	 * 盘点机盘点软件版本
	 */
	private String version;
	
	/**
	 * 软件安装文件下载地址
	 */
	private String downloadUrl;
	
	/**
	 * 启动升级时间
	 */
	private Date openUpdateTime;

	private String md5Key;
	
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

	public Date getOpenUpdateTime() {
		return openUpdateTime;
	}

	public void setOpenUpdateTime(Date openUpdateTime) {
		this.openUpdateTime = openUpdateTime;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
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
