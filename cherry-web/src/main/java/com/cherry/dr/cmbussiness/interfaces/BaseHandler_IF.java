/*	
 * @(#)BaseHandler_IF.java     1.0 2011/05/12	
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
 * 处理器共通 IF
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public interface BaseHandler_IF {
	
	/**
	 * 处理规则文件
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	public void executeRuleFile (CampBaseDTO campBaseDTO) throws Exception;

}
