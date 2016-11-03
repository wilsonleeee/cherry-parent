package com.mqhelper.common.service;

import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINBEMQHELPERCM99_Service.java    1.0 2012-2-22     
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

/**
 * MQHelper模块共通
 * 
 * 
 * */
public class BINBEMQHELPERCM99_Service extends BaseService {

	/**
	 * 往老后台品牌数据库中的MQ_Log表中插入日志并返回自增ID
	 * 
	 * 
	 * */
	public int insertMQ_Log(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQHELPERCM99.insertMQ_Log");
		return witBaseServiceImpl.saveBackId(map);
	}
	
}
