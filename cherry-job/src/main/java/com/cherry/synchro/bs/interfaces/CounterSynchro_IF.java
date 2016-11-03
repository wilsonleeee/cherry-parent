/*  
 * @(#)CounterSynchro_IF.java     1.0 2011/05/31      
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
package com.cherry.synchro.bs.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryException;

public interface CounterSynchro_IF {
	
	/**
	 * 柜台管理
	 * @param param
	 * @throws CherryException 
	 */
	public void synchroCounter(Map<String,Object> param) throws CherryException;		

	/**
	 * 拼装调用柜台存储过程下发柜台的柜台信息
	 * @param param
	 * @return
	 * @throws CherryException
	 * @throws Exception 
	 */
	public Map<String,Object> assemblingSynchroInfo(Map<String,Object> param) throws CherryException, Exception; 
}
