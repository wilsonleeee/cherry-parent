/*  
 * @(#)BINOLMOCIO03_IF.java     1.0 2011/05/31      
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

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO03_IF extends ICherryInterface {

	/**
	 * 保存问卷，在添加问题类型的时候做的操作
	 * 
	 */
	public void tran_savePaper(Map<String,Object> map,List<Map<String,Object>> list) throws CherryException;
	
	/**
	 * 判断系统是否已经存在相同名称的问卷
	 * 
	 */
	public boolean isExsitSameNamePaper(Map<String, Object> map);
	
}
