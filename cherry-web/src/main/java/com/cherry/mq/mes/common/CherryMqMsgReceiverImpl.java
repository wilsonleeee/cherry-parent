/*		
 * @(#)CherryMqMsgReceiverImpl.java     1.0 2015-12-30		
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
package com.cherry.mq.mes.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.CherryUtil;
import com.cherry.mq.mes.action.BINBEMQMES03_Action;
import com.cherry.mq.mes.interfaces.CherryMessageReceiver_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * @ClassName: CherryMqMsgReceiverImpl 
 * @Description: TODO(新MQ逻辑的监听器) 
 * @author menghao
 * @version v1.0.0 2015-12-30 
 *
 */
public class CherryMqMsgReceiverImpl implements CherryMessageReceiver_IF{
	
	@Resource(name="binBEMQMES03_Action")
	private BINBEMQMES03_Action binBEMQMES03_Action;
	
	/** 组织品牌信息 **/
	public static Map<String,String[]> brandMap = new HashMap<String,String[]>();
	
	private static final Logger logger = LoggerFactory.getLogger(CherryMqMsgReceiverImpl.class);
	
	/**
     * 消息接收入口
     * @param msg
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(String msg) throws Exception {
		
		logger.debug("******************************MQ接收消息处理开始***************************");
    	logger.debug("*****************MQ消息体 START ****************");
    	logger.debug(msg);
    	logger.debug("*****************MQ消息体 END ****************");
		// 设置异常处理标志
		boolean errorFlag = false;
		
		Map<String, Object> map = null;
		
		try {
			map = (Map<String, Object>) JSONUtil.deserialize(msg);
			//遍历maps将key做如下处理：如果第二个字母是大写则不处理，否则将首字母转为小写
			map = CherryUtil.dealMap(map);
			// MQ消息原始数据，用于插入 MongoDB 消息警告表
			map.put("messageBody", msg);
		} catch(Exception e) {
			// 数据格式有误【日志与mongoDB都记录】
			logger.error(MessageConstants.MSG_ERROR_43+"；"+e.getMessage(),e);
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_43);
		}
		
		try {
			// 接收消息
			binBEMQMES03_Action.receiveMessage(map);
		} catch (Exception e) {
			errorFlag = true;
			if(!(e instanceof CherryMQException)) {
				// 其他未知异常
				String brandCode = "";
				String tradeNoIF = "";
				String tradeType = "";
				if(map != null) {
					if(map.get("brandCode") != null) {
						brandCode = map.get("brandCode").equals("") ? "" : "品牌CODE为\"" + map.get("brandCode") + "\";";
					}
					if(map.get("tradeType") != null) {
					     tradeType = map.get("tradeType").equals("") ? "" : "业务类型为\"" + map.get("tradeType") + "\";";
					}
					if(map.get("tradeNoIF") != null) {
						 tradeNoIF = map.get("tradeNoIF").equals("") ? "" : "单据号为\"" + map.get("tradeNoIF") + "\";";
					}
				}
				logger.error("MQ异常："+brandCode + tradeType + tradeNoIF + e.getMessage(),e);
			}
			
		} finally {
			if(errorFlag) {
				logger.debug("******************************MQ接收消息异常结束***************************");
			} else {
				logger.debug("******************************MQ接收消息正常结束***************************");
			}
		}
    }

	@Override
	public void receiveMessage(String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
