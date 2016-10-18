/*  
 * @(#)BINBEMQMES98_Service.java     1.0 2011/11/04      
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 管理MQ消息处理器和规则计算处理器共通 Service
 * 
 * @author hub
 * @version 1.0 2011.11.04
 */
public class BINBEMQMES98_Service extends BaseService{
	
	/**
	 * 取得处理器名称List
	 * 
	 * @param map 查询参数
	 * @return 处理器名称List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getHandlerNameList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES98.getHandlerNameList");
		return baseConfServiceImpl.getList(map);
	}
}
