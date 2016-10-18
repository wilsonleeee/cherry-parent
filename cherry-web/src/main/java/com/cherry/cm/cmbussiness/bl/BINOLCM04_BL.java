/*		
 * @(#)BINOLCM04_BL.java     1.0 2010/12/06		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM04_Service;

/**
 * 共通处理：操作MQ日志表
 * @author dingyc
 *
 */
public class BINOLCM04_BL {
	
	@Resource
	private BINOLCM04_Service binolcm04_service;

	/**
	 * 取得单据号
	 * @param orgId 组织ID
	 * @param brandId 品牌ID
	 * @param userName 用户登录名
	 * @param type 业务类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void insertMQLog(Map<String, Object> map){
		binolcm04_service.insertMQLog(map);		
	}
}
