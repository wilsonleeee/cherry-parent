/*	
 * @(#)RivalSynchroService.java     1.0 2011/6/22		
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
 * 竞争对手
 * @author dingyc
 *
 */
public class RivalSynchroService {	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	public void addRival(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "RivalSynchro.addRival");
		baseSynchroService.execProcedure(param);
	}
	
	/**
	 * 同步竞争对手（包括新增、更新与删除）
	 * @param param
	 */
	public void synchroRival(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "RivalSynchro.synchroRival");
		baseSynchroService.execProcedure(param);
	}
}
