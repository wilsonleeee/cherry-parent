/*	
 * @(#)BINOLJNMAN06_IF.java     1.0 2012/10/30		
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
package com.cherry.jn.man.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 积分规则配置添加IF
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public interface BINOLJNMAN06_IF {
	
	/**
	 * 保存规则配置
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveConfig(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存组合规则
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveComb(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存规则匹配顺序
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_savePriority(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得规则配置列表
	 * 
	 * @param map
	 * @return List
	 * 			规则配置列表
	 */
	public List<Map<String, Object>> getRuleConfList (Map<String, Object> map);

}
