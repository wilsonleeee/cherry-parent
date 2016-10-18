/*		
 * @(#)CherryRuleMsgReceiverImpl.java     1.0 2011/11/16		
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

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mq.mes.action.BINBEMQMES02_Action;
import com.cherry.mq.mes.interfaces.CherryMessageReceiver_IF;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 规则处理消息的MQ监听器
 * 
 * @author hub
 * @version 1.0 2011.11.16
 */
public class CherryRuleMsgReceiverImpl implements CherryMessageReceiver_IF{
	
	@Resource
	private BINBEMQMES02_Action binBEMQMES02_Action;
	
	private static final Logger logger = LoggerFactory.getLogger(CherryRuleMsgReceiverImpl.class);
	
	/**
     * 消息接收入口
     * @param msg
     * @throws Exception
     */
	@Override
	public void handleMessage(String msg) throws Exception {
		Map<String, Object> map = null;
		try {
			map = (Map) JSONUtil.deserialize(msg);
			// 接收消息
			binBEMQMES02_Action.receiveMessage(map);
		} catch (Exception e) {
			String tradeNoIF = "";
			String tradeType = "";
			if(map != null){
				if(map.get("tradeType") != null) {
				     tradeType = map.get("tradeType").equals("")?"":"业务类型为\"" + map.get("tradeType") + "\";";
				}
				if(map.get("tradeNoIF") != null) {
					 tradeNoIF = map.get("tradeNoIF").equals("")?"":"单据号为\"" + map.get("tradeNoIF") + "\";";
				}
			}
			logger.error(tradeType + tradeNoIF + e.getMessage(),e);
		} 
    }

	@Override
	public void receiveMessage(String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
