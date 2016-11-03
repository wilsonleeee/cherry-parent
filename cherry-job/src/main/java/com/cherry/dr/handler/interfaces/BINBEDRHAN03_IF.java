/*	
 * @(#)BINBEDRHAN03_IF.java     1.0 2012/04/23
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
package com.cherry.dr.handler.interfaces;

import java.util.Map;

/**
 * 降级处理IF
 * 
 * @author hub
 * @version 1.0 2012.04.23
 */
public interface BINBEDRHAN03_IF {
	
	/**
	 * 
	 * 降级处理batch主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	public int tran_levelDown(Map<String, Object> map) throws Exception;
}
