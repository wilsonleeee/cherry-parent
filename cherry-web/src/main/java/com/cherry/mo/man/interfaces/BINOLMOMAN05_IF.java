/*  
 * @(#)BINOLMOMAN05_IF.java    1.0 2011-7-29     
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

import java.io.File;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOMAN05_IF extends ICherryInterface {

	/**
	 * 解析excel并将结果添加到数据库中
	 * 
	 * 
	 * */
	public void tran_importUdisk(File file,Map<String,Object> map) throws Exception;
	
	/**
	 * 非excel导入的方式添加
	 * 
	 * */
	public void tran_addUdisk(String[] udiskSnArr,String[] employeeIdArr,String[] categoryNameArr,Map<String,Object> map) throws Exception;
	
	public Map<String,Object> getInformation(Map<String,Object> map)throws Exception;
}
