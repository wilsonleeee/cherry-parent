/*	
 * @(#)BINOLPLUPM05_BL.java     1.0 2010/12/28		
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

import com.cherry.cm.util.CherryUtil;
import com.cherry.pl.upm.service.BINOLPLUPM05_Service;

/**
 * 
 * 安全策略添加BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
public class BINOLPLUPM05_BL {

	@Resource
	private BINOLPLUPM05_Service binOLPLUPM05_Service;

	/**
	 * 插入密码安全配置表
	 * 
	 * @param Map
	 *            更新条件
	 * @return
	 */
	public void tran_addPwConfig(Map<String, Object> map) throws Exception {
		map = CherryUtil.removeEmptyVal(map);
		// 插入密码安全配置表
		binOLPLUPM05_Service.insertPasswordConfig(map);
	}
}
