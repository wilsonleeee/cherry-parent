/*	
 * @(#)SaleTargetSynchro.java     1.0 2011/5/20		
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
package com.cherry.synchro.mo.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.synchro.mo.interfaces.SaleTargetSynchro_IF;
import com.cherry.synchro.mo.service.SaleTargetSynchroService;

/**
 * @author dingyc
 *
 */
public class SaleTargetSynchro implements SaleTargetSynchro_IF {

	@Resource
	private SaleTargetSynchroService saleTargetSynchroService;

	@Override
	public void synchroSaleTarget(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			saleTargetSynchroService.synchroSaleTarget(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
}
