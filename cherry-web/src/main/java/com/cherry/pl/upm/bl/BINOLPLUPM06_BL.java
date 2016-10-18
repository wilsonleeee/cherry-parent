/*	
 * @(#)BINOLPLUPM06_BL.java     1.0 2010/12/28		
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

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pl.upm.service.BINOLPLUPM06_Service;

/**
 * 
 * 安全策略编辑BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
public class BINOLPLUPM06_BL {

	@Resource
	private BINOLPLUPM06_Service binOLPLUPM06_Service;

	/**
	 * 更新密码安全配置表
	 * 
	 * @param Map
	 *            更新条件
	 * @return
	 */
	public void tran_updatePwConfig(Map<String, Object> map) throws Exception {
		map = CherryUtil.removeEmptyVal(map);
		// 更新密码安全配置表
		int result = binOLPLUPM06_Service.updatePasswordConfig(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
	}
}
