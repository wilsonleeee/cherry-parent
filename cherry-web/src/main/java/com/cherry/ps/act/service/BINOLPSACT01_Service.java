/*  
 * @(#)BINOLPSACT01_Service.java     1.0 2011/05/31      
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
package com.cherry.ps.act.service;

import java.util.HashMap;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;


@SuppressWarnings("unchecked")
public class BINOLPSACT01_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	
	public HashMap getConditionKeyword (String conditionKeyword ){
		// 将要传递的值放入parameterMap里面
		HashMap parameterMap  = new HashMap();
		parameterMap.put("conditionKeyword", conditionKeyword);		
		// 设置sqlId
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "getConditionName");
		return (HashMap) baseServiceImpl.get(parameterMap);
	}
}
