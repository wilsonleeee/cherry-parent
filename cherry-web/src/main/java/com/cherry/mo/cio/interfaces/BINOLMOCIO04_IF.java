package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/*  
 * @(#)BINOLMOCIO04_IF.java    1.0 2012-3-9     
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
public interface BINOLMOCIO04_IF extends ICherryInterface {

	/**
	 * 根据问卷ID获取问卷问题信息
	 * @param map 存放的是要获取的问题所属的问卷ID
	 * @return list 返回问题List
	 * 
	 * */
	public List<List> getPaperQuestion(Map<String,Object> map);
}
