/*  
 * @(#)BINBEMQMES04_Service.java     1.0 2013/07/08      
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
package com.cherry.mq.mes.service;

import java.util.Map;

import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 机器连接处理 Service
 * 
 * @author niushunjie
 * @version 1.0 2013.07.08
 */
public class BINBEMQMES04_Service extends BaseService{
    /**
     * 插入机器连接信息表(MongoDB)
     * 
     * @param map
     * @throws CherryMQException 
     */
    public void addMongoDBMCRLog(Map<String, Object> map) throws CherryMQException {
        try {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("OrgCode", map.get("orgCode"));
            dbObject.put("BrandCode", map.get("brandCode"));
            dbObject.put("BIN_MachineInfoID", map.get("BIN_MachineInfoID"));
            dbObject.put("MachineCode", map.get("machineCode"));
            String recordDate = ConvertUtil.getString(map.get("updatetime")).split(" ")[0];
            dbObject.put("RecordDate", recordDate);
            dbObject.put("FirstConnectTime", map.get("updatetime"));
            dbObject.put("LastConnectTime", map.get("updatetime"));
            MongoDB.insert(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject);
        } catch (Exception e) {
            throw new CherryMQException(MessageConstants.MSG_ERROR_12);
        }
    }
    
    /**
     * 更新机器连接信息表(MongoDB)
     * 
     * @param map
     * @throws CherryMQException 
     */
    public int updateMongoDBMCRLog (Map<String,Object> map) throws CherryMQException{
        try {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("OrgCode", map.get("orgCode"));
            dbObject.put("BrandCode", map.get("brandCode"));
            dbObject.put("BIN_MachineInfoID", map.get("BIN_MachineInfoID"));
            String recordDate = ConvertUtil.getString(map.get("updatetime")).split(" ")[0];
            dbObject.put("RecordDate", recordDate);
            DBObject update = new BasicDBObject();
            DBObject updateSet = new BasicDBObject();
            updateSet.put("LastConnectTime", map.get("updatetime"));
            update.put("$set", updateSet);
            return MongoDB.update(MessageConstants.MQ_MCR_LOG_COLL_NAME, dbObject,update);
        } catch (Exception e) {
            throw new CherryMQException(MessageConstants.MSG_ERROR_70);
        }
    } 
}