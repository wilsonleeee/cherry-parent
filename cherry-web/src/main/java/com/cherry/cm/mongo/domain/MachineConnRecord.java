/*  
 * @(#)MachineConnRecord.java    1.0 2014-01-09     
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
package com.cherry.cm.mongo.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 机器连接信息表
 * 
 * @author niushunjie
 * @version 1.0 2014-01-09
 */
@Document(collection="MGO_MachineConnRecord")
@CompoundIndexes({
    @CompoundIndex(name="OrgCode_BrandCode_BIN_MachineInfoID_RecordDate", def="{'OrgCode':1,'BrandCode':1,'BIN_MachineInfoID':1,'RecordDate':1}")
})
public class MachineConnRecord {
    
    @Id
    private ObjectId id;
    
    /** 组织代码 **/
    @Field("OrgCode")
    private String orgCode;
    
    /** 品牌代码 **/
    @Field("BrandCode")
    private String brandCode;
    
    /** 机器ID **/
    @Field("BIN_MachineInfoID")
    private int machineInfoID;
    
    /** 机器号 **/
    @Field("MachineCode")
    private String machineCode;
    
    /** 记录日期 **/
    @Field("RecordDate")
    private String recordDate;
    
    /** 第一次连接时间 **/
    @Field("FirstConnectTime")
    private String firstConnectTime;
    
    /** 最后一次连接时间 **/
    @Field("LastConnectTime")
    private String lastConnectTime;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

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

    public int getMachineInfoID() {
        return machineInfoID;
    }

    public void setMachineInfoID(int machineInfoID) {
        this.machineInfoID = machineInfoID;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getFirstConnectTime() {
        return firstConnectTime;
    }

    public void setFirstConnectTime(String firstConnectTime) {
        this.firstConnectTime = firstConnectTime;
    }

    public String getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(String lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }
}