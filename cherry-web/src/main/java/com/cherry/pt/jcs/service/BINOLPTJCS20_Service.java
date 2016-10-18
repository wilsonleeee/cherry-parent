/*  
 * @(#)BINOLPTJCS20_Service.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLPTJCS20_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updPrtPriceSolution(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS20.updPrtPriceSolution");
		return baseServiceImpl.update(map);
	}
}