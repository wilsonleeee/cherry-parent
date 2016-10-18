/*	
 * @(#)CounterMessageSynchroService.java     1.0 2011/06/13		
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
package com.cherry.synchro.mo.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;
/**
 * 柜台消息发布
 * @author dingyc
 *
 */
public class CounterMessageSynchroService {	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	public void publishCounterMessage(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterMessageSynchro.publishCounterMessage");
		baseSynchroService.execProcedure(param);
	}
	
	public void updForbiddenCounters(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterMessageSynchro.updForbiddenCounters");
		baseSynchroService.execProcedure(param);
	}
	
	public void updCounterMessage(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CounterMessageSynchro.updCounterMessage");
		baseSynchroService.execProcedure(param);
	}
}
