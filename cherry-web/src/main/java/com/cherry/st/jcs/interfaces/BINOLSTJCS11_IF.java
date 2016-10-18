/*
 * @(#)BINOLSTJCS11_IF.java     1.0 2014/06/20
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

package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * BB柜台维护IF
 * @author zhangle
 * @version 1.0 2014.06.20
 */
public interface BINOLSTJCS11_IF extends ICherryInterface {
	
	/**
	 * 获取BB柜台count
	 * @param map
	 * @return
	 */
	public int getBBCounterCount(Map<String, Object> map);
	
	/**
	 * 获取BB柜台List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBBCounterList(Map<String, Object> map);
	
	/**
	 * 根据ID获取BB柜台
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBBCounter(Map<String, Object> map) ;
	
	/**
	 * 保存BB柜台
	 * @param map
	 */
	public void tran_save(Map<String, Object> map) throws Exception;
	
	/**
	 * 批量停用/启用柜台
	 * @param map
	 * @throws Exception
	 */
	public void tran_disabled(Map<String, Object> map) throws Exception;
	
	/**
	 * 导入BB柜台
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object>	tran_import(Map<String, Object> map) throws Exception;
	
}
