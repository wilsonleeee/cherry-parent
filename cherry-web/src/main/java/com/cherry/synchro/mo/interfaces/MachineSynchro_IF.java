/*  
 * @(#)MachineSynchro_IF.java     1.0 2011/05/31      
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
@SuppressWarnings("unchecked")
public interface MachineSynchro_IF {
	 /**
	  * 增加机器
	 * @param param
	 * @throws CherryException 
	 */
	@Deprecated
	public void addMachine(Map param) throws CherryException;		
	 /**
	  * 修改机器
	 * @param param
	 */
	@Deprecated
	public void updMachine(Map param) throws CherryException;	
	 /**
	  * 删除机器
	 * @param param
	 */	
	@Deprecated
	public void delMachine(Map param) throws CherryException;
	 /**
	  * 绑定机器
	 * @param param
	 */
	@Deprecated
	public void synchroMachineToCounter(Map param) throws CherryException;	
	 /**
	  * 机器升级
	 * @param param
	 */
	public void synchroMachineUpgrade(Map param) throws CherryException;	
	
	 /**
	  * Machine操作
	  * 本方法是对当前接口以上方法(增加、修改、删除、绑定、升级)的集成
	 * @param param
	 */
	public void synchroMachine(Map param) throws CherryException;	
}
