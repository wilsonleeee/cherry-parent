
/*  
 * @(#)UdiskSynchro_IF.java    1.0 2011-10-31     
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface UdiskSynchro_IF extends ICherryInterface {

	/**
	 * 添加U盘信息及其绑定信息
	 * 
	 * */
	public void addUdisk(List<Map<String,Object>> paramList) throws Exception;
	
	
	/**
	 * 绑定U盘
	 * 
	 * */
	public void bindUdisk(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 解除U盘绑定
	 * 
	 * */
	public void unbindUdisk(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 删除U盘信息
	 * 
	 * */
	public void deleteUdisk(Map<String, Object> paramMap) throws Exception;
}
