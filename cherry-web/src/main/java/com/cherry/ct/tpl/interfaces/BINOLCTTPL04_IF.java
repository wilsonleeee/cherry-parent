/*
 * @(#)BINOLCTTPL04_IF.java     1.0 2013/11/19
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
 * 沟通模板内容非法字符设置 IF
 * 
 * @author ZhangLe
 * @version 1.0 2013.11.19
 */
public interface BINOLCTTPL04_IF {

	/**
	 * 获取非法字符count
	 * @param map
	 * @return
	 */
	public int getIllegalCharCount(Map<String,Object> map);
	
	/**
	 * 获取非法字符List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getIllegalCharList(Map<String,Object> map);
	
	/**
	 * 添加非法字符
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addIllegalChar(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新犯非法字符
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_updateIllegalChar(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取非法字符Map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getIllegalCharMap(Map<String, Object> map);
	
	/**
	 * 判断非法字符是否重复，重复返回true，不重复返回false
	 * @param map
	 * @return
	 */
	public boolean isRepeat(Map<String, Object> map);
}
