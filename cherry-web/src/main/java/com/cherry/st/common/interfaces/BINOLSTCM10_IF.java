
/*  
 * @(#)BINOLSTCM10_IF.java    1.0 2011-11-21     
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
package com.cherry.st.common.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * @author zhanggl
 * 
 * 
 * */

public interface BINOLSTCM10_IF extends ICherryInterface {

	/**
	 * 给定业务类型,取得对应的部门,带有权限
	 * 
	 * */
	public Map<String,Object> getDepartInfoByBusinessType(Map<String,Object> param)throws Exception;
	
}
