
/*  
 * @(#)LogicInventorySynchro_IF.java    1.0 2011-9-21     
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
package com.cherry.synchro.st.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface LogicInventorySynchro_IF extends ICherryInterface {

	/**
	 * 同步逻辑仓库
	 * 
	 * */
	public void synchroLogicInventory(Map<String,Object> paramMap) throws Exception;
}
