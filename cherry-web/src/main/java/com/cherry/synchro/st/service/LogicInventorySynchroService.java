/*  
 * @(#)LogicInventorySynchro_Service.java    1.0 2011-9-21     
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
package com.cherry.synchro.st.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

public class LogicInventorySynchroService {

	@Resource
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 同步逻辑仓库
	 * 
	 * */
	public void synchroLogicInventory(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "LogicInventorySynchro.synchroLogicInventory");
		baseSynchroService.execProcedure(map);
	}
}
