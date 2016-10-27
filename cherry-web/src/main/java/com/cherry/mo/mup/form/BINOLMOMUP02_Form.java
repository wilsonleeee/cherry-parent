package com.cherry.mo.mup.form;

import com.cherry.cm.form.DataTable_BaseForm;

import java.util.Date;

/**
 * Created by Wangminze on 2016/10/27.
 */
public class BINOLMOMUP02_Form extends DataTable_BaseForm {

    /**
     * 软件版本信息ID
     */
    private int softwareVersionInfoId;
    /**
     * 盘点机盘点软件版本
     */
    private String version;

    /**
     * 软件安装文件下载地址
     */
    private String downloadUrl;

    /**
     * MD5秘钥
     */
    private String md5Key;

    /**
     * 放开更新时间
     */
    private Date openUpdateTime;

    /**
     * 有效标志
     */
    private String validFlag;

    public int getSoftwareVersionInfoId() {
        return softwareVersionInfoId;
    }

    public void setSoftwareVersionInfoId(int softwareVersionInfoId) {
        this.softwareVersionInfoId = softwareVersionInfoId;
    }

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

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public Date getOpenUpdateTime() {
        return openUpdateTime;
    }

    public void setOpenUpdateTime(Date openUpdateTime) {
        this.openUpdateTime = openUpdateTime;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }
}
