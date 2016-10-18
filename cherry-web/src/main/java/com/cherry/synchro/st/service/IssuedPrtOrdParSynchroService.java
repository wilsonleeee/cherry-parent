
/*  
 * @(#)IssuedPrtOrdParSynchro_Service.java    1.0 2011-8-12     
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

@SuppressWarnings("unchecked")
public class IssuedPrtOrdParSynchroService{

	@Resource
	private BaseSynchroService baseSynchroService;	
	
	public void issuedPrtOrdParSynchro(Map map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "IssuedPrtOrdParSynchro.IssuedPrtOrdParam");
		baseSynchroService.execProcedure(paramMap);
	}
	
	public void delPrtOrdParSynchro(Map map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "IssuedPrtOrdParSynchro.delPrtOrdParam");
		baseSynchroService.execProcedure(paramMap);
	}
	
}
