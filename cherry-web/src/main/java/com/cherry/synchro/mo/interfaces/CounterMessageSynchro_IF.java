/*	
 * @(#)CounterMessageSynchro_IF.java     1.0 2011/6/13		
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
 * 柜台消息新增，发布
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public interface CounterMessageSynchro_IF {
	 /**
	  * 发布柜台消息
	 * @param param
	 * @throws CherryException 
	 */
	public void publishCounterMessage(Map param) throws CherryException;		
	 /**
	  * 更新禁止柜台数据
	 * @param param
	 * @throws CherryException 
	 */	
	public void updForbiddenCounters(Map param) throws CherryException;	
	/**
	 * 更新柜台消息的status数据
	 * @param param
	 * @throws CherryException
	 */
	public void updCounterMessage(Map param) throws CherryException;

}
