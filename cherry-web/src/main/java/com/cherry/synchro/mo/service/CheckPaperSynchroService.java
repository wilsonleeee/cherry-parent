/*	
 * @(#)CheckPaperSynchroService.java     1.0 2011/06/01		
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
 * 考核问卷
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class CheckPaperSynchroService {	
	@Resource
	private BaseSynchroService baseSynchroService;	
	
	public void addCheckPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CheckPaperSynchro.addCheckPaper");
		baseSynchroService.execProcedure(param);
	}	
	public void updCheckPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CheckPaperSynchro.updCheckPaper");
		baseSynchroService.execProcedure(param);
	}
	public void delCheckPaper(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "CheckPaperSynchro.delCheckPaper");
		baseSynchroService.execProcedure(param);
	}
}
