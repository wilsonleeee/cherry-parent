package com.cherry.cm.core;

import java.util.Map;

/**
 * Created by dingyongchang on 2016/12/1.
 */
public class TransRepeaterConfigDTO {
    private String brandCode;
    private String sourceTradeType;
    private String repeaterBeanName;
    private String configParamJson;
    private Map configParamJsonMap;

    public String getBrandCode() {
        return brandCode;
    }

    public String getSourceTradeType() {
        return sourceTradeType;
    }

    public void setSourceTradeType(String sourceTradeType) {
        this.sourceTradeType = sourceTradeType;
    }

    public String getRepeaterBeanName() {
        return repeaterBeanName;
    }

    public void setRepeaterBeanName(String repeaterBeanName) {
        this.repeaterBeanName = repeaterBeanName;
    }

    public Map getConfigParamJsonMap() {
        return configParamJsonMap;
    }

    public void setConfigParamJsonMap(Map configParamJsonMap) {
        this.configParamJsonMap = configParamJsonMap;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getConfigParamJson() {
        return configParamJson;
    }

    public void setConfigParamJson(String configParamJson) {
        this.configParamJson = configParamJson;
    }
}
