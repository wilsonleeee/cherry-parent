/*	
 * @(#)MQBaseDTO.java     1.0 2011/12/14	
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.cm.activemq.dto;

/**
 * 共通MQ消息体 DTO
 *
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class MQBaseDTO {

    /**
     * 组织代码
     */
    private String orgCode;

    /**
     * 组织代码
     */
    private int organizationInfoID;

    /**
     * 品牌代码
     */
    private String brandCode;

    /**
     * 品牌ID
     */
    private int brandInfoID;

    /**
     * 单据号
     */
    private String tradeNoIF;

    /**
     * 修改回数
     */
    private String modifyCounts;

    /**
     * 业务类型
     */
    private String tradeType;

    /**
     * 发送队列的名字
     */
    private String queueName;
    /**
     * 原始消息体
     */
    private String originalMsg;

    /**
     * 发送日志区分
     * 1：记入发送日志表（MongoDB）中，仅作为日志备查，无其它作用
     * 为空或其它值，不记录
     * 不重要的MQ消息，建议不记录日志
     */
    private boolean isNeedSaveLog;

    /**
     * 同步日志区分
     * 0：发送方和接收方均不记录日志，适用于消息通知类的不重要的MQ
     * 1：发送方和接收方都要记入SQLserver数据库，供MQ同步机制使用
     * 2：发送方记入MongoDB，接收方接收后直接从MongoDB删除（目前仅新后台发送到新后台的MQ如此）
     */
    private String syncLogFlag;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getTradeNoIF() {
        return tradeNoIF;
    }

    public void setTradeNoIF(String tradeNoIF) {
        this.tradeNoIF = tradeNoIF;
    }

    public String getModifyCounts() {
        return modifyCounts;
    }

    public void setModifyCounts(String modifyCounts) {
        this.modifyCounts = modifyCounts;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String convertNullVal(String val) {
        return null == val ? "" : val;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getOriginalMsg() {
        return originalMsg;
    }

    public void setOriginalMsg(String originalMsg) {
        this.originalMsg = originalMsg;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
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

    public boolean isNeedSaveLog() {
        return isNeedSaveLog;
    }

    public void setNeedSaveLog(boolean needSaveLog) {
        isNeedSaveLog = needSaveLog;
    }

    public String getSyncLogFlag() {
        return syncLogFlag;
    }

    public void setSyncLogFlag(String syncLogFlag) {
        this.syncLogFlag = syncLogFlag;
    }
}
