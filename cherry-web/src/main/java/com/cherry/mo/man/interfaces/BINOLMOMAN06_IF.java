/*  
 * @(#)BINOLMOMAN06_IF.java    1.0 2011-7-28     
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

package com.cherry.mo.man.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOMAN06_IF extends ICherryInterface {

	/**
	 * 取得U盘信息list
	 * 
	 * */
	public List<Map<String,Object>> getUdiskList(Map<String,Object> map) throws Exception;
	
	/**
	 * 取得U盘信息总数
	 * 
	 * 
	 * */
	public int getUdiskCount(Map<String,Object> map) throws Exception;
	
	/**
	 * U盘与员工绑定
	 * 
	 * 
	 * */
	public void tran_employeeBind(Map<String,Object> map)throws Exception;
	
	/**
	 * 批量解除员工绑定
	 * 
	 * 
	 * */
	public void tran_employeeUnbind(List<Map<String,Object>> list,Map<String,Object> map)throws Exception;
	
	/**
	 * 逻辑删除U盘信息
	 * 
	 * 
	 * 
	 * */
	public void tran_deleteUdisk(List<Map<String,Object>> list,Map<String,Object> map)throws Exception;
	
}
