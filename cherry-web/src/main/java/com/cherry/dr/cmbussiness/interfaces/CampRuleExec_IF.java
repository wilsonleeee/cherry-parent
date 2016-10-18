/*	
 * @(#)CampRuleExec_IF.java     1.0 2011/05/12	
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

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 会员规则执行共通IF
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public interface CampRuleExec_IF {
	
	/**
	 *会员入会规则执行
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 *会员入会规则执行前处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 *会员入会规则执行后处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception;
}
