/*		
 * @(#)BINBEMQMES02_Action.java     1.0 2011/11/15		
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
package com.cherry.mq.mes.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;

/**
 * 消息处理(规则处理)Action
 * 
 * @author hub
 * @version 1.0 2011.11.15
 */
public class BINBEMQMES02_Action {
	
	@Resource
	private BINBEMQMES99_BL binBEMQMES99_BL;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/**
	 * 接收消息
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void receiveMessage(Map<String, Object> map) throws Exception {
		try {
			// 数据源
			String dataSourceName = (String) map.get("dataSourceName");
			// 将获取的数据源名设定到线程本地变量contextHolder中
			CustomerContextHolder.setCustomerDataSourceType(dataSourceName);
			// 分析规则处理消息体数据--事务处理
			binBEMQMES99_BL.tran_analyzeMessageRule(map);
		} catch (Exception e) {
			// 规则执行异常
			if (e instanceof CherryDRException) {
				CherryDRException drEx = (CherryDRException) e;
				// 会员相关处理异常,需要更新会员重算表
				if (null != map && DroolsConstants.ERROR_TYPE_1 == drEx.getErrType()) {
					// 更新重算信息表(MQ)
					//binbedrcom01BL.updateReCalcMQ(map);
				}
			}
			throw e;
		} finally {
			// 清除数据源ThreadLocal变量
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}
}
