/*	
 * @(#)SaleTargetSynchroService.java     1.0 2011/5/20		
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
 * 销售目标
 * @author dingyc
 *
 */
public class SaleTargetSynchroService {	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	public void synchroSaleTarget(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "SaleTargetSynchro.synchroSaleTarget");
		baseSynchroService.execProcedure(param);
	}
}
