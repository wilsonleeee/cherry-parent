/*  
 * @(#)BINOLMOCIO02_IF.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO02_IF extends ICherryInterface {

	public List<Map<String,Object>> getPaperList(Map<String,Object> map);
	public int getPaperCount(Map<String,Object> map);
	/**
	 * 问卷的停用和启用
	 * 
	 * */
	public void tran_paperDisableOrEnable(Map<String,Object> map) throws Exception;
	
	/**
	 * 问卷的逻辑删除
	 * 
	 * */
	public void tran_deletePaper(Map<String,Object> map) throws Exception;

	/**
	 * 检查是否存在某种类型某种状态的时间有效问卷
	 * 
	 * */
	public List<Map<String,Object>> isExistSomePaper(Map<String,Object> map);
	
	/**
	 * 取得某张问卷的下发信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getPaperIssum(Map<String,Object> map) throws Exception;
}
