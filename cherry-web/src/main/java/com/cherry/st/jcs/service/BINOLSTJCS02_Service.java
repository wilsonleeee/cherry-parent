package com.cherry.st.jcs.service;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINOLSTJCS02_Service.java    1.0 2012-6-14     
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
public class BINOLSTJCS02_Service extends BaseService {

	/**
	 * 添加实体仓库,并返回自增ID
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int addDepot(Map<String,Object> map){
		return baseServiceImpl.saveBackId(map, "BINOLSTJCS02.addDepot");
	}
}
