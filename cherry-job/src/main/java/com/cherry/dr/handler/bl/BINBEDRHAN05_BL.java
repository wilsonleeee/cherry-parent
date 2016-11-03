/*	
 * @(#)BINBEDRHAN05_BL.java     1.0 2013/11/21
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
package com.cherry.dr.handler.bl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;

/**
 * 刷新业务处理器BL
 * 
 * @author hub
 * @version 1.0 2013.11.21
 */
public class BINBEDRHAN05_BL implements CherryMessageHandler_IF{
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRHAN05_BL.class.getName());
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;

	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		// 品牌代码
		String brandCode = (String) map.get("brandCode");
		logger.info("******************************刷新业务处理器开始！品牌代号：" + brandCode + " ***************************");
		try {
			binBEMQMES98_BL.refreshHandler();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.info("******************************刷新业务处理器异常结束！品牌代号：" + brandCode + " ***************************");
		}
		logger.info("******************************刷新业务处理器正常结束！品牌代号：" + brandCode + " ***************************");
	}

}
