/*  
 * @(#)BINOLPTUNQ02_IF    1.0 2016-06-06     
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
package com.cherry.pt.unq.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 单码查询IF
 * 
 * @author zw
 * @version 1.0 2016.06.06
 */
public interface BINOLPTUNQ02_IF{

	/**
	 * 
	 * 取得单码查询List总条数
	 *
	 * @param map
	 * @return
	 */
	public int getSingleCodeSearchCount(Map<String, Object> map);

	/**
	 * 
	 * 取得单码查询List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSingleCodeList(Map<String, Object> map);


}
