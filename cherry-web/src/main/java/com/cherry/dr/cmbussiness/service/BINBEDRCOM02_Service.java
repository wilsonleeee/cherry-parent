/*	
 * @(#)BINBEDRCOM02_Service.java     1.0 2011/09/02
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
package com.cherry.dr.cmbussiness.service;

import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.mq.MQLogDTO;

/**
 * MQ收发日志表共通处理 Service
 * 
 * @author hub
 * @version 1.0 2011.09.02
 */
public class BINBEDRCOM02_Service extends BaseService{
	
	/**
	 * 插入MQ收发日志表
	 * 
	 * @param campBaseDTO
	 */
	public void insertMQLog(MQLogDTO mqLogDTO){
		baseServiceImpl.save(mqLogDTO, "BINBEDRCOM02.insertMQLog");
	}
}
