/*  
 * @(#)BINOLPTJCS05_IF.java     1.0 2011/05/31      
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
package com.cherry.pt.jcs.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTJCS35_IF extends ICherryInterface {

	/**
	 * 解析文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseFile(File file,UserInfo userInfo) throws Exception;

	/**
	 * 产品批量导入
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> tran_import(Map<String, Object> map) throws Exception;
}
