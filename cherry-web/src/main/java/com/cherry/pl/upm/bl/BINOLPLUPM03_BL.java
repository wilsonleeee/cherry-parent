/*		
 * @(#)BINOLPLUPM03_BL.java     1.0 2010/12/24		
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

import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.upm.service.BINOLPLUPM03_Service;

/**
 * 用户信息查询 BL
 * 
 */

public class BINOLPLUPM03_BL {

	@Resource
	private BINOLPLUPM03_Service binolplupm03_Service;
	
	/**
	 * 更新用户信息
	 * 
	 * @param map
	 * @return int
	 */
	public void tran_UpdateUser(Map<String, Object> map) throws Exception{
		
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, binolplupm03_Service.getSYSDate());
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLUPM03");
		// 更新用户信息
		int result = binolplupm03_Service.updateUser(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
	}
}
