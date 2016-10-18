/*
 * @(#)BINOLMOMAN08_IF.java     1.0 2012/11/15
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
package com.cherry.mo.man.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * POS配置项Interface
 * 
 * @author liuminghao
 * @version 1.0 2011.3.15
 */
public interface BINOLMOMAN08_IF extends ICherryInterface {

	/**
	 * 取得POS配置项总数
	 * 
	 * @param map
	 * @return POS配置项总数
	 */
	public int searchMachineInfoCount(Map<String, Object> map);

	/**
	 * 取得POS配置项List 新数据
	 * 
	 * @param map
	 * @return POS配置项List
	 */
	public List<Map<String, Object>> searchPosConfigNewInfoList(
			Map<String, Object> map);

	/**
	 * 取得POS配置项INFO
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getPosConfig(Map<String, Object> map);

	/**
	 * 新增配置项数据
	 * 
	 * @param map
	 */
	public int tran_addPosConfig(Map<String, Object> map) throws Exception;

	/**
	 * 修改配置项数据
	 * 
	 * @param map
	 */
	public void tran_updatePosConfig(Map<String, Object> map) throws Exception;

	public void getLogInvWSMap(Map<String, Object> map) throws Exception;

}
