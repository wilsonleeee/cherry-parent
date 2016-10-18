/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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
package com.cherry.ct.tpl.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 沟通模板一览 IF
 * 
 * @author ZhangGS
 * @version 1.0 2011.11.19
 */
public interface BINOLCTTPL01_IF {
	
	/**
	 * 获取模板数量
	 * 
	 * @param map
	 * @return 沟通模板数量
	 */
	public int getTemplateCount(Map<String, Object> map);
	
	/**
	 * 获取模板信息List
	 * 
	 * @param map
	 * @return 沟通模板List
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public List getTemplateList (Map<String, Object> map) throws Exception;
	
	
}
