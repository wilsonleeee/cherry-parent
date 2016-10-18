/*
 * @(#)BINOLCTTPL02_Action.java     1.0 2013.5.29
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

import java.util.Map;

/**
 * 
 * @author ZhangGS
 * @version 1.0 2013.5.29
 */

public interface BINOLCTTPL02_IF {
	/**
	 * 沟通模板保存处理
	 * 
	 * @param Map,Map
	 * 			保存处理的参数集合
	 * @return 无
	 * 			
	 * @throws Exception 
	 */
	public void saveTemplate(Map<String, Object> map, String type) throws Exception;
	
	/**
	 * 获取沟通模板详细信息
	 * 
	 * @param Map,Map
	 * 			查询参数集合
	 * @return 无
	 * 			
	 * @throws Exception 
	 */
	public Map<String, Object> getTemplateInfo(Map<String, Object> map) throws Exception;
}
