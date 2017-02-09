package com.cherry.cm.core;

/**
 * Created by dingyongchang on 2016/12/15.
 */
public class BrandInfoDTO {
    private String orgCode;
    private String brandCode;
    private int organizationInfoID;
    private int brandInfoID;
    //新后台品牌数据库数据源名称
    private String dataSourceName;
    //老后台品牌数据库数据源名称
    private String oldDataSourceName;
    //接口数据库数据源名称
    private String tpifDataSourceName;

    private String aesKey;
    //TODO:duibaAppKey,duibaAppSecret暂时放在品牌数据源配置信息里
    private String duibaAppKey;
    private String duibaAppSecret;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public int getOrganizationInfoID() {
        return organizationInfoID;
    }

    public void setOrganizationInfoID(int organizationInfoID) {
        this.organizationInfoID = organizationInfoID;
    }

    public int getBrandInfoID() {
        return brandInfoID;
    }

    public void setBrandInfoID(int brandInfoID) {
        this.brandInfoID = brandInfoID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getDuibaAppKey() {
        return duibaAppKey;
    }

    public void setDuibaAppKey(String duibaAppKey) {
        this.duibaAppKey = duibaAppKey;
    }

    public String getDuibaAppSecret() {
        return duibaAppSecret;
    }

    public void setDuibaAppSecret(String duibaAppSecret) {
        this.duibaAppSecret = duibaAppSecret;
    }

    public String getOldDataSourceName() {
        return oldDataSourceName;
    }

    public void setOldDataSourceName(String oldDataSourceName) {
        this.oldDataSourceName = oldDataSourceName;
    }

    public String getTpifDataSourceName() {
        return tpifDataSourceName;
    }

    public void setTpifDataSourceName(String tpifDataSourceName) {
        this.tpifDataSourceName = tpifDataSourceName;
    }
}
