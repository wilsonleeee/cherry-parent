package com.cherry.cm.core;

import java.util.Map;

/**
 * Created by dingyongchang on 2017/2/9.
 */
public class WebserviceConfigDTO {
    private String orgCode;
    private String brandCode;
    private String webserviceIdentifier;
    private String webserviceURL;
    private String accountName;
    private String accountPWD;
    private String appID;
    private String appSecret;
    private String token;
    private String secretKey;
    private String extensionConfig;
    private Map extensionConfigMap;

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

    public String getWebserviceIdentifier() {
        return webserviceIdentifier;
    }

    public void setWebserviceIdentifier(String webserviceIdentifier) {
        this.webserviceIdentifier = webserviceIdentifier;
    }

    public String getWebserviceURL() {
        return webserviceURL;
    }

    public void setWebserviceURL(String webserviceURL) {
        this.webserviceURL = webserviceURL;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPWD() {
        return accountPWD;
    }

    public void setAccountPWD(String accountPWD) {
        this.accountPWD = accountPWD;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getExtensionConfig() {
        return extensionConfig;
    }

    public void setExtensionConfig(String extensionConfig) {
        this.extensionConfig = extensionConfig;
    }

    public Map getExtensionConfigMap() {
        return extensionConfigMap;
    }

    public void setExtensionConfigMap(Map extensionConfigMap) {
        this.extensionConfigMap = extensionConfigMap;
    }

}
