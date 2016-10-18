/*	
 * @(#)BINOLMQCOM01_Service.java     1.0 2011/12/14
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
package com.cherry.cm.activemq.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.BaseService;
import com.mongodb.DBObject;

/**
 * 发送MQ消息共通处理 Service
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class BINOLMQCOM01_Service extends BaseService {
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMQCOM01_Service.class);
	
	/**
	 * 
	 * 插入MQ收发日志表
	 * 
	 * @param mqInfoDTO MQ消息 DTO
	 */
	public void insertMQLog(MQInfoDTO mqInfoDTO) {
		
		baseServiceImpl.save(mqInfoDTO, "BINOLMQCOM01.insertMQLog");
	}
	
	/**
	 * 查询品牌信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> selBrandInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMQCOM01.searchBrandInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 插入MQ消息发送日志表
	 * 
	 * @param dbObject 插入内容
	 * @throws Exception 
	 */
	public void addMongoDBBusLog (DBObject dbObject) throws Exception{
		// 如果第一次插入失败将尝试重新插入
		for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
			try {
				MongoDB.insert(CherryConstants.MGO_MQSENDLOG, dbObject);
				break;
			} catch (IllegalStateException ise) {
				if (i == CherryConstants.MGO_MAX_RETRY) {
					throw ise;
				}
				logger.error("**************************** Write mongodb fails! method : addMongoDBBusLog  time : " + (i + 1));
				long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
				// 延迟等待
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				StringBuffer bf = new StringBuffer();
				bf.append("************ method addMongoDBBusLog throw exception! Exception Class : ")
				.append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
				logger.error(bf.toString(),e);
				throw e;
			} catch (Throwable t) {
				throw new Exception("method addMongoDBBusLog throw Throwable!!");
			}
		}
	}
	
	/**
	 * 
	 * 插入MQ收发日志表（新后台内部发送消息用）
	 * 
	 * @param mqLog 插入内容
	 * @throws Exception 
	 */
	public void addMongoDBMqLog (DBObject mqLog) throws Exception{
		// 如果第一次插入失败将尝试重新插入
		for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
			try {
				MongoDB.insert(CherryConstants.MGO_MQLOG, mqLog);
				break;
			} catch (IllegalStateException ise) {
				if (i == CherryConstants.MGO_MAX_RETRY) {
					throw ise;
				}
				logger.error("**************************** Write mongodb fails! method : addMongoDBMqLog  time : " + (i + 1));
				long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
				// 延迟等待
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				StringBuffer bf = new StringBuffer();
				bf.append("************ method addMongoDBMqLog throw exception! Exception Class : ")
				.append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
				logger.error(bf.toString(),e);
				throw e;
			} catch (Throwable t) {
				throw new Exception("method addMongoDBMqLog throw Throwable!!");
			}
		}
	}

}
