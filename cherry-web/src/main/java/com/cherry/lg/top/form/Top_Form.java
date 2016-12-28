/*
 * @(#)Top_Form.java   1.0 2013/04/19
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
package com.cherry.lg.top.form;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * TopForm
 * @author niushunjie
 * @version 1.0 2013.04.19
 */
public class Top_Form extends DataTable_BaseForm{
    
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 在线用户List */
    private List<Map<String,Object>> onlineUserInfoList;
    
    /** 消息List */
    private List<Map<String,Object>> msgList;
    
    /**消息ID*/
    private String messageID;

    private String counterMessageId;

    public List<Map<String, Object>> getOnlineUserInfoList() {
        return onlineUserInfoList;
    }

    public void setOnlineUserInfoList(List<Map<String, Object>> onlineUserInfoList) {
        this.onlineUserInfoList = onlineUserInfoList;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public List<Map<String, Object>> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Map<String, Object>> msgList) {
        this.msgList = msgList;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getCounterMessageId() {
        return counterMessageId;
    }

    public void setCounterMessageId(String counterMessageId) {
        this.counterMessageId = counterMessageId;
    }
}