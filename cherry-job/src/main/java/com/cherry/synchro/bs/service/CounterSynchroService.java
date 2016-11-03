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

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.IfServiceImpl;

public class CounterSynchroService {
	
	/** 操作接口数据库的dao实现类 */
	@Resource
	protected IfServiceImpl ifServiceImpl;
	
	/** 操作Cherry数据库的dao实现类 */
	@Resource
	protected BaseServiceImpl baseServiceImpl;
	
	/**
	 * 添加/更新/启用/停用老后台配置数据中柜台信息 
	 * 
	 * */
	public void synchroCounter(Map<String,Object> param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterSynchro.synchroCounter");
		ifServiceImpl.updateProcs(param);
	}
	
	/**
	 * 取得柜台信息(新老后台交互时使用)
	 * 
	 * @param map 查询条件
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "CounterSynchro.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
}
