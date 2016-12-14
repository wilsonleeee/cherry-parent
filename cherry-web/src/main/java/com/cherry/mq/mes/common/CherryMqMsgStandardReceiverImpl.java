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

import com.cherry.mq.mes.action.BINBEMQMES04_Action;
import com.cherry.mq.mes.interfaces.CherryMessageReceiver_IF;
import com.googlecode.jsonplugin.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: CherryMqMsgReceiverImpl 
 * @Description: TODO(新MQ逻辑的监听器) 
 * @author Wangminze
 * @version 2016/11/30
 *
 */
public class CherryMqMsgStandardReceiverImpl implements CherryMessageReceiver_IF{
	
	@Resource(name="binBEMQMES04_Action")
	private BINBEMQMES04_Action binBEMQMES04_Action;
	
	/** 组织品牌信息 **/
	public static Map<String,String[]> brandMap = new HashMap<String,String[]>();
	
	private static final Logger logger = LoggerFactory.getLogger(CherryMqMsgStandardReceiverImpl.class);
	
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

			// MQ消息原始数据，用于插入 MongoDB 消息警告表
			map.put("messageBody", msg);
		} catch(Exception e) {
			// 数据格式有误【日志与mongoDB都记录】
			logger.error(MessageConstants.MSG_ERROR_43+"；"+e.getMessage(),e);
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_43);
		}
		
		try {
			// 接收消息
			binBEMQMES04_Action.receiveMessage(map);
		} catch (Exception e) {
			errorFlag = true;
			if(!(e instanceof CherryMQException)) {
				// 其他未知异常
				String brandCode = "";
				String tradeNoIF = "";
				String tradeType = "";
				if(map != null) {
					if(map.get("BrandCode") != null) {
						brandCode = map.get("BrandCode").equals("") ? "" : "品牌CODE为\"" + map.get("BrandCode") + "\";";
					}
					if(map.get("TradeType") != null) {
					     tradeType = map.get("TradeType").equals("") ? "" : "业务类型为\"" + map.get("TradeType") + "\";";
					}
					if(map.get("TradeNoIF") != null) {
						 tradeNoIF = map.get("TradeNoIF").equals("") ? "" : "单据号为\"" + map.get("TradeNoIF") + "\";";
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
