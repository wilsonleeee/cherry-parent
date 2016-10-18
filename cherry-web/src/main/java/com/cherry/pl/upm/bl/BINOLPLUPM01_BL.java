/*		
 * @(#)BINOLPLUPM01_BL.java     1.0 2010/12/24		
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
package com.cherry.pl.upm.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.upm.service.BINOLPLUPM01_Service;

/**
 * 用户信息查询 BL
 * 
 */

public class BINOLPLUPM01_BL {

	@Resource
	private BINOLPLUPM01_Service binolplupm01_Service;

	/**
	 * 取得用户总数
	 * 
	 * @param map
	 * @return
	 */
	public int getUserCount(Map<String, Object> map) {

		return binolplupm01_Service.getUserCount(map);
	}

	/**
	 * 取得用户信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getUserList(Map<String, Object> map) {

		return binolplupm01_Service.getUserList(map);
	}
	

	/**
	 * 取得用户信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getUser(Map<String, Object> map) {

		return binolplupm01_Service.getUser(map);
	}
	
	//伦理删除用户信息
	public void tran_inValidUser(Map<String, Object> map) throws Exception{
		
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, binolplupm01_Service.getSYSDate());
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLUPM01");
		int result = binolplupm01_Service.inValidUser(map);
		if (result==0){
			throw new CherryException("ECM00038");
		}
	}
}
