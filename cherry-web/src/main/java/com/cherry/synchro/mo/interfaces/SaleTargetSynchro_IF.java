/*	
 * @(#)SaleTargetSynchro_IF.java     1.0 2011/5/20		
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
package com.cherry.synchro.mo.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryException;
/**
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public interface SaleTargetSynchro_IF {
	 /**
	  * 同步销售目标
	 * @param param
	 * @throws CherryException 
	 */
	public void synchroSaleTarget(Map param) throws CherryException;		


}
