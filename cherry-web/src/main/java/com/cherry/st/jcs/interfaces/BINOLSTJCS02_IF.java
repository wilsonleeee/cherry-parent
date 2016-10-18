package com.cherry.st.jcs.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/*  
 * @(#)BINOLSTJCS02_IF.java    1.0 2012-6-14     
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
public interface BINOLSTJCS02_IF extends ICherryInterface {

	/**
	 * 保存添加的逻辑仓库,如果同时设定了部门仓库关系也一起添加
	 * 
	 * */
	public void tran_save(Map<String,Object> map) throws Exception;
	
}
