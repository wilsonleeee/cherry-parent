/*	
 * @(#)PaperSynchroService.java     1.0 2011/5/23		
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
 * 问卷
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class PaperSynchroService {	
	@Resource
	private BaseSynchroService baseSynchroService;	
	
	public void addPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PaperSynchro.addPaper");
		baseSynchroService.execProcedure(param);
	}	
	public void updPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PaperSynchro.updPaper");
		baseSynchroService.execProcedure(param);
	}
	public void delPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PaperSynchro.delPaper");
		baseSynchroService.execProcedure(param);
	}
	public void synchroForbiddenCounter(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PaperSynchro.synchroForbiddenCounter");
		baseSynchroService.execProcedure(param);
	}
}
