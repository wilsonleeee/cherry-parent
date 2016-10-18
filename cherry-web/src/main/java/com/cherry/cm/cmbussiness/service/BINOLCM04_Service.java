/*	
 * @(#)BINOLCM04_Service.java     1.0 2010/12/06		
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
package com.cherry.cm.cmbussiness.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * MQ日志表
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM04_Service extends BaseService{
	/**
	 * 插入MQ日志表
	 * @param map
	 * @return
	 */
	public void insertMQLog (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM04.insertMQLog");
		 baseServiceImpl.save(map);
	}
}
