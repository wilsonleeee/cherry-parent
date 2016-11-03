/*	
 * @(#)BINBECTSMG01_IF.java     1.0 2013/02/18
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
package com.cherry.ct.smg.interfaces;

import java.util.Map;

/**
 * 发送沟通信息IF
 * 
 * @author ZhangGS
 * @version 1.0 2013.02.18
 */
public interface BINBECTSMG01_IF {
	/**
	 * 
	 * 运行单个调度计划
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	public int runSchedules(String schedulesID) throws Exception;
	
	/**
	 * 
	 * 运行所有调度计划
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	public int runAllSchedules(Map<String, Object> map) throws Exception;
}
