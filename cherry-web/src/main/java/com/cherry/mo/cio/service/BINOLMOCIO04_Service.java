package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINOLMOCIO04_Service.java    1.0 2012-3-9     
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
public class BINOLMOCIO04_Service extends BaseService {

	/**
	 * 取得某张问卷的有效时间
	 * 
	 * */
	public List<Map<String,Object>> getValidTime(Integer paperId){
		return baseServiceImpl.getList(paperId, "BINOLMOCIO04.getValidTime");
	}
}
