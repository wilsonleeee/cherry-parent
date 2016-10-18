/*  
 * @(#)CounterSynchroService.java     1.0 2011/05/31      
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
package com.cherry.synchro.bs.service;

import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

public class CounterSynchroService {
	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 添加和更新老后台配置数据中柜台信息（不包括删除）
	 * 
	 * */
	public void synchroCounter(Map<String,Object> param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterSynchro.synchroCounter");
		baseSynchroService.execProcedure(param);
	}
	
	/**
	 * 更新老后台配置数据中的柜台信息（已废弃）
	 * 
	 * */
	@Deprecated
	public void updCounter(Map<String,Object> param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterSynchro.updCounter");
		baseSynchroService.execProcedure(param);
	}

	/**
	 * 物理删除老后台中配置数据库中的柜台信息
	 * 
	 * */
	public void delCounter(Map<String,Object> param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterSynchro.delCounter");
		baseSynchroService.execProcedure(param);
	}
}
