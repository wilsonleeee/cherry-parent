/*	
 * @(#)CampRuleExec_IF.java     1.0 2012/04/23	
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
package com.cherry.dr.cmbussiness.interfaces;

import java.util.List;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 会员规则执行共通IF(BATCH)
 * 
 * @author hub
 * @version 1.0 2012.04.23
 */
public interface CampRuleBatchExec_IF {
	
	/**
	 *会员入会规则执行
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void ruleExec(List<CampBaseDTO> campBaseList) throws Exception;
	
	/**
	 *会员入会规则执行前处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void beforExec(List<CampBaseDTO> campBaseList) throws Exception;
	
	/**
	 *会员入会规则执行后处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void afterExec(List<CampBaseDTO> campBaseList) throws Exception;
	
}
