/*	
 * @(#)RivalSynchro_IF.java     1.0 2011/6/22		
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
 * 竞争对手
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public interface RivalSynchro_IF {
	 /**
	  * 设定竞争对手
	 * @param param
	 * @throws CherryException 
	 */
	public void addRival(Map param) throws CherryException;
	
	/**
	 * 新增、编辑、删除竞争对手（即同步竞争对手）
	 * @param param
	 * @throws CherryException
	 */
	public void synchroRival(Map param) throws CherryException;

}
